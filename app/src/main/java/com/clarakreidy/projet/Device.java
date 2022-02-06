package com.clarakreidy.projet;

public class Device {
    private Integer id;
    private String name;
    private String type;
    private String picture;
    private Boolean isChecked = false;
    private Integer status;

    public Device(Integer id, String name, String type, String picture) {
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

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean isChecked() {
        if(isChecked != null) return isChecked;
        else return false;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
