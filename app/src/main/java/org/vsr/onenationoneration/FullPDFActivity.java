package org.vsr.onenationoneration;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.gson.JsonObject;
import com.google.zxing.WriterException;

import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetailsData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetailsDataList;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetails_RationCardAddressData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetails_RationCardMembersData;
import org.vsr.onenationoneration.apiCall.bin.StateLocalDetailsData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Member;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullPDFActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_generate;
    TextView tv_link;
    ImageView iv_image;
    ScrollView scrollView;
    HorizontalScrollView horizontalScrollView;
    View viewOflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String destination;


    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer, savedGovtName, savedTollFreeNo, savedWebsiteLink, savedEmailOfDept;
    ActionBar actionBar;
    TextView tvStateName, tvDepartmentName, tvDepartmentName2, tvDepartmentName3, tvDepartmentName4, tvSchemeName, tvSchemeName2, tvSchemeName3, tvSchemeName4, tvRationCard, tvRationCard2, tvRationCard3, tvRationCard4, tvGender, tvGender2, tvNooffamilyMember, tvNooffamilyMember2, tvHof, tvHof2, tvHusbandorFather, tvHusbandorFather2;
    TextView tvDOB, tvDOB2, tvAddress, tvAddress2, tvMriMr, tvMriMr2, tvMriMr3, tvPin, tvHomeFps, tvFpsCode;
    private List<FamilyDetails_RCDetails_RationCardMembersData> familyDetails_rcDetails_rationCardMembersData;
    TextView tvDisclaimer, tvDisclaimer2, tvInfo1, tvInfo2, tvInfo3, tvInfo4, tvTollFreeNo, tvEmailOfDept, tvWebsiteLink;
    ImageView ivUSer, ivStateLogo, ivStateLogo2, ivStateLogo3, ivStateLogo4, qrimg, qrimg2;
    TextView tv_FamilyMember, tv_FamilyMember2, tv_FamilyMember3, tv_FamilyMember4, tv_FamilyMember5, tv_FamilyMember6, tv_FamilyMember7, tv_FamilyMember8, tv_FamilyMember9, tv_FamilyMember10, tv_ExtraFamilyMember, tv_issueDate, tv_printDate, tv_issueDate2, tv_printDate2;
    LinearLayout llRuntime;
    String strRationCardNo;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpdf);

        init();
        fn_permission();
        listener();
    }


    private void init() {
        btn_generate = findViewById(R.id.btn_generate);
        tv_link = findViewById(R.id.tv_mr_stategovtname);
        iv_image = findViewById(R.id.iv_mr_govtlogo);
        scrollView = findViewById(R.id.fullpdf_scrollView);
        horizontalScrollView = findViewById(R.id.fullpdf_horiscrollView);
        viewOflayout = findViewById(R.id.fullpdf_horiscrollView);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");
        actionBar = getSupportActionBar();
        actionBar.setTitle("m-Ration Card");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ivStateLogo = findViewById(R.id.iv_mr_govtlogo);
        ivStateLogo2 = findViewById(R.id.iv_mr_govtlogo2);
        ivStateLogo3 = findViewById(R.id.iv_mr_govtlogo3);
        ivStateLogo4 = findViewById(R.id.iv_mr_govtlogo4);
        tvStateName = findViewById(R.id.tv_mr_stategovtname);
        tvDepartmentName = findViewById(R.id.tv_mr_depname);
        // tvDepartmentName2 = findViewById(R.id.tv_mr_depname2);
        // tvDepartmentName3 = findViewById(R.id.tv_mr_depname3);
        //  tvDepartmentName4 = findViewById(R.id.tv_mr_depname4);
        tvSchemeName = findViewById(R.id.tv_mr_schemname);
        tvSchemeName2 = findViewById(R.id.tv_mr_schemname2);
        tvSchemeName3 = findViewById(R.id.tv_mr_ratiocard3);
        tvSchemeName4 = findViewById(R.id.tv_mr_ratiocard4);
        tvRationCard = findViewById(R.id.tv_mr_ratiocardno);
        tvRationCard2 = findViewById(R.id.tv_mr_ratiocardno2);
        tvRationCard3 = findViewById(R.id.tv_mr_ratiocardno3);
        tvRationCard4 = findViewById(R.id.tv_mr_ratiocardno4);
        tvGender = findViewById(R.id.tv_mr_gender);
        tvGender2 = findViewById(R.id.tv_mr_gender2);
        ivUSer = findViewById(R.id.iv_mr_userimage);
        tvNooffamilyMember = findViewById(R.id.tv_mr_nooffamilymembers);
        // tvNooffamilyMember2 = findViewById(R.id.tv_mr_nooffamilymembers2);
        tvHof = findViewById(R.id.tv_mr_hof);
        tvHof2 = findViewById(R.id.tv_mr_hof2);
        tvHusbandorFather = findViewById(R.id.tv_mr_sonof);
        tvHusbandorFather2 = findViewById(R.id.tv_mr_sonof2);
        tvDOB = findViewById(R.id.tv_mr_dob);
        tvDOB2 = findViewById(R.id.tv_mr_dob2);
        //tvMriMr = findViewById(R.id.tv_mr_mrimr);
        tvMriMr2 = findViewById(R.id.tv_mr_mrimr2);
        // tvMriMr3 = findViewById(R.id.tv_mr_mrimr3);
        tvAddress = findViewById(R.id.tv_mr_address);
        tvAddress2 = findViewById(R.id.tv_mr_address2);
        qrimg = findViewById(R.id.iv_mr_qrcode);
        qrimg2 = findViewById(R.id.iv_mr_qrcode2);
        // tvPin = findViewById(R.id.tv_mr_pin);
        tvHomeFps = findViewById(R.id.tv_mr_homefps);
        tvFpsCode = findViewById(R.id.tv_mr_fpscode);
        tvInfo2 = findViewById(R.id.tv_tmr_info2);
        tvInfo2.setVisibility(View.INVISIBLE);
        //  llRuntime = (LinearLayout) findViewById(R.id.ll_runtime);
        tv_FamilyMember = findViewById(R.id.tv_mr_familymembers);
        tv_FamilyMember2 = findViewById(R.id.tv_mr_familymembers2);
        tv_FamilyMember3 = findViewById(R.id.tv_mr_familymembers3);
        tv_FamilyMember4 = findViewById(R.id.tv_mr_familymembers4);
        tv_FamilyMember5 = findViewById(R.id.tv_mr_familymembers5);
        tv_FamilyMember6 = findViewById(R.id.tv_mr_familymembers6);
        tv_FamilyMember7 = findViewById(R.id.tv_mr_familymembers7);
        tv_FamilyMember8 = findViewById(R.id.tv_mr_familymembers8);
        tv_FamilyMember9 = findViewById(R.id.tv_mr_familymembers9);
        tv_FamilyMember10 = findViewById(R.id.tv_mr_familymembers10);
        tv_ExtraFamilyMember = findViewById(R.id.tv_mr_extrafamilymembers);
        tv_issueDate = findViewById(R.id.tv_mr_issuedate);
        tv_printDate = findViewById(R.id.tv_mr_printdate);
        tv_issueDate2 = findViewById(R.id.tv_mr_issuedate2);
        tv_printDate2 = findViewById(R.id.tv_mr_printdate2);
        tvTollFreeNo = findViewById(R.id.tv_tollfreeno);
        tvEmailOfDept = findViewById(R.id.tv_emailofdept);
        tvWebsiteLink = findViewById(R.id.tv_websitelink);
//        tvDisclaimer=findViewById(R.id.tv_disclaimer1);
//        tvDisclaimer2=findViewById(R.id.tv_disclaimer2);


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
        savedGovtName = Paper.book().read("bottomGovtName");
        savedTollFreeNo = Paper.book().read("bottomTollFreeNo");
        savedEmailOfDept = Paper.book().read("bottomEmailOfDept");
        savedWebsiteLink = Paper.book().read("bottomWebsiteLink");
        tvTollFreeNo.setText(savedTollFreeNo);
        tvEmailOfDept.setText(savedEmailOfDept);
        tvWebsiteLink.setText(savedWebsiteLink);
//        tvDisclaimer.setText(savedDisclaimer);
//        tvDisclaimer2.setText(savedDisclaimer);

        stateDetails();
        familyDetails();
    }

    private void listener() {
        btn_generate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_generate:

                if (boolean_save) {
//                    Intent intent = new Intent(getApplicationContext(), PDFViewActivity.class);
//                    startActivity(intent);

                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(FullPDFActivity.this);
                        progressDialog.setMessage("Please wait");
                        int totalHeight = scrollView.getChildAt(0).getHeight();// parent view height
                        int totalWidth = horizontalScrollView.getChildAt(0).getWidth();// parent view width

                        bitmap = loadBitmapFromView(viewOflayout, totalWidth, totalHeight);
                        createPdf();
//                        saveBitmap(bitmap);
                    } else {
                    }
                    createPdf();
                    break;
                }
        }
    }

    private void createPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        try {
            destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            String fileName = "filename.pdf";
            destination += fileName;
            final Uri uri = Uri.parse("file://" + destination);
            File mypath2 = new File(destination);
            document.writeTo(new FileOutputStream(mypath2));
            document.close();
            Log.i("SRSS", "" + mypath2);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter2(this, destination);

            printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Log.e("dsagsfd", "" + e);
        }
    }


    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(FullPDFActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(FullPDFActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(FullPDFActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(FullPDFActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
              //  Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void stateDetails() {
        String tokenDateTime = getTokenDateTime();
        final String date = getDateTime();
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
                String changedStateName = savedStateName.replace("[", ":");
                String[] modifiedStateName = changedStateName.split(":");
                tvStateName.setText("Govt. of " + modifiedStateName[0]);
                tvDepartmentName.setText(response.body().getStateLocalDetailsDataLists().getDepartmentName());
                // tvDepartmentName2.setText(response.body().getStateLocalDetailsDataLists().getDepartmentName());
                // tvDepartmentName3.setText(response.body().getStateLocalDetailsDataLists().getDepartmentName());
                // tvDepartmentName4.setText(response.body().getStateLocalDetailsDataLists().getDepartmentName());

                tv_printDate.setText("Download Date : " + date);
                tv_printDate2.setText("Download Date : " + date);
                Toast.makeText(FullPDFActivity.this, "" + date, Toast.LENGTH_SHORT).show();
                String strStateLogo = response.body().getStateLocalDetailsDataLists().getBase64StateLogo();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imageBytes = Base64.decode(strStateLogo, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivStateLogo.setImageBitmap(decodedImage);
                ivStateLogo2.setImageBitmap(decodedImage);
                ivStateLogo3.setImageBitmap(decodedImage);
                ivStateLogo4.setImageBitmap(decodedImage);
            }

            @Override
            public void onFailure(Call<StateLocalDetailsData> call, Throwable t) {
                Log.e("Registrationerror", t.toString());
            }
        });
    }

    public void familyDetails() {
        familyDetails_rcDetails_rationCardMembersData = new ArrayList<>();
        // familyDetails_rcDetails_rationCardAddressData = new ArrayList<>();
        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("TxnId", savedTXNIDFromRegActivity);
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        //jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "SHFQM0JFSW9IQ0hSSjA0citiQk81RVJmcTlZeUNsRW5NYUVieEFwMTUzdz06MDAwMDAwMDAwMDAwMDAxOjYzNzMwNzk1MDUyNzE0MDA4MA=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        final Call<FamilyDetailsData> familyDetailsDataCall = apiInterface.familyDetailsData(jsonObject);
        familyDetailsDataCall.enqueue(new Callback<FamilyDetailsData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<FamilyDetailsData> call, Response<FamilyDetailsData> response) {
                String myConvertDate = response.body().getFamilyDetails_rcDetailsDataLists().getIssueDate();
                DateFormat iFormate = new SimpleDateFormat("dd MMMM yyyy");
                DateFormat oFormate = new SimpleDateFormat("dd-MM-yyyy");
                String strConvertDate = "";
                try {
                    strConvertDate = oFormate.format(iFormate.parse(myConvertDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_issueDate.setText("Issue Date: " + strConvertDate);
                tv_issueDate2.setText("Issue Date: " + strConvertDate);
                String SchemeCode = response.body().getFamilyDetails_rcDetailsDataLists().getSchemeCode();
                if (SchemeCode.equalsIgnoreCase("2") || SchemeCode.equalsIgnoreCase("31")) {
                    tvInfo2.setVisibility(View.VISIBLE);
                    tvInfo2.setText("It is valid to draw ration from anywhere in India\n" +
                            "under One Nation One Ration Card Scheme.");
                } else {
                    tvInfo2.setVisibility(View.INVISIBLE);
                }
                String SchemeName = response.body().getFamilyDetails_rcDetailsDataLists().getSchemeName();
                tvSchemeName.setText(SchemeName);
                tvSchemeName2.setText(SchemeName);
                tvSchemeName3.setText(SchemeName);
                tvSchemeName4.setText(SchemeName);
                String houseNo = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanent_House_Name_No();
                String locality = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanent_Locality_Colony();
                String village = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanentVillage();
                String tahsil = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanentTahsil();
                String district = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanentDistrict();
                String state = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanentState();
                String pin = response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardAddresses().getPermanent_PIN();
                tvAddress.setText(houseNo + "," + locality + ", " + village + ", " + tahsil + ", " + district + ", " + state + ", " + pin);
                tvAddress2.setText(houseNo + ", " + locality + ", " + village + "," + tahsil + ", " + district + "," + state + ", " + pin);
                // tvPin.setText(pin);
                tvHomeFps.setText(response.body().getFamilyDetails_rcDetailsDataLists().getFpsName());
                tvFpsCode.setText(response.body().getFamilyDetails_rcDetailsDataLists().getFpsCode());

                familyDetails_rcDetails_rationCardMembersData.addAll(response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardMembersData());
                //List<FamilyDetails_RCDetails_RationCardMembersData> tempList=familyDetails_rcDetails_rationCardMembersData;

                Collections.sort(familyDetails_rcDetails_rationCardMembersData, new Comparator<FamilyDetails_RCDetails_RationCardMembersData>() {
                    @Override
                    public int compare(FamilyDetails_RCDetails_RationCardMembersData lhs, FamilyDetails_RCDetails_RationCardMembersData rhs) {
                        return lhs.getMemberId().compareTo(rhs.getMemberId());
                    }
                });


                for (int i = 0; i < familyDetails_rcDetails_rationCardMembersData.size(); i++) {
                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsHOF().equalsIgnoreCase("Y")) {
                        final String strUserImage = familyDetails_rcDetails_rationCardMembersData.get(i).getBase64ImageData();
                        if (strUserImage.equalsIgnoreCase("")) {

                        } else {

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            imageBytes = Base64.decode(strUserImage, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            ivUSer.setImageBitmap(decodedImage);
                        }

                        String Membercount = String.valueOf(familyDetails_rcDetails_rationCardMembersData.size());
                        tvNooffamilyMember.setText(Membercount);
                        // tvNooffamilyMember2.setText(Membercount);
                        tvHof.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_In_Aadhar_EN());
                        tvHof2.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_In_Aadhar_EN());
                        tvHusbandorFather.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getFather_Name_EN());
                        tvHusbandorFather2.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getFather_Name_EN());
                        tvGender.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getGenderName());
                        tvGender2.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getGenderName());
                        String myConvertedDate = familyDetails_rcDetails_rationCardMembersData.get(i).getDateOfBirth();
                        DateFormat iFormatter = new SimpleDateFormat("MMMM dd yyyy");
                        DateFormat oFormatter = new SimpleDateFormat("dd-MM-yyyy");
                        String strConvertedDate = "";
                        try {
                            strConvertedDate = oFormatter.format(iFormatter.parse(myConvertedDate));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvDOB.setText(strConvertedDate);
                        tvDOB2.setText(strConvertedDate);

                    }
                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsNFSA().equalsIgnoreCase("Y")) {
                        tvRationCard.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo());
                        tvRationCard2.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo());
                        tvRationCard3.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo());
                        tvRationCard4.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo());
                    }

                    String convertedAadharNo = "";
                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo().equalsIgnoreCase("0") || familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo().equalsIgnoreCase("")) {
                        convertedAadharNo = "NA";

                    } else {
                        String strAadhar = familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo();
                        String newster = strAadhar.substring(8, 12);
                        convertedAadharNo = "XXXXXXXX" + newster;

                    }


                    // Add textview 1
                    TextView textView1 = new TextView(FullPDFActivity.this);
                    textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    int srno = i;
                    String strHoFandHoFn = "";

                    String strVeriforNot = "";

                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsHOF().equalsIgnoreCase("Y") || familyDetails_rcDetails_rationCardMembersData.get(i).getIsHOF().equalsIgnoreCase("y")) {
                        strHoFandHoFn = "(HoF)";
                    }
                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsNFSA().equalsIgnoreCase("Y") || familyDetails_rcDetails_rationCardMembersData.get(i).getIsNFSA().equalsIgnoreCase("y")) {
                        strHoFandHoFn = "(HoFN)";
                    }
                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsHOF().equalsIgnoreCase("Y") && familyDetails_rcDetails_rationCardMembersData.get(i).getIsNFSA().equalsIgnoreCase("Y")) {
                        strHoFandHoFn = "(HoF HoFN)";
                    }

                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsAadharVerified().equalsIgnoreCase("1") || familyDetails_rcDetails_rationCardMembersData.get(i).getIsAadharVerified().equals(1)) {
                        // strVeriforNot = "Verified";
                    } else if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsAadharVerified().equalsIgnoreCase("0") || familyDetails_rcDetails_rationCardMembersData.get(i).getIsAadharVerified().equals(0)) {
                        strVeriforNot = " Not Verified";
                    }
                    switch (srno) {
                        case 0:
                            tv_FamilyMember.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 1:
                            tv_FamilyMember2.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 2:
                            tv_FamilyMember3.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 3:
                            tv_FamilyMember4.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 4:
                            tv_FamilyMember5.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 5:
                            tv_FamilyMember6.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 6:
                            tv_FamilyMember7.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 7:
                            tv_FamilyMember8.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 8:
                            tv_FamilyMember9.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;
                        case 9:
                            tv_FamilyMember10.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears());
                            break;

                    }
                    if (i > 9) {
                        tv_ExtraFamilyMember.setText("+" + (i - 9) + " more members");
                    }
//                    textView1.setText(srno + 1 + ". " + familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN() + strHoFandHoFn + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears() + " " + convertedAadharNo + strVeriforNot);
//                    //  textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
//                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
////                    View view=new View(MRationCard.this);
////                    view.setLayoutParams(new LinearLayout.LayoutParams(La))
//                    llRuntime.addView(textView1);
//
//
//                    View v = new View(FullPDFActivity.this);
//                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
//                    v.setBackgroundColor(Color.parseColor("#000000"));
//
//                    llRuntime.addView(v);
                }

                Bitmap bitmap;
                QRGEncoder qrgEncoder;
                String inputValue = "RC No: " + tvRationCard.getText().toString() + "\n" + "HoF: " + tvHof.getText().toString() + "\n" + "s/o: " + tvHusbandorFather.getText().toString() + "\n" + "DOB: " + tvDOB.getText().toString() + "\n" + "Address: " + tvAddress.getText().toString();
                if (inputValue.length() > 0) {
                    WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = windowManager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrimg.setImageBitmap(bitmap);
                        qrimg2.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        Log.v("GenerateQrCode", e.toString());
                    }
                } else {
                    Toast.makeText(FullPDFActivity.this, "QR Code Not available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FamilyDetailsData> call, Throwable t) {
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
                Intent intent = new Intent(FullPDFActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(FullPDFActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
