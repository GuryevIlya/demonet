package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author delet
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserResponse {
    public List<UserVK> response;

    public List<UserVK> getResponse() {
        return response;
    }

    public void setResponse(List<UserVK> response) {
        this.response = response;
    }
    
}
