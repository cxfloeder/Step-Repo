package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/**
 * A class to implement Season Entities for Datastore use.
 */
public final class Season {
    public static final String SEASON_PROPERTY = "season";
    public static final String SEASON_ENTITY = "Season";
    public final String season;

    public Season(Entity entity) {
        season = (String) entity.getProperty(SEASON_PROPERTY);
    }

    public Season(String season) {
        this.season = season;
    }

    /** Return an Entity for DataStore */
    public Entity toEntity() {
        Entity seasonEntity = new Entity(SEASON_ENTITY);
        seasonEntity.setProperty(SEASON_PROPERTY, season);
        return seasonEntity;
    }
}