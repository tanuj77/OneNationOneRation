package org.vsr.onenationoneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.vsr.onenationoneration.adapters.REERecViewCustomAdapter;
import org.vsr.onenationoneration.adapters.RERecViewCustomAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.RCEEntitlementsDataList;
import org.vsr.onenationoneration.apiCall.bin.RCEMembersDataList;
import org.vsr.onenationoneration.apiCall.bin.RCEntitlementData;
import org.vsr.onenationoneration.others.ConnectionDetector;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RationEntitlementActivity extends AppCompatActivity {
    private RecyclerView recyclerView, recyclerView2;
    private RERecViewCustomAdapter mAdapter;
    private REERecViewCustomAdapter entitlementAdapter;
    ConnectionDetector cd;
    CoordinatorLayout coordinatorLayout;
    private static RelativeLayout bottomLayout;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;
    private List<RCEMembersDataList> rceMembersDataLists;
    private List<RCEEntitlementsDataList> rceEntitlementsDataLists;
    private ApiInterface apiInterface;
    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    TextView tvSchemeName, tvAllocMonthYear, tvFPSName, tvFPSCode, tvFPSAddress, tvDisclaimer;
    Button buttonNext;
    String strAllocMonth, strAllocYear, strRationCardNo;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rationentitlement);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Ration Entitlement");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        tvSchemeName = findViewById(R.id.tv_re_schemename);
        tvAllocMonthYear = findViewById(R.id.tv_re_allocmy);
        tvFPSName = findViewById(R.id.tv_re_fpsname);
        tvFPSCode = findViewById(R.id.tv_re_fpscode);
        tvFPSAddress = findViewById(R.id.tv_re_fpsaddress);
        recyclerView = findViewById(R.id.recycler_view_re);
        buttonNext = findViewById(R.id.btn_re_next);
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
        savedUniqueDeviceID = Paper.book().read("deviceUniqueID");
        savedAadharCardNumber = Paper.book().read("AadharCardNumber");
        savedDepartmentName = Paper.book().read("bottomDepartmentName");
        savedAsOn = Paper.book().read("bottomAsOn");
        savedDisclaimer = Paper.book().read("bottomDisclaimer");

        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomDisclaimer.setText(savedDisclaimer);
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);

        strAllocMonth = getIntent().getStringExtra("selectedMonth");
        strAllocYear = getIntent().getStringExtra("selectedYear");

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        rceMembersDataLists = new ArrayList<>();
        rceEntitlementsDataLists = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        populatRecyclerView();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RationEntitlementActivity.this, RationEntitlementActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardNo);
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                bundle.putString("selectedMonth", strAllocMonth);
                bundle.putString("selectedYear", strAllocYear);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void populatRecyclerView() {
        pd = new ProgressDialog(RationEntitlementActivity.this);
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
        jsonObject.addProperty("AllocMonth", strAllocMonth.trim());
        jsonObject.addProperty("AllocYear", strAllocYear.trim());
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00981");
        jsonObject.addProperty("AuthString", "eG5tc1ZheGtBZHpwRW5aREJ4Z0tlWjZldE9VdzRyVUVmV0pCbHN2ZFNZMD06MDAwMDAwMDAwMDAwMDAxOjYzNzMxMjkzMzM3NjAzMjEyOQ=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        final Call<RCEntitlementData> rcEntitlementDataCall = apiInterface.rcEntitlementData(jsonObject);
        rcEntitlementDataCall.enqueue(new Callback<RCEntitlementData>() {
            @Override
            public void onResponse(Call<RCEntitlementData> call, Response<RCEntitlementData> response) {
                String dateInString = response.body().getDateOfData();

                String[] updatedDate = dateInString.split("-");
                String month = updatedDate[0];
                String date = updatedDate[1];
                String year = updatedDate[2];
                String formatedDate = date + "/" + month + "/" + year;

                tvbottomAsOn.setText(formatedDate);
                tvbottomSourceName.setText(response.body().getModuleName() + "(" + response.body().getModuleLink() + ")");
                Log.i("RSmembers", "" + response.body().getRceMembersDataLists());
                Log.i("RSentitlement", "" + response.body().getRceEntitlementsDataLists());
                rceMembersDataLists.addAll(response.body().getRceMembersDataLists());

                tvSchemeName.setText(response.body().getSchemeName());
                tvAllocMonthYear.setText(response.body().getAllocMonth() + "/" + response.body().getAllocYear());
                tvFPSName.setText(response.body().getFpsName());
                tvFPSCode.setText(response.body().getFpsNo());
                String strAddress = response.body().getFpsAddress();
                String strAddressModified = strAddress.replace("&amp;", "");
                tvFPSAddress.setText(strAddressModified);
                mAdapter = new RERecViewCustomAdapter(rceMembersDataLists, savedTXNIDFromRegActivity);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<RCEntitlementData> call, Throwable t) {
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
                Intent intent = new Intent(RationEntitlementActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(RationEntitlementActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}