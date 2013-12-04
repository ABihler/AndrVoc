package de.robertmathes.android.orangeiron.model;

import java.util.List;

public class VocabularyServer {

    private String serverType;
    private String serverName;
    private String serverDescription;
    private String serverUrl;
    private int serverVersion;
    private int version;
    private List<Lesson> lessons;

    public String getServerType() {
        return serverType;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerDescription() {
        return serverDescription;
    }

    public void setServerDescription(String serverDescription) {
        this.serverDescription = serverDescription;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getServerVersion() {
        return serverVersion;
    }

    public int getVersion() {
        return version;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

}
