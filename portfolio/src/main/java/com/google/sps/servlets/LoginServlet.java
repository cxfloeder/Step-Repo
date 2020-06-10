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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String LOG_PAGE_URL = "/login";
    private static final String COMMENTS_URL = "/comments.html";
    private static final String HOME_PAGE_URL = "/home.html";
    private static final String TEXT_TYPE = "text/html";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_TYPE);
        UserService userService = UserServiceFactory.getUserService();

        if(userService.isUserLoggedIn()) {
            response.sendRedirect(COMMENTS_URL);   
        } else {
            response.sendRedirect(HOME_PAGE_URL);
        }
    }

     /**
     * Converts a Java List<String> into a JSON string using Gson.  
     */
    private String messageListAsJson(List<String> output) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(output);
        return jsonMessage;
    }
}