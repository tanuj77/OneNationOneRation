package org.vsr.onenationoneration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;


import org.vsr.onenationoneration.Helper.LocalHelper;
import org.vsr.onenationoneration.adapters.LanguagesAdapter;
import org.vsr.onenationoneration.adapters.StateAdapter;
import org.vsr.onenationoneration.apiCall.ApiClient;
import org.vsr.onenationoneration.apiCall.ApiInterface;
import org.vsr.onenationoneration.apiCall.bin.LanguageData;
import org.vsr.onenationoneration.apiCall.bin.LanguagesDataList;
import org.vsr.onenationoneration.apiCall.bin.StateData;
import org.vsr.onenationoneration.apiCall.bin.StateDataList;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerState, spinnerLanguages;
    Button buttonSubmit;
    List<StateDataList> StateDataLists = new ArrayList<>();
    List<LanguagesDataList> languagesDataLists;
    StateAdapter stateAdapter;
    LanguagesAdapter languagesAdapter;
    String stateID, stateName;
    String deviceUniqueIdentifier = null;
    TelephonyManager telephonyManager;
    String statusState;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerState = findViewById(R.id.spinner_state);
        spinnerLanguages = findViewById(R.id.spinner_language);
        buttonSubmit = findViewById(R.id.btn_submit);
        spinnerState.setOnItemSelectedListener(this);
        spinnerLanguages.setOnItemSelectedListener(this);
        Paper.init(this);

        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language", "en");
        String deviceUniqueIdentifier = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        Paper.book().write("deviceUniqueID", deviceUniqueIdentifier);
        updateView((String) Paper.book().read("deviceUniqueID"));

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceId();
                if (stateID.trim().equalsIgnoreCase("000") || stateID == "000") {
                    Toast.makeText(MainActivity.this, "Please Select State", Toast.LENGTH_SHORT).show();
                } else if (statusState.equalsIgnoreCase("ACCP") || statusState == "ACCP") {
                    Intent intent = new Intent(MainActivity.this, Registration.class);
                    startActivity(intent);
                }
            }
        });

        String tokenDateTime = getTokenDateTime();
        String date = getDateTime();
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Token", "00000" + tokenDateTime + "00001"); // yha per token pass karna
        jsonObject.addProperty("AuthString", "NGNuYjJ5TGNDb1E3bWZMeE1EZ3ZtOUlIc3ZNZ3pHeTZrK3dwbU05aGJjdz06MDAwMDAwMDAwMDAwMDAxOjYzNzI0NTU3NjEzOTg5MjA5NQ=="); // yha per AuthString pass karna
        jsonObject.addProperty("DateOfData", date); // yha per DateOfData pass karna
        Call<StateData> StateDataCall = apiInterface.stateData(jsonObject);
        StateDataCall.enqueue(new Callback<StateData>() {
            @Override
            public void onResponse(Call<StateData> call, Response<StateData> response) {
                statusState = response.body().getStatus();
                StateDataList stateDataList = new StateDataList();
                stateDataList.setStateName("Select State ");
                stateDataList.setState_code("000");
                StateDataLists.addAll(response.body().getStateDataLists());
                Collections.reverse(StateDataLists);  // And  .....wes ....se
                StateDataLists.add(stateDataList);
                Collections.reverse(StateDataLists);

                stateAdapter = new StateAdapter(MainActivity.this, R.layout.textview_with_padding, StateDataLists);
                stateAdapter.setDropDownViewResource(R.layout.textview_with_padding);
                spinnerState.setAdapter(stateAdapter);
            }

            @Override
            public void onFailure(Call<StateData> call, Throwable t) {
                Log.e("stateerror", t.toString());
            }
        });


        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {
                    stateID = StateDataLists.get(position).getState_code();
                    stateName = StateDataLists.get(position).getStateName();

                    Paper.book().write("stateID", stateID);
                    Paper.book().write("stateName", stateName);
                    updateView((String) Paper.book().read("stateID"));
                    Log.i("SRSstateid", stateID);

                    String tokenDateTime = getTokenDateTime();
                    String date = getDateTime();

                    languagesDataLists = new ArrayList<>();
                    //final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    JsonObject jsonObjectLanguages = new JsonObject();
                    jsonObjectLanguages.addProperty("State", stateID);
                    jsonObjectLanguages.addProperty("Token", "00000" + tokenDateTime + "00001"); // yha per token pass karna
                    jsonObjectLanguages.addProperty("AuthString", "ZlhvK3QwcHVxdW11N3pEZElJb3R4WThJcE5TQmpMVFB4Um04SmpRWlF5UT06MDAwMDAwMDAwMDAwMDAxOjYzNzI5OTg3NDY4NDQ0MTkzMA=="); // yha per AuthString pass karna
                    jsonObjectLanguages.addProperty("DateOfData", date); // yha per DateOfData pass karna
                    //Call<StateData> StateDataCall = apiInterface.stateData(jsonObject);
                    Call<LanguageData> languageDataCall = apiInterface.languageData(jsonObjectLanguages);
                    languageDataCall.enqueue(new Callback<LanguageData>() {
                        @Override
                        public void onResponse(Call<LanguageData> call, Response<LanguageData> response) {
                            languagesDataLists.addAll(response.body().getLanguagesDataLists());
                            languagesAdapter = new LanguagesAdapter(MainActivity.this, R.layout.textview_with_padding, languagesDataLists);
                            languagesAdapter.setDropDownViewResource(R.layout.textview_with_padding);
                            spinnerLanguages.setAdapter(languagesAdapter);
                        }

                        @Override
                        public void onFailure(Call<LanguageData> call, Throwable t) {
                            Log.e("languageerror", t.toString());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {
                    String selectedLanguage = languagesDataLists.get(position).getLanguageName();
                    String selectedLanguageID = languagesDataLists.get(position).getLanguageId().toString();
                    switch (selectedLanguage) {
                        case "English":
                            Paper.book().write("language", "en");
                            Paper.book().write("languageID", selectedLanguageID);
                            updateView((String) Paper.book().read("language"));
                            updateView((String) Paper.book().read("languageID"));
                            break;
                        case "Hindi":
                            Paper.book().write("language", "hi");
                            Paper.book().write("languageID", selectedLanguageID);
                            updateView((String) Paper.book().read("language"));
                            updateView((String) Paper.book().read("languageID"));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    private void updateView(String lang) {
        Context context = LocalHelper.setLocale(this, lang);

        Resources resources = context.getResources();
//        textView.setText(context.getResources().getString(R.string.hello));
//        textInputLayout.setHint(context.getResources().getString(R.string.hint_adhaar));
//        editText.setHint(context.getResources().getString(R.string.hint_adhaar));

        ArrayList stateList = new ArrayList();
        stateList.add(context.getResources().getString(R.string.state_ap));
        stateList.add(context.getResources().getString(R.string.state_ap2));
        stateList.add(context.getResources().getString(R.string.state_assam));
        stateList.add(context.getResources().getString(R.string.state_bihar));
        stateList.add(context.getResources().getString(R.string.state_chhatisgarh));
        stateList.add(context.getResources().getString(R.string.state_goa));
        stateList.add(context.getResources().getString(R.string.state_gujarat));
        stateList.add(context.getResources().getString(R.string.state_haryana));
        stateList.add(context.getResources().getString(R.string.state_hp));
        stateList.add(context.getResources().getString(R.string.state_jharkhand));
        stateList.add(context.getResources().getString(R.string.state_karnataka));
        stateList.add(context.getResources().getString(R.string.state_kerala));
        stateList.add(context.getResources().getString(R.string.state_mp));
        stateList.add(context.getResources().getString(R.string.state_maharasthra));
        stateList.add(context.getResources().getString(R.string.state_manipur));
        stateList.add(context.getResources().getString(R.string.state_meghalaya));
        stateList.add(context.getResources().getString(R.string.state_mizoram));
        stateList.add(context.getResources().getString(R.string.state_nagaland));
        stateList.add(context.getResources().getString(R.string.state_odisha));
        stateList.add(context.getResources().getString(R.string.state_punjab));
        stateList.add(context.getResources().getString(R.string.state_rajsthan));
        stateList.add(context.getResources().getString(R.string.state_sikkim));
        stateList.add(context.getResources().getString(R.string.state_tn));
        stateList.add(context.getResources().getString(R.string.state_telangana));
        stateList.add(context.getResources().getString(R.string.state_tripura));
        stateList.add(context.getResources().getString(R.string.state_uk));
        stateList.add(context.getResources().getString(R.string.state_up));
        stateList.add(context.getResources().getString(R.string.state_wb));

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        //  spinnerState.setAdapter(aa);
        buttonSubmit.setText(context.getResources().getString(R.string.btn_submit));
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void deviceId() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                        return;
                    }
                    //String imeiNumber = telephonyManager.getDeviceId();
                    deviceUniqueIdentifier = telephonyManager.getDeviceId();
                    Paper.book().write("deviceUniqueID", deviceUniqueIdentifier);
                    updateView((String) Paper.book().read("deviceUniqueID"));

                    if (deviceUniqueIdentifier != null && !deviceUniqueIdentifier.isEmpty()) {
                        Paper.book().write("deviceUniqueID", deviceUniqueIdentifier);
                        updateView((String) Paper.book().read("deviceUniqueID"));
                        Toast.makeText(MainActivity.this, "IMEI Granted..", Toast.LENGTH_LONG).show();
                    } else {
                        String serialNumber = android.os.Build.SERIAL;
                        Paper.book().write("deviceUniqueID", serialNumber);
                        updateView((String) Paper.book().read("deviceUniqueID"));
                        Toast.makeText(MainActivity.this, "Serial Granted..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission not Granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}