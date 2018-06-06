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
package com.crxmarkets.web.server;

import com.crxmarkets.services.VolumeCalculatorLocalBean;
import com.crxmarkets.web.client.shared.CalculationResult;
import com.crxmarkets.web.client.shared.CalculationTask;
import com.crxmarkets.web.client.shared.CalculatorResource;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorResourceImpl implements CalculatorResource {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorResource.class);

    @EJB
    private VolumeCalculatorLocalBean calculatorBean;

    @Override
    public CalculationResult calculate(CalculationTask task) {

        // String[] stringHeights = task.getHeights().split(",");
        // int[] h = new int[stringHeights.length];
        int[] h = new int[task.getHeights().size()];
        for (int i = 0; i < h.length; i++) {
            h[i] = task.getHeights().get(i);
            //h[i] = Integer.valueOf(stringHeights[i].trim());
        }

        int[] waterLevels = calculatorBean.calculateWaterLevels(h);
        int totalVolume = calculatorBean.calculateTotalVolume(h, waterLevels);

        CalculationResult result = new CalculationResult();

        List<Integer> waterLevelsList = new ArrayList<>();
        for (int i : waterLevels) {
            waterLevelsList.add(i);
        }
        result.setLevels(waterLevelsList);
        result.setTotalVolume(totalVolume);

        return result;
    }

}
