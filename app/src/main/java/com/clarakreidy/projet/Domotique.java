package com.clarakreidy.projet;

public class Domotique {
    private Integer id;
    private String name;
    private String type;
    private String picture;
    private boolean isChecked = false;

    public Domotique(Integer id, String name, String type, String picture) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.picture = picture;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String url) { this.picture = url;}

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public String toString() {
        return name;
    }
}
