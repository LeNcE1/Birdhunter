package com.example.lence.bird_hunter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class Birds {

    @SerializedName("birds")
    @Expose
    private List<String> birds = null;

    public List<String> getBirds() {
        return birds;
    }

    public void setBirds(List<String> birds) {
        this.birds = birds;
    }

}