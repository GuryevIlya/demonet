package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author delet
 */
public class Group {
    @JsonIgnore public String photo_50;
    @JsonIgnore public String photo_200;
    private String id;
    private String description;
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("is_closed")
    private String isClosed;
    private String type;
    @JsonProperty("photo_100")
    private String photoURL;
    @JsonIgnore private Object city;
    @JsonIgnore private Object country;
    @JsonIgnore private Object place;
    @JsonIgnore private Object members_count;

    public String getDescription() {
        return description;
    }

    public void setPhoto_50(String photo_50) {
        this.photo_50 = photo_50;
    }

    public void setPhoto_200(String photo_200) {
        this.photo_200 = photo_200;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public void setPlace(Object place) {
        this.place = place;
    }

    public void setMembers_count(Object members_count) {
        this.members_count = members_count;
    }

    public String getPhoto_50() {
        return photo_50;
    }

    public String getPhoto_200() {
        return photo_200;
    }

    public Object getCity() {
        return city;
    }

    public Object getCountry() {
        return country;
    }

    public Object getPlace() {
        return place;
    }

    public Object getMembers_count() {
        return members_count;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }
    
}
