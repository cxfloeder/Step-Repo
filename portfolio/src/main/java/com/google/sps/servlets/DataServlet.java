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
    private static final String COMMENT_INPUT = "comment-input";
    private static final String NUM_COMMENTS_INPUT = "num-comments";
    private static final String DATASTORE_LABEL = "Task";
    private static final String COMMENTS_URL = "/comments.html";
    private static final String JSON_RESPONSE = "application/json;";

    public DataServlet() {
        super();
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(DATASTORE_LABEL);
        PreparedQuery results = datastore.prepare(query);

        // Get the number of comments the user wants to view.
        int numComments = Integer.parseInt(request.getParameter(NUM_COMMENTS_INPUT));
        
        ArrayList<String> commentList = new ArrayList<String>();
        int counter = 0;

        // Add the users desired amount of non-empty comments.
        for (Entity entity : results.asIterable()) {
            if(counter == numComments)
            {
                break;
            }
            String comment = (String) entity.getProperty(COMMENT_INPUT);
            if(!comment.equals(""))
            {
                commentList.add(comment);
                counter++;
            }
        }
        
        // Convert the java ArrayList<String> data to a JSON String.
        String jsonMessage = messageListAsJson(commentList);

        // Send the JSON message as the response.
        response.setContentType(JSON_RESPONSE);
        response.getWriter().println(jsonMessage);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {   
        // Get the comment input from the user.
        String comment = request.getParameter(COMMENT_INPUT);
        
        Entity taskEntity = new Entity(DATASTORE_LABEL);
        taskEntity.setProperty(COMMENT_INPUT, comment);

        // Store the user comment in datastore.
        datastore.put(taskEntity);

        response.sendRedirect(COMMENTS_URL);
    }

    /**
     * Converts a Java List<String> into a JSON string using Gson.  
     */
    private String messageListAsJson(List<String> commentList) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(commentList);
        return jsonMessage;
    }
}
