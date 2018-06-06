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
public class CalculationResult implements Serializable {

    private static final long serialVersionUID = -2271275127575921712L;

    private Integer totalVolume;

    private List<Integer> input;
    
    private List<Integer> levels;

    public CalculationResult() {
    }

    public Integer getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Integer totalVolume) {
        this.totalVolume = totalVolume;
    }

    public List<Integer> getInput() {
        return input;
    }

    public void setInput(List<Integer> input) {
        this.input = input;
    }
    
    public List<Integer> getLevels() {
        return levels;
    }

    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.totalVolume);
        hash = 19 * hash + Objects.hashCode(this.input);
        hash = 19 * hash + Objects.hashCode(this.levels);
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
        final CalculationResult other = (CalculationResult) obj;
        if (!Objects.equals(this.totalVolume, other.totalVolume)) {
            return false;
        }
        if (!Objects.equals(this.input, other.input)) {
            return false;
        }
        if (!Objects.equals(this.levels, other.levels)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return '{' + "vol=" + totalVolume + ", in=" + input  + ", out=" + levels + '}';
    }

}
