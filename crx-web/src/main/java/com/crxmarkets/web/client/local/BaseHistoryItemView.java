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

import com.crxmarkets.web.client.shared.HistoryItem;
import com.google.gwt.user.client.TakesValue;
import javax.inject.Inject;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;

/**
 *
 * @author skrymets
 */
public class BaseHistoryItemView implements TakesValue<HistoryItem> {

    @Inject
    @AutoBound
    protected DataBinder<HistoryItem> binder;

    @Override
    public HistoryItem getValue() {
        return binder.getModel();
    }

    @Override
    public void setValue(final HistoryItem model) {
        binder.setModel(model);
    }

}
