package org.vsr.onenationoneration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.GrievanceTypeDataList;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceAddResponseData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrievanceAddResponseActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String selectedFilePath, documentToUpload;
    ActionBar actionBar;
    Button bUpload, btnSubmit;
    EditText et_grivanceDiscription;
    TextView tvAttachement1;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo, savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    String strIssueCode, strResponseDate, strTentativeCompletionDate, strIssueStatusId,strRespondedBy;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievancesaddresponse);

        Paper.init(this);
        strRationCardNo = Paper.book().read("GrievancesDateActivity_RationCard");
        savedTXNIDFromRegActivity = Paper.book().read("GrievancesDateActivity_TXNID");
        strIssueCode = Paper.book().read("GrievancesListActivity_Adapter_IssueCode");
        savedStateID = Paper.book().read("stateID");
        savedStateName = Paper.book().read("stateName");
        savedLanguageID = Paper.book().read("languageID");
        // savedTXNID = Paper.book().read("TXNID");
        savedUniqueDeviceID = Paper.book().read("deviceUniqueID");
        savedAadharCardNumber = Paper.book().read("AadharCardNumber");
        savedDepartmentName = Paper.book().read("bottomDepartmentName");
        savedAsOn = Paper.book().read("bottomAsOn");
        savedSourceName = Paper.book().read("bottomSourceName");
        savedDisclaimer = Paper.book().read("bottomDisclaimer");

//        strRationCardNo = getIntent().getStringExtra("RationCard");
//        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");
//        strIssueCode = getIntent().getStringExtra("IssueCode");

        strResponseDate = getIntent().getStringExtra("ResponseDate");
        strTentativeCompletionDate = getIntent().getStringExtra("TentativeCompletionDate");
        strIssueStatusId = getIntent().getStringExtra("IssueStatusId");
        strRespondedBy = getIntent().getStringExtra("RespondedBy");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Grievance Response");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        et_grivanceDiscription = findViewById(R.id.et_griveanceresponse__discription);
        et_grivanceDiscription.setSelection(0);//set cursor at to starting in this edittext
        tvAttachement1 = findViewById(R.id.tv_griveanceresponse_attachement1);
        bUpload = findViewById(R.id.btn_griveanceresponse_uploadfile);
        btnSubmit = findViewById(R.id.btn_grievanceresponse_submit);


        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);



        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomAsOn.setText(savedAsOn);
        tvbottomSourceName.setText(savedSourceName);
        tvbottomDisclaimer.setText(savedDisclaimer);

        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitImageNData();
                // finish();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    tvAttachement1.setText(selectedFilePath);
                    ConvertToString(selectedFileUri);
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void ConvertToString(Uri uri) {
        String uriString = uri.toString();
        Log.d("data", "onActivityResult: uri" + uriString);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            Log.d("data", "onActivityResult: bytes size=" + bytes.length);
            Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            documentToUpload = Base64.encodeToString(bytes, Base64.DEFAULT);
            Log.i("ShriRadheyShyam", documentToUpload);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void submitImageNData() {
        pd = new ProgressDialog(GrievanceAddResponseActivity.this);
        pd.setMessage("Loading..");
        pd.show();
        String tokenDateTime = getTokenDateTime();
        final String date = getDateTime();
        String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
        String extension = selectedFilePath.substring(selectedFilePath.lastIndexOf("."));

        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("TxnId", savedTXNIDFromRegActivity);
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00585" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "MERNK0lGOXcyR2J2cGt6RU00K2t6bjFIMnFLQUh6WDY4cGVscjAxVTNVVT06MDAwMDAwMDAwMDAwMDAxOjYzNzM4MTgzNjk3MTE0Njc3OQ=="); // yha per AuthString pass karna
        jsonObject.addProperty("IssueCode", strIssueCode);
        JsonObject grievanceDataJson = new JsonObject();
        grievanceDataJson.addProperty("ResponseDate", strResponseDate);//06-10-2020
        grievanceDataJson.addProperty("TentativeCompletionDate", "");//strTentativeCompletionDate
        grievanceDataJson.addProperty("RespondedBy", strRespondedBy);//Name of Hof
        grievanceDataJson.addProperty("ReponseDetails", et_grivanceDiscription.getText().toString());
        grievanceDataJson.addProperty("IssueStatusId", strIssueStatusId);
        jsonObject.add("ResponseData", grievanceDataJson);

        JsonArray jsonArray = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("AttachmentName", filename);
        object.addProperty("AttachmentFileType", extension);
        object.addProperty("Base64Data", documentToUpload);
        jsonArray.add(object);
        jsonObject.add("AttachmentDetails", jsonArray);
        final Call<RCGrievanceAddResponseData> rcGrievanceAddResponseDataCall = apiInterface.rcGrievanceAddResponseData(jsonObject);
        rcGrievanceAddResponseDataCall.enqueue(new Callback<RCGrievanceAddResponseData>() {
            @Override
            public void onResponse(Call<RCGrievanceAddResponseData> call, Response<RCGrievanceAddResponseData> response) {
                Log.i("SRSuploadsubmitt", documentToUpload);
                if (response.body().getStatus().trim().equalsIgnoreCase("ACCP") || response.body().getStatus().trim() == "ACCP") {
                    Toast.makeText(GrievanceAddResponseActivity.this, response.body().getRemarks(), Toast.LENGTH_LONG).show();
                    et_grivanceDiscription.setText("");
                    tvAttachement1.setText("");
                    Intent intent = new Intent(GrievanceAddResponseActivity.this, GrievanceFullDetails.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GrievanceAddResponseActivity.this, response.body().getStatus() + "/" + response.body().getRemarks(), Toast.LENGTH_LONG).show();
                }
                Log.i("SRSuploadsubmit", response.body().getStatus());
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<RCGrievanceAddResponseData> call, Throwable t) {
                Log.e("homeerror", t.toString());
                pd.dismiss();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // onBackPressed();
                Intent intent3 = new Intent(GrievanceAddResponseActivity.this, GrievanceFullDetails.class);
                startActivity(intent3);
                return true;
            case R.id.item_home:
                Intent intent = new Intent(GrievanceAddResponseActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(GrievanceAddResponseActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}