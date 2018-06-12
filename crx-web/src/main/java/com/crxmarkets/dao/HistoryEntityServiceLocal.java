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
package com.crxmarkets.dao;

import com.crxmarkets.web.client.shared.HistoryItem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author skrymets
 */
@Local
public interface HistoryEntityServiceLocal {

    void create(final HistoryItem newHistoryItem);

    HistoryItem find(final Long id);

    boolean delete(final Long id);

    List<HistoryItem> getFullHistory();

    void update(final HistoryItem historyItem);

    int clearHistory();

}
