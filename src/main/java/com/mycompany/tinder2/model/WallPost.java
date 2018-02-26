package com.mycompany.tinder2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author delet
 */
public class WallPost {
    private int id;
    @JsonProperty("owner_id")
    private int ownerId;
    @JsonProperty("from_id")
    private int fromId;
    private String text;
    private int date;
    @JsonProperty("marked_as_ads")
    private int markedAsAds;
    @JsonProperty("post_type")
    private String postType;
    @JsonProperty("is_pinned")
    private int isPined;
    @JsonIgnore private Object attachments;
    @JsonIgnore private Object comments;
    @JsonIgnore private Object likes;
    @JsonIgnore private Object reposts;
    @JsonIgnore private Object views;
    @JsonIgnore private Object signer_id;
    @JsonIgnore private Object copy_history;
    @JsonIgnore private Object geo;
    @JsonIgnore private Object can_delete;

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getFromId() {
        return fromId;
    }

    public String getText() {
        return text;
    }

    public int getDate() {
        return date;
    }

    public int getMarkedAsAds() {
        return markedAsAds;
    }

    public String getPostType() {
        return postType;
    }

    public void setMarkedAsAds(int markedAsAds) {
        this.markedAsAds = markedAsAds;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public int getIsPined() {
        return isPined;
    }

    public void setIsPined(int isPined) {
        this.isPined = isPined;
    }

    public Object getAttachments() {
        return attachments;
    }

    public Object getComments() {
        return comments;
    }

    public Object getLikes() {
        return likes;
    }

    public Object getReposts() {
        return reposts;
    }

    public Object getViews() {
        return views;
    }

    public void setAttachments(Object attachments) {
        this.attachments = attachments;
    }

    public void setComments(Object comments) {
        this.comments = comments;
    }

    public void setLikes(Object likes) {
        this.likes = likes;
    }

    public void setReports(Object reposts) {
        this.reposts = reposts;
    }

    public void setViews(Object views) {
        this.views = views;
    }

    public Object getSigner_id() {
        return signer_id;
    }

    public void setReposts(Object reposts) {
        this.reposts = reposts;
    }

    public Object getCopy_history() {
        return copy_history;
    }

    public void setCopy_history(Object copy_history) {
        this.copy_history = copy_history;
    }

    public void setGeo(Object geo) {
        this.geo = geo;
    }

    public Object getGeo() {
        return geo;
    }

    public Object getCan_delete() {
        return can_delete;
    }

    public void setCan_delete(Object can_delete) {
        this.can_delete = can_delete;
    }
    
    
    
}
