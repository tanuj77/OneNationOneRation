package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrievanceTypeDataList {
    @SerializedName("GrievanceTypeCode")
    @Expose
    private String grievanceTypeCode;

    @SerializedName("GrievanceTypeName")
    @Expose
    private String grievanceTypeName;

    public String getGrievanceTypeCode() {
        return grievanceTypeCode;
    }

    public void setGrievanceTypeCode(String grievanceTypeCode) {
        this.grievanceTypeCode = grievanceTypeCode;
    }

    public String getGrievanceTypeName() {
        return grievanceTypeName;
    }

    public void setGrievanceTypeName(String grievanceTypeName) {
        this.grievanceTypeName = grievanceTypeName;
    }
}
