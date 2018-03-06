package com.mycompany.tinder2.model.vk;

import java.util.List;
/**
 *
 * @author delet
 */
public class GroupResponce {
    private int count;
    private List<Group> response;

    public void setCount(int count) {
        this.count = count;
    }

    public void setResponse(List<Group> response) {
        this.response = response;
    }

    public int getCount() {
        return count;
    }

    public List<Group> getResponse() {
        return response;
    }
}
