package com.mycompany.tinder2.model.pages;

/**
 *
 * @author delet
 */
public class User {
    private String name;
    private String sername;
    private String urlPhoto;
    private Integer id;

    public String getName() {
        return name;
    }

    public String getSername() {
        return sername;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSername(String sername) {
        this.sername = sername;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
