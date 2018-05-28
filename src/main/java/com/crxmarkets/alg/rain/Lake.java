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
public class Lake {

    final Integer leftBoundary;
    final Integer rightBoundary;
    final Integer surface;

    public Lake(Integer leftShore, Integer rightShore, Integer surface) {
        Objects.requireNonNull(leftShore);
        Objects.requireNonNull(rightShore);
        Objects.requireNonNull(surface);
        if ((leftShore > rightShore) || leftShore < 0 || rightShore < 0) {
            throw new IllegalArgumentException("Invalid boundaries indexes");
        }
        this.leftBoundary = leftShore;
        this.rightBoundary = rightShore;
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

}
