package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FamilyDetails_RCDetails_RationCardAddressData {

    @SerializedName("Present_House_Name_No")
    @Expose
    private String present_House_Name_No;

    @SerializedName("Present_Landmark_Locality_Colony")
    @Expose
    private String present_Landmark_Locality_Colony;

    @SerializedName("Present_PIN")
    @Expose
    private String present_PIN;

    @SerializedName("WardNo")
    @Expose
    private String wardNo;

    @SerializedName("Panchayat")
    @Expose
    private String panchayat;

    @SerializedName("Present_PLCCode")
    @Expose
    private String present_PLCCode;

    @SerializedName("STATE")
    @Expose
    private String sTATE;

    @SerializedName("District")
    @Expose
    private String district;

    @SerializedName("Tahsil")
    @Expose
    private String tahsil;

    @SerializedName("Town_Vill")
    @Expose
    private String town_Vill;

    @SerializedName("PresentState")
    @Expose
    private String presentState;

    @SerializedName("PresentDistrict")
    @Expose
    private String presentDistrict;

    @SerializedName("PresentTahsil")
    @Expose
    private String presentTahsil;

    @SerializedName("PresentVillage")
    @Expose
    private String presentVillage;

    @SerializedName("Permanent_House_Name_No")
    @Expose
    private String permanent_House_Name_No;

    @SerializedName("Permanent_Locality_Colony")
    @Expose
    private String permanent_Locality_Colony;

    @SerializedName("Permanent_PIN")
    @Expose
    private String permanent_PIN;

    @SerializedName("Permanent_PLCCode")
    @Expose
    private String permanent_PLCCode;

    @SerializedName("PermanentState")
    @Expose
    private String permanentState;

    @SerializedName("PermanentDistrict")
    @Expose
    private String permanentDistrict;

    @SerializedName("PermanentTahsil")
    @Expose
    private String permanentTahsil;

    @SerializedName("PermanentVillage")
    @Expose
    private String permanentVillage;

    public String getPermanent_House_Name_No() {
        return permanent_House_Name_No;
    }

    public String getPermanent_Locality_Colony() {
        return permanent_Locality_Colony;
    }

    public String getPermanent_PIN() {
        return permanent_PIN;
    }

    public String getPermanentState() {
        return permanentState;
    }

    public String getPermanentDistrict() {
        return permanentDistrict;
    }

    public String getPermanentTahsil() {
        return permanentTahsil;
    }

    public String getPermanentVillage() {
        return permanentVillage;
    }
}