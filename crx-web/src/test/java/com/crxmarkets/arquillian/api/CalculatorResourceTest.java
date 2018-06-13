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
package com.crxmarkets.arquillian.api;

import com.crxmarkets.api.CalculatorResourceImpl;
import com.crxmarkets.dao.HistoryEntityService;
import com.crxmarkets.services.VolumeCalculatorService;
import com.crxmarkets.services.VolumeCalculatorServiceLocal;
import com.crxmarkets.web.client.shared.CalculatorResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author skrymets
 */
@RunWith(Arquillian.class)
public class CalculatorResourceTest {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorResourceTest.class);

    private static final String REST_PATH = "rest";

    @Deployment
    public static WebArchive createDeployment() {

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "crx-test.war")
                .addPackage(VolumeCalculatorServiceLocal.class.getPackage())
                .addPackage(CalculatorResource.class.getPackage())
                .addPackage(CalculatorResourceImpl.class.getPackage())
                .addPackage(HistoryEntityService.class.getPackage())
                .addAsDirectory("WEB-INF/lib")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml") 
                ;

        LOG.info(war.toString(true));
        return war;
    }

    @Test
    public void testInit() {

    }

}
