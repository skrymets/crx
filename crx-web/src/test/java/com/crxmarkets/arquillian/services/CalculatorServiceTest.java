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

import com.crxmarkets.alg.rain.Calculator2;
import com.crxmarkets.services.VolumeCalculatorServiceLocal;
import java.io.File;
import java.util.Arrays;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
//@Ignore
public class CalculatorServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorServiceTest.class);

    @Deployment
    public static WebArchive createDeployment() {

        // -------------------------------------------------------------------------------
        // Import Maven runtime dependencies
        // -------------------------------------------------------------------------------
//        File[] runtimeDependencies = Maven.resolver()
//                .loadPomFromFile("pom.xml", "ejb-test")
//                .importRuntimeDependencies()
//                .resolve()
//                //.withTransitivity()
//                .withoutTransitivity()
//                .asFile();
//        
//        LOG.info("Resolved libraries: {}", Arrays.asList(runtimeDependencies));
        
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "service-test.war")
                .addPackage(VolumeCalculatorServiceLocal.class.getPackage())
                .addPackage(Calculator2.class.getPackage())
                //.addAsLibraries(runtimeDependencies)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("log4j.properties", "log4j.properties")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("test-beans.xml", "beans.xml")
                .addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml");
        
        LOG.info(war.toString(true));
        
        return war;
    }

    @EJB
    VolumeCalculatorServiceLocal serviceBean;

    @Test
    public void testCalculateLevelsOnEmptyData() {
        System.out.println("testCalculateLevelsOnEmptyData");
        assertNotNull(serviceBean);

        int[] calculatedLevels = serviceBean.calculateWaterLevels(new int[]{});
        assertArrayEquals(calculatedLevels, new int[]{});
    }

    @Test
    public void testCalculateTotalOnEmptyData() {
        System.out.println("testCalculateTotalOnEmptyData");
        assertNotNull(serviceBean);

        int volume = serviceBean.calculateTotalVolume(new int[]{}, new int[]{});
        assertTrue(volume == Integer.MIN_VALUE);
    }

    @Test
    public void testSimpleLevelsData() {
        System.out.println("testSimpleLevelsData");
        int[] calculatedLevels = serviceBean.calculateWaterLevels(new int[]{3, 2, 1, 4});
        System.out.println(Arrays.toString(calculatedLevels));
        assertArrayEquals(calculatedLevels, new int[]{3, 3, 3, 4});
    }
    
    @Test
    public void testBiasLevelsData() {
        System.out.println("testBiasLevelsData");
        int[] calculatedLevels = serviceBean.calculateWaterLevels(new int[]{3, 2, 1, 0});
        assertArrayEquals(calculatedLevels, new int[]{3, 2, 1, 0});
    }
    
    @Test
    public void testSimpleVolumeData() {
        System.out.println("testSimpleVolumeData");
        int[] heights = new int[]{3, 2, 1, 3};
        
        int[] calculatedLevels = serviceBean.calculateWaterLevels(heights);
        int calculatedTotalVolume = serviceBean.calculateTotalVolume(heights, calculatedLevels);
        assertTrue(calculatedTotalVolume == 3);
    }

}
