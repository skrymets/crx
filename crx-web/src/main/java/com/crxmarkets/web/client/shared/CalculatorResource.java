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

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author skrymets
 */
@Path("calculator")
public interface CalculatorResource {

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("echo/{data}")
    public String testMethod(@PathParam("data") String data);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("task")
    public CalculationResult calculate(CalculationTask task);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("history")
    public long saveInHistory(HistoryItem historyItem);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("history")
    public List<HistoryItem> getCalculatorHistory();

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("history")
    public int cleanupCalculatorHistory();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("history/{itemId:[0-9]+}")
    public HistoryItem getHistoryItem(@PathParam("itemId") long id);

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("history/{itemId:[0-9]+}")
    public boolean deleteHistoryItem(@PathParam("itemId") long id);

}
