package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateLocalDetailsDataList {

    @SerializedName("Base64StateLogo")
    @Expose
    private String base64StateLogo;

    @SerializedName("DepartmentName")
    @Expose
    private String departmentName;

    @SerializedName("Disclaimer")
    @Expose
    private String disclaimer;

    @SerializedName("GovtName")
    @Expose
    private String govtName;

    @SerializedName("TollFreeNo")
    @Expose
    private String tollFreeNo;

    @SerializedName("EmailIdofDept")
    @Expose
    private String emailIdofDept;

    @SerializedName("WebsiteLink")
    @Expose
    private String websiteLink;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getBase64StateLogo() {
        return base64StateLogo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getGovtName() {
        return govtName;
    }

    public String getTollFreeNo() {
        return tollFreeNo;
    }

    public String getEmailIdofDept() {
        return emailIdofDept;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }
   }
