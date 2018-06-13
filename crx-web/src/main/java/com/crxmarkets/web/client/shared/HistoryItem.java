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
package com.crxmarkets.web.client.shared;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

/**
 *
 * @author skrymets
 */
@Entity
@Portable
@Bindable
@NamedQueries({
    @NamedQuery(name = HistoryItem.ALL_HISTORY_QUERY, query = "SELECT h FROM HistoryItem h ORDER BY h.id")
        // , @NamedQuery(name = HistoryItem.CLEAN_HISTORY_QUERY, query = "DELETE FROM HistoryItem")
})
public class HistoryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ALL_HISTORY_QUERY = "allHistoryItems";
    public static final String CLEAN_HISTORY_QUERY = "deleteAllHistoryItems";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String task;

    private String calculation;

    private int total;

    private Date dateTime;

    public HistoryItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCalculation() {
        return calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof HistoryItem) && ((HistoryItem) obj).getId() != 0 && ((HistoryItem) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return (int) getId();
    }

}
