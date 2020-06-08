package com.google.sps.data;

import com.google.appengine.api.datastore.Key;

public class Comment {
    public String comment;
    public Key id;
    public String email;

    public Comment(String comment, Key id, String email) {
        this.comment = comment;
        this.id = id;
        this.email = email;
    }

    // pubic void setComment(String comment)


    // public String getComment() {
    //     return comment;
    // }

    // public Key getID() {
    //     return id;
    // }

    // public String getEmail() {
    //     return email;
    // }

    @Override
    public String toString() {
        return "comment: " + comment + " id: " + id + " email: " + email;
    }
}

// find the right place to put it and add correct import to Dataservlet

// create toString method to format comments nicely