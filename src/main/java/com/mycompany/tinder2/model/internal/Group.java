package com.mycompany.tinder2.model.internal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author delet
 */
public class Group {
    private String Name;
    private String description;
    private Integer id;
    private Map<String, Integer> wallVector = new HashMap<String, Integer>();

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Map<String, Integer> getVallVector() {
        return wallVector;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWallVector(Map<String, Integer> vallVector) {
        this.wallVector = vallVector;
    }
}
