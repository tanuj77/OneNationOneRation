package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FamilyDetails_RCDetails_RationCardMembersData {
    @SerializedName("MemberId")
    @Expose
    private String memberId;

    @SerializedName("RationCardNo")
    @Expose
    private String rationCardNo;

    @SerializedName("Member_Name_EN")
    @Expose
    private String member_Name_EN;

    @SerializedName("Member_Name_LL")
    @Expose
    private String member_Name_LL;

    @SerializedName("Member_Name_In_Aadhar_EN")
    @Expose
    private String member_Name_In_Aadhar_EN;

    @SerializedName("Member_Name_In_Aadhar_LL")
    @Expose
    private String member_Name_In_Aadhar_LL;

    @SerializedName("IsAadharVerified")
    @Expose
    private String isAadharVerified;

    @SerializedName("Mother_Name_LL")
    @Expose
    private String mother_Name_LL;

    @SerializedName("Mother_Name_EN")
    @Expose
    private String mother_Name_EN;

    @SerializedName("Father_Name_LL")
    @Expose
    private String father_Name_LL;

    @SerializedName("Father_Name_EN")
    @Expose
    private String father_Name_EN;

    @SerializedName("Spouse_Name_LL")
    @Expose
    private String spouse_Name_LL;

    @SerializedName("Spouse_Name_EN")
    @Expose
    private String spouse_Name_EN;

    @SerializedName("Gender")
    @Expose
    private String gender;

    @SerializedName("GenderName")
    @Expose
    private String genderName;

    @SerializedName("ActualOrDeclaredDOB")
    @Expose
    private String actualOrDeclaredDOB;

    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;

    @SerializedName("AgeInYears")
    @Expose
    private String ageInYears;

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;

    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo;

    @SerializedName("EmailId")
    @Expose
    private String emailId;

    @SerializedName("OccupationCode")
    @Expose
    private String occupationCode;

    @SerializedName("Occupation")
    @Expose
    private String occupation;

    @SerializedName("MaritalStatusCode")
    @Expose
    private String maritalStatusCode;

    @SerializedName("MaritalStatusName")
    @Expose
    private String maritalStatusName;

    @SerializedName("RSCode")
    @Expose
    private String rSCode;

    @SerializedName("Relation")
    @Expose
    private String relation;

    @SerializedName("Nationality")
    @Expose
    private String nationality;

    @SerializedName("NationalityName")
    @Expose
    private String nationalityName;

    @SerializedName("IsNFSA")
    @Expose
    private String isNFSA;

    @SerializedName("IsHOF")
    @Expose
    private String isHOF;

    @SerializedName("AnnualIncome")
    @Expose
    private String annualIncome;

    @SerializedName("DateOfDeath")
    @Expose
    private String dateOfDeath;

    @SerializedName("CCatCode")
    @Expose
    private String cCatCode;

    @SerializedName("CastCategory")
    @Expose
    private String castCategory;

    @SerializedName("BankACNo")
    @Expose
    private String bankACNo;

    @SerializedName("BankCode")
    @Expose
    private String bankCode;

    @SerializedName("Bank")
    @Expose
    private String bank;

    @SerializedName("BranchCode")
    @Expose
    private String branchCode;

    @SerializedName("BranchName")
    @Expose
    private String branchName;

    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;

    @SerializedName("IsApplicant")
    @Expose
    private String isApplicant;

    @SerializedName("IsPhysicallyChallenged")
    @Expose
    private String isPhysicallyChallenged;

    @SerializedName("PhysicallyChallengedCode")
    @Expose
    private String physicallyChallengedCode;

    @SerializedName("PhysicallyChallenged_Name_EN")
    @Expose
    private String physicallyChallenged_Name_EN;

    @SerializedName("PhysicallyChallengedPercentage")
    @Expose
    private String physicallyChallengedPercentage;

    @SerializedName("EpicNo")
    @Expose
    private String epicNo;

    @SerializedName("NPRNo")
    @Expose
    private String nPRNo;

    @SerializedName("UIDAINo")
    @Expose
    private String uIDAINo;

    @SerializedName("UIDAIEnrolmentNo")
    @Expose
    private String uIDAIEnrolmentNo;

    @SerializedName("DocumentId")
    @Expose
    private String documentId;

    @SerializedName("Base64ImageData")
    @Expose
    private String base64ImageData;

    public String getMemberId() {
        return memberId;
    }

    public String getRationCardNo() {
        return rationCardNo;
    }

    public String getMember_Name_EN() {
        return member_Name_EN;
    }

    public String getMember_Name_In_Aadhar_EN() {
        return member_Name_In_Aadhar_EN;
    }

    public String getIsAadharVerified() {
        return isAadharVerified;
    }

    public String getMother_Name_EN() {
        return mother_Name_EN;
    }

    public String getFather_Name_EN() {
        return father_Name_EN;
    }

    public String getSpouse_Name_EN() {
        return spouse_Name_EN;
    }

    public String getGender() {
        return gender;
    }

    public String getGenderName() {
        return genderName;
    }

    public String getActualOrDeclaredDOB() {
        return actualOrDeclaredDOB;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAgeInYears() {
        return ageInYears;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getRelation() {
        return relation;
    }

    public String getIsNFSA() {
        return isNFSA;
    }

    public String getIsHOF() {
        return isHOF;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public String getBankACNo() {
        return bankACNo;
    }

    public String getBank() {
        return bank;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getiFSCCode() {
        return iFSCCode;
    }

    public String getIsPhysicallyChallenged() {
        return isPhysicallyChallenged;
    }

    public String getPhysicallyChallenged_Name_EN() {
        return physicallyChallenged_Name_EN;
    }

    public String getPhysicallyChallengedPercentage() {
        return physicallyChallengedPercentage;
    }

    public String getuIDAINo() {
        return uIDAINo;
    }

    public String getBase64ImageData() {
        return base64ImageData;
    }
}