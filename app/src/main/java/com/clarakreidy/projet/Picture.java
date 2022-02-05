package com.clarakreidy.projet;

public class Picture {
    private Integer id;
    private String url;

    public Picture(Integer id, String url) {
        this.id = id;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url.replace("img/", "").replace(".png", "");
    }
}
