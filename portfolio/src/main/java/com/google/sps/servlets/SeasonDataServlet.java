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

import com.google.sps.data.Season;
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
    private static final String JSON_TYPE = "application/json";
    private static final String CHART_PAGE = "/chart.html";
    private static final String SEASON_PARAM = "season";
    private static final String SUMMER_PROP = "Summer";
    private static final String WINTER_PROP = "Winter";
    private static final String FALL_PROP = "Fall";
    private static final String SPRING_PROP = "Spring";
    private HashMap<String, Integer> seasonData = new HashMap<String, Integer>();

    public SeasonDataServlet() {
        super();
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(Season.SEASON_ENTITY);
        PreparedQuery results = datastore.prepare(query);
        seasonData = loadSeasons(results);

        String jsonMessage = messageListAsJson(seasonData);
        response.setContentType(JSON_TYPE);
        response.getWriter().println(jsonMessage);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String season = request.getParameter(SEASON_PARAM);

        Entity seasonEntity = parseForm(season);
        datastore.put(seasonEntity);

        response.sendRedirect(CHART_PAGE);
    }

    /**
     * Converts a Java List<Season> into a JSON string using Gson.  
     */
    private String messageListAsJson(HashMap<String, Integer> seasonData) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(seasonData);
        return jsonMessage;
    }

    private Entity parseForm(String season) {
        return new Season(season).toEntity();
    }

    private HashMap<String, Integer> loadSeasons(PreparedQuery results) {
        HashMap<String, Integer> seasonList = new HashMap<String, Integer>();
        int summerVotes = 0;
        int winterVotes = 0;
        int fallVotes = 0;
        int springVotes = 0;

        // Add the users desired amount of non-empty comments.
        for (Entity entity : results.asIterable()) {
            Season currentSeason = new Season(entity);

            if(currentSeason.season == null) {
                // Do nothing if season is null.
            } else if(currentSeason.season.equals(SUMMER_PROP)) {
                summerVotes++;
            } else if(currentSeason.season.equals(WINTER_PROP)) {
                winterVotes++;
            } else if(currentSeason.season.equals(FALL_PROP)) {
                fallVotes++;
            } else {
                springVotes++;
            }
        }

        seasonList.put(SUMMER_PROP, summerVotes);
        seasonList.put(WINTER_PROP, winterVotes);
        seasonList.put(FALL_PROP, fallVotes);
        seasonList.put(SPRING_PROP, springVotes);

        return seasonList;
    }
}