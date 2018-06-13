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
package com.crxmarkets.api;

import com.crxmarkets.dao.HistoryEntityServiceLocal;
import com.crxmarkets.services.VolumeCalculatorServiceLocal;
import com.crxmarkets.web.client.shared.CalculationResult;
import com.crxmarkets.web.client.shared.CalculationTask;
import com.crxmarkets.web.client.shared.CalculatorResource;
import com.crxmarkets.web.client.shared.HistoryItem;
import com.google.gwt.regexp.shared.RegExp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Pattern;

public class CalculatorResourceImpl implements CalculatorResource {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorResource.class);

    @EJB
    private VolumeCalculatorServiceLocal calculatorService;

    @EJB
    private HistoryEntityServiceLocal historyEntityService;

    private final Pattern listOfNumbersPattern = Pattern.compile("(([\\-]?\\d+[ ]))+");

    @Override
    public CalculationResult calculate(CalculationTask task) {

        if (task == null || task.getHeights() == null || task.getHeights().size() < 3) {
            throw new BadRequestException("Task data is incomplete.");
        }

        int[] h = new int[task.getHeights().size()];
        for (int i = 0; i < h.length; i++) {
            h[i] = task.getHeights().get(i);
        }

        int[] waterLevels = calculatorService.calculateWaterLevels(h);
        int totalVolume = calculatorService.calculateTotalVolume(h, waterLevels);

        CalculationResult result = new CalculationResult();

        List<Integer> waterLevelsList = new ArrayList<>();
        for (int i : waterLevels) {
            waterLevelsList.add(i);
        }

        result.setInput(task.getHeights());
        result.setLevels(waterLevelsList);
        result.setTotalVolume(totalVolume);

        return result;
    }

    @Override
    public long saveInHistory(HistoryItem hi) {
        if (hi == null
                || hi.getTask() == null
                || hi.getTask().isEmpty()
                || hi.getDateTime() == null
                || hi.getCalculation() == null
                || hi.getCalculation().isEmpty()) {
            throw new BadRequestException("History item data is incomplete.");
        }

        if (!listOfNumbersPattern.matcher(hi.getCalculation()).matches()
                || !listOfNumbersPattern.matcher(hi.getCalculation()).matches()) {
            throw new BadRequestException("History item data is incomplete.");
        }

        historyEntityService.create(hi);
        return hi.getId();
    }

    @Override
    public List<HistoryItem> getCalculatorHistory() {
        return historyEntityService.getFullHistory();
    }

    @Override
    public int cleanupCalculatorHistory() {
        return historyEntityService.clearHistory();
    }

    @Override
    public HistoryItem getHistoryItem(long id) {
        HistoryItem item = historyEntityService.find(id);
        if (item == null) {
            throw new NotFoundException();
        }
        return item;
    }

    @Override
    public boolean deleteHistoryItem(long id) {
        HistoryItem item = historyEntityService.find(id);
        if (item == null) {
            throw new NotFoundException();
        }
        return historyEntityService.delete(id);
    }

}
