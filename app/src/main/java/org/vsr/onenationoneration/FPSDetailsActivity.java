package org.vsr.onenationoneration;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FPSDetailsActivity extends AppCompatActivity {
    ArrayList<FamilyDetails_RCDetailsDataList> familyDetails_rcDetailsDataLists = new ArrayList<FamilyDetails_RCDetailsDataList>();
    TextView tvFPSName, tvFPSCode, tvFPSNo, tvFPSTimmings, tvKeroseneCode, tvKeroseneName,tvDisclaimer;
    String savedStateID, savedLanguageID, savedUniqueDeviceID,savedDisclaimer;
    String strRationCardNo,savedTXNIDFromRegActivity;
    private Timer timer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpsdetails);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        tvFPSName = findViewById(R.id.tv_fpsname);
        tvFPSNo = findViewById(R.id.tv_fpscode);
        tvFPSCode = findViewById(R.id.tv_fpscode);
        tvFPSTimmings = findViewById(R.id.tv_fpstimmings);
        tvKeroseneCode = findViewById(R.id.tv_kerosenefpscode);
        tvKeroseneName = findViewById(R.id.tv_kerosenefpsname);
        tvDisclaimer=findViewById(R.id.bottomnav);

        Paper.init(this);
        savedStateID = Paper.book().read("stateID");
        savedLanguageID = Paper.book().read("languageID");
        savedUniqueDeviceID = Paper.book().read("deviceUniqueID");
        savedDisclaimer=Paper.book().read("Disclaimer");
        Log.i("SRSinfo", savedStateID + "  " + savedLanguageID + "  " + savedUniqueDeviceID);

        fpsDetails();

        tvDisclaimer.setText(savedDisclaimer);
    }

    public void fpsDetails() {
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
                tvFPSName.setText(familyDetails_rcDetailsDataLists.get(0).getFpsName());
                tvFPSNo.setText(familyDetails_rcDetailsDataLists.get(0).getfPSNo());
                tvFPSCode.setText(familyDetails_rcDetailsDataLists.get(0).getFpsCode());
                tvFPSTimmings.setText(familyDetails_rcDetailsDataLists.get(0).getfPSTimings());
                tvKeroseneCode.setText(familyDetails_rcDetailsDataLists.get(0).getKeroseneFPSCode());
                tvKeroseneName.setText(familyDetails_rcDetailsDataLists.get(0).getKeroseneFPSName());
            }

            @Override
            public void onFailure(Call<FamilyDetailsData> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_home) {
            Intent intent = new Intent(FPSDetailsActivity.this, HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TXNID", savedTXNIDFromRegActivity);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (item.getItemId() == R.id.item_language) {
            Intent intent = new Intent(FPSDetailsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
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
