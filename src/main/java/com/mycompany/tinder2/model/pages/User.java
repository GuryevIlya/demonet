package com.mycompany.tinder2.model.pages;

/**
 *
 * @author delet
 */
public class User {
    private Integer commonFriendsCount;
    private String name;
    private String firstName;
    private String lastName;
    private String urlPhoto;
    private Integer id;

    public Integer getCommonFriendsCount() {
        return commonFriendsCount;
    }

    public void setCommonFriendsCount(Integer commonFriendsCount) {
        this.commonFriendsCount = commonFriendsCount;
    }

    public String getName() {
        return name;
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

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
}
