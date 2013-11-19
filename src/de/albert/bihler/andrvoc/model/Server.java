package de.albert.bihler.andrvoc.model;

/**
 * 
 * @author Robert Mathes <robert.d.mathes@gmail.com>
 * @date 11/16/2013
 * 
 */
public class Server {

    private long id;
    private String name;
    private String url;
    private int serverVersion;
    private int dataVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
