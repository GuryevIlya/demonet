package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author delet
 */
public class Items {
    @JsonIgnore public int error;
    private List<Group> items;

    public List<Group> getItems() {
        return items;
    }

    public void setItems(List<Group> items) {
        this.items = items;
    }
}
