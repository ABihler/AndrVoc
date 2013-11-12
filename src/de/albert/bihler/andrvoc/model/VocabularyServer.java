package de.albert.bihler.andrvoc.model;

import java.util.List;

public class VocabularyServer {

    private String serverType;
    private String serverName;
    private int serverVersion;
    private int version;
    private List<Lesson> lessons;

    public String getServerType() {
	return serverType;
    }

    public String getServerName() {
	return serverName;
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
