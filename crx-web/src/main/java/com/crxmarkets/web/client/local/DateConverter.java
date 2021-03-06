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

import org.jboss.errai.databinding.client.api.Converter;

import java.util.Date;

public class DateConverter implements Converter<Date, String> {

    @Override
    public Date toModelValue(final String inValue) {
        if (inValue == null || inValue.equals("")) {
            return null;
        }

        final elemental2.core.JsDate jsDate = new elemental2.core.JsDate(inValue);
        return new Date((long) jsDate.getTime());
    }

    @Override
    public String toWidgetValue(final Date modelValue) {
        if (modelValue == null) {
            return "";
        } else {
            final elemental2.core.JsDate jsDate = new elemental2.core.JsDate(((Long) modelValue.getTime()).doubleValue());
            return jsDate.toLocaleString();
        }
    }

    @Override
    public Class<Date> getModelType() {
        return Date.class;
    }

    @Override
    public Class<String> getComponentType() {
        return String.class;
    }

}
