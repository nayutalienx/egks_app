package com.example.demochat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostData {
    private int id;
    private String request__2;
    private int acquirerId;
    private int payType;
    private int payElementId;
    private String inputprop__0;
    private String __RequestVerificationToken;


    public PostData(String request__2, int acquirerId, int payType, int payElementId, String inputprop__0, String __RequestVerificationToken) {
        this.request__2 = request__2;
        this.acquirerId = acquirerId;
        this.payType = payType;
        this.payElementId = payElementId;
        this.inputprop__0 = inputprop__0;
        this.__RequestVerificationToken = __RequestVerificationToken;
    }


}
