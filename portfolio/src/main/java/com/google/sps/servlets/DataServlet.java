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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    ArrayList<String> messageList = new ArrayList<String>(
        Arrays.asList("The weather is rainy.", "Bring an Umbrella.", "Watch out for big puddles."));

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Convert the java ArrayList<String> data to a JSON String.
        String jsonMessage = convertToJsonUsingGson(messageList);

        // Send the JSON message as the response.
        response.setContentType("text/html;");
        response.getWriter().println(jsonMessage);
    }

    /**
     * Converts a Java ArrayList<String> into a JSON string using Gson.  
     */
    private String convertToJsonUsingGson(ArrayList<String> messageList) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(messageList);
        return jsonMessage;
    }
}
