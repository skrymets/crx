/*
 * Copyright 2018 skrymets.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crxmarkets.web.client.local;

import com.crxmarkets.web.client.local.img.Rect;
import com.crxmarkets.web.client.shared.CalculationResult;
import com.crxmarkets.web.client.shared.CalculationTask;
import com.crxmarkets.web.client.shared.CalculatorResource;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import elemental2.dom.CanvasGradient;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.CanvasRenderingContext2D.FillStyleUnionType;
import elemental2.dom.CanvasRenderingContext2D.StrokeStyleUnionType;
import elemental2.dom.HTMLCanvasElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

/**
 *
 * @author skrymets
 */
@Templated("calculator-page.html#calculator-widget")
public class CalculatorWidget implements IsElement {

    // static final int height = 400;
    // static final int width = 500;


    @Inject
    Caller<CalculatorResource> calculatorResource;

    @Inject
    @DataField
    TextBox rawTaskData;
    
    @Inject
    @DataField
    Label calculationData;
    
    @Inject
    @DataField
    Label calculationResultData;

    @Inject
    @DataField
    Button calculateButton;

    @Inject
    @DataField("calculator-screen")
    HTMLCanvasElement canvas;
     
    @EventHandler("calculateButton")
    public void onCalculateClick(final @ForEvent("click") ClickEvent event) {

        String taskString = rawTaskData.getText();
        String[] numbersString = taskString.split(" ");
        final List<Integer> hills = Stream.of(numbersString).map(Integer::valueOf).collect(Collectors.toList());
        
        // hills = Arrays.asList(3, 2, 1, 0, 0, 2, 1, 0, 2);
        
        RemoteCallback<CalculationResult> callback = (CalculationResult response) -> {
            calculationResultData.setText(response.toString());
            draw(hills, response.getLevels());
        };

        CalculationTask task = new CalculationTask();
        task.setHeights(hills);
        calculationData.setText(hills.toString());
        calculatorResource.call(callback).calculate(task);

    }

    private void skyGradient(CanvasRenderingContext2D context) {
        // add linear gradient
        CanvasGradient grd = context.createLinearGradient(canvas.width/2, canvas.height, canvas.width / 2, 0);
        // light blue
        grd.addColorStop(0, "#8ED6FF");
        // dark blue
        grd.addColorStop(1, "#004CB3");

        context.rect(0, 0, canvas.width, canvas.height);
        context.fillStyle = FillStyleUnionType.of(grd);
        context.fill();
    }
    
    private void drawData(CanvasRenderingContext2D context, List<Integer> heights, List<Integer> lakes) {
        
        int numberOfHills = heights.size();
        
        final double sideOffset = 25.0;
        final double drawingAreaWidth = canvas.width - (2 * sideOffset);
        final double drawingAreaHeight = canvas.height - (2 * sideOffset);
        
        double singleHillMaxWidth = drawingAreaWidth / numberOfHills;
        
        int highestHill = Collections.max(heights);
        int lowestHill = Collections.min(heights);
        int delta = highestHill + ((lowestHill >= 0) ? 0 : -lowestHill );
        double verticalUnitScaleRatio = drawingAreaHeight / delta;
        
        double zeroLevel = (highestHill * verticalUnitScaleRatio) + sideOffset;
        
        List<Rect> hillsRectangles = new ArrayList<>(numberOfHills);
        List<Rect> lakesRectangles = new ArrayList<>(numberOfHills);
        List<Rect> underZeroRectangles = new ArrayList<>(numberOfHills);
        
        for (int index = 0; index < heights.size(); index++) {
            
            int h = heights.get(index);
            int l = lakes.get(index);
            
            double width = singleHillMaxWidth;
            double left = sideOffset + (index * singleHillMaxWidth);
            double hillHeight = (h + (lowestHill < 0 ? -lowestHill : 0)) * verticalUnitScaleRatio;
            double lakeHeight = (l + (lowestHill < 0 ? -lowestHill : 0)) * verticalUnitScaleRatio;
            double hillTop = drawingAreaHeight - hillHeight + sideOffset;
            double lakeTop = drawingAreaHeight - lakeHeight + sideOffset;
            
            hillsRectangles.add(new Rect(left, hillTop, width, hillHeight + 2.0));
            lakesRectangles.add(new Rect(left, lakeTop, width, lakeHeight + 2.0 ));
            
            underZeroRectangles.add(new Rect(
                    left, 
                    ((h >= 0) ? zeroLevel : hillTop), 
                    width, 
                    ((h > 0) ? (hillTop + hillHeight - zeroLevel) : hillHeight) + 2.0)
            );
        }
        
        context.fillStyle = FillStyleUnionType.of("#026682");
        context.strokeStyle = StrokeStyleUnionType.of("#026682");
        for (Rect rect : lakesRectangles) {
            context.fillRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
            context.strokeRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
        }
        
        context.fillStyle = FillStyleUnionType.of("#d8c024");
        context.strokeStyle = StrokeStyleUnionType.of("#d8c024");
        for (Rect rect : hillsRectangles) {
            context.fillRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
            context.strokeRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
        }
        
        context.fillStyle = FillStyleUnionType.of("#7c622c");
        context.strokeStyle = StrokeStyleUnionType.of("#7c622c");
        for (Rect rect : underZeroRectangles) {
            context.fillRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
            context.strokeRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
        }
        
        // Draw Zero Level ---------------------------------------------------------------
        context.strokeStyle = StrokeStyleUnionType.of("#888888");
        context.beginPath();
        for (int i = 0; i <= delta; i++) {
            double unitLevel = (i * verticalUnitScaleRatio) + sideOffset;
            context.moveTo(sideOffset, unitLevel);
            context.lineTo(sideOffset + drawingAreaWidth, unitLevel);
        }
        context.stroke();
        
        context.strokeStyle = StrokeStyleUnionType.of("#000000");
        context.beginPath();
        context.moveTo(sideOffset, zeroLevel);
        context.lineTo(sideOffset + drawingAreaWidth, zeroLevel);
        context.stroke();
        
    }
        
    private void draw(List<Integer> hills, List<Integer> lakes) {
        
        final CanvasRenderingContext2D context = (CanvasRenderingContext2D) (Object) canvas.getContext("2d");
        
        canvas.width = 600;
        canvas.height = 400;
        
        context.clearRect(0, 0, canvas.width, canvas.height);
        
        skyGradient(context);
        drawData(context, hills, lakes);
        

        // Rect rect = new Rect(10.0, 10.0, 240.0, 200.0);
        // context.strokeRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());

    }
    
    @PostConstruct
    public void init() {

    }

}
