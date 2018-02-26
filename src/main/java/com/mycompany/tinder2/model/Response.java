package com.mycompany.tinder2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author delet
 */
public class Response<T> {
    @JsonIgnore public int error;
    private List<T> items;
    @JsonIgnore private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
    
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
