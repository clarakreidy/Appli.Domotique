package com.clarakreidy.projet;

public class Room {
    private Integer id;
    private String picture;
    private String name;

    public Room(Integer id, String url, String name) {
        this.id = id;
        this.picture = url;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }
}
