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

import com.google.gwt.user.client.Window;
import javax.enterprise.context.ApplicationScoped;
import org.jboss.errai.ioc.client.api.UncaughtExceptionHandler;

/**
 *
 * @author skrymets
 */
@ApplicationScoped
public class GlobalExceptionHandler {

    @UncaughtExceptionHandler
    private void handle(Throwable t) {
        String unknown_error = "Unknown error. Check the application logs please.";
        String msg = (t == null)
                ? unknown_error
                : (t.getMessage() == null || t.getMessage().isEmpty())
                        ? unknown_error
                        : t.getMessage();
        Window.alert(msg);
    }
}
