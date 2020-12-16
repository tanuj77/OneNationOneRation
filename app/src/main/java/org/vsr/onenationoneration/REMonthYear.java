package org.vsr.onenationoneration;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.RCEntitlementData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class REMonthYear extends AppCompatActivity {
    String strMonth, strYear;
    static final String[] Months = new String[]{"January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    Button buttonCAlendar;
    String savedStateID, savedStateName, savedLanguageID, savedUniqueDeviceID, savedTXNIDFromRegActivity, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo;
    ProgressDialog pd;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthyear);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Ration Entitlement");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        buttonCAlendar = findViewById(R.id.button);
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
        tvbottomDisclaimer.setText(savedDisclaimer);
      //set year
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);

        final Spinner spinYear = findViewById(R.id.spinnerYears);
        spinYear.setAdapter(adapter);
//below two lines for setting default year in spinner
        int selectionPosition = adapter.getPosition(String.valueOf(thisYear));
        spinYear.setSelection(selectionPosition);
        // Set months
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Months);
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinMonths = findViewById(R.id.spinnerMonths);
        spinMonths.setAdapter(adapterMonths);
        String monthgot = getMonth();
        Toast.makeText(REMonthYear.this, monthgot, Toast.LENGTH_LONG).show();
        int selectPosition = adapterMonths.getPosition(monthgot);
        spinMonths.setSelection(selectPosition);
        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = (String) spinMonths.getItemAtPosition(position);
                switch (selectedMonth) {
                    case "January":
                        strMonth = "1";
                        break;
                    case "February":
                        strMonth = "2";
                        break;
                    case "March":
                        strMonth = "3";
                        break;
                    case "April":
                        strMonth = "4";
                        break;
                    case "May":
                        strMonth = "5";
                        break;
                    case "June":
                        strMonth = "6";
                        break;
                    case "July":
                        strMonth = "7";
                        break;
                    case "August":
                        strMonth = "8";
                        break;
                    case "September":
                        strMonth = "9";
                        break;
                    case "October":
                        strMonth = "10";
                        break;
                    case "November":
                        strMonth = "11";
                        break;
                    case "December":
                        strMonth = "12";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strYear = (String) spinYear.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonCAlendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                populatRecyclerView();
            }
        });
    }

    private void populatRecyclerView() {
        pd = new ProgressDialog(REMonthYear.this);
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
        jsonObject.addProperty("AllocMonth", strMonth);
        jsonObject.addProperty("AllocYear", strYear);
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
                String status = String.valueOf(response.body().getRceEntitlementsDataLists());
                if (status.equals(null) || status.equalsIgnoreCase("null")) {
                    Toast.makeText(REMonthYear.this, "Entitlement Details Not Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(REMonthYear.this, RationEntitlementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("RationCard", strRationCardNo);
                    bundle.putString("TXNID", savedTXNIDFromRegActivity);
                    bundle.putString("selectedMonth", strMonth);
                    bundle.putString("selectedYear", strYear);
                    Toast.makeText(REMonthYear.this, strMonth + "/" + strYear, Toast.LENGTH_LONG).show();
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
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

    private String getMonth() {
        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        return month;
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
                Intent intent = new Intent(REMonthYear.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(REMonthYear.this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}