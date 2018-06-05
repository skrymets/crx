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
package com.crxmarkets.services;

import java.util.UUID;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local
public class VolumeCalculatorBean implements VolumeCalculatorLocalBean {

    @Override
    public String getImplementationVersion() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Long calculateVolume() {
        return 0L;
    }

}