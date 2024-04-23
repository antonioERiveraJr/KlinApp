package com.example.cleaningapplication.Model;

public class Applicants {
    String applicantImage;
    String applicantName;
    float applicantRatings;
    String applicantEmail;
    String applicantAddress;
    String applicant;
    String applicantId;
    String number;
    String applicantDate;

    public Applicants(String applicantDate,String applicantId,String applicantImage, String applicantName, float applicantRatings, String applicantEmail, String applicantAddress, String applicant, String number) {
        this.applicantImage = applicantImage;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.applicantRatings = applicantRatings;
        this.applicantEmail = applicantEmail;
        this.applicantAddress = applicantAddress;
        this.applicant = applicant;
        this.number = number;
        this.applicantDate = applicantDate;
    }


    public Applicants() {

    }

    public String getApplicantDate() {
        return applicantDate;
    }

    public void setApplicantDate(String applicantDate) {
        this.applicantDate = applicantDate;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantImage() {
        return applicantImage;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public float getApplicantRatings() {
        return applicantRatings;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public String getApplicant() {
        return applicant;
    }

    public String getNumber() {
        return number;
    }

    public void setApplicantImage(String applicantImage) {
        this.applicantImage = applicantImage;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setApplicantRatings(float applicantRatings) {
        this.applicantRatings = applicantRatings;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

