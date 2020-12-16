package org.vsr.onenationoneration;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.gson.JsonObject;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetailsData;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetailsDataList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GasConnectionActivity extends AppCompatActivity {
    ArrayList<FamilyDetails_RCDetailsDataList> familyDetails_rcDetailsDataLists = new ArrayList<FamilyDetails_RCDetailsDataList>();
    TextView tvGasConnectionStatus, tvGasConnectionNo, tvGasOilCompanyCode, tvGasCompany, tvGasConnectionConsumerNo, tvGasAgencyCode, tvGasCompanyName, tvDisclaimer;
    String savedStateID, savedStateName, savedLanguageID, savedTXNIDFromRegActivity, savedUniqueDeviceID, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo;
    ProgressDialog pd;
    private Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasconnection);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Gas Connection");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tvGasConnectionStatus = findViewById(R.id.tv_gas_connection_status);
        tvGasConnectionNo = findViewById(R.id.tv_gas_connection_number);
        tvGasOilCompanyCode = findViewById(R.id.tv_gas_oilCompanyCode);
        tvGasCompany = findViewById(R.id.tv_gascompany);
        tvGasConnectionConsumerNo = findViewById(R.id.tv_gasconnectionconsumerNo);
        tvGasAgencyCode = findViewById(R.id.tv_gas_agencycode);
        tvGasCompanyName = findViewById(R.id.tv_gas_agencyname);
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

        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);
        gasConnectionDetails();
    }

    public void gasConnectionDetails() {
        pd = new ProgressDialog(GasConnectionActivity.this);
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
                familyDetails_rcDetailsDataLists.add(response.body().getFamilyDetails_rcDetailsDataLists());
                if (familyDetails_rcDetailsDataLists.get(0).getGas_Connection_Status().equalsIgnoreCase("1")) {
                    tvGasConnectionStatus.setText("Single");
                } else if (familyDetails_rcDetailsDataLists.get(0).getGas_Connection_Status().equalsIgnoreCase("2")) {
                    tvGasConnectionStatus.setText("Double");
                } else {
                    tvGasConnectionStatus.setText(familyDetails_rcDetailsDataLists.get(0).getGas_Connection_Status());
                }

                tvGasConnectionNo.setText(familyDetails_rcDetailsDataLists.get(0).getGas_Connection_Number());
                tvGasOilCompanyCode.setText(familyDetails_rcDetailsDataLists.get(0).getGas_OilCompanyCode());
                tvGasCompany.setText(familyDetails_rcDetailsDataLists.get(0).getGasCompany());
                tvGasConnectionConsumerNo.setText(familyDetails_rcDetailsDataLists.get(0).getGasConnectionConsumerNo());
                tvGasAgencyCode.setText(familyDetails_rcDetailsDataLists.get(0).getGas_AgencyCode());
                tvGasCompanyName.setText(familyDetails_rcDetailsDataLists.get(0).getAgencyName());
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<FamilyDetailsData> call, Throwable t) {
                pd.dismiss();
            }
        });
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
                Intent intent = new Intent(GasConnectionActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(GasConnectionActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}