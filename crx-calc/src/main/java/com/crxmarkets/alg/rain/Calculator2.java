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
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author skrymets
 */
public class Calculator2 {

    static List<Peak> filterPeaksByIndexes(List<Peak> peaks, int start, int end) {
        if (peaks == null || peaks.isEmpty() || start > end || start < 0 || end < 0) {
            return Collections.emptyList();
        }
        return peaks.stream().filter((Peak p) -> {
            int firstIndex = p.indexes.get(0);
            int lastIndex = p.indexes.get(p.indexes.size() - 1);
            return (firstIndex >= start && firstIndex <= end) || (lastIndex >= start && lastIndex <= end);
        }).collect(Collectors.toList());
    }

    static int getLastPeakIndex(Peak peak) {
        Objects.requireNonNull(peak);
        return peak.indexes.get(peak.indexes.size() - 1);
    }

    public void discoverLakes(Deque<List<Peak>> islands, List<Lake> lakesAccumulator, int[] heights) {

        if (heights == null || heights.length == 0) {
            return;
        }

        if (islands == null || islands.isEmpty()) {
            return;
        }

        List<Peak> peaks = islands.pollFirst();
        if (peaks == null || peaks.size() < 2) {
            // Lakes can exist only between two peaks
            // thus there is no lake in this island
            discoverLakes(islands, lakesAccumulator, heights);
            return;
        }
        /* -------------------------------------------------------------------------------
         * Overview 
         * -------------------------------------------------------------------------------
         * 1. Find two highest peaks
         * 2. Create a lake between them (see the note below)
         * 3. Remove all the peaks that were sank by this lake from further processing
         *    by processing only leftmost and rightmost remainig parts of the current 
         *    island (including these two highests peaks)
         */

        // Step 1. ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Peak highestOne = null;
        Peak highestTwo = null;

        for (Peak peak : peaks) {
            if (highestOne == null) {
                highestOne = peak;
                continue;
            } else if (highestTwo == null) {
                highestTwo = peak;
                continue;
            }

            boolean higherThanFirst = (highestOne.height < peak.height);
            boolean higherThanSecond = (highestTwo.height < peak.height);

            if (higherThanFirst) {
                if (highestOne.height >= highestTwo.height) {
                    highestTwo = peak;
                } else {
                    highestOne = peak;
                }
                continue;
            }

            if (higherThanSecond) {
                highestTwo = peak;
            }
        }

        if (highestOne == null || highestTwo == null) {
            throw new IllegalStateException("Algorithm was unable to find highest peaks.");
        }

        // Peaks' indexes (intervals) can't intersect or be coincedent, so it's safe to
        // compare max() of their values
        boolean firstIsLeft
                = Collections.max(highestOne.indexes)
                < Collections.max(highestTwo.indexes);

        Peak leftPeak = (firstIsLeft) ? highestOne : highestTwo;
        Peak rightPeak = (firstIsLeft) ? highestTwo : highestOne;

        // Step 2. ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Important note!
        // Peak - doesn't mean a "vertical wall". A peak may start from a slope going up, 
        // and end up with a slope going down. We have to find a point on that slopes 
        // (if any) that corresponds to the surface level (regarding of which peack is 
        // the base).
        int leftShore = getLastPeakIndex(leftPeak);
        int rightShore = rightPeak.indexes.get(0);
        int leftBoundary = leftShore + 1;
        int rightBoundary = rightShore - 1;

        int level = Math.min(leftPeak.height, rightPeak.height);
        if (leftPeak.height == level) { // Left peak is the base
            for (int index = rightShore - 1; index > leftShore; index--) {
                if (heights[index] >= level) {
                    rightBoundary = index - 1;
                } else {
                    break;
                }
            }
        } else {
            for (int index = leftShore + 1; index < rightShore; index++) {
                if (heights[index] >= level) {
                    leftBoundary = index + 1;
                } else {
                    break;
                }
            }
        }

        Lake lake = new Lake(leftBoundary, rightBoundary, level);
        lakesAccumulator.add(lake);

        // Step 3. ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        List<Peak> leftIsland = filterPeaksByIndexes(peaks, 0, leftPeak.indexes.get(0));
        if (leftIsland.size() > 1) {
            islands.addLast(leftIsland);
        }

        List<Peak> rightIsland = filterPeaksByIndexes(peaks, getLastPeakIndex(rightPeak), Integer.MAX_VALUE);
        if (rightIsland.size() > 1) {
            islands.addLast(rightIsland);
        }

        discoverLakes(islands, lakesAccumulator, heights);
    }

    public List<Peak> discoverPeaks(int[] heights) {

        // Is there enough data for at least two peaks?
        if (heights == null || heights.length < 3) {
            return Collections.emptyList();
        }

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
                peaks.add(next.peak(prevH));
                next = next.nextStep(direction, i); // start new discovery iteration
            }
            pattern = next;
            prevH = currentH;
        }

        // Flush the remainig state if any.
        PeakMatcher next = pattern.nextStep(Trend.DOWN, heights.length);
        if (next.matchPattern()) {
            peaks.add(next.peak(prevH));
        }

        return peaks;
    }
}
