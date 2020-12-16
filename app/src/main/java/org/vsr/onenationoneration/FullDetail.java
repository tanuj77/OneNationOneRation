package org.vsr.onenationoneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetailsData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetails_RationCardMembersData;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullDetail extends AppCompatActivity {
    CircleImageView iv_profile;
    TextView tvName, tvPhrcNo, tvFatherName, tvMotherName, tvSpouseName, tvGenderDob, tvRelationshipHOF, tvOccupation, tvSpecialStatus, tvAadharNo, tvMobileNo, tvEmail, tvBankAcco, tvBankName, tvBankBranch, tvIfsc, tvDisclaimer;
    ImageView ivEye;
    String memberID;
    private List<FamilyDetails_RCDetails_RationCardMembersData> familyDetails_rcDetails_rationCardMembersData;
    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String eyeStatus = "hide";
    String strRationCardNo;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulldetail);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        memberID = getIntent().getStringExtra("MemberID");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Member Details");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        iv_profile = findViewById(R.id.fd_profile_image);
        tvName = findViewById(R.id.tvfulldetail_name);
        tvPhrcNo = findViewById(R.id.tvfulldetail_phrcno);
        tvFatherName = findViewById(R.id.tv_fathername);
        tvMotherName = findViewById(R.id.tv_mothername);
        tvSpouseName = findViewById(R.id.tv_spousename);
        tvGenderDob = findViewById(R.id.tv_genderdob);
        tvRelationshipHOF = findViewById(R.id.tv_relationshiphof);
        tvOccupation = findViewById(R.id.tv_occupation);
        tvSpecialStatus = findViewById(R.id.tv_specialstatus);
        tvAadharNo = findViewById(R.id.tv_aadharno);
        ivEye = findViewById(R.id.iv_eye);
        tvMobileNo = findViewById(R.id.tv_mobileno);
        tvEmail = findViewById(R.id.tv_email);
        tvBankAcco = findViewById(R.id.tv_bankacco);
        tvIfsc = findViewById(R.id.tv_ifsc);
        tvBankName = findViewById(R.id.tv_bankname);
        tvBankBranch = findViewById(R.id.tv_bankbranch);
        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

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
        tvbottomAsOn.setText(savedAsOn);
        tvbottomSourceName.setText(savedSourceName);
        tvbottomDisclaimer.setText(savedDisclaimer);
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);

        pd = new ProgressDialog(FullDetail.this);
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
        //jsonObject.addProperty("IMEINo", savedUniqueDeviceID);
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "SHFQM0JFSW9IQ0hSSjA0citiQk81RVJmcTlZeUNsRW5NYUVieEFwMTUzdz06MDAwMDAwMDAwMDAwMDAxOjYzNzMwNzk1MDUyNzE0MDA4MA=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        final Call<FamilyDetailsData> familyDetailsDataCall = apiInterface.familyDetailsData(jsonObject);
        familyDetailsDataCall.enqueue(new Callback<FamilyDetailsData>() {
            @Override
            public void onResponse(Call<FamilyDetailsData> call, Response<FamilyDetailsData> response) {

                Log.i("RS", "" + response);
                familyDetails_rcDetails_rationCardMembersData.addAll(response.body().getFamilyDetails_rcDetailsDataLists().getFamilyDetails_rcDetails_rationCardMembersData());

                // Toast.makeText(FullDetail.this, "First page is loaded", Toast.LENGTH_LONG).show();
                for (int i = 0; i < familyDetails_rcDetails_rationCardMembersData.size(); ) {
                    if (memberID.equalsIgnoreCase(familyDetails_rcDetails_rationCardMembersData.get(i).getMemberId())) {
                        final String strUserImage = familyDetails_rcDetails_rationCardMembersData.get(i).getBase64ImageData();
                        if (strUserImage.equalsIgnoreCase("")) {
                        } else {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            imageBytes = Base64.decode(strUserImage, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            iv_profile.setImageBitmap(decodedImage);
                        }
                        String strName = familyDetails_rcDetails_rationCardMembersData.get(i).getMember_Name_EN();
                        String strPhrcNo = familyDetails_rcDetails_rationCardMembersData.get(i).getMemberId();
                        String strFatherName = familyDetails_rcDetails_rationCardMembersData.get(i).getFather_Name_EN();
                        String strMotherName = familyDetails_rcDetails_rationCardMembersData.get(i).getMother_Name_EN();
                        String strSpouseName = familyDetails_rcDetails_rationCardMembersData.get(i).getSpouse_Name_EN();

                        String strMobileno = familyDetails_rcDetails_rationCardMembersData.get(i).getMobileNo();
                        String strEmail = familyDetails_rcDetails_rationCardMembersData.get(i).getEmailId();
                        String strBankAcco = familyDetails_rcDetails_rationCardMembersData.get(i).getBankACNo();
                        String strIfsc = familyDetails_rcDetails_rationCardMembersData.get(i).getiFSCCode();
                        String strBankName = familyDetails_rcDetails_rationCardMembersData.get(i).getBank();
                        String strBankBranch = familyDetails_rcDetails_rationCardMembersData.get(i).getBranchName();

                        if (strName.trim().equalsIgnoreCase("") || strName.equals("null")) {
                            strName = "NA";
                        }
                        if (strPhrcNo.trim().equalsIgnoreCase("") || strPhrcNo.equals("null")) {
                            strPhrcNo = "NA";
                        }
                        if (strFatherName.trim().equalsIgnoreCase("") || strFatherName.equals("null")) {
                            strFatherName = "NA";
                        }
                        if (strMotherName.trim().equalsIgnoreCase("") || strMotherName.equals("null")) {
                            strMotherName = "NA";
                        }
                        if (strSpouseName.trim().equalsIgnoreCase("") || strSpouseName.equals("null")) {
                            strSpouseName = "NA";
                        }
                        tvName.setText(strName);
                        tvPhrcNo.setText(strPhrcNo);
                        tvFatherName.setText(strFatherName);
                        tvMotherName.setText(strMotherName);
                        tvSpouseName.setText(strSpouseName);

                        String myConvertedDate = familyDetails_rcDetails_rationCardMembersData.get(i).getDateOfBirth();
                        DateFormat iFormatter = new SimpleDateFormat("MMMM dd yyyy");
                        DateFormat oFormatter = new SimpleDateFormat("dd-MM-yyyy");
                        String strConvertedDate = "";
                        try {
                            strConvertedDate = oFormatter.format(iFormatter.parse(myConvertedDate));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String typeOfDOB = "";
                        if (familyDetails_rcDetails_rationCardMembersData.get(i).getActualOrDeclaredDOB().equalsIgnoreCase("A")) {
                            typeOfDOB = "Actual";
                        } else if (familyDetails_rcDetails_rationCardMembersData.get(i).getActualOrDeclaredDOB().equalsIgnoreCase("D")) {
                            typeOfDOB = "Declared";
                        } else if (familyDetails_rcDetails_rationCardMembersData.get(i).getActualOrDeclaredDOB().equalsIgnoreCase("E")) {
                            typeOfDOB = "Exact";
                        } else {
                            typeOfDOB = "";
                        }
                        tvGenderDob.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getGender() + " " + familyDetails_rcDetails_rationCardMembersData.get(i).getAgeInYears() + "  (" + strConvertedDate + ")  " + typeOfDOB);
                        tvRelationshipHOF.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getRelation());
                        String strOccupation = familyDetails_rcDetails_rationCardMembersData.get(i).getOccupation();
                        String strAnnualIncome = familyDetails_rcDetails_rationCardMembersData.get(i).getAnnualIncome();
                        if (strOccupation.trim().equalsIgnoreCase("") || strOccupation.equals("null") || strOccupation.trim().equalsIgnoreCase("NOT AVAILABLE")) {
                            strOccupation = "NA";
                        }
                        if (strAnnualIncome.trim().equalsIgnoreCase("") || strAnnualIncome.equals("null") || strAnnualIncome.trim().equalsIgnoreCase("NOT AVAILABLE")) {
                            strAnnualIncome = "NA";
                        }
                        tvOccupation.setText(strOccupation + " / " + strAnnualIncome);

                        String strPhysicalChallenged = familyDetails_rcDetails_rationCardMembersData.get(i).getIsPhysicallyChallenged();
                        String strPhysicalChallengedName = familyDetails_rcDetails_rationCardMembersData.get(i).getPhysicallyChallenged_Name_EN();
                        String strPhysicalChallengedPer = familyDetails_rcDetails_rationCardMembersData.get(i).getPhysicallyChallengedPercentage();
                        if (strPhysicalChallenged.trim().equalsIgnoreCase("") || strPhysicalChallenged.equals("null")) {
                            strPhysicalChallenged = "NA";
                        }
                        if (strPhysicalChallengedName.trim().equalsIgnoreCase("") || strPhysicalChallengedName.equals("null")) {
                            strPhysicalChallengedName = "NA";
                        }
                        if (strPhysicalChallengedPer.trim().equalsIgnoreCase("") || strPhysicalChallengedPer.equals("null")) {
                            strPhysicalChallengedPer = "NA";
                        }

                        tvSpecialStatus.setText(strPhysicalChallenged + " / " + strPhysicalChallengedName + " / " + strPhysicalChallengedPer);
                        tvAadharNo.setText(familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo());
                        if (familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo().equalsIgnoreCase("0") || familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo().trim().equalsIgnoreCase("")) {
                            tvAadharNo.setText("NA");
                            ivEye.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                            ivEye.setVisibility(View.INVISIBLE);
                            eyeStatus = "hide";
                        } else {
                            ivEye.setVisibility(View.VISIBLE);
                            ivEye.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                            eyeStatus = "hide";
                            String strAadhar = familyDetails_rcDetails_rationCardMembersData.get(i).getuIDAINo();
                            String newster = strAadhar.substring(8, 12);
                            tvAadharNo.setText("XXXXXXXX" + newster);
                        }
                        final int finalI = i;
                        ivEye.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (eyeStatus.equalsIgnoreCase("hide")) {
                                    tvAadharNo.setText(familyDetails_rcDetails_rationCardMembersData.get(finalI).getuIDAINo());
                                    ivEye.setBackgroundResource(R.drawable.ic_eye_icon_24dp);
                                    eyeStatus = "show";
                                } else {
                                    String strAadhar = familyDetails_rcDetails_rationCardMembersData.get(finalI).getuIDAINo();
                                    String newster = strAadhar.substring(8, 12);
                                    tvAadharNo.setText("XXXXXXXX" + newster);
                                    ivEye.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                                    eyeStatus = "hide";
                                }
                            }
                        });

                        if (strMobileno.trim().equalsIgnoreCase("") || strMobileno.equals("null")) {
                            strMobileno = "NA";
                        }
                        if (strEmail.trim().equalsIgnoreCase("") || strEmail.equals("null")) {
                            strEmail = "NA";
                        }
                        if (strBankAcco.trim().equalsIgnoreCase("") || strBankAcco.equals("null")) {
                            strBankAcco = "NA";
                        }
                        if (strIfsc.trim().equalsIgnoreCase("") || strIfsc.equals("null")) {
                            strIfsc = "NA";
                        }
                        if (strBankName.trim().equalsIgnoreCase("") || strBankName.equals("null")) {
                            strBankName = "NA";
                        }
                        if (strBankBranch.trim().equalsIgnoreCase("") || strBankBranch.equals("null")) {
                            strBankBranch = "NA";
                        }
                        tvMobileNo.setText(strMobileno);
                        tvEmail.setText(strEmail);
                        tvBankAcco.setText(strBankAcco);
                        tvIfsc.setText(strIfsc);
                        tvBankName.setText(strBankName);
                        tvBankBranch.setText(strBankBranch);
                    }
                    i++;
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<FamilyDetailsData> call, Throwable t) {
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
                onBackPressed();
                return true;
            case R.id.item_home:
                Intent intent = new Intent(FullDetail.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(FullDetail.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}