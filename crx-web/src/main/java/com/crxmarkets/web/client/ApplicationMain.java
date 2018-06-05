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

import com.crxmarkets.web.client.shared.CalculatorService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import javax.annotation.PostConstruct;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ioc.client.api.EntryPoint;

/**
 *
 * @author skrymets
 */
@EntryPoint
public class ApplicationMain {

    @PostConstruct
    public void init() {

        final Label versionLabel = new Label("What version is it?");

        RemoteCallback<String> versionCallback = new RemoteCallback<String>() {
            @Override
            public void callback(String version) {
                versionLabel.setText(version);
            }
        };

        Button versionButton = new Button("Get the Version...", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RestClient.create(
                        CalculatorService.class,
                        GWT.getHostPageBaseURL() + "rest", versionCallback)
                        .getVersion();
            }
        });

        RootPanel.get().add(versionLabel);
        RootPanel.get().add(versionButton);
    }

}