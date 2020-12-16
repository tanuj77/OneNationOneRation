package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RCGrievanceDetailsIssuesDataList {
    @SerializedName("ResponseId")
    @Expose
    private String responseId;

    @SerializedName("IssueId")
    @Expose
    private String issueId;

    @SerializedName("ResponseDate")
    @Expose
    private String responseDate;

    @SerializedName("IssueDescription")
    @Expose
    private String issueDescription;

    @SerializedName("IssueStatusId")
    @Expose
    private String issueStatusId;

    @SerializedName("IssueStatus")
    @Expose
    private String issueStatus;


    @SerializedName("IsClosureAction")
    @Expose
    private String isClosureAction;

    @SerializedName("IsDisbursedAction")
    @Expose
    private String isDisbursedAction;


    @SerializedName("IsResponse")
    @Expose
    private String isResponse;

    @SerializedName("IsAutoClosed")
    @Expose
    private String isAutoClosed;

    @SerializedName("FeedBy")
    @Expose
    private String feedBy;

    @SerializedName("FeedDate")
    @Expose
    private String feedDate;


    @SerializedName("AttachmentDetails")
    @Expose
    private List<RCGrievanceDetailsAttachementDataList> rcGrievanceDetailsAttachementDataLists;

    public String getResponseId() {
        return responseId;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getIssueStatusId() {
        return issueStatusId;
    }

       public String getIssueStatus() {
        return issueStatus;
    }

      public String getIsClosureAction() {
        return isClosureAction;
    }

    public String getIsDisbursedAction() {
        return isDisbursedAction;
    }

    public String getIsResponse() {
        return isResponse;
    }

    public String getIsAutoClosed() {
        return isAutoClosed;
    }

       public String getFeedBy() {
        return feedBy;
    }

    public String getFeedDate() {
        return feedDate;
    }

    public List<RCGrievanceDetailsAttachementDataList> getRcGrievanceDetailsAttachementDataLists() {
        return rcGrievanceDetailsAttachementDataLists;
    }
}