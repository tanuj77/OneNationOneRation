package org.vsr.onenationoneration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.vsr.onenationoneration.Helper.LocalHelper;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.AadharVerifyData;
import org.vsr.onenationoneration.apiCall.bin.StateLocalDetailsData;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {
    private EditText inputAadhar;
    ImageView ivStateLogo;
    private TextView tvErrorAadhar, tvAadhar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomDisclaimer;
    private Button btnSignUp;
    String savedStateID, savedStateName, savedLanguageID, savedUniqueDeviceID, strDepartmentName, strAsOn, strDisclaimer;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ivStateLogo = findViewById(R.id.iv_statelogo);
        tvAadhar = findViewById(R.id.reg_tv_aadhar);
        tvErrorAadhar = findViewById(R.id.reg_tv_erroraadhar);
        inputAadhar = findViewById(R.id.reg_input_adhar);
        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        Paper.init(this);
        savedStateID = Paper.book().read("stateID");
        savedStateName = Paper.book().read("stateName");
        savedLanguageID = Paper.book().read("languageID");
        savedUniqueDeviceID = Paper.book().read("deviceUniqueID");
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);

        btnSignUp = findViewById(R.id.btn_signup);

        stateDetails();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(Registration.this);
                pd.setMessage("Verifying Aadhar..");
                pd.show();
                submitForm();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateAadhar()) {
            return;
        }

        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("UID", inputAadhar.getText().toString());
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "MkxjUXUvNjhPV0N0K3k2WG12b3UwRmU4S29WNnc4MnhUMjJCQ1pUd3hxQT06MDAwMDAwMDAwMDAwMDAxOjYzNzMwNzg1NTc5OTMxNTcyMg=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        Call<AadharVerifyData> aadharVerifyDataCall = apiInterface.aadharVerifyData(jsonObject);
        aadharVerifyDataCall.enqueue(new Callback<AadharVerifyData>() {
            @Override
            public void onResponse(Call<AadharVerifyData> call, Response<AadharVerifyData> response) {
                Paper.book().write("AadharCardNumber", inputAadhar.getText().toString());
                String status = response.body().getStatus();
                if (status.trim().equalsIgnoreCase("ACCP") || status.trim() == "ACCP") {
                    Toast.makeText(getApplicationContext(), response.body().getRemarks(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Registration.this, Verifyotp.class);
                    String txnID = response.body().getTxnId();
                    intent.putExtra("TXNID", txnID);
                    startActivity(intent);
                    pd.dismiss();
                } else {
                    pd.dismiss();
                    Toast.makeText(Registration.this, response.body().getRemarks(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AadharVerifyData> call, Throwable t) {
                pd.dismiss();
                Log.e("aadharverifyerror", t.toString());
            }
        });
    }

    public void stateDetails() {
        pd = new ProgressDialog(Registration.this);
        pd.setMessage("Loading..");
        pd.show();
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
                strDepartmentName = response.body().getStateLocalDetailsDataLists().getDepartmentName();
                String dateInString = response.body().getDateOfData();

                String[] updatedDate = dateInString.split("-");
                String month = updatedDate[0];
                String date = updatedDate[1];
                String year = updatedDate[2];
                String formatedDate = date + "/" + month + "/" + year;

                strAsOn = formatedDate;
                strDisclaimer = response.body().getStateLocalDetailsDataLists().getDisclaimer();
                String govtName = response.body().getStateLocalDetailsDataLists().getGovtName();
                String tollFreeNo = response.body().getStateLocalDetailsDataLists().getTollFreeNo();
                String emailOfDept = response.body().getStateLocalDetailsDataLists().getEmailIdofDept();
                String websiteLink = response.body().getStateLocalDetailsDataLists().getWebsiteLink();
                Paper.book().write("bottomDepartmentName", strDepartmentName);
                Paper.book().write("bottomAsOn", strAsOn);
                Paper.book().write("bottomSourceName", "source Name(RegistrationActivity)");
                Paper.book().write("bottomDisclaimer", strDisclaimer);
                Paper.book().write("bottomGovtName", govtName);
                Paper.book().write("bottomTollFreeNo", tollFreeNo);
                Paper.book().write("bottomEmailOfDept", emailOfDept);
                Paper.book().write("bottomWebsiteLink", websiteLink);

                tvbottomDepartmentName.setText(strDepartmentName);
                tvbottomStateName.setText(savedStateName);
                tvbottomDisclaimer.setText(strDisclaimer);
                String strStateLogo = response.body().getStateLocalDetailsDataLists().getBase64StateLogo();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imageBytes = Base64.decode(strStateLogo, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivStateLogo.setImageBitmap(decodedImage);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<StateLocalDetailsData> call, Throwable t) {
                Log.e("Registrationerror", t.toString());
                pd.dismiss();
            }
        });
    }

    private boolean validateAadhar() {
        if (inputAadhar.getText().toString().trim().isEmpty()) {
            tvErrorAadhar.setVisibility(View.VISIBLE);
            tvErrorAadhar.setError(getString(R.string.err_msg_aadhar));
            requestFocus(inputAadhar);
            return false;
        } else {
            tvErrorAadhar.setVisibility(View.INVISIBLE);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.reg_input_adhar:
                    validateAadhar();
                    break;
            }
        }
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