package com.mycompany.tinder2.model.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author delet
 */
public class UserVectors {
    private Integer id;
    private String city;
    private String firstName;
    private String lastName;
    private String photo;
    private Map<String, Integer> infoVector;
    private Map<String, Integer> wallTextVector;
    private Map<String, Integer> wallMediaVector;
    private Map<String, Integer> wallHelpRepostVector;
    private Set<String> groupNames = new HashSet<String>();
    private List<Group> groups;
    

    
    public UserVectors(){
    }
    
    public UserVectors(Integer id){
        this.id = id;
    }

    public Set<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(Set<String> groupNames) {
        this.groupNames = groupNames;
    }
    
    
    public Integer getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoto() {
        return photo;
    }

    
    public Map<String, Integer> getInfoVector() {
        return infoVector;
    }

    public Map<String, Integer> getWallTextVector() {
        return wallTextVector;
    }

    public Map<String, Integer> getWallMediaVector() {
        return wallMediaVector;
    }

    public Map<String, Integer> getWallHelpRepostVector() {
        return wallHelpRepostVector;
    }

    public List<Group> getGroups() {
        return groups;
    }
    
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public void setInfoVector(Map<String, Integer> infoVector) {
        this.infoVector = infoVector;
    }

    public void setWallTextVector(Map<String, Integer> wallVector) {
        this.wallTextVector = wallVector;
    }

    public void setWallMediaVector(Map<String, Integer> wallMediaVector) {
        this.wallMediaVector = wallMediaVector;
    }  

    public void setWallHelpRepostVector(Map<String, Integer> wallHelpRepostVector) {
        this.wallHelpRepostVector = wallHelpRepostVector;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    
    public boolean isInterest(String interest){
        for(String group: groupNames){
            if(group.contains(interest)){
                return true;
            }
        }
        
        for(String item: infoVector.keySet()){
            if(item.contains(interest)){
                return true;
            }
        }
        return false;
    }
    
}
