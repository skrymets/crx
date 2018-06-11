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
import com.crxmarkets.web.client.shared.HistoryItem;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import elemental2.dom.HTMLDivElement;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.slf4j.Logger;
import com.crxmarkets.web.client.local.CalcWidgetEvent.Type;
import javax.enterprise.inject.Instance;
import org.slf4j.LoggerFactory;

/**
 *
 * @author skrymets
 */
@Page(role = DefaultPage.class, path = "/calculator")
@Templated(value = "calculator-page.html#default-page-content", stylesheet = "calculator-page.css")
public class CalculatorPage implements IsElement {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorPage.class);

    @Inject
    Caller<CalculatorResource> calculatorResource;

    @Inject
    @DataField("heights-input")
    TextBox heightsInput;

    @Inject
    @DataField("calculate-button")
    Button calculateButton;

    @Inject
    @DataField("modal-calculator-widget")
    HTMLDivElement modal;

    @Inject
    Instance<CalculatorWidget> calculatorWidgetInst;

    private CalculatorWidget calculatorWidget;

    @PostConstruct
    public void setup() {
        LOG.info("Default page instance {} has been created.", CalculatorPage.class.getName());
        calculatorWidget = calculatorWidgetInst.get();
        modal.appendChild(calculatorWidget.getElement());
    }

    public void onModalCloseButtonClick(final @Observes @CalcWidgetEvent(Type.CLOSE) CalculatorWidget widget) {
        hideModal();
        calculatorWidget.setValue(null);
    }

    @EventHandler("calculate-button")
    public void onCalculateClick(final @ForEvent("click") ClickEvent event) {

        String rawInputString = heightsInput.getText();
        //TODO: Verify the rawInputString

        String[] numbersString = rawInputString.split(" ");
        final List<Integer> hills = Stream
                .of(numbersString)
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        RemoteCallback<CalculationResult> responseCallback = (response) -> {
            saveCalculationToHistory(response);
            presentResults(response);
        };

        ErrorCallback errorCallback = (RestErrorCallback) (Request message, Throwable throwable) -> {
            if (throwable != null) {
                String details = throwable.getMessage();
                Window.alert(details);
            }
            return false;
        };

        CalculationTask task = new CalculationTask();
        task.setHeights(hills);
        calculatorResource.call(responseCallback, errorCallback).calculate(task);

    }

    protected void saveCalculationToHistory(CalculationResult model) {
        HistoryItem hi = new HistoryItem();
        hi.setDateTime(new Date());
        hi.setTotal(model.getTotalVolume());
        hi.setTask(model.getInput().stream().map(String::valueOf).collect(Collectors.joining(" ")));
        hi.setCalculation(model.getLevels().stream().map(String::valueOf).collect(Collectors.joining(" ")));

        calculatorResource.call((Long response) -> {
            LOG.info("Calculation results saved in history with ID: " + response.toString());
        }).saveInHistory(hi);
    }

    private void presentResults(CalculationResult calculationResult) {
        calculatorWidget.setValue(calculationResult);
        showModal();
    }

    private void showModal() {
        modal.classList.add("displayed");
    }

    private void hideModal() {
        modal.classList.remove("displayed");
    }

}
