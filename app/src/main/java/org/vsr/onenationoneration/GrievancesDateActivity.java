package org.vsr.onenationoneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GrievancesDateActivity extends AppCompatActivity implements
        View.OnClickListener {
    Button btnFromDate, btnToDate, btnSubmit;
    EditText etFromDate, etToDate;
    private int mYear, mMonth, mDay;
    private int preYear, preMonth, preDay;
    String strRationCardNo;
    TextView tvAttachement1, tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String savedStateID, savedStateName, savedLanguageID, savedUniqueDeviceID, savedTXNIDFromRegActivity, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievancesdate);
        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Grievances");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etFromDate = findViewById(R.id.et_from_date);
        etToDate = findViewById(R.id.et_to_date);
        btnFromDate = findViewById(R.id.btn_fromdate);
        btnToDate = findViewById(R.id.btn_todate);
        btnSubmit = findViewById(R.id.btn_grievlist_submit);

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

        final Calendar c = Calendar.getInstance();
        preYear = c.get(Calendar.YEAR);
        preMonth = c.get(Calendar.MONTH);
        preDay = c.get(Calendar.DAY_OF_MONTH);
        etFromDate.setText(preYear + "-" + (preMonth - 5) + "-" + preDay);
        etToDate.setText(preYear + "-" + (preMonth + 1) + "-" + preDay);

        btnFromDate.setOnClickListener(this);
        btnToDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnFromDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            //etFromDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            etFromDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnToDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // etToDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            etToDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnSubmit) {
            Paper.book().write("GrievancesDateActivity_RationCard", strRationCardNo);
            Paper.book().write("GrievancesDateActivity_TXNID", savedTXNIDFromRegActivity);
            Paper.book().write("GrievancesDateActivity_FromDate", etFromDate.getText().toString().trim());
            Paper.book().write("GrievancesDateActivity_ToDate", etToDate.getText().toString().trim());
            Intent intent = new Intent(GrievancesDateActivity.this, GrievancesListActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("RationCard", strRationCardNo);
//            bundle.putString("TXNID", savedTXNIDFromRegActivity);
//            bundle.putString("FromDate", etFromDate.getText().toString().trim());
//            bundle.putString("ToDate", etToDate.getText().toString().trim());
//            intent.putExtras(bundle);
            startActivity(intent);
        }
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
                Intent intent = new Intent(GrievancesDateActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(GrievancesDateActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
