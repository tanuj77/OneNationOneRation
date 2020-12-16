package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguagesDataList {

    @SerializedName("LanguageId")
    @Expose
    private Integer languageId;

    @SerializedName("LanguageName")
    @Expose
    private String languageName;


    @SerializedName("LanguageNameLL")
    @Expose
    private String languageNameLL;

    @SerializedName("LanguageCode")
    @Expose
    private String languageCode;


    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;


    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;

    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;

    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;

    public Integer getLanguageId() {
        return languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getLanguageNameLL() {
        return languageNameLL;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
