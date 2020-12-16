package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RCEEntitlementsDataList {

    @SerializedName("AllocationOrderNumber")
    @Expose
    private String allocationOrderNumber;

    @SerializedName("CommCode")
    @Expose
    private String commCode;

    @SerializedName("CommName")
    @Expose
    private String commName;

    @SerializedName("EntlQty")
    @Expose
    private String entlQty;

    @SerializedName("Unit")
    @Expose
    private String unit;

    @SerializedName("EP")
    @Expose
    private String ep;

    @SerializedName("CIP")
    @Expose
    private String cip;

    @SerializedName("SS")
    @Expose
    private String ss;

    @SerializedName("OE")
    @Expose
    private String oe;

    @SerializedName("RatePerUnit")
    @Expose
    private String ratePerUnit;

    @SerializedName("TotalAmt")
    @Expose
    private String totalAmt;

    @SerializedName("TotalSubsidy")
    @Expose
    private String totalSubsidy;

    @SerializedName("Remarks")
    @Expose
    private String remarks;

    public String getAllocationOrderNumber() {
        return allocationOrderNumber;
    }

    public String getCommName() {
        return commName;
    }

    public String getEntlQty() {
        return entlQty;
    }

    public String getUnit() {
        return unit;
    }

    public String getRatePerUnit() {
        return ratePerUnit;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

      public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEp() {
        return ep;
    }

    public String getCip() {
        return cip;
    }

       public String getSs() {
        return ss;
    }

       public String getOe() {
        return oe;
    }

       public String getTotalSubsidy() {
        return totalSubsidy;
    }
}
