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

import java.util.LinkedList;
import java.util.List;

/**
 * @author skrymets
 */
public interface PeakMatcher {

    static PeakMatcher start(Integer index) {
        return new InitialState(index);
    }

    public static enum Trend {
        UP, FLAT, DOWN
    }

    public PeakMatcher nextStep(Trend newTrend, Integer index);

    public boolean matchPattern();

    public Peak peak(Integer height);
}

class GoingDownState implements PeakMatcher {

    private final List<Integer> peakIndexes;

    GoingDownState(List<Integer> peakIndexes) {
        this.peakIndexes = peakIndexes;
    }

    @Override
    public PeakMatcher nextStep(PeakMatcher.Trend newTrend, Integer index) {
        switch (newTrend) {
            case UP:
                peakIndexes.add(index);
                return new GoingUpState(peakIndexes);
            case FLAT:
            // Important note!
            // When descending - a plateau can never lead us to a peak.
            // A peak starts only after ascent.
            // So, we consider that we still are descending
            // This will prevent us to resolve uncertainty about whether
            // this plateau should end up with a peak or not.
            case DOWN:
                return this;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public boolean matchPattern() {
        return false;
    }

    @Override
    public Peak peak(Integer height) {
        return null;
    }
}

class GoingUpState implements PeakMatcher {

    private final List<Integer> peakIndexes;

    GoingUpState(List<Integer> peakIndexes) {
        this.peakIndexes = peakIndexes;
    }

    @Override
    public PeakMatcher nextStep(PeakMatcher.Trend newTrend, Integer index) {
        switch (newTrend) {
            case UP:
                peakIndexes.clear();
                peakIndexes.add(index);
                return this;
            case FLAT:
                peakIndexes.add(index);
                return new OnPlateauState(peakIndexes);
            case DOWN:
                return new CompletePatternState(peakIndexes);
            default:
                throw new AssertionError();
        }
    }

    @Override
    public boolean matchPattern() {
        return false;
    }

    @Override
    public Peak peak(Integer height) {
        return null;
    }
}

class OnPlateauState implements PeakMatcher {

    private final List<Integer> peakIndexes;

    OnPlateauState(List<Integer> peakIndexes) {
        this.peakIndexes = peakIndexes;
    }

    @Override
    public PeakMatcher nextStep(PeakMatcher.Trend newTrend, Integer index) {
        switch (newTrend) {
            case UP:
                peakIndexes.clear();
                peakIndexes.add(index);
                return new GoingUpState(peakIndexes);
            case FLAT:
                peakIndexes.add(index);
                return this;
            case DOWN:
                return new CompletePatternState(peakIndexes);
            default:
                throw new AssertionError();
        }
    }

    @Override
    public boolean matchPattern() {
        return false;
    }

    @Override
    public Peak peak(Integer height) {
        return null;
    }
}

class InitialState implements PeakMatcher {

    private final List<Integer> peakIndexes = new LinkedList<>();

    InitialState(Integer initialIndex) {
        peakIndexes.add(initialIndex);
    }

    @Override
    public PeakMatcher nextStep(Trend newTrend, Integer index) {

        switch (newTrend) {
            case UP:
                peakIndexes.clear();
                peakIndexes.add(index);
                return new GoingUpState(peakIndexes);
            case FLAT:
                peakIndexes.add(index);
                return new OnPlateauState(peakIndexes);
            case DOWN:
                return new CompletePatternState(peakIndexes);
            default:
                throw new AssertionError();
        }
    }

    @Override
    public boolean matchPattern() {
        return false;
    }

    @Override
    public Peak peak(Integer height) {
        return null;
    }

}

class CompletePatternState implements PeakMatcher {

    private final List<Integer> peakIndexes;

    CompletePatternState(List<Integer> peakIndexes) {
        this.peakIndexes = peakIndexes;
    }

    @Override
    public PeakMatcher nextStep(Trend newTrend, Integer index) {
        switch (newTrend) {
            case UP:
                peakIndexes.clear();
                peakIndexes.add(index);
                return new GoingUpState(peakIndexes);
            case FLAT:
            // Important note!
            // After complete state a new plateau can never lead us to a peak.
            // A peak starts only after ascent.
            // So, we consider that we still are descending
            // Dis will prevent us to resolve uncertainty about whether
            // this plateau should end up with a peak or not.
            case DOWN:
                peakIndexes.clear();
                return new GoingDownState(peakIndexes);
            default:
                throw new AssertionError();
        }
    }

    @Override
    public boolean matchPattern() {
        return true;
    }

    @Override
    public Peak peak(Integer height) {
        return new Peak(new LinkedList<>(peakIndexes), height);
    }
}
