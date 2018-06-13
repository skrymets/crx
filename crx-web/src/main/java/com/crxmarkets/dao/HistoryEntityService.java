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
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author skrymets
 */
@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HistoryEntityService implements HistoryEntityServiceLocal {

    @PersistenceContext(unitName = "default-persistence-unit")
    private EntityManager em;

    @Override
    public List<HistoryItem> getFullHistory() {
        return em.createNamedQuery(HistoryItem.ALL_HISTORY_QUERY, HistoryItem.class).getResultList();
    }

    @Override
    public void create(final HistoryItem newHistoryItem) {
        em.persist(newHistoryItem);
    }

    @Override
    public HistoryItem find(Long id) {
        return em.find(HistoryItem.class, id);
    }

    @Override
    public void update(final HistoryItem historyItem) {
        em.merge(historyItem);
    }

    @Override
    public boolean delete(final Long id) {
        final HistoryItem historyItem = find(id);
        if (historyItem == null) {
            throw new IllegalArgumentException(
                    "Unable to find "
                    + HistoryItem.class.getSimpleName()
                    + " with the given id: "
                    + id);
        } else {
            em.remove(historyItem);
            return true;
        }
    }

    @Override
    public int clearHistory() {

        List<HistoryItem> items = getFullHistory();
        for (HistoryItem item : items) {
            em.remove(item);
        }
        return items.size();
    }

}
