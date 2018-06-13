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
import com.crxmarkets.services.VolumeCalculatorServiceLocal;
import com.crxmarkets.web.client.shared.CalculatorResource;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
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

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "crx-test.war")
                .addPackage(VolumeCalculatorServiceLocal.class.getPackage())
                .addPackage(CalculatorResource.class.getPackage())
                .addPackage(CalculatorResourceImpl.class.getPackage())
                .addPackage(HistoryEntityService.class.getPackage())
                .addAsDirectory("WEB-INF/lib")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("log4j.properties", "log4j.properties")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");

        LOG.info(war.toString(true));
        return war;
    }

    @Test
    public void testEchoEndpoint() {
        LOG.info("URL: {}", deploymentURL.toString());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(deploymentURL.toString() + "rest/calculator/echo/abcd");
        String result = target.request().get(String.class);
        LOG.info("Result: {}", result);
    }

    @Test
    public void testSimpleCalculateTask() {
//
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target(deploymentURL.toString() + "rest/calculator/task");
//
//        CalculationTask task = new CalculationTask();
//        task.setHeights(asList(new Integer[]{3, 2, 1, 4}));
//        System.out.println(task.toString());
//
//        CalculationResult response = target.request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(task), CalculationResult.class);
//
//        List<Integer> expectedResult = asList(new Integer[]{3, 3, 3, 4});
//
//        assertNotNull(response);
//        assertTrue(response.getTotalVolume() == 3);
//        assertEquals(response.getLevels(), expectedResult);
    }

//    @Test
//    public void testFailedCalculateTask() {
//    }
//
//    @Test
//    public void testCreateCorrectHistoryItem() {
//    }
//
//    @Test
//    public void testCreateInvalidHistoryItem() {
//    }
//
//    @Test
//    public void testGetExistingHistoryItem() {
//    }
//
//    @Test
//    public void testGetNonExistingHistoryItem() {
//    }
//
//    @Test
//    public void testGetAllHistoryData() {
//    }
//
//    @Test
//    public void testCleanupAllHistoryData() {
//    }

}
