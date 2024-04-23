package com.example.cleaningapplication.Model;

public class Workers {

    private String password, confirmPassword, id, phoneNumber, address,
            name, profileImage, userId, birthDate, status, search, comment,sss,nbi,email;
    private int ratingCount;
    private float rating,finalRating;

    public Workers() {

    }

    public float getFinalRating() {
        return finalRating;
    }

    public void setFinalRating(float finalRating) {
        this.finalRating = finalRating;
    }

    public Workers(String email, String password, String phoneNumber, String address,
                   String profileImage, String userId, String status,
                   String search, String comment, String birthDate, String id, String name, String confirmPassword,
                   String nbi, String sss, float rating, int ratingCount, float finalRating) {

        this.nbi = nbi;
        this.finalRating = finalRating;
        this.sss = sss;
        this.email = email;
        this.name = name;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.confirmPassword = confirmPassword;
        this.comment = comment;
        this.birthDate = birthDate;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.id = id;
        this.profileImage = profileImage;
        this.userId = userId;
        this.status = status;
        this.search = search;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNbi() {
        return nbi;
    }

    public void setNbi(String nbi) {
        this.nbi = nbi;
    }
    public String getSss() {
        return sss;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
