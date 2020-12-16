package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FamilyDetails_RCDetailsDataList {
    @SerializedName("RationCardNo")
    @Expose
    private String rationCardNo;

    @SerializedName("FPSNo")
    @Expose
    private String fPSNo;

    @SerializedName("FPSCode")
    @Expose
    private String fpsCode;

    @SerializedName("FPSTimings")
    @Expose
    private String fPSTimings;

    @SerializedName("AFSOCode")
    @Expose
    private String afsoCode;

    @SerializedName("DFSOCode")
    @Expose
    private String dfsoCode;

    @SerializedName("FPSName")
    @Expose
    private String fpsName;

    @SerializedName("KeroseneFPSCode")
    @Expose
    private String keroseneFPSCode;

    @SerializedName("KeroseneFPSName")
    @Expose
    private String keroseneFPSName;

    @SerializedName("TFSO_Name")
    @Expose
    private String tfso_Name;

    @SerializedName("DFSO_Name")
    @Expose
    private String dfso_Name;

    @SerializedName("SchemeCode")
    @Expose
    private String schemeCode;

    @SerializedName("SchemeName")
    @Expose
    private String schemeName;

    @SerializedName("SchemeShortName")
    @Expose
    private String schemeShortName;

    @SerializedName("IsNFSACrireriaRequired")
    @Expose
    private String isNFSACrireriaRequired;

    @SerializedName("Pre_RationCardNo")
    @Expose
    private String pre_RationCardNo;

    @SerializedName("OldRationCardNo")
    @Expose
    private String oldRationCardNo;

    @SerializedName("FinancialStatusRegNo")
    @Expose
    private String financialStatusRegNo;

    @SerializedName("HouseTypeCode")
    @Expose
    private String houseTypeCode;

    @SerializedName("HouseTypeName")
    @Expose
    private String houseTypeName;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("LiftingTypeCode")
    @Expose
    private String liftingTypeCode;

    @SerializedName("BenificiaryType")
    @Expose
    private String benificiaryType;

    @SerializedName("Gas_Connection_Status")
    @Expose
    private String gas_Connection_Status;

    @SerializedName("Gas_Connection_Number")
    @Expose
    private String gas_Connection_Number;

    @SerializedName("Gas_OilCompanyCode")
    @Expose
    private String gas_OilCompanyCode;

    @SerializedName("GasCompany")
    @Expose
    private String gasCompany;

    @SerializedName("GasConnectionConsumerNo")
    @Expose
    private String gasConnectionConsumerNo;

    @SerializedName("Gas_AgencyCode")
    @Expose
    private String gas_AgencyCode;

    @SerializedName("AgencyName")
    @Expose
    private String agencyName;

    @SerializedName("ECompanyCode")
    @Expose
    private String eCompanyCode;

    @SerializedName("ECompanyName_EN")
    @Expose
    private String eCompanyName_EN;

    @SerializedName("ElectricConnectionConsumerNo")
    @Expose
    private String electricConnectionConsumerNo;

    @SerializedName("IssueDate")
    @Expose
    private String issueDate;

    @SerializedName("RationCardMembers")
    @Expose
    private List<FamilyDetails_RCDetails_RationCardMembersData> familyDetails_rcDetails_rationCardMembersData;

    @SerializedName("RationCardAddress")
    @Expose
    private FamilyDetails_RCDetails_RationCardAddressData familyDetails_rcDetails_rationCardAddresses;

    @SerializedName("RationCardNominee")
    @Expose
    private FamilyDetails_RCDetails_RationCardNomineeData familyDetails_rcDetails_rationCardNomineeData;

    public String getfPSNo() {
        return fPSNo;
    }

    public String getFpsCode() {
        return fpsCode;
    }

    public String getfPSTimings() {
        return fPSTimings;
    }

    public String getFpsName() {
        return fpsName;
    }

    public String getKeroseneFPSCode() {
        return keroseneFPSCode;
    }

    public String getKeroseneFPSName() {
        return keroseneFPSName;
    }

    public void setKeroseneFPSName(String keroseneFPSName) {
        this.keroseneFPSName = keroseneFPSName;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public String getSchemeName() {
        return schemeName;
    }


    public String getSchemeShortName() {
        return schemeShortName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

       public String getGas_Connection_Status() {
        return gas_Connection_Status;
    }

    public String getGas_Connection_Number() {
        return gas_Connection_Number;
    }

    public String getGas_OilCompanyCode() {
        return gas_OilCompanyCode;
    }

      public String getGasCompany() {
        return gasCompany;
    }

      public String getGasConnectionConsumerNo() {
        return gasConnectionConsumerNo;
    }

    public String getGas_AgencyCode() {
        return gas_AgencyCode;
    }

       public String getAgencyName() {
        return agencyName;
    }

    public String getIssueDate() {
        return issueDate;
    }


    public List<FamilyDetails_RCDetails_RationCardMembersData> getFamilyDetails_rcDetails_rationCardMembersData() {
        return familyDetails_rcDetails_rationCardMembersData;
    }


    public FamilyDetails_RCDetails_RationCardAddressData getFamilyDetails_rcDetails_rationCardAddresses() {
        return familyDetails_rcDetails_rationCardAddresses;
    }

    public FamilyDetails_RCDetails_RationCardNomineeData getFamilyDetails_rcDetails_rationCardNomineeData() {
        return familyDetails_rcDetails_rationCardNomineeData;
    }
}
