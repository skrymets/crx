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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author skrymets
 */
public class Application {

    public static void main(String[] args) {
        //int[] heights = {0, 3, 0, 4, 1, 2, 1, 4, 0, 3, 0};
        int[] heights = {3, 0, 2, 1, 4};

        Calculator2 calc = new Calculator2();

        List<Peak> peaks = calc.discoverPeaks(heights);

        List<Lake> lakes = new ArrayList<>();
        LinkedList<List<Peak>> taskQueue = new LinkedList<>();
        taskQueue.add(peaks);
        calc.discoverLakes(taskQueue, lakes);

        System.out.println("peaks: " + peaks.toString());
        System.out.println("lakes: " + lakes.toString());

    }

}
