package de.robertmathes.android.orangeiron.model;

import java.util.UUID;

/**
 * 
 * @author Robert Mathes <robert.d.mathes@gmail.com>
 * @date 11/16/2013
 * 
 */
public class Server {

    private long id;
    private UUID uuid;
    private String name;
    private String description;
    private String url;
    private int serverVersion;
    private int dataVersion;

    public Server() {
    }

    public Server(VocabularyServer vocServer) {
        this.name = vocServer.getServerName();
        this.description = vocServer.getServerDescription();
        this.url = vocServer.getServerUrl();
        this.serverVersion = vocServer.getServerVersion();
        this.dataVersion = vocServer.getVersion();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(int serverVersion) {
        this.serverVersion = serverVersion;
    }

    public int getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

}
