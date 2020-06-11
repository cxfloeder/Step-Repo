// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/season-data")
public class SeasonDataServlet extends HttpServlet {
    private final DatastoreService datastore;
    private static final String DATASTORE_LABEL = "Task";
    private static final String JSON_TYPE = "application/json";
    private static final String CHART_PAGE = "/chart.html";
    private HashMap<String, Integer> seasonData = new HashMap<String, Integer>();

    public SeasonDataServlet() {
        super();
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jsonMessage = messageListAsJson(seasonData);
        response.setContentType(JSON_TYPE);
        response.getWriter().println(jsonMessage);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String season = request.getParameter("season");
        int seasonVotes;

        if(seasonData.containsKey(season)) {
            seasonVotes = seasonData.get(season) + 1;
            seasonData.put(season, seasonVotes);
        } else {
            seasonData.put(season, 1);
        }

        response.sendRedirect(CHART_PAGE);
    }

    /**
     * Converts a Java List<Comment> into a JSON string using Gson.  
     */
    private String messageListAsJson(HashMap<String, Integer> seasonData) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(seasonData);
        return jsonMessage;
    }
}