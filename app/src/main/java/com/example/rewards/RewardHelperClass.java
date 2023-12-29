package com.example.rewards;

public class RewardHelperClass {
    String name, description, point;

    public RewardHelperClass(){

    }

    public RewardHelperClass(String name, String description, String point) {
        this.name = name;
        this.description = description;
        this.point = point;
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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
