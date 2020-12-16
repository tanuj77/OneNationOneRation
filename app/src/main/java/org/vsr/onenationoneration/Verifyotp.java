package org.vsr.onenationoneration;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chaos.view.PinView;
import com.google.gson.JsonObject;
import org.vsr.onenationoneration.Helper.LocalHelper;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.AadharVerifyData;
import org.vsr.onenationoneration.apiCall.bin.OTPData;
import org.vsr.onenationoneration.apiCall.bin.StateLocalDetailsData;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verifyotp extends AppCompatActivity {
    TextView tvDisclaimer, tvResendOtp;
    Button verifyOtpButton;
    PinView pinView;
    String enteredOTP, savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ImageView ivStateLogo;
    public int counter = 59, counter2 = 59;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomDisclaimer, countTime;
    ProgressDialog pd;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);
        ivStateLogo = findViewById(R.id.iv_statelogo);
        pinView = findViewById(R.id.pinView);
        countTime = findViewById(R.id.tv_counttime);
        tvResendOtp = findViewById(R.id.tv_resendotp);
        verifyOtpButton = findViewById(R.id.vo_button);
        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        String yourtext = "<a style='text-decoration:underline' href='Didn't get the OTP? RESEND OTP.'> Didn't get the OTP? RESEND OTP. </a>";
        if (Build.VERSION.SDK_INT >= 24) {
            tvResendOtp.setText(Html.fromHtml(yourtext, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvResendOtp.setText(Html.fromHtml(yourtext));
        }

        Paper.init(this);
        savedStateID = Paper.book().read("stateID");
        savedStateName = Paper.book().read("stateName");
        savedLanguageID = Paper.book().read("languageID");
        savedUniqueDeviceID = Paper.book().read("deviceUniqueID");
        savedAadharCardNumber = Paper.book().read("AadharCardNumber");
        savedDepartmentName = Paper.book().read("bottomDepartmentName");
        savedAsOn = Paper.book().read("bottomAsOn");
        savedSourceName = Paper.book().read("bottomSourceName");
        savedDisclaimer = Paper.book().read("bottomDisclaimer");

        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomDisclaimer.setText(savedDisclaimer);
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID + "  " + savedDisclaimer);

        Intent i = getIntent();
        savedTXNIDFromRegActivity = i.getStringExtra("TXNID");
        stateDetails();

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Verifyotp.this);
                pd.setMessage("Verifying Aadhar..");
                pd.show();
                enteredOTP = pinView.getText().toString();
                submitOTP();
            }

            private void submitOTP() {
                String tokenDateTime = getTokenDateTime();
                String date = getDateTime();
                final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("State", savedStateID);
                jsonObject.addProperty("LanguageId", savedLanguageID);
                jsonObject.addProperty("TxnId", savedTXNIDFromRegActivity);
                jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
                jsonObject.addProperty("OTP", enteredOTP);
                jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
                jsonObject.addProperty("AuthString", "NElLM3BCdVhqaE1mKzROaTFYUUk2YmxvcU1EZUlodDhWNURJYVpCMWp6VT06MDAwMDAwMDAwMDAwMDAxOjYzNzMwNzg1NjY0NTcxODUwNQ=="); // yha per AuthString pass karna
                jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
                Call<OTPData> otpDataCall = apiInterface.otpData(jsonObject);
                otpDataCall.enqueue(new Callback<OTPData>() {
                    @Override
                    public void onResponse(Call<OTPData> call, Response<OTPData> response) {

                        String status = response.body().getStatus();
                        if (status.equalsIgnoreCase("ACCP") || status == "ACCP") {
                            Toast.makeText(getApplicationContext(), response.body().getRemarks(), Toast.LENGTH_LONG).show();
                            Log.i("SRStxnID verifOTP", savedTXNIDFromRegActivity);
                            Intent intent = new Intent(Verifyotp.this, HomeActivity.class);
                            intent.putExtra("TXNID", savedTXNIDFromRegActivity);
                            startActivity(intent);
                            pd.dismiss();
                        } else {
                            pd.dismiss();
                            Toast.makeText(Verifyotp.this, response.body().getRemarks(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OTPData> call, Throwable t) {
                        pd.dismiss();
                        Log.e("otpverifyerror", t.toString());
                    }
                });
            }
        });


        new CountDownTimer(59000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countTime.setText(String.valueOf(counter));
                counter--;
            }

            @Override
            public void onFinish() {
                countTime.setVisibility(View.INVISIBLE);
                tvResendOtp.setVisibility(View.VISIBLE);
            }
        }.start();


        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvResendOtp.setVisibility(View.INVISIBLE);
                countTime.setVisibility(View.VISIBLE);
                new CountDownTimer(59000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        countTime.setText(String.valueOf(counter2));
                        counter2--;
                    }
                    @Override
                    public void onFinish() {
                        countTime.setVisibility(View.INVISIBLE);
                        tvResendOtp.setVisibility(View.VISIBLE);
                    }
                }.start();
                submitForm();            }
        });
    }

    public void stateDetails() {
        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("Language", savedLanguageID);
        jsonObject.addProperty("PageId", "11");
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "dGs3dWlCNlBCei9ZNXMrQUVFNTZJbnBIUUJETEVrbDd5UFlFS0JqZTdzST06MDAwMDAwMDAwMDAwMDAxOjYzNzMxMjkzMjcxMjcxODM2Mg=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date);
        Call<StateLocalDetailsData> stateLocalDetailsDataCall = apiInterface.stateLocalDetailsData(jsonObject);
        stateLocalDetailsDataCall.enqueue(new Callback<StateLocalDetailsData>() {
            @Override
            public void onResponse(Call<StateLocalDetailsData> call, Response<StateLocalDetailsData> response) {

                String strStateLogo = response.body().getStateLocalDetailsDataLists().getBase64StateLogo();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imageBytes = Base64.decode(strStateLogo, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivStateLogo.setImageBitmap(decodedImage);
            }

            @Override
            public void onFailure(Call<StateLocalDetailsData> call, Throwable t) {
                Log.e("Registrationerror", t.toString());
            }
        });
    }

    private void submitForm() {

        pinView.setText("");
        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("UID", savedAadharCardNumber);
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        //jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "MkxjUXUvNjhPV0N0K3k2WG12b3UwRmU4S29WNnc4MnhUMjJCQ1pUd3hxQT06MDAwMDAwMDAwMDAwMDAxOjYzNzMwNzg1NTc5OTMxNTcyMg=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        Call<AadharVerifyData> aadharVerifyDataCall = apiInterface.aadharVerifyData(jsonObject);
        aadharVerifyDataCall.enqueue(new Callback<AadharVerifyData>() {
            @Override
            public void onResponse(Call<AadharVerifyData> call, Response<AadharVerifyData> response) {
                savedTXNIDFromRegActivity = response.body().getTxnId();
                Toast.makeText(getApplicationContext(), response.body().getRemarks(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AadharVerifyData> call, Throwable t) {
                Log.e("aadharverifyerror", t.toString());
            }
        });

    }

    private String getTokenDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}