package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import javax.servlet.http.HttpServletRequest;

public class Comment {
    public static final String TIMESTAMP_PROPERTY = "timestamp";
    public static final String MESSAGE_PROPERTY = "message";
    public static final String EMAIL_PROPERTY = "email";

    public static final String COMMENT_FORM_ID = "comment-input";

    public static final String COMMENT_ENTITY = "Comment";
    
    public final long timestamp;
    public final String message;
    public String email;

    public Comment(Entity entity) {
        timestamp = (long) entity.getProperty(TIMESTAMP_PROPERTY);
        message = (String) entity.getProperty(MESSAGE_PROPERTY);
        email = (String) entity.getProperty(EMAIL_PROPERTY);
    }

    public Comment(HttpServletRequest request, String userEmail) {
        timestamp = System.currentTimeMillis();
        message = request.getParameter(COMMENT_FORM_ID); 
        email = userEmail;
    }

    /** Return an Entity for DataStore */
    public Entity toEntity() {
        Entity commentEntity = new Entity(COMMENT_ENTITY);
        commentEntity.setProperty(TIMESTAMP_PROPERTY, timestamp);
        commentEntity.setProperty(MESSAGE_PROPERTY, message);
        commentEntity.setProperty(EMAIL_PROPERTY, email);
        return commentEntity;
    }
}
