package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RCEMembersDataList {
    @SerializedName("MemberCode")
    @Expose
    private String memberCode;

    @SerializedName("MemberName")
    @Expose
    private String memberName;

    @SerializedName("Relation")
    @Expose
    private String relation;

    @SerializedName("UIDNo")
    @Expose
    private String uIDNo;

    @SerializedName("UIDVeriStatus")
    @Expose
    private String uIDVeriStatus;

    @SerializedName("DAuthStatus")
    @Expose
    private String dAuthStatus;

    @SerializedName("eKYCStatus")
    @Expose
    private String eKYCStatus;

    public String getMemberCode() {
        return memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getRelation() {
        return relation;
    }

    public String getuIDNo() {
        return uIDNo;
    }

    public String geteKYCStatus() {
        return eKYCStatus;
    }
}
