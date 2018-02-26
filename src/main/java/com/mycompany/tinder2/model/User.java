package com.mycompany.tinder2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author delet
 */
public class User {
    private Integer id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("relation_partner")
    private User relationPartner;
    @JsonProperty("maiden_name")
    private String maidenName;
    @JsonProperty("photo_100")
    private String photoURL;
    private Integer sex;
    private int relation;
    private String deactivated;
    private int pending;
    private Country country;
    private String activities;
    private String books;
    private String about;
    private String bdate;
    private City city;
    private int university;
    @JsonProperty("university_name")
    private String universityName;
    private int faculty;
    @JsonProperty("education_form")
    private String educationForm;
    @JsonProperty("education_status")
    private String educationStatus;
    
    public void setEducationForm(String educationForm) {
        this.educationForm = educationForm;
    }
    
    public String getEducationForm() {
        return educationForm;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    
    
    public void setGames(String games) {
        this.games = games;
    }

    public String getGames() {
        return games;
    }
    @JsonProperty("faculty_name")
    private String facultyName;
    private int graduation;
    private String interests;
    @JsonProperty("home_town")
    private String homeTown;
    private String games;

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setUniversity(int university) {
        this.university = university;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public void setFaculty(int faculty) {
        this.faculty = faculty;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setGraduation(int graduation) {
        this.graduation = graduation;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getAbout() {
        return about;
    }

    public String getBdate() {
        return bdate;
    }

    public City getCity() {
        return city;
    }

    public int getUniversity() {
        return university;
    }

    public String getUniversityName() {
        return universityName;
    }

    public int getFaculty() {
        return faculty;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public int getGraduation() {
        return graduation;
    }

    public String getInterests() {
        return interests;
    }


    public void setCountry(Country country) {
        this.country = country;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public Country getCountry() {
        return country;
    }

    public String getActivities() {
        return activities;
    }

    public String getBooks() {
        return books;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }

    public void setRelationPartner(User relationPartner) {
        this.relationPartner = relationPartner;
    }

    public User getRelationPartner() {
        return relationPartner;
    }
    
    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoURL() {
        return photoURL;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }
    
    
    
}
