package com.mycompany.tinder2.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author delet
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GroupVK {
    private String id;
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("is_closed")
    private String isClosed;
    @JsonProperty("admin_level")
    private String adminLevel;
    private String type;
    private String activity;
    @JsonProperty("age_limits")
    private Integer ageLimits; 
    private CityVK city; 
    private String description;
    @JsonProperty("photo_100")
    private String photoURL;
    @JsonProperty("members_count")
    private Integer membersCount;

    public String getDescription() {
        return description;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setAgeLimits(Integer ageLimits) {
        this.ageLimits = ageLimits;
    }

    public void setCity(CityVK city) {
        this.city = city;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public CityVK getCity() {
        return city;
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

    public String getActivity() {
        return activity;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public Integer getAgeLimits() {
        return ageLimits;
    }

    public Object getMembersCount() {
        return membersCount;
    }
    
}
