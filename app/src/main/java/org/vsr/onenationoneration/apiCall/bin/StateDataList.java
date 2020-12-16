package org.vsr.onenationoneration.apiCall.bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateDataList {
    @SerializedName("_StateCode")
    @Expose
    private String state_code;

    @SerializedName("_StateName")
    @Expose
    private String stateName;

    @SerializedName("_StateShortName")
    @Expose
    private String stateShortName;

    @SerializedName("_OwnershipType")
    @Expose
    private String ownershipType;


    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

   }
