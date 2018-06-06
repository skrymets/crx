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
package com.crxmarkets.web.client.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 *
 * @author skrymets
 */
@Portable
public class CalculationTask implements Serializable {

    private static final long serialVersionUID = 327336346849922741L;

    private List<Integer> heights;

    public CalculationTask() {
    }

    public List<Integer> getHeights() {
        return heights;
    }

    public void setHeights(List<Integer> heights) {
        this.heights = heights;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.heights);
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
        final CalculationTask other = (CalculationTask) obj;
        if (!Objects.equals(this.heights, other.heights)) {
            return false;
        }
        return true;
    }

}
