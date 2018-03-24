package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author delet
 */
public class Items {
    @JsonIgnore public int error;
    private List<GroupVK> items;

    public List<GroupVK> getItems() {
        return items;
    }

    public void setItems(List<GroupVK> items) {
        this.items = items;
    }
}
