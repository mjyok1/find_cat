package com.example.find_cat_info;

import java.io.Serializable;

public class CatData implements Serializable {

    String image;
    String name;
    boolean neuter;
    boolean isMan;
    String description;

    public CatData(String image, String name, boolean neuter, boolean isMan) {
        this.image = image;
        this.name = name;
        this.neuter = neuter;
        this.isMan = isMan;
    }

    public CatData(String image, String name, boolean neuter, boolean isMan, String description) {
        this.image = image;
        this.name = name;
        this.neuter = neuter;
        this.isMan = isMan;
        this.description = description;
    }
}
