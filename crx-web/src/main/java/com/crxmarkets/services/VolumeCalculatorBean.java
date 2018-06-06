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
package com.crxmarkets.services;

import com.crxmarkets.alg.rain.Calculator2;
import com.crxmarkets.alg.rain.Lake;
import com.crxmarkets.alg.rain.Peak;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local
public class VolumeCalculatorBean implements VolumeCalculatorLocalBean {

    @Override
    public int[] calculateWaterLevels(int[] heights) {

        if (heights == null || heights.length == 0) {
            return new int[]{};
        }

        Calculator2 calc = new Calculator2();
        List<Peak> peaks = calc.discoverPeaks(heights);
        List<Lake> lakes = new ArrayList<>();
        LinkedList<List<Peak>> taskQueue = new LinkedList<>();
        taskQueue.add(peaks);
        calc.discoverLakes(taskQueue, lakes, heights);

        int[] levels = Arrays.copyOf(heights, heights.length);
        for (Lake lake : lakes) {
            for (int i = lake.getLeftBoundary(); i <= lake.getRightBoundary(); i++) {
                levels[i] = lake.getSurface();
            }
        }

        return levels;
    }

    @Override
    public int calculateTotalVolume(int[] heights, int[] levels) {
        if (heights == null
                || levels == null
                || heights.length == 0
                || levels.length == 0
                || heights.length != levels.length) {
            return Integer.MIN_VALUE;
        }

        int totalVolume = 0;
        for (int i = 0; i < heights.length; i++) {
            totalVolume += (levels[i] - heights[i]);
        }

        return totalVolume;

    }

}
