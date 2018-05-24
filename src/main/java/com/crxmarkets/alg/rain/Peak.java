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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Peak implements Comparable<Peak> {

    /**
     * Indexes of vertexes that constitute a peak
     * A sharp peak has exactly 1 vertex
     * A plateau has more than one vertex
     */
    final List<Integer> indexes;

    Peak(List<Integer> indexes) {
        Objects.requireNonNull(indexes);
        if (indexes.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        return "P(" + indexes.toString() + ')';
    }

    @Override
    public int compareTo(Peak other) {
        if (other == null) {
            return 1;
        } else {
            //TODO: As the list is final, we can calculate and save max() once
            return Collections.max(this.indexes).compareTo(Collections.max(other.indexes));
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.indexes);
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
        if (!Objects.equals(this.indexes, other.indexes)) {
            return false;
        }
        return true;
    }

}
