package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AadharVerifyData {
    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("AckCode")
    @Expose
    private String ackCode;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Remarks")
    @Expose
    private String remarks;

    @SerializedName("DateOfData")
    @Expose
    private String dateOfData;

    @SerializedName("TxnId")
    @Expose
    private String txnId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

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

    public String getDateOfData() {
        return dateOfData;
    }

    public void setDateOfData(String dateOfData) {
        this.dateOfData = dateOfData;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
