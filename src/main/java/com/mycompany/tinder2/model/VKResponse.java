package com.mycompany.tinder2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author delet
 */
public class VKResponse<T> {
    private Response<T> response;
    @JsonIgnore private Object error;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response<T> response) {
        this.response = response;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
    
    
}
