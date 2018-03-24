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
    private Map<String, Integer> vallVector = new HashMap<String, Integer>();

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
        return vallVector;
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

    public void setVallVector(Map<String, Integer> vallVector) {
        this.vallVector = vallVector;
    }
}
