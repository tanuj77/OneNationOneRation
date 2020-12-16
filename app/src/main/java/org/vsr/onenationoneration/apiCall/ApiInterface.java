package org.vsr.onenationoneration.apiCall;


import com.google.gson.JsonObject;

import org.vsr.onenationoneration.apiCall.bin.AadharVerifyData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetailsData;
import org.vsr.onenationoneration.apiCall.bin.GrievanceTypeData;
import org.vsr.onenationoneration.apiCall.bin.LanguageData;
import org.vsr.onenationoneration.apiCall.bin.OTPData;
import org.vsr.onenationoneration.apiCall.bin.RCEntitlementData;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceAddResponseData;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceData;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceDetailsData;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceListData;
import org.vsr.onenationoneration.apiCall.bin.StateData;
import org.vsr.onenationoneration.apiCall.bin.StateLocalDetailsData;
import org.vsr.onenationoneration.apiCall.bin.TMRData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    // @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("Locations/get_state_list_json")
    Call<StateData> stateData(@Body JsonObject jsonObject);

    @POST("MiscMasters/get_state_local_details")
    Call<StateLocalDetailsData> stateLocalDetailsData(@Body JsonObject jsonObject);

    @POST("MiscMasters/get_state_languages")
    Call<LanguageData> languageData(@Body JsonObject jsonObject);

    @POST("Public/get_public_user_verification")
    Call<AadharVerifyData> aadharVerifyData(@Body JsonObject jsonObject);

    @POST("Public/get_public_user_verification_otp")
    Call<OTPData> otpData(@Body JsonObject jsonObject);

    @POST("RationCard/get_ration_card_details")
    Call<FamilyDetailsData> familyDetailsData(@Body JsonObject jsonObject);

    @POST("RationCard/get_ration_card_entitlement")
    Call<RCEntitlementData> rcEntitlementData(@Body JsonObject jsonObject);

    @POST("RationCard/track_my_ration")
    Call<TMRData> tmrData(@Body JsonObject jsonObject);

    @POST("PGRMS/grievance_type_list")
    Call<GrievanceTypeData> grievanceTypeData(@Body JsonObject jsonObject);

    @POST("PGRMS/set_ration_card_grievance")
    Call<RCGrievanceData> rcGrievanceData(@Body JsonObject jsonObject);

    @POST("PGRMS/get_ration_card_grievance_list")
    Call<RCGrievanceListData> rcGrievanceListData(@Body JsonObject jsonObject);

    @POST("PGRMS/get_ration_card_grievance_details")
    Call<RCGrievanceDetailsData> rcGrievanceDetailsData(@Body JsonObject jsonObject);

    @POST("PGRMS/set_ration_card_grievance_addnew_response")
    Call<RCGrievanceAddResponseData> rcGrievanceAddResponseData(@Body JsonObject jsonObject);
}