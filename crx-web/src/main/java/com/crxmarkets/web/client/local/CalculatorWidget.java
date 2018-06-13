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

import com.crxmarkets.web.client.local.CalcWidgetEvent.Type;
import com.crxmarkets.web.client.local.graphics.Rect;
import com.crxmarkets.web.client.shared.CalculationResult;
import com.crxmarkets.web.client.shared.CalculatorResource;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import elemental2.dom.CSSProperties.WidthUnionType;
import elemental2.dom.CanvasGradient;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.CanvasRenderingContext2D.FillStyleUnionType;
import elemental2.dom.CanvasRenderingContext2D.StrokeStyleUnionType;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLDivElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author skrymets
 */
@Templated("calculator-page.html#calculator-widget")
public class CalculatorWidget implements IsElement {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorWidget.class);

    private CalculationResult model;

    @Inject
    Caller<CalculatorResource> calculatorResource;

    @Inject
    @DataField("modal-screen-container")
    HTMLDivElement screenContainer;

    @Inject
    @DataField("calculator-screen")
    HTMLCanvasElement canvas;

    @Inject
    @DataField("modal-calculation-result")
    HTMLDivElement volumeDataLabel;

    @Inject
    @DataField("modal-close-button")
    Button modalCloseButton;

    @Inject
    @CalcWidgetEvent(Type.CLOSE)
    Event<CalculatorWidget> closeEvent;

    @PostConstruct
    public void setup() {
        LOG.info("Calculator Widget instance has been created");
        modalCloseButton.addClickHandler((ClickEvent event) -> {
            closeEvent.fire(CalculatorWidget.this);
        });

    }

    public CalculationResult getValue() {
        return this.model;
    }

    public void setValue(final CalculationResult model) {
        this.model = model;
        volumeDataLabel.textContent = (model == null) ? "" : String.valueOf(model.getTotalVolume());
        draw();
    }

    private void skyGradient(CanvasRenderingContext2D context) {
        // add linear gradient
        CanvasGradient grd = context.createLinearGradient(canvas.width / 2, canvas.height, canvas.width / 2, 0);
        // light blue
        grd.addColorStop(0, "#8ED6FF");
        // dark blue
        grd.addColorStop(1, "#004CB3");

        context.rect(0, 0, canvas.width, canvas.height);
        context.fillStyle = FillStyleUnionType.of(grd);
        context.fill();
    }

    private void drawData(CanvasRenderingContext2D context) {

        List<Integer> heights = model.getInput();
        List<Integer> lakes = model.getLevels();

        int numberOfHills = heights.size();

        final double sideOffset = 25.0;
        final double drawingAreaWidth = canvas.width - (2 * sideOffset);
        final double drawingAreaHeight = canvas.height - (2 * sideOffset);

        double singleHillMaxWidth = drawingAreaWidth / numberOfHills;

        int highestHill = Collections.max(heights);
        int lowestHill = Collections.min(heights);
        int delta = highestHill + ((lowestHill >= 0) ? 0 : -lowestHill);
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
            lakesRectangles.add(new Rect(left, lakeTop, width, lakeHeight + 2.0));

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

//        context.fillText(String.valueOf(canvas.width), 30, 30);
//        context.fillText(String.valueOf(canvas.height), 30, 60);
    }

    private void draw() {

        CanvasRenderingContext2D context = getDrawingContext();
        clearCanvas(context);

        if (model == null) {
            drawNoDataMessage(context);
            return;
        }

        skyGradient(context);
        drawData(context);

        // Rect rect = new Rect(10.0, 10.0, 240.0, 200.0);
        // context.strokeRect(rect.getX(), rect.getY(), rect.getW(), rect.getH());
    }

    protected void drawNoDataMessage(CanvasRenderingContext2D context) {
        context.save();
        context.fillStyle = FillStyleUnionType.of("#e57e16");
        context.font = "20pt Sans";
        context.textAlign = "center";
        context.fillText("There is no data to plot.", canvas.width / 2, canvas.height / 2);
        context.restore();
    }

    protected CanvasRenderingContext2D getDrawingContext() {
        return (CanvasRenderingContext2D) (Object) canvas.getContext("2d");
    }

    protected void clearCanvas() {
        CanvasRenderingContext2D context2D = getDrawingContext();
        clearCanvas(context2D);
    }

    protected void clearCanvas(final CanvasRenderingContext2D context) {

        canvas.style.width = WidthUnionType.of("100%");
        canvas.width = canvas.offsetWidth; //screenContainer.offsetWidth
        canvas.height = 400;

        context.clearRect(0, 0, canvas.width, canvas.height);
    }

}
