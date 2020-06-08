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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String LOG_PAGE_URL = "/login";


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        UserService userService = UserServiceFactory.getUserService();

        if(userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            String logoutURL = userService.createLogoutURL(LOG_PAGE_URL);

            out.println("<p>Hello " + userEmail + "! You are logged in!</p>");
            out.println("<p>Logout <a href=\"" + logoutURL + "\">here</a>.</p>");
        } else {
            String loginURL = userService.createLoginURL(LOG_PAGE_URL);

            out.println("<p>You are not logged in.</p>");
            out.println("<p>Login <a href=\"" + loginURL + "\">here</a>.</p>");
        }
    }
}