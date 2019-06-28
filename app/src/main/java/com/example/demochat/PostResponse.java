package com.example.demochat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("validationInfo")
    @Expose
    private String validationInfo;



    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValidationInfo() {
        return validationInfo;
    }

    public void setValidationInfo(String validationInfo) {
        this.validationInfo = validationInfo;
    }

    public PostResponse(Boolean error, String status, String message, String validationInfo) {
        this.error = error;
        this.status = status;
        this.message = message;
        this.validationInfo = validationInfo;
    }
}
