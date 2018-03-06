package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author delet
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class City {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
