package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author delet
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserResponse {
    public List<User> response;

    public List<User> getResponse() {
        return response;
    }

    public void setResponse(List<User> response) {
        this.response = response;
    }
    
}
