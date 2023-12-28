package com.example.rewards;

public class MainModel {

    String Image, Reward_name, Re_Desc, Re_validity, Reward_point;

    MainModel(){

    }

    public MainModel(String image, String reward_name, String re_Desc, String re_validity, String reward_point) {
        Image = image;
        Reward_name = reward_name;
        Re_Desc = re_Desc;
        Re_validity = re_validity;
        Reward_point = reward_point;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getReward_name() {
        return Reward_name;
    }

    public void setReward_name(String reward_name) {
        Reward_name = reward_name;
    }

    public String getRe_Desc() {
        return Re_Desc;
    }

    public void setRe_Desc(String re_Desc) {
        Re_Desc = re_Desc;
    }

    public String getRe_validity() {
        return Re_validity;
    }

    public void setRe_validity(String re_validity) {
        Re_validity = re_validity;
    }

    public String getReward_point() {
        return Reward_point;
    }

    public void setReward_point(String reward_point) {
        Reward_point = reward_point;
    }
}
