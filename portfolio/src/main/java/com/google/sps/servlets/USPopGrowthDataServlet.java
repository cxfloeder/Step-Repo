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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pop-data")
public class USPopGrowthDataServlet extends HttpServlet {
    private static final String JSON_TYPE = "application/json";
    private static final String POP_DATA_FILE = "/WEB-INF/us-pop-growth-rate-by-year.csv";
    private LinkedHashMap<Integer, Double> populationGrowth = new LinkedHashMap<Integer, Double>();

    /**
     * Read and store population data from csv file.
     */
    @Override
    public void init() {
        Scanner scan = new Scanner(getServletContext().getResourceAsStream(POP_DATA_FILE));

        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] data = line.split(",");

            Integer year = Integer.valueOf(data[0]);
            Double growthRate = Double.valueOf(data[1]);

            populationGrowth.put(year, growthRate);
        }
        scan.close();
    } 

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Convert the LinkedHashMap to a JSON String.
        String jsonMessage = messageListAsJson(populationGrowth);

        // Send the JSON message as the response.
        response.setContentType(JSON_TYPE);
        response.getWriter().println(jsonMessage);
    }

    /**
     * Converts a Java LinkedHashMap into a JSON string using Gson.  
     */
    private String messageListAsJson(LinkedHashMap<Integer, Double> output) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(output);
        return jsonMessage;
    }
}