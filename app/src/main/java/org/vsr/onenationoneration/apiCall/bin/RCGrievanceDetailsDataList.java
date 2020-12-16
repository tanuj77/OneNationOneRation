package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RCGrievanceDetailsDataList {
    @SerializedName("ComplaintID")
    @Expose
    private String complaintID;

    @SerializedName("IssueCode")
    @Expose
    private String issueCode;

    @SerializedName("ReportedDate")
    @Expose
    private String reportedDate;

    @SerializedName("SubjectCode")
    @Expose
    private String subjectCode;

    @SerializedName("IssueSubject")
    @Expose
    private String issueSubject;

    @SerializedName("Name_EN")
    @Expose
    private String name_EN;

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;

    @SerializedName("EmailID")
    @Expose
    private String emailID;

    @SerializedName("AadharUniqueId")
    @Expose
    private String aadharUniqueId;

    @SerializedName("RationCardNo")
    @Expose
    private String rationCardNo;

    @SerializedName("FPSCode")
    @Expose
    private String fPSCode;

    @SerializedName("FPS_Name_EN")
    @Expose
    private String fPS_Name_EN;

    @SerializedName("ComplaintDetails")
    @Expose
    private String complaintDetails;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("Tahsil")
    @Expose
    private String tahsil;

    @SerializedName("District")
    @Expose
    private String district;

    @SerializedName("DistrictName")
    @Expose
    private String districtName;

    @SerializedName("State")
    @Expose
    private String state;

    @SerializedName("StateName")
    @Expose
    private String stateName;

    @SerializedName("FPSDistrict")
    @Expose
    private String fPSDistrict;

    @SerializedName("FPSDistrictName")
    @Expose
    private String fPSDistrictName;

    @SerializedName("FPSState")
    @Expose
    private String fPSState;

    @SerializedName("FPSStateName")
    @Expose
    private String fPSStateName;

    @SerializedName("IssueDescription")
    @Expose
    private String issueDescription;

    @SerializedName("IsResponse")
    @Expose
    private String isResponse;

    @SerializedName("TentativeCompletionDate")
    @Expose
    private String tentativeCompletionDate;

    @SerializedName("IssueStatusId")
    @Expose
    private String issueStatusId;

    @SerializedName("IssueStatusDescription")
    @Expose
    private String issueStatusDescription;

    @SerializedName("IssueStatus")
    @Expose
    private String issueStatus;

    @SerializedName("IssuePriorityId")
    @Expose
    private String issuePriorityId;

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

    @SerializedName("GrievanceTypeName")
    @Expose
    private String grievanceTypeName;

    @SerializedName("FeedBy")
    @Expose
    private String feedBy;

    @SerializedName("FeedDate")
    @Expose
    private String feedDate;

    @SerializedName("FeedByMobileNo")
    @Expose
    private String feedByMobileNo;

    @SerializedName("FeedByEmailID")
    @Expose
    private String feedByEmailID;

    @SerializedName("DeleteStatus")
    @Expose
    private String deleteStatus;

    @SerializedName("DeletedBy")
    @Expose
    private String deletedBy;

    @SerializedName("DeletedDate")
    @Expose
    private String deletedDate;

    @SerializedName("IssueResponses")
    @Expose
    private List<RCGrievanceDetailsIssuesDataList> rcGrievanceDetailsIssuesDataLists;

    public String getComplaintID() {
        return complaintID;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public String getReportedDate() {
        return reportedDate;
    }

      public String getSubjectCode() {
        return subjectCode;
    }

    public String getIssueSubject() {
        return issueSubject;
    }

      public String getName_EN() {
        return name_EN;
    }

       public String getMobileNo() {
        return mobileNo;
    }

       public String getEmailID() {
        return emailID;
    }

       public String getAadharUniqueId() {
        return aadharUniqueId;
    }

       public String getRationCardNo() {
        return rationCardNo;
    }

      public String getfPSCode() {
        return fPSCode;
    }

      public String getfPS_Name_EN() {
        return fPS_Name_EN;
    }

       public String getComplaintDetails() {
        return complaintDetails;
    }

    public String getAddress() {
        return address;
    }

      public String getTahsil() {
        return tahsil;
    }

       public String getDistrict() {
        return district;
    }

       public String getDistrictName() {
        return districtName;
    }

       public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getfPSDistrict() {
        return fPSDistrict;
    }

    public String getfPSDistrictName() {
        return fPSDistrictName;
    }


    public String getfPSState() {
        return fPSState;
    }

    public String getfPSStateName() {
        return fPSStateName;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getIsResponse() {
        return isResponse;
    }

    public String getTentativeCompletionDate() {
        return tentativeCompletionDate;
    }

    public String getIssueStatusId() {
        return issueStatusId;
    }

    public String getIssueStatusDescription() {
        return issueStatusDescription;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public String getIssuePriorityId() {
        return issuePriorityId;
    }

    public String getModeOfGrievanceCode() {
        return modeOfGrievanceCode;
    }

    public String getModeOfGrievanceName() {
        return modeOfGrievanceName;
    }

    public String getPlatformOfGrievanceCode() {
        return platformOfGrievanceCode;
    }

    public String getPlatformOfGrievanceName() {
        return platformOfGrievanceName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getGrievanceTypeName() {
        return grievanceTypeName;
    }

    public String getFeedBy() {
        return feedBy;
    }

    public String getFeedDate() {
        return feedDate;
    }
       public String getFeedByMobileNo() {
        return feedByMobileNo;
    }

    public String getFeedByEmailID() {
        return feedByEmailID;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public String getDeletedDate() {
        return deletedDate;
    }


    public List<RCGrievanceDetailsIssuesDataList> getRcGrievanceDetailsIssuesDataLists() {
        return rcGrievanceDetailsIssuesDataLists;
    }
}