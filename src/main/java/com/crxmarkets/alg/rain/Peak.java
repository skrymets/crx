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

import java.util.List;
import java.util.Objects;

public class Peak implements Comparable<Peak> {

    /**
     * Indexes of vertexes that constitute a peak
     * A sharp peak has exactly 1 vertex
     * A plateau has more than one vertex
     */
    final List<Integer> indexes;

    final Integer height;

    Peak(List<Integer> indexes, Integer h) {
        Objects.requireNonNull(indexes);
        Objects.requireNonNull(h);
        if (indexes.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.indexes = indexes;
        this.height = h;
    }

    @Override
    public String toString() {
        return "P(" + indexes.toString() + " @ " + height + ')';
    }

    @Override
    public int compareTo(Peak other) {
        if (other == null) {
            return 1;
        } else {
            return this.height.compareTo(other.height);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.indexes);
        hash = 59 * hash + Objects.hashCode(this.height);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Peak other = (Peak) obj;
        if (!Objects.equals(this.height, other.height)) {
            return false;
        }
        if (!Objects.equals(this.indexes, other.indexes)) {
            return false;
        }
        return true;
    }

}
