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

import com.crxmarkets.web.client.shared.CalculatorResource;
import com.crxmarkets.web.client.shared.HistoryItem;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.components.ListComponent;
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
    @AutoBound
    private DataBinder<List<HistoryItem>> binder;

    @Inject
    @Bound
    @DataField("history-list")
    private ListComponent<HistoryItem, HistoryItemDisplay> historyList;

    @Inject
    private NavBar navbar;

    @Inject
    private Caller<CalculatorResource> contactService;

    @PostConstruct
    public void setup() {
        contactService
                .call((final List<HistoryItem> his) -> binder.getModel().addAll(his))
                .getCalculatorHistory();

        //DOMUtil.removeAllElementChildren(historyList.getElement());
        historyList.setSelector(display -> display.setSelected(true));
        historyList.setDeselector(display -> display.setSelected(false));

    }

    public void selectComponent(final @Observes HistoryItemDisplay component) {
        if (historyList.getSelectedComponents().contains(component)) {
            historyList.deselectAll();
        } else {
            historyList.deselectAll();
            historyList.selectComponent(component);
        }
    }
}
