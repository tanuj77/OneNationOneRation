package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RCEntitlementData {
    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("RCNo")
    @Expose
    private String rcNo;

    @SerializedName("FPSNo")
    @Expose
    private String fpsNo;

    @SerializedName("FPSName")
    @Expose
    private String fpsName;

    @SerializedName("FPSAddress")
    @Expose
    private String fpsAddress;

    @SerializedName("AllocMonth")
    @Expose
    private String allocMonth;

    @SerializedName("AllocYear")
    @Expose
    private String allocYear;

    @SerializedName("SchemeCode")
    @Expose
    private String schemeCode;

    @SerializedName("SchemeName")
    @Expose
    private String schemeName;

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

    @SerializedName("DataGeneratedDate")
    @Expose
    private String dataGeneratedDate;

    @SerializedName("ModuleName")
    @Expose
    private String moduleName;

    @SerializedName("ModuleLink")
    @Expose
    private String moduleLink;

    @SerializedName("Members")
    @Expose
    private List<RCEMembersDataList> rceMembersDataLists;

    @SerializedName("Entitlements")
    @Expose
    private List<RCEEntitlementsDataList> rceEntitlementsDataLists;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRcNo() {
        return rcNo;
    }

      public String getFpsNo() {
        return fpsNo;
    }

    public String getFpsName() {
        return fpsName;
    }

    public String getFpsAddress() {
        return fpsAddress;
    }

    public String getAllocMonth() {
        return allocMonth;
    }

    public String getAllocYear() {
        return allocYear;
    }

    public String getSchemeName() {
        return schemeName;
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

      public String getModuleName() {
        return moduleName;
    }

    public String getModuleLink() {
        return moduleLink;
    }


    public List<RCEMembersDataList> getRceMembersDataLists() {
        return rceMembersDataLists;
    }

      public List<RCEEntitlementsDataList> getRceEntitlementsDataLists() {
        return rceEntitlementsDataLists;
    }
}