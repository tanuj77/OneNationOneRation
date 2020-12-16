package org.vsr.onenationoneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.vsr.onenationoneration.adapters.RecViewCustomAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetailsData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetails_RationCardMembersData;
import org.vsr.onenationoneration.others.ConnectionDetector;
import org.vsr.onenationoneration.others.RecyclerTouchListener;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Banke Bihari on 22/07/2020.
 */
public class FamilyDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecViewCustomAdapter mAdapter;

    ConnectionDetector cd;
    CoordinatorLayout coordinatorLayout;
    private static RelativeLayout bottomLayout;
    RecyclerView.LayoutManager mLayoutManager;
    private List<FamilyDetails_RCDetails_RationCardMembersData> familyDetails_rcDetails_rationCardMembersData;
    private ApiInterface apiInterface;
    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer, strRationCardNo;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    ActionBar actionBar;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familydetails);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");
        actionBar = getSupportActionBar();
        actionBar.setTitle("Family Details");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        recyclerView = findViewById(R.id.recycler_view);
        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        cd = new ConnectionDetector(getApplicationContext());
        bottomLayout = findViewById(R.id.loadItemsLayout_recyclerView);

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

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        familyDetails_rcDetails_rationCardMembersData = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        populatRecyclerView();

    }

    private void populatRecyclerView() {
        pd = new ProgressDialog(FamilyDetailsActivity.this);
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
                // mAdapter = new RecViewCustomAdapter(RecyclerViewActivity.this, android.R.layout.simple_spinner_item, facilityTypeDetailsLists);
                //  ArrayList<FamilyDetails_RCDetails_RationCardMembersData> sortedList = new ArrayList<>();
                Collections.sort(familyDetails_rcDetails_rationCardMembersData, new Comparator<FamilyDetails_RCDetails_RationCardMembersData>() {
                    @Override
                    public int compare(FamilyDetails_RCDetails_RationCardMembersData lhs, FamilyDetails_RCDetails_RationCardMembersData rhs) {
                        return lhs.getMemberId().compareTo(rhs.getMemberId());
                    }
                });
                mAdapter = new RecViewCustomAdapter(FamilyDetailsActivity.this, familyDetails_rcDetails_rationCardMembersData, strRationCardNo, savedTXNIDFromRegActivity);

                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                String dataDateTimeAckCode = response.body().getAckCode();
                String myConvertedDateTime = dataDateTimeAckCode.substring(9, 22);
                //Jan  1 1943///////////2807202014235100981
                //  String myConvertedDate=familyDetails_rcDetails_rationCardMembersData.get(i).getDateOfBirth();
                DateFormat iFormatter = new SimpleDateFormat("ddMMyyyyhhmmss");
                DateFormat oFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String strDateTime = "";
                try {
                    strDateTime = oFormatter.format(iFormatter.parse(myConvertedDateTime));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<FamilyDetailsData> call, Throwable t) {
                pd.dismiss();
                Log.e("homeerror", t.toString());
            }
        });

//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onLongClick(View child, int childPosition) {
//
//            }
//
//            @Override
//            public void onClick(View child, int childPosition) {
//                FamilyDetails_RCDetails_RationCardMembersData ftdl = (FamilyDetails_RCDetails_RationCardMembersData) familyDetails_rcDetails_rationCardMembersData.get(childPosition);
//                String memberID = ftdl.getMemberId();
//                Intent intent = new Intent(FamilyDetailsActivity.this, FullDetail.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("RationCard", strRationCardNo);
//                bundle.putString("MemberID", memberID);
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//            }
//        }));

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
                Intent intent = new Intent(FamilyDetailsActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(FamilyDetailsActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}