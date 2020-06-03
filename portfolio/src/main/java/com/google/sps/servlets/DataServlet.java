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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
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
    private final DatastoreService datastore;
    private final String inputName = "comment-input";
    private final String datastoreLabel = "Task";

    public DataServlet() {
        super();
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(datastoreLabel);
        PreparedQuery results = datastore.prepare(query);

        ArrayList<String> commentList = new ArrayList<String>();
        for (Entity entity : results.asIterable()) {
            commentList.add((String) entity.getProperty(inputName));
        }

        // Convert the java ArrayList<String> data to a JSON String.
        String jsonMessage = messageListAsJson(commentList);

        // Send the JSON message as the response.
        response.setContentType("application/json;");
        response.getWriter().println(jsonMessage);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the user.
        String comment = request.getParameter(inputName);        
        
        Entity taskEntity = new Entity(datastoreLabel);
        taskEntity.setProperty(inputName, comment);

        // Store the user comment in datastore.
        datastore.put(taskEntity);

        response.sendRedirect("/home.html");
    }

    /**
     * Converts a Java ArrayList<String> into a JSON string using Gson.  
     */
    private String messageListAsJson(ArrayList<String> commentList) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(commentList);
        return jsonMessage;
    }
}
