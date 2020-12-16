package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FamilyDetailsData {
    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("RCNo")
    @Expose
    private String rcNo;

    @SerializedName("AckCode")
    @Expose
    private String ackCode;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Remarks")
    @Expose
    private String remarks;

    @SerializedName("DateOfData")
    @Expose
    private String dateOfData;

    @SerializedName("ModuleName")
    @Expose
    private String moduleName;

    @SerializedName("ModuleLink")
    @Expose
    private String moduleLink;

    @SerializedName("RCDetails")
    @Expose
    private FamilyDetails_RCDetailsDataList familyDetails_rcDetailsDataLists;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDateOfData() {
        return dateOfData;
    }

    public void setDateOfData(String dateOfData) {
        this.dateOfData = dateOfData;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleLink() {
        return moduleLink;
    }

    public void setModuleLink(String moduleLink) {
        this.moduleLink = moduleLink;
    }

    public FamilyDetails_RCDetailsDataList getFamilyDetails_rcDetailsDataLists() {
        return familyDetails_rcDetailsDataLists;
    }

    public void setFamilyDetails_rcDetailsDataLists(FamilyDetails_RCDetailsDataList familyDetails_rcDetailsDataLists) {
        this.familyDetails_rcDetailsDataLists = familyDetails_rcDetailsDataLists;
    }
}
