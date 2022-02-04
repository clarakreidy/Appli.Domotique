package com.clarakreidy.projet;

public class Room {
    private Integer id;
    private String url;
    private String name;

    public Room(Integer id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
