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
package com.crxmarkets.arquillian.services;

import com.crxmarkets.services.VolumeCalculatorService;
import com.crxmarkets.services.VolumeCalculatorServiceLocal;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
//@Ignore
public class GreetingBeanTest {

    private static final Logger LOG = LoggerFactory.getLogger(GreetingBeanTest.class);

    @Deployment
    public static JavaArchive createDeployment() {
//        JBossDeploymentStructureDescriptor descriptor = Descriptors
//                .create(JBossDeploymentStructureDescriptor.class)
//                .getOrCreateDeployment()
//                .getOrCreateExclusions()
//                .getOrCreateModule()
//                .name("org.jboss.logging")
//                .up().up().up();
//
//        LOG.info(descriptor.exportAsString());
//                .addAsManifestResource(new StringAsset(descriptor.exportAsString()), "jboss-deployment-structure.xml")

        JavaArchive jar = ShrinkWrap
                .create(JavaArchive.class, "crx-test.jar")
                .addPackage(VolumeCalculatorServiceLocal.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        LOG.info(jar.toString(true));
        return jar;
    }

    @EJB
    VolumeCalculatorServiceLocal bean;

    @Test
    public void startAndHealthCheck() {
        assertNotNull(bean);

        int volume = bean.calculateTotalVolume(new int[]{}, new int[]{});
        assertTrue(volume == Integer.MIN_VALUE);
        LOG.info(String.valueOf(volume));
    }

}
