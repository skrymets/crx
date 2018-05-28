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
public class Calculator2Test {

    public Calculator2Test() {
    }

    @Test
    public void testDiscoverNotEnoughData() {
        System.out.println("testDiscoverNotEnoughData");
        List<Peak> discoverPeaks;

        discoverPeaks = (new Calculator2()).discoverPeaks(new int[]{});
        assertNotNull(discoverPeaks);
        assertTrue(discoverPeaks.isEmpty());

        discoverPeaks = (new Calculator2()).discoverPeaks(new int[]{1, 2});
        assertNotNull(discoverPeaks);
        assertTrue(discoverPeaks.isEmpty());
    }

    @Test
    public void testDiscoverFlatFieldOnly() {
        System.out.println("testDiscoverFlatFieldOnly");
        List<Peak> discoverPeaks = (new Calculator2())
                .discoverPeaks(new int[]{1, 1, 1, 1, 1});

        assertNotNull(discoverPeaks);
        // A flat field is actually a single peak with a plateau on top of it.
        assertEquals(1, discoverPeaks.size());

        List<Peak> expected = asList(
                new Peak(asList(new Integer[]{0, 1, 2, 3, 4}), 1)
        );
        String discoveredAsText = discoverPeaks.toString();
        System.out.println(discoveredAsText);
        assertEquals(expected.toString(), discoveredAsText);
    }

    @Test
    public void testDiscoverAlwaysAscent() {
        System.out.println("testDiscoverAlwaysAscent");
        List<Peak> discoverPeaks = (new Calculator2())
                .discoverPeaks(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        assertNotNull(discoverPeaks);
        System.out.println(discoverPeaks);

        // "Always ascent" is the same as the only peak at the most right
        assertEquals(1, discoverPeaks.size());
        List<Peak> expected = asList(
                new Peak(asList(new Integer[]{9}), 10)
        );
        assertEquals(expected.toString(), discoverPeaks.toString());
    }

    @Test
    public void testDiscoverAlwaysDescent() {
        System.out.println("testDiscoverAlwaysDescent");
        List<Peak> discoverPeaks = (new Calculator2())
                .discoverPeaks(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
        assertNotNull(discoverPeaks);
        System.out.println(discoverPeaks);

        // "Always descent" is the same as the only peak at the most left
        assertEquals(1, discoverPeaks.size());
        List<Peak> expected = asList(
                new Peak(asList(new Integer[]{0}), 10)
        );
        assertEquals(expected.toString(), discoverPeaks.toString());
    }

    @Test
    public void testDiscoverSingleMountain() {
        System.out.println("testDiscoverSingleMountain");
        List<Peak> discoverPeaks = (new Calculator2())
                .discoverPeaks(new int[]{1, 2, 3, 4, 5, 4, 3, 2, 1});
        assertNotNull(discoverPeaks);

        System.out.println(discoverPeaks.toString());
        assertEquals(discoverPeaks.size(), 1);
    }

    @Test
    public void testDiscoverPeaksOnSides() {
        System.out.println("testDiscoverPeaksOnSides");
        List<Peak> discoverPeaks = (new Calculator2())
                .discoverPeaks(new int[]{10, 2, 2, 10});
        assertNotNull(discoverPeaks);
        System.out.println(discoverPeaks);
        assertEquals(2, discoverPeaks.size());

        List<Peak> expected;
        expected = asList(
                new Peak(asList(new Integer[]{0}), 10),
                new Peak(asList(new Integer[]{3}), 10)
        );
        assertEquals(expected.toString(), discoverPeaks.toString());

        // -------------------------------------------------------------------------------
        discoverPeaks = (new Calculator2())
                .discoverPeaks(new int[]{10, 10, 2, 10});

        expected = asList(
                new Peak(asList(new Integer[]{0, 1}), 10),
                new Peak(asList(new Integer[]{3}), 10)
        );
        System.out.println(discoverPeaks);
        assertEquals(expected.toString(), discoverPeaks.toString());
    }

    @Test
    public void testDiscoverArbitraryPeaks() {

        System.out.println("testDiscoverArbitraryPeaks");

        List<Peak> expected;

        // -------------------------------------------------------------------------------
        //  11, 19, 1, 5, 4, 2, 8, 2, 17, 3, 12, 12, 18, 9, 12, 10, 9, 20, 2, 10, 1, 10, 10, 12, 8
        //  0   1   2  3  4  5  6  7  8   9  10  11  12  13 14  15  16 17  18 19  20 21  22  23  24
        //      ^      ^        ^     ^               ^     ^          ^      ^              ^      
        int[] heights1 = new int[]{
            11, 19, 1, 5, 4, 2, 8, 2, 17, 3, 12, 12, 18, 9, 12, 10, 9, 20, 2, 10, 1, 10, 10, 12, 8
        };

        expected = asList(
                new Peak(asList(new Integer[]{1}), 19),
                new Peak(asList(new Integer[]{3}), 5),
                new Peak(asList(new Integer[]{6}), 8),
                new Peak(asList(new Integer[]{8}), 17),
                new Peak(asList(new Integer[]{12}), 18),
                new Peak(asList(new Integer[]{14}), 12),
                new Peak(asList(new Integer[]{17}), 20),
                new Peak(asList(new Integer[]{19}), 10),
                new Peak(asList(new Integer[]{23}), 12)
        );
        List<Peak> discoverPeaks = (new Calculator2()).discoverPeaks(heights1);

        String discoveredAsText = discoverPeaks.toString();
        System.out.println(discoveredAsText);

        assertEquals(expected.toString(), discoveredAsText);
        // -------------------------------------------------------------------------------

        // -------------------------------------------------------------------------------
        //  6, 7, 0, 19, 19, 4, 8, 8, 8, 14, 14, 14, 12, 13, 8, 8, 12, 18, 12, 4, 11, 12, 4, 20, 2
        //  0  1  2  3   4   5  6  7  8  9   10  11  12  13  14 15 16  17  18  19 20  21 22  23  24
        //     ^     ^   ^               ^   ^   ^       ^             ^              ^      ^ 
        int[] heights2 = new int[]{
            6, 7, 0, 19, 19, 4, 8, 8, 8, 14, 14, 14, 12, 13, 8, 8, 12, 18, 12, 4, 11, 12, 4, 20, 2
        };
        expected = asList(
                new Peak(asList(new Integer[]{1}), 7),
                new Peak(asList(new Integer[]{3, 4}), 19),
                new Peak(asList(new Integer[]{9, 10, 11}), 14),
                new Peak(asList(new Integer[]{13}), 13),
                new Peak(asList(new Integer[]{17}), 18),
                new Peak(asList(new Integer[]{21}), 12),
                new Peak(asList(new Integer[]{23}), 20)
        );
        discoverPeaks = (new Calculator2()).discoverPeaks(heights2);

        discoveredAsText = discoverPeaks.toString();
        System.out.println(discoveredAsText);

        assertEquals(expected.toString(), discoveredAsText);
        // -------------------------------------------------------------------------------
    }

    @Test
    public void testDiscoverLakesInvalidInput() {
        System.out.println("testDiscoverLakesInvalidInput");
        Calculator2 calc = new Calculator2();
        List<Lake> discoveredLakes = null; // new LinkedList<>();
        calc.discoverLakes(null, discoveredLakes);
        assertTrue(true);

        discoveredLakes = new LinkedList<>();
        calc.discoverLakes(null, discoveredLakes);
        assertNotNull(discoveredLakes);
        assertTrue(discoveredLakes.isEmpty());
    }

    @Test
    public void testDiscoverNoLakesOnSinglePeak() {
        System.out.println("testDiscoverNoLakesOnSinglePeak");
        int[] heights = new int[]{0, 5, 5, 5, 0};
        List<Lake> discoveredLakes = new LinkedList<>();

        Calculator2 calc = new Calculator2();
        List<Peak> peaks = calc.discoverPeaks(heights);
        calc.discoverLakes(new LinkedList<>(singletonList(peaks)), discoveredLakes);
        assertTrue(discoveredLakes.isEmpty());
    }

    @Test
    public void testDiscoverSimpleLakes() {
        System.out.println("testDiscoverSimpleLakes");

        int[] heights = new int[]{3, 0, 0, 8, 4, 6};

        Calculator2 calc = new Calculator2();
        List<Peak> peaks = calc.discoverPeaks(heights);
        List<Lake> discoveredLakes = new LinkedList<>();

        calc.discoverLakes(new LinkedList<>(singletonList(peaks)), discoveredLakes);

        Collections.sort(discoveredLakes);
        List<Lake> expected = asList(
                new Lake(1, 2, 3),
                new Lake(4, 4, 6)
        );

        String discoveredAsText = discoveredLakes.toString();
        System.out.println(discoveredAsText);
        assertEquals(expected.toString(), discoveredAsText);
    }

    @Test
    public void testDiscoverNoLakesOnPureSlopes() {
        System.out.println("testDiscoverNoLakesOnPureSlopes");

        int[] slopeDown = new int[]{5, 4, 3, 2, 1, 0};
        Calculator2 calc = new Calculator2();
        List<Peak> peaks = calc.discoverPeaks(slopeDown);
        List<Lake> discoveredLakes = new LinkedList<>();
        calc.discoverLakes(new LinkedList<>(singletonList(peaks)), discoveredLakes);
        assertTrue(discoveredLakes.isEmpty());

        int[] slopeUp = new int[]{0, 1, 2, 3, 4, 5};
        calc = new Calculator2();
        peaks = calc.discoverPeaks(slopeUp);
        calc.discoverLakes(new LinkedList<>(singletonList(peaks)), discoveredLakes);
        assertTrue(discoveredLakes.isEmpty());
    }
}
