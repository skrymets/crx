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
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.Templated;

/**
 *
 * @author skrymets
 */
@Page(role = DefaultPage.class, path = "/calculator")
@Templated(value = "calculator-page.html#default-page-content", stylesheet = "calculator-page.css")
public class CalculatorPage implements IsElement {

    @Inject
    Instance<CalculatorWidget> calculatorWidget;

    @Inject
    JQuery $;
    
    @PostConstruct
    public void setup() {
        $.wrap(getElement()).children().first().after(calculatorWidget.get().getElement());
    }

}
