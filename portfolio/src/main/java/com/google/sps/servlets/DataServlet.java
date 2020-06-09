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
    private static final String NUM_COMMENTS_INPUT = "num-comments";
    private static final String DATASTORE_LABEL = "Task";
    private static final String COMMENTS_URL = "/comments.html";
    private static final String TEXT_RESPONSE = "text/html;";
    private static final String LOGIN_PAGE = "/login";
    private static final String EMAIL_PROP = "email";
    private static final int DEFAULT_COMMENT_SIZE = 20;

    public DataServlet() {
        super();
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(Comment.COMMENT_ENTITY).addSort(Comment.TIMESTAMP_PROPERTY, SortDirection.DESCENDING);
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
       
        // Convert the java ArrayList<String> data to a JSON String.
        String jsonMessage = messageListAsJson(commentList);

        // Send the JSON message as the response.
        response.setContentType(TEXT_RESPONSE);
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

        String email = userService.getCurrentUser().getEmail();
        Entity taskEntity = parseForm(request, email);
       
        // Store the user comment in datastore.
        datastore.put(taskEntity);

        response.sendRedirect(COMMENTS_URL);
    }

    /**
     * Converts a Java List<Comment> into a JSON string using Gson.  
     */
    private String messageListAsJson(List<Comment> commentList) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(commentList);
        return jsonMessage;
    }

    private Entity parseForm(HttpServletRequest request, String email) {
        return new Comment(request, email).toEntity();
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
            Comment comObject = new Comment(entity);
            if(!comObject.message.equals(""))
            {
                commentList.add(comObject);
                counter++;
            }
        }
        return commentList;
    }
}
