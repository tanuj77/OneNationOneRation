package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RCGrievanceListDataList {

    @SerializedName("IssueCode")
    @Expose
    private String issueCode;

    @SerializedName("Name_EN")
    @Expose
    private String name_EN;

    @SerializedName("RationCardNo")
    @Expose
    private String rationCardNo;

    @SerializedName("FPSCode")
    @Expose
    private String fPSCode;

    @SerializedName("FPS_Name_EN")
    @Expose
    private String fPS_Name_EN;

    @SerializedName("ModeOfGrievanceCode")
    @Expose
    private String modeOfGrievanceCode;

    @SerializedName("ModeOfGrievanceName")
    @Expose
    private String modeOfGrievanceName;

    @SerializedName("PlatformOfGrievanceCode")
    @Expose
    private String platformOfGrievanceCode;

    @SerializedName("PlatformOfGrievanceName")
    @Expose
    private String platformOfGrievanceName;

    @SerializedName("IssueDate")
    @Expose
    private String issueDate;

    @SerializedName("SubjectCode")
    @Expose
    private String subjectCode;

    @SerializedName("GrievanceTypeName")
    @Expose
    private String grievanceTypeName;

    @SerializedName("STATE")
    @Expose
    private String sTATE;

    @SerializedName("DeleteStatus")
    @Expose
    private String deleteStatus;

    @SerializedName("IssueStatusId")
    @Expose
    private String issueStatusId;

    @SerializedName("IssueStatusDescription")
    @Expose
    private String issueStatusDescription;

    @SerializedName("IssueStatus")
    @Expose
    private String issueStatus;

    @SerializedName("FeedBy")
    @Expose
    private String feedBy;

    @SerializedName("FeedDate")
    @Expose
    private String feedDate;

    @SerializedName("DeletedBy")
    @Expose
    private String deletedBy;

    @SerializedName("DeletedDate")
    @Expose
    private String deletedDate;


    public String getIssueCode() {
        return issueCode;
    }

     public String getIssueDate() {
        return issueDate;
    }

    public String getGrievanceTypeName() {
        return grievanceTypeName;
    }

       public String getIssueStatus() {
        return issueStatus;
    }
 }