package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RCGrievanceDetailsAttachementDataList {
    @SerializedName("AttachmentName")
    @Expose
    private String attachmentName;

    @SerializedName("AttachmentFileType")
    @Expose
    private String attachmentFileType;

    @SerializedName("Base64Data")
    @Expose
    private String base64Data;

    public String getAttachmentName() {
        return attachmentName;
    }

    public String getAttachmentFileType() {
        return attachmentFileType;
    }

    public String getBase64Data() {
        return base64Data;
    }
}