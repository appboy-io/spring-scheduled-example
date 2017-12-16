package com.tefnut.dev.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Patch")
public final class Patch {

    private ObjectId id;
    private String gameName;
    private String currentPatch;

    public Patch() {
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(final String gameName) {
        this.gameName = gameName;
    }

    public String getCurrentPatch() {
        return currentPatch;
    }

    public void setCurrentPatch(final String currentPatch) {
        this.currentPatch = currentPatch;
    }
}
