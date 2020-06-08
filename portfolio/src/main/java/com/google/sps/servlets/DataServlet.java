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

import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Query.SortDirection;
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
    private static final String LOGIN_PAGE = "/login";
    private static final String EMAIL_PROP = "email";
    private static final String TIME_PROP = "timestamp";
    private static final String COMMENT_PROP = "comment";
    private static final int DEFAULT_COMMENT_SIZE = 20;

    public DataServlet() {
        super();
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(DATASTORE_LABEL).addSort(TIME_PROP, SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);

        int numComments;
        // Get the number of comments the user wants to view. 
        try {
            numComments = Integer.parseInt(request.getParameter(NUM_COMMENTS_INPUT));
        } catch (NumberFormatException e) {
            numComments = DEFAULT_COMMENT_SIZE;
        }

        // Fill the ArrayList with the desired amount of non-empty comments.
        ArrayList<Comment> commentList = loadComments(numComments, results);
        
        // THE PROBLEM IS THAT YOUR ARE TRYING TO CONVERT OBJECTS TO JSON


        // Convert the java ArrayList<String> data to a JSON String.
        String jsonMessage = messageListAsJson(commentList);

        // Send the JSON message as the response.
        response.setContentType(JSON_RESPONSE);
        response.getWriter().println(jsonMessage);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {   
        UserService userService = UserServiceFactory.getUserService();

        // Only logged-in users can comment.
        if(!userService.isUserLoggedIn()) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        // Get the user's email and comment input.
        String comment = request.getParameter(COMMENT_INPUT);
        String email = userService.getCurrentUser().getEmail();

        Entity taskEntity = new Entity(DATASTORE_LABEL);
        taskEntity.setProperty(COMMENT_PROP, comment);
        taskEntity.setProperty(EMAIL_PROP, email);
        taskEntity.setProperty(TIME_PROP, System.currentTimeMillis());

        // Store the user comment in datastore.
        datastore.put(taskEntity);

        response.sendRedirect(COMMENTS_URL);
    }

    /**
     * Converts a Java List<Comment> into a JSON string using Gson.  
     */
    private String messageListAsJson(List<Comment> commentList) {
        Gson gson = new Gson();
        String jsonMessage = "";
        for(int i =0; i < commentList.size(); i++) {
            jsonMessage += gson.toJson(commentList.get(i).toString()) + " ";
        }
        return jsonMessage;
    }

    private ArrayList<Comment> loadComments(int numCommentsToLoad, PreparedQuery results)
    {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        int counter = 0;

        // Add the users desired amount of non-empty comments.
        for (Entity entity : results.asIterable()) {
            if(counter == numCommentsToLoad)
            {
                break;
            }
            String comment = (String) entity.getProperty(COMMENT_PROP);
            String email = (String) entity.getProperty(EMAIL_PROP);
            if(!comment.equals(""))
            {
                Comment comObject = new Comment(comment, entity.getKey(), email);
                commentList.add(comObject);
                counter++;
            }
        }
        return commentList;
    }
}
