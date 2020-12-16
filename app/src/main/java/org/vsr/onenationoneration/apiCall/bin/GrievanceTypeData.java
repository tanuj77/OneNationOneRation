package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GrievanceTypeData {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Remarks")
    @Expose
    private String remarks;

    @SerializedName("AckCode")
    @Expose
    private String ackCode;

    @SerializedName("State")
    @Expose
    private String state;

    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("MasterData")
    @Expose
    private List<GrievanceTypeDataList> grievanceTypeDataLists;


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

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<GrievanceTypeDataList> getGrievanceTypeDataLists() {
        return grievanceTypeDataLists;
    }

    public void setGrievanceTypeDataLists(List<GrievanceTypeDataList> grievanceTypeDataLists) {
        this.grievanceTypeDataLists = grievanceTypeDataLists;
    }
}
