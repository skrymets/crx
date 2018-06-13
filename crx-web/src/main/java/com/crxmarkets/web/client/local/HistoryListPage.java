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
import com.crxmarkets.web.client.shared.CalculatorResource;
import com.crxmarkets.web.client.shared.HistoryItem;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import elemental2.dom.HTMLDivElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.components.ListComponent;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

/**
 *
 * @author skrymets
 */
@Page(path = "/history")
@Templated(value = "calculator-page.html#history-page-content", stylesheet = "calculator-page.css")
public class HistoryListPage {

    @Inject
    Caller<CalculatorResource> calculatorResource;

    @Inject
    @AutoBound
    private DataBinder<List<HistoryItem>> binder;

    @Inject
    @Bound
    @DataField("history-list")
    private ListComponent<HistoryItem, HistoryItemDisplay> historyList;

    @Inject
    private NavBar navbar;

    @Inject
    private Caller<CalculatorResource> calculatorService;

    @Inject
    @DataField("button-cleanup-history")
    Button buttonCleanupHistory;

    @Inject
    @DataField("modal-calculator-widget")
    HTMLDivElement modal;

    @Inject
    Instance<CalculatorWidget> calculatorWidgetInst;

    private CalculatorWidget calculatorWidget;

    @PostConstruct
    public void setup() {
        populateHistoryList();

        buttonCleanupHistory.addClickHandler((ClickEvent event) -> {
            calculatorResource.call(
                    (Integer response) -> {
                        Window.alert("History is clear. Number of deleted items: " + String.valueOf(response));
                        binder.getModel().clear();
                        populateHistoryList();
                    },
                    (RestErrorCallback) (Request message, Throwable throwable) -> {
                        if (throwable != null) {
                            String details = throwable.getMessage();
                            Window.alert(details);
                        }
                        return false;
                    })
                    .cleanupCalculatorHistory();
        });

        calculatorWidget = calculatorWidgetInst.get();
        modal.appendChild(calculatorWidget.getElement());
    }

    public void onModalCloseButtonClick(final @Observes @CalcWidgetEvent(CalcWidgetEvent.Type.CLOSE) CalculatorWidget widget) {
        hideModal();
        calculatorWidget.setValue(null);
    }

    protected void populateHistoryList() {
        DOMUtil.removeAllElementChildren(historyList.getElement());
        binder.getModel().clear();
        calculatorService
                .call((final List<HistoryItem> his) -> binder.getModel().addAll(his))
                .getCalculatorHistory();
    }

    public void selectComponent(final @Observes HistoryItemDisplay component) {
        HistoryItem model = component.binder.getModel();
        CalculationResult cr = new CalculationResult();
        cr.setInput(Stream
                .of(model.getTask().split(" "))
                .map(Integer::valueOf)
                .collect(Collectors.toList()));
        cr.setLevels(Stream
                .of(model.getCalculation().split(" "))
                .map(Integer::valueOf)
                .collect(Collectors.toList()));
        cr.setTotalVolume(model.getTotal());
        presentResults(cr);
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
