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

import static java.util.Arrays.asList;
import java.util.Collections;
import static java.util.Collections.singletonList;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author skrymets
 */
public class IssuesTest {

    public IssuesTest() {
    }


    @Test
    public void testIssueBetweenToSidePeaks() {
        System.out.println("testIssueBetweenToSidePeaks");
        int[] heights = new int[]{4, 3, 2, 3, 2, 3, 5};
        final Calculator2 calc = new Calculator2();
        List<Peak> discoverPeaks = calc.discoverPeaks(heights);
        assertEquals(3, discoverPeaks.size());

        List<Peak> expectedPeaks = asList(
                new Peak(asList(new Integer[]{0}), 4),
                new Peak(asList(new Integer[]{3}), 3),
                new Peak(asList(new Integer[]{6}), 5)
        );
        
        String discoveredAsText = discoverPeaks.toString();
        System.out.println(discoveredAsText);
        assertEquals(expectedPeaks.toString(), discoveredAsText);
        

        List<Lake> discoveredLakes = new LinkedList<>();
        calc.discoverLakes(new LinkedList<>(singletonList(discoverPeaks)), discoveredLakes, heights);

        Collections.sort(discoveredLakes);
        List<Lake> expectedLakes = asList(
                new Lake(1, 5, 4)
        );

        discoveredAsText = discoveredLakes.toString();
        System.out.println(discoveredAsText);
        assertEquals(expectedLakes.toString(), discoveredAsText);
    }

}
