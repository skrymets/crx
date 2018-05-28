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
package com.crxmarkets.alg.rain;

import com.crxmarkets.alg.rain.PeakMatcher.Trend;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author skrymets
 */
public class Calculator2 {

    public static void main(String[] args) {
    }

    public List<Peak> discoverPeaks(int[] heights) {

        // Is there enough data for at least two peaks?
        if (heights == null || heights.length < 3) {
            return Collections.emptyList();
        }

        // Find all peacks ---------------------------------------------------------------
        List<Peak> peaks = new ArrayList<>();

        int prevH = heights[0]; // previous' height initial value;

        PeakMatcher pattern = PeakMatcher.start(0);

        for (int i = 1; i < heights.length; i++) {
            int currentH = heights[i];

            /*
                positive difference means - going UP, 
                0(zero) means - plateau, 
                negative difference means - going DOWN
             */
            int diff = currentH - prevH;
            Trend direction = (diff == 0)
                    ? Trend.FLAT
                    : (diff > 0)
                            ? Trend.UP
                            : Trend.DOWN;

            PeakMatcher next = pattern.nextStep(direction, i);
            if (next.matchPattern()) {
                peaks.add(next.peak());
                next = next.nextStep(direction, i); // start new discovery iteration
            }
            pattern = next;
            prevH = currentH;
        }

        // Flush the remainig state if any.
        PeakMatcher next = pattern.nextStep(Trend.DOWN, heights.length);
        if (next.matchPattern()) {
            peaks.add(next.peak());
        }

        return peaks;
    }
}