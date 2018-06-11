/**
 * Copyright (C) 2016 Red Hat, Inc. and/or its affiliates.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crxmarkets.web.client.local;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.MouseEvent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated(value = "calculator-page.html#history-item", stylesheet = "calculator-page.css")
public class HistoryItemDisplay extends BaseHistoryItemView implements IsElement {

    @Inject
    @DataField("history-item")
    private HTMLDivElement historyItem;

    @Inject
    @Bound(property = "task")
    @DataField("hi-calculator-task")
    private HTMLDivElement taskData;

    @Inject
    @Bound(property = "total")
    @DataField("hi-total-volume")
    private HTMLDivElement totalVolume;

    @Inject
    @Bound(property = "calculation")
    @DataField("hi-calculated-levels")
    private HTMLDivElement calculatedLevels;

    @Inject
    @Bound(converter = DateConverter.class, property = "dateTime")
    @DataField("hi-task-date")
    private HTMLDivElement taskDate;

    @Inject
    private Event<HistoryItemDisplay> click;

    @EventHandler("history-item")
    public void onClick(final @ForEvent("click") MouseEvent event) {
        click.fire(this);
    }

//    public void setSelected(final boolean selected) {
//        if (selected) {
//            historyItem.classList.remove("selected");
//        } else {
//            historyItem.classList.add("selected");
//        }
//    }

}
