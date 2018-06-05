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

import com.crxmarkets.web.client.shared.CalculationResult;
import com.crxmarkets.web.client.shared.CalculationTask;
import com.crxmarkets.web.client.shared.CalculatorResource;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.CanvasRenderingContext2D.FillStyleUnionType;
import elemental2.dom.CanvasRenderingContext2D.StrokeStyleUnionType;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLDivElement;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static elemental2.dom.DomGlobal.document;

/**
 *
 * @author skrymets
 */
@Templated("CalculatorWidget.html#calcWidget")
public class CalculatorWidget implements IsElement {

    static final int height = 400;
    static final int width = 500;


    @Inject
    Caller<CalculatorResource> calculatorResource;

    @Inject
    @DataField
    Label calculationResultData;

    @Inject
    @DataField
    Button calculateButton;

    @Inject
    @DataField
    Button drawButton;

    @Inject
    @DataField("calculatorScreen")
    HTMLCanvasElement canvas;

    @EventHandler("drawButton")
    public void onDrawClick(final @ForEvent("click") ClickEvent event) {
    }

    @EventHandler("calculateButton")
    public void onCalculateClick(final @ForEvent("click") ClickEvent event) {

        RemoteCallback<CalculationResult> callback = (CalculationResult response) -> {
            calculationResultData.setText(response.toString());
        };

        CalculationTask task = new CalculationTask();
        task.setHeights(Arrays.asList(3, 2, 1, 0, 0, 2, 1, 0, 2));
        //task.setHeights("3,2,1,0,0,2,1,0,2");

        calculatorResource.call(callback).calculate(task);

    }

    @PostConstruct
    public void init() {
        
        canvas.width = width;
        canvas.height = height;

        final CanvasRenderingContext2D context = (CanvasRenderingContext2D) (Object) canvas.getContext("2d");
        context.clearRect(0, 0, canvas.width, canvas.height);

        context.fillStyle = FillStyleUnionType.of("#ffffff");
        context.strokeStyle = StrokeStyleUnionType.of("#000000");

        context.beginPath();
        context.moveTo(10, 10);
        context.lineTo(40, 40);
        context.stroke();
    }

}
