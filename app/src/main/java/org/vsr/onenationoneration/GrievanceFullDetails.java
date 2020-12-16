package org.vsr.onenationoneration;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.vsr.onenationoneration.adapters.TMRRecViewCustomAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceDetailsAttachementDataList;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceDetailsData;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceDetailsDataList;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceDetailsIssuesDataList;
import org.vsr.onenationoneration.others.ConnectionDetector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.vsr.onenationoneration.FullPDFActivity.REQUEST_PERMISSIONS;

public class GrievanceFullDetails extends AppCompatActivity {
    public TextView tvGrievanceCode, tvGrievanceDate, tvSubject, tvLatestStatus, tvDescOfGrievance, tvAttachements1, tvResponseDetails, tvStatusChangedto, tvRespondedBy, tvRespondedOnDetails, tvDescription, tvAttachements2, tvStatusChangedto2, tvRespondedBy2, tvRespondedOnDate, tvDescription2, tvAttachements3;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo, savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ProgressDialog pd;
    ActionBar actionBar;

    ConnectionDetector cd;
    private static RelativeLayout bottomLayout;
    String strIssueCode, strResponseDate,strRespondedBy,strTentativeCompletionDate, strIssueStatusId;
    RecyclerView.LayoutManager mLayoutManager;
    private RCGrievanceDetailsDataList rcGrievanceDetailsDataLists;
    private List<RCGrievanceDetailsIssuesDataList> rcGrievanceDetailsIssuesDataLists;
    private List<RCGrievanceDetailsAttachementDataList> rcGrievanceDetailsAttachementDataLists;
    Button btnAddResponse;
    boolean boolean_permission;
    private Timer timer;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievancesfulldetails);

//        strRationCardNo = getIntent().getStringExtra("RationCard");
//        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");
//        strIssueCode = getIntent().getStringExtra("IssueCode");
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

        actionBar = getSupportActionBar();
        actionBar.setTitle("Grievances Details");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        tvGrievanceCode = findViewById(R.id.tv_grfd_grievancecode);
        tvGrievanceDate = findViewById(R.id.tv_grfd_grievancedate);
        tvSubject = findViewById(R.id.tv_grfd_subject);
        tvLatestStatus = findViewById(R.id.tv_grfd_lateststatus);
        tvDescOfGrievance = findViewById(R.id.tv_grfd_descofgrievance);
        //tvAttachements1 = findViewById(R.id.tv_grfd_attachements1);
        recyclerView = findViewById(R.id.recycler_view_grievancefulldetails);

        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);
        btnAddResponse = findViewById(R.id.btn_griefull_submit);

        cd = new ConnectionDetector(getApplicationContext());
        bottomLayout = findViewById(R.id.loadItemsLayout_recyclerView);



        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomAsOn.setText(savedAsOn);
        tvbottomSourceName.setText(savedSourceName);
        tvbottomDisclaimer.setText(savedDisclaimer);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcGrievanceDetailsIssuesDataLists = new ArrayList<>();
        rcGrievanceDetailsAttachementDataLists = new ArrayList<>();

        fn_permission();
        populatRecyclerView();

        btnAddResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GrievanceFullDetails.this, GrievanceAddResponseActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putString("RationCard", strRationCardNo);
//                bundle.putString("TXNID", savedTXNIDFromRegActivity);
//                bundle.putString("IssueCode", strIssueCode);
                bundle.putString("ResponseDate", strResponseDate);
                bundle.putString("TentativeCompletionDate", strTentativeCompletionDate);
                bundle.putString("IssueStatusId", strIssueStatusId);
                bundle.putString("RespondedBy",strRespondedBy);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void populatRecyclerView() {
        pd = new ProgressDialog(GrievanceFullDetails.this);
        pd.setMessage("Loading..");
        pd.show();
        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("TxnId", savedTXNIDFromRegActivity);
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00585" + tokenDateTime + "00981");
        jsonObject.addProperty("IssueCode", strIssueCode.trim());
        jsonObject.addProperty("AuthString", "VWg1cDk1TGtQY3Y3RlFrV2pzWWFZUzJxcDJHSHIzNVd5VDRsRlpDK1FCVT06MDAwMDAwMDAwMDAwMDAxOjYzNzM4MTExMTk3MjA1NTY2MQ=="); // yha per AuthString pass karna
        // yha per DateOfData pass karna
        final Call<RCGrievanceDetailsData> rcGrievanceDetailsDataCall = apiInterface.rcGrievanceDetailsData(jsonObject);
        rcGrievanceDetailsDataCall.enqueue(new Callback<RCGrievanceDetailsData>() {
            @Override
            public void onResponse(Call<RCGrievanceDetailsData> call, Response<RCGrievanceDetailsData> response) {
                Log.i("SRSwatchfull", "" + response.body());
                strResponseDate = response.body().getResponseDate();
                strRespondedBy=response.body().getRcGrievanceDetailsDataLists().getName_EN();
                strTentativeCompletionDate = response.body().getRcGrievanceDetailsDataLists().getTentativeCompletionDate();
                strIssueStatusId = response.body().getRcGrievanceDetailsDataLists().getIssueStatusId();
                rcGrievanceDetailsDataLists = response.body().getRcGrievanceDetailsDataLists();
                rcGrievanceDetailsIssuesDataLists.addAll(response.body().getRcGrievanceDetailsDataLists().getRcGrievanceDetailsIssuesDataLists());
                rcGrievanceDetailsAttachementDataLists.addAll(response.body().getRcGrievanceDetailsDataLists().getRcGrievanceDetailsIssuesDataLists().get(0).getRcGrievanceDetailsAttachementDataLists());

                tvGrievanceCode.setText(rcGrievanceDetailsDataLists.getIssueCode());
                tvGrievanceDate.setText(rcGrievanceDetailsDataLists.getIssueDate());
                tvSubject.setText(rcGrievanceDetailsDataLists.getIssueSubject());
                tvLatestStatus.setText(rcGrievanceDetailsDataLists.getIssueStatus());
                tvDescOfGrievance.setText(rcGrievanceDetailsDataLists.getIssueStatusDescription());
             //   tvAttachements1.setText("Attachements(if any)");

                TMRRecViewCustomAdapter tmrRecViewCustomAdapter = new TMRRecViewCustomAdapter(GrievanceFullDetails.this, rcGrievanceDetailsIssuesDataLists);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(tmrRecViewCustomAdapter);
                tmrRecViewCustomAdapter.notifyDataSetChanged();

//                tvStatusChangedto.setText("Status Changed To");
//                tvRespondedBy.setText(rcGrievanceDetailsIssuesDataLists.get(0).getFeedBy());
//                tvRespondedOnDate.setText(rcGrievanceDetailsIssuesDataLists.get(0).getResponseDate());
//                tvDescription.setText(rcGrievanceDetailsIssuesDataLists.get(0).getIssueDescription());

//                String strBase64Data = rcGrievanceDetailsAttachementDataLists.get(0).getBase64Data();
//                String strFileName = rcGrievanceDetailsAttachementDataLists.get(0).getAttachmentName();
//                String strFileType = rcGrievanceDetailsAttachementDataLists.get(0).getAttachmentFileType();
//                // writeResponseBodyToDisk(strBase64Data,strFileName,strFileType);
//                storetoPdfandOpen(GrievanceFullDetails.this, strBase64Data, strFileName, strFileType);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<RCGrievanceDetailsData> call, Throwable t) {
                pd.dismiss();
                Log.e("homeerror", t.toString());
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
                onBackPressed();
                return true;
            case R.id.item_home:
                Intent intent = new Intent(GrievanceFullDetails.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(GrievanceFullDetails.this, MainActivity.class);
                startActivity(intent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void storetoPdfandOpen(Context context, String strpdfbase64Data, String fileName, String fileType) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(strpdfbase64Data, Base64.DEFAULT);

            String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            //  String strfileName = "04novfilename.pdf";
            String strfileName = fileName;
            destination += strfileName;
            // File someFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "haribol" + fileName);

            File file = new File(destination);
            Log.i("SRS_filepathfirst", "" + file);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(imageBytes);
            Toast.makeText(GrievanceFullDetails.this, "SRS_done", Toast.LENGTH_SHORT).show();
            //   fos.flush();
            fos.close();


            File file1 = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(imageBytes);
            fileOutputStream.close();

//            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//            pdfIntent.setDataAndType(Uri.fromFile(someFile), fileType);
//            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            startActivity(pdfIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final void selectPdfFromStorage() {
        //Toast.makeText((Context)this, (CharSequence)"selectPDF", 1).show();
        Intent browseStorage = new Intent("android.intent.action.GET_CONTENT");
        browseStorage.setType("application/pdf");
        browseStorage.addCategory("android.intent.category.OPENABLE");
        this.startActivityForResult(Intent.createChooser(browseStorage, (CharSequence)"Select PDF"), 99);
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(GrievanceFullDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(GrievanceFullDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(GrievanceFullDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(GrievanceFullDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}