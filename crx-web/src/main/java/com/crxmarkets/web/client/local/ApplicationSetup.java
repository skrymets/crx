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

import com.crxmarkets.web.client.local.JQueryProducer.JQuery;
import com.google.gwt.user.client.ui.RootPanel;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLDocument;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.NavigationPanel;
import org.jboss.errai.ui.shared.api.annotations.DataField;

/**
 *
 * @author skrymets
 */
@EntryPoint
public class ApplicationSetup {

    @Inject
    NavBar navBar;

    @Inject
    Instance<CalculatorWidget> calculatorWidget;

    @Inject
    private JQuery $;

    @Inject
    private HTMLDocument document;

    @PostConstruct
    public void init() {
        $.wrap($.wrap(document.body).children().first()).after(navBar.getElement());
        $.wrap($.wrap(document.body).children().first()).after(calculatorWidget.get().getElement());
    }
}
