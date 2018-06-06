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

import java.util.Objects;

/**
 *
 * @author skrymets
 */
public class Lake implements Comparable<Lake> {

    final Integer leftBoundary;
    final Integer rightBoundary;
    final Integer surface;

    public Lake(Integer leftBoundary, Integer rightBoundary, Integer surface) {
        Objects.requireNonNull(leftBoundary);
        Objects.requireNonNull(rightBoundary);
        Objects.requireNonNull(surface);
        if ((leftBoundary > rightBoundary) || leftBoundary < 0 || rightBoundary < 0) {
            throw new IllegalArgumentException("Invalid boundaries indexes");
        }
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.surface = surface;
    }

    public Long voulmeOver(Integer[] bottom) {
        Objects.requireNonNull(bottom);
        if (leftBoundary >= bottom.length || rightBoundary >= bottom.length) {
            throw new IllegalArgumentException("Inappropriate bottom data");
        }

        Long volume = 0L;
        for (int i = leftBoundary; i <= rightBoundary; i++) {
            volume += (surface - bottom[i]);
        }

        return volume;
    }

    public Integer getLeftBoundary() {
        return leftBoundary;
    }

    public Integer getRightBoundary() {
        return rightBoundary;
    }

    public Integer getSurface() {
        return surface;
    }

    @Override
    public String toString() {
        return "L(" + leftBoundary + ':' + rightBoundary + " @ " + surface + ')';
    }

    @Override
    public int compareTo(Lake other) {
        if (other == null) {
            return 1;
        }

        // Please mind that lakes can not overlap each other
        return this.rightBoundary.compareTo(other.rightBoundary);
    }

}
