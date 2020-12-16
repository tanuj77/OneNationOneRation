package org.vsr.onenationoneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetailsData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetailsDataList;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetails_RationCardMembersData;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    LinearLayout llFpsDetails;
    Button btnFamilyDetails, btnRationEntitlement, btnGasConnection, btnMRationCard, btnTrackMyRation, btnGrievance;
    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ImageView ivUser, ivFullViewAC;
    String strRationCardPermanent;
    TextView tvShopNo, tvFpsCode, tvFpsName, tvName, tvMemberId, tvRationCardNo, tvSchemeName, tvAadharNo, tvShopTimmings;
    ArrayList<FamilyDetails_RCDetailsDataList> familyDetails_rcDetailsDataLists = new ArrayList<FamilyDetails_RCDetailsDataList>();
    private List<FamilyDetails_RCDetails_RationCardMembersData> familyDetails_rcDetails_rationCardMembersData;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    ActionBar actionBar;
    String eyeStatus = "hide";
    String deviceUniqueIdentifier = null;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent i = getIntent();
        savedTXNIDFromRegActivity = i.getStringExtra("TXNID");
        Log.i("SRStxnID HOME", "" + savedTXNIDFromRegActivity);
        actionBar = getSupportActionBar();
        actionBar.setTitle("RC No.:");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        ivUser = findViewById(R.id.iv_home_profileimage);
        llFpsDetails = findViewById(R.id.ll_fpsdetails);
        tvShopNo = findViewById(R.id.tv_home_shopno);
        tvFpsCode = findViewById(R.id.tv_home_fpscode);
        tvFpsName = findViewById(R.id.tv_home_fpsname);
        tvShopTimmings = findViewById(R.id.tv_shoptimmings);
        tvName = findViewById(R.id.tvhome_name);
        tvMemberId = findViewById(R.id.tv_home_memberid);
        tvRationCardNo = findViewById(R.id.tv_home_rationcardno);
        tvSchemeName = findViewById(R.id.tv_home_schemename);
        tvAadharNo = findViewById(R.id.tv_home_aadharno);
        ivFullViewAC = findViewById(R.id.iv_fullview);
        btnFamilyDetails = findViewById(R.id.btn_fd);
        btnRationEntitlement = findViewById(R.id.btn_re);
        btnGasConnection = findViewById(R.id.btn_gc);
        btnMRationCard = findViewById(R.id.btn_mrationcard);
        btnTrackMyRation = findViewById(R.id.btn_tmr);
        btnGrievance = findViewById(R.id.btn_grievance);
        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        //  getDeviceIMEI();
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
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);

        familyDetails();

        btnFamilyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FamilyDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);

            }


        });

        btnRationEntitlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, REMonthYear.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnGasConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GasConnectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnMRationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MRationCard.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        llFpsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FPSDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnTrackMyRation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TMRMonthYear.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnGrievance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GrievancesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardPermanent);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void familyDetails() {
        pd = new ProgressDialog(HomeActivity.this);
        pd.setMessage("Loading..");
        pd.show();
        familyDetails_rcDetails_rationCardMembersData = new ArrayList<>();
        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("State", savedStateID);
        jsonObject.addProperty("LanguageId", savedLanguageID);
        jsonObject.addProperty("TxnId", savedTXNIDFromRegActivity);
        jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "SHFQM0JFSW9IQ0hSSjA0citiQk81RVJmcTlZeUNsRW5NYUVieEFwMTUzdz06MDAwMDAwMDAwMDAwMDAxOjYzNzMwNzk1MDUyNzE0MDA4MA=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        final Call<FamilyDetailsData> familyDetailsDataCall = apiInterface.familyDetailsData(jsonObject);
        familyDetailsDataCall.enqueue(new Callback<FamilyDetailsData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<FamilyDetailsData> call, Response<FamilyDetailsData> response) {

                tvbottomSourceName.setText(response.body().getModuleName() + " (" + response.body().getModuleLink() + ")");
                Paper.book().write("bottomSourceName", response.body().getModuleName() + " (" + response.body().getModuleLink() + ")");

                familyDetails_rcDetailsDataLists.add(response.body().getFamilyDetails_rcDetailsDataLists());

                tvShopNo.setText(familyDetails_rcDetailsDataLists.get(0).getfPSNo());
                tvFpsCode.setText(familyDetails_rcDetailsDataLists.get(0).getFpsCode());
                tvFpsName.setText(familyDetails_rcDetailsDataLists.get(0).getFpsName());
                tvShopTimmings.setText(familyDetails_rcDetailsDataLists.get(0).getfPSTimings());
                tvSchemeName.setText(familyDetails_rcDetailsDataLists.get(0).getSchemeName() + " (" + familyDetails_rcDetailsDataLists.get(0).getSchemeShortName() + ")");
                familyDetails_rcDetails_rationCardMembersData.addAll(response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardMembersData());
                for (int i = 0; i < familyDetails_rcDetails_rationCardMembersData.size(); i++) {
                    if (familyDetails_rcDetails_rationCardMembersData.get(i).getIsNFSA().equalsIgnoreCase("Y")) {
                        final String strUserImage = familyDetails_rcDetails_rationCardMembersData.get(i).getBase64ImageData();
                        if (strUserImage.equalsIgnoreCase("")) {
                        } else {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] imageBytes = baos.toByteArray();
                           // String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            imageBytes = Base64.decode(strUserImage, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            ivUser.setImageBitmap(decodedImage);
                        }
                        tvName.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN());
                        tvMemberId.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getMemberId());
                        tvRationCardNo.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo());
                        actionBar.setTitle("PDS Passbook");
                        actionBar.setSubtitle("RC No.: " + familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo());
                        strRationCardPermanent = familyDetails_rcDetails_rationCardMembersData.get(i).getRationCardNo();

                        if (familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo().equalsIgnoreCase("0") || familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo().equalsIgnoreCase("")) {
                            tvAadharNo.setText("Not Available");
                            ivFullViewAC.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                            eyeStatus = "hide";
                        } else {
                            ivFullViewAC.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                            eyeStatus = "hide";
                            String strAadhar = familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo();
                            String newster = strAadhar.substring(8, 12);
                            tvAadharNo.setText("XXXXXXXX" + newster);

                        }
                        final int finalI = i;
                        ivFullViewAC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (eyeStatus.equalsIgnoreCase("hide")) {
                                    tvAadharNo.setText(familyDetails_rcDetails_rationCardMembersData.get(finalI).getuIDAINo());
                                    ivFullViewAC.setBackgroundResource(R.drawable.ic_eye_icon_24dp);
                                    eyeStatus = "show";
                                } else {
                                    String strAadhar = familyDetails_rcDetails_rationCardMembersData.get(finalI).getuIDAINo();
                                    String newster = strAadhar.substring(8, 12);
                                    tvAadharNo.setText("XXXXXXXX" + newster);
                                    ivFullViewAC.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                                    eyeStatus = "hide";
                                }
                            }
                        });

                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<FamilyDetailsData> call, Throwable t) {
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
            case R.id.item_home:
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getFirstLetterFromEachWordInSentence(final String string) {
        if (string == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        Arrays.asList(string.split(" ")).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                sb.append(s.charAt(0)).append(" ");
            }
        });
        return sb.toString().trim();
    }

    public void onBackPressed() {
        finishAndRemoveTask();
        finishAffinity();
        finish();
        //FinishAffinity removes the connection of the existing activity to its stack. And then finish helps you exit that activity. Which will eventually exit the application.
    }

/////Time out of app if it is in background/pause state
    @Override
    protected void onPause() {
        super.onPause();
        timer = new Timer();
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask, 180000); //auto logout in 3 minutes...time is in miliseconds here
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private class LogOutTimerTask extends TimerTask {
        @Override
        public void run() {
            //redirect user to login screen
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }
}
