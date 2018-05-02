package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author delet
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommonFriends {
    private List<Integer> response;

    public void setResponse(List<Integer> response) {
        this.response = response;
    }

    public List<Integer> getResponse() {
        return response;
    }
}
