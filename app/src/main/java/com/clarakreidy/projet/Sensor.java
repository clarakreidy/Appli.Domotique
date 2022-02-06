package com.clarakreidy.projet;

public class Sensor {
    private Integer id;
    private String name;
    private String type;
    private String picture;
    private Boolean isChecked = false;
    private Float value;
    private String unit;

    public Sensor(Integer id, String name, String type, String picture) {
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

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean isChecked() {
        if(isChecked != null) return isChecked;
        else return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
