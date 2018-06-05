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
package com.crxmarkets.web.client;

import com.crxmarkets.web.client.shared.CalculationResult;
import com.crxmarkets.web.client.shared.CalculationTask;
import com.crxmarkets.web.client.shared.CalculatorResource;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import java.util.Arrays;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

/**
 *
 * @author skrymets
 */
@Templated("#calcWidget")
public class CalculatorWidget {
    
    @Inject
    Caller<CalculatorResource> calculatorResource;
    
    @Inject
    @DataField
    Label calculationResultData;
    
    @Inject
    @DataField
    Button calculateButton;
    
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
    
}
