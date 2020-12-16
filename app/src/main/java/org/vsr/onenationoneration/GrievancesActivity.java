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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.vsr.onenationoneration.adapters.GrievanceTypeAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.GrievanceTypeData;
import org.vsr.onenationoneration.apiCall.bin.GrievanceTypeDataList;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

public class GrievancesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String selectedFilePath, filename, extension, documentToUpload="", statusGrievsanceTypeStatus, grievanceTypeCode, grievanceTypeName;
    ActionBar actionBar;
    Button bUpload, btnSubmit, btnGrivanceList;
    EditText et_grivanceDiscription;
    Spinner spinnerGrievanceType;
    TextView tvAttachement1, tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo, savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    List<GrievanceTypeDataList> grievanceTypeDataListList = new ArrayList<>();
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievances);
        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");
        actionBar = getSupportActionBar();
        actionBar.setTitle("Grievances");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        spinnerGrievanceType = findViewById(R.id.spinner_griev_grievancetype);
        et_grivanceDiscription = findViewById(R.id.et_griev_grievancediscription);
        spinnerGrievanceType.setOnItemSelectedListener(this);
        tvAttachement1 = findViewById(R.id.tv_gri_attachement1);
        bUpload = findViewById(R.id.btn_uploadfile);
        btnSubmit = findViewById(R.id.btn_griev_submit);
        btnGrivanceList = findViewById(R.id.btn_griev_grivancelist);

        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        Paper.init(this);
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

        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomAsOn.setText(savedAsOn);
        tvbottomSourceName.setText(savedSourceName);
        tvbottomDisclaimer.setText(savedDisclaimer);

        grievanceType();
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
            }
        });
        btnGrivanceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GrievancesActivity.this, GrievancesDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardNo);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
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
                    filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
                    extension = selectedFilePath.substring(selectedFilePath.lastIndexOf("."));
                    ConvertToString(selectedFileUri);
                } else {
                    tvAttachement1.setText("");
                    filename = "";
                    extension = "";
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
            //byte[] up = Base64.decode(documentToUpload, Base64.DEFAULT);
            //OutputStream op = getContentResolver().openOutputStream()

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

    private void grievanceType() {
        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", "00");
        jsonObject.addProperty("Token", "00XXX" + tokenDateTime + "00001"); // yha per token pass karna
        jsonObject.addProperty("AuthString", "VmU4UjZON1dzSTZSSFBQUFl3blpkLzZLTVd0c01WcEJjRTU3MUNyTmY5az06MDAwMDAwMDAwMDAwMDAxOjYzNzA2MzA2NDIzNzc1NjQ2OQ=="); // yha per AuthString pass karna
        Call<GrievanceTypeData> grievanceTypeDataCall = apiInterface.grievanceTypeData(jsonObject);
        grievanceTypeDataCall.enqueue(new Callback<GrievanceTypeData>() {
            @Override
            public void onResponse(Call<GrievanceTypeData> call, Response<GrievanceTypeData> response) {
                statusGrievsanceTypeStatus = response.body().getStatus();
                GrievanceTypeDataList grievanceTypeDataList = new GrievanceTypeDataList();
                grievanceTypeDataList.setGrievanceTypeCode("000");
                grievanceTypeDataList.setGrievanceTypeName(" Select ");
                grievanceTypeDataListList.addAll(response.body().getGrievanceTypeDataLists());
                Collections.reverse(grievanceTypeDataListList);
                grievanceTypeDataListList.add(grievanceTypeDataList);
                Collections.reverse(grievanceTypeDataListList);

                GrievanceTypeAdapter adapter = new GrievanceTypeAdapter(GrievancesActivity.this, R.layout.textview_with_padding, grievanceTypeDataListList);
                adapter.setDropDownViewResource(R.layout.textview_with_padding);
                spinnerGrievanceType.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<GrievanceTypeData> call, Throwable t) {
                Log.e("stateerror", t.toString());
            }
        });

        spinnerGrievanceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {
                    grievanceTypeCode = grievanceTypeDataListList.get(position).getGrievanceTypeCode();
                    grievanceTypeName = grievanceTypeDataListList.get(position).getGrievanceTypeName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void submitImageNData() {
        pd = new ProgressDialog(GrievancesActivity.this);
        pd.setMessage("Loading..");
        pd.show();
        String tokenDateTime = getTokenDateTime();
        final String date = getDateTime();

        if (documentToUpload.equalsIgnoreCase(null) || documentToUpload == "") {
            documentToUpload = "";
        }

        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("TxnId", savedTXNIDFromRegActivity);
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00585" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "T2gxNVZybVZveUw3OC91VzdHTG5YMklsblVDbGx5b0VMaHY1M2NTSUZraz06MDAwMDAwMDAwMDAwMDAxOjYzNzM2NDc0NzA4NjI1NjY3Nw=="); // yha per AuthString pass karna
        JsonObject grievanceDataJson = new JsonObject();
        DateFormat griveancedateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date grievancedate = new Date();
        String strGrievDate = griveancedateFormat.format(grievancedate);
        grievanceDataJson.addProperty("GrievanceDate", strGrievDate);//06-10-2020
        grievanceDataJson.addProperty("GrievanceTypeCode", grievanceTypeCode);//RNR
        grievanceDataJson.addProperty("GrievanceDetails", et_grivanceDiscription.getText().toString());
        grievanceDataJson.addProperty("GrievanceStatus", "ACCP");
        jsonObject.add("GrievanceData", grievanceDataJson);

        JsonArray jsonArray = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("AttachmentName", filename);
        object.addProperty("AttachmentFileType", extension);
        object.addProperty("Base64Data", documentToUpload);
        jsonArray.add(object);
        jsonObject.add("AttachmentDetails", jsonArray);
        final Call<RCGrievanceData> rcGrievanceDataCall = apiInterface.rcGrievanceData(jsonObject);
        rcGrievanceDataCall.enqueue(new Callback<RCGrievanceData>() {
            @Override
            public void onResponse(Call<RCGrievanceData> call, Response<RCGrievanceData> response) {
                Log.i("SRSuploadsubmitt", documentToUpload);
                if (response.body().getStatus().equalsIgnoreCase("ACCP") || response.body().getStatus() == "ACCP") {
                    Toast.makeText(GrievancesActivity.this, "" + date + "Grievance submitted successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(GrievancesActivity.this, response.body().getStatus() + "/" + response.body().getRemarks(), Toast.LENGTH_LONG).show();
                }

                Log.i("SRSuploadsubmit", response.body().getStatus());
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<RCGrievanceData> call, Throwable t) {
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
                onBackPressed();
                return true;
            case R.id.item_home:
                Intent intent = new Intent(GrievancesActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(GrievancesActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}