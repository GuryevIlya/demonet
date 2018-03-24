package com.mycompany.tinder2.model.vk;

import java.util.List;
/**
 *
 * @author delet
 */
public class GroupResponce {
    private int count;
    private List<GroupVK> response;

    public void setCount(int count) {
        this.count = count;
    }

    public void setResponse(List<GroupVK> response) {
        this.response = response;
    }

    public int getCount() {
        return count;
    }

    public List<GroupVK> getResponse() {
        return response;
    }
}
