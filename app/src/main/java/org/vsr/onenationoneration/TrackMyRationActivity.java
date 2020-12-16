package org.vsr.onenationoneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.vsr.onenationoneration.adapters.TMRRecViewCustomAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.TMRData;
import org.vsr.onenationoneration.apiCall.bin.TMRReceivedDetails;
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

public class TrackMyRationActivity extends AppCompatActivity {
    private RecyclerView recyclerView, recyclerView2;
    private TMRRecViewCustomAdapter mAdapter;
    ConnectionDetector cd;
    CoordinatorLayout coordinatorLayout;
    private static RelativeLayout bottomLayout;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;
    private List<TMRData> tmrData;
    private List<TMRReceivedDetails> tmrReceivedDetailsList;
    private ApiInterface apiInterface;
    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    TextView tvSchemeName, tvAllocMonthYear, tvFPSName, tvFPSCode, tvFPSAddress, tvDisclaimer;
    String strAllocMonth, strAllocYear, strRationCardNo;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackmyration);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Track My Ration");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        tvSchemeName = findViewById(R.id.tv_tmr_schemename);
        tvAllocMonthYear = findViewById(R.id.tv_tmr_allocmy);
        tvFPSName = findViewById(R.id.tv_tmr_fpsname);
        tvFPSCode = findViewById(R.id.tv_tmr_fpscode);
        tvFPSAddress = findViewById(R.id.tv_tmr_fpsaddress);
        recyclerView = findViewById(R.id.recycler_view_tmr);
        // recyclerView2=findViewById(R.id.recycler_view_re2);

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
        savedSourceName = Paper.book().read("bottomSourceName");
        savedDisclaimer = Paper.book().read("bottomDisclaimer");

        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomDisclaimer.setText(savedDisclaimer);
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);

        strAllocMonth = getIntent().getStringExtra("selectedMonth");
        strAllocYear = getIntent().getStringExtra("selectedYear");

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        tmrData = new ArrayList<>();
        tmrReceivedDetailsList = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        populatRecyclerView();
    }

    private void populatRecyclerView() {
        pd = new ProgressDialog(TrackMyRationActivity.this);
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
        jsonObject.addProperty("AuthString", "RWxEdTNibkU2Z3VsTXNPVlJaTWs1K2JMVVIrY1JxNTBrbXZaTHpWbERWZz06MDAwMDAwMDAwMDAwMDAxOjYzNzMzNTYwMzUxNjY4MzIwMQ=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        final Call<TMRData> tmrDataCall = apiInterface.tmrData(jsonObject);
        tmrDataCall.enqueue(new Callback<TMRData>() {
            @Override
            public void onResponse(Call<TMRData> call, Response<TMRData> response) {
                String dateInString = response.body().getDateOfData();

                String[] updatedDate = dateInString.split("-");
                String month = updatedDate[0];
                String date = updatedDate[1];
                String year = updatedDate[2];
                String formatedDate = date + "/" + month + "/" + year;

                tvbottomAsOn.setText(formatedDate);
                tvbottomSourceName.setText(response.body().getModuleName() + "(" + response.body().getModuleLink() + ")");
                tmrReceivedDetailsList.addAll(response.body().getTmrReceivedDetailsList());

                tvSchemeName.setText(response.body().getSchemeName());
                tvAllocMonthYear.setText(response.body().getAllocMonth() + "/" + response.body().getAllocYear());
                tvFPSName.setText(response.body().getFpsName());
                tvFPSCode.setText(response.body().getFpsNo());
                String strAddress = response.body().getFpsAddress();
                String strAddressModified = strAddress.replace("&amp;", "");
                tvFPSAddress.setText(strAddressModified);
                mAdapter = new TMRRecViewCustomAdapter(tmrReceivedDetailsList);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<TMRData> call, Throwable t) {
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
                Intent intent = new Intent(TrackMyRationActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(TrackMyRationActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}