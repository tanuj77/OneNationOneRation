package org.vsr.onenationoneration;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.JsonObject;
import org.vsr.onenationoneration.adapters.TMRRecViewCustomAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceListData;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceListDataList;
import org.vsr.onenationoneration.others.ConnectionDetector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class GrievancesListActivity extends AppCompatActivity {
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo, savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ProgressDialog pd;
    ActionBar actionBar;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    ConnectionDetector cd;
    private static RelativeLayout bottomLayout;
    String strFromDate, strToDate;
    private List<RCGrievanceListDataList> rcGrievanceListDataListList;
    RecyclerView.LayoutManager mLayoutManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievanceslist);

//        strRationCardNo = getIntent().getStringExtra("RationCard");
//        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");
//        strFromDate = getIntent().getStringExtra("FromDate");
//        strToDate = getIntent().getStringExtra("ToDate");

        Paper.init(this);
        strRationCardNo = Paper.book().read("GrievancesDateActivity_RationCard");
        savedTXNIDFromRegActivity = Paper.book().read("GrievancesDateActivity_TXNID");
        strFromDate = Paper.book().read("GrievancesDateActivity_FromDate");
        strToDate = Paper.book().read("GrievancesDateActivity_ToDate");

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
        actionBar.setTitle("Grievances List");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        recyclerView = findViewById(R.id.recycler_view_grievancelist);

        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        cd = new ConnectionDetector(getApplicationContext());
        bottomLayout = findViewById(R.id.loadItemsLayout_recyclerView);

        tvbottomDepartmentName.setText(savedDepartmentName);
        tvbottomStateName.setText(savedStateName);
        tvbottomAsOn.setText(savedAsOn);
        tvbottomSourceName.setText(savedSourceName);
        tvbottomDisclaimer.setText(savedDisclaimer);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcGrievanceListDataListList = new ArrayList<>();
        populatRecyclerView();
    }

    private void populatRecyclerView() {
        pd = new ProgressDialog(GrievancesListActivity.this);
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
        jsonObject.addProperty("DateFrom", strFromDate.trim());
        jsonObject.addProperty("DateTo", strToDate.trim());
        jsonObject.addProperty("AuthString", "U1FhdlR4SG5oNlRMbHl4dzRvTC9oNVk0bEhQTmluY2QwN0s4NGNoM3V1QT06MDAwMDAwMDAwMDAwMDAxOjYzNzM4MTEwODg3NDM2MTgxMQ=="); // yha per AuthString pass karna
        // yha per DateOfData pass karna
        final Call<RCGrievanceListData> rcGrievanceListDataCall = apiInterface.rcGrievanceListData(jsonObject);
        rcGrievanceListDataCall.enqueue(new Callback<RCGrievanceListData>() {
            @Override
            public void onResponse(Call<RCGrievanceListData> call, Response<RCGrievanceListData> response) {

                Log.i("SRSwatch", "" + response.body());
                List<RCGrievanceListDataList> abc = response.body().getRcGrievanceListDataLists();
                rcGrievanceListDataListList.addAll(abc);
                // mAdapter = new RecViewCustomAdapter(RecyclerViewActivity.this, android.R.layout.simple_spinner_item, facilityTypeDetailsLists);
                //  ArrayList<FamilyDetails_RCDetails_RationCardMembersData> sortedList = new ArrayList<>();
                Collections.sort(rcGrievanceListDataListList, new Comparator<RCGrievanceListDataList>() {
                    @Override
                    public int compare(RCGrievanceListDataList lhs, RCGrievanceListDataList rhs) {
                        return lhs.getIssueCode().compareTo(rhs.getIssueCode());
                    }
                });
                TMRRecViewCustomAdapter mAdapter = new TMRRecViewCustomAdapter(GrievancesListActivity.this, rcGrievanceListDataListList, strRationCardNo, savedTXNIDFromRegActivity);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                //  Toast.makeText(FamilyDetailsActivity.this, "First page is loaded", Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<RCGrievanceListData> call, Throwable t) {
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
                Intent intent = new Intent(GrievancesListActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(GrievancesListActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}