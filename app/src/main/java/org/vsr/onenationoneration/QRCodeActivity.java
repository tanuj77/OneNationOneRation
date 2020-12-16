package org.vsr.onenationoneration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import java.util.Timer;
import java.util.TimerTask;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class QRCodeActivity extends AppCompatActivity {
    String TAG = "GenerateQrCode", inputValue;
    ImageView qrimg;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String savedStateID, savedStateName, savedLanguageID, savedUniqueDeviceID, savedTXNIDFromRegActivity, savedAadharCardNumber, savedDepartmentName, savedAsOn, savedSourceName, savedDisclaimer;
    ActionBar actionBar;
    TextView tvbottomDepartmentName, tvbottomStateName, tvbottomAsOn, tvbottomSourceName, tvbottomDisclaimer;
    String strRationCardNo, strSchemeName, strCommodityName, strQuantity, strRatePerUnit, strMonthYear;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        savedTXNIDFromRegActivity = getIntent().getStringExtra("TXNID");

        actionBar = getSupportActionBar();
        actionBar.setTitle("QR Code");
        actionBar.setSubtitle("RC No.:" + strRationCardNo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        qrimg = findViewById(R.id.iv_qrcode);
        tvbottomDepartmentName = findViewById(R.id.tv_bottom_departmentname);
        tvbottomStateName = findViewById(R.id.tv_bottom_statename);
        tvbottomAsOn = findViewById(R.id.tv_bottom_ason);
        tvbottomSourceName = findViewById(R.id.tv_bottom_sourcename);
        tvbottomDisclaimer = findViewById(R.id.tv_bottom_Disclaimer);

        strRationCardNo = getIntent().getStringExtra("RationCard");
        strSchemeName = getIntent().getStringExtra("SchemeName");
        strCommodityName = getIntent().getStringExtra("CommodityName");
        strQuantity = getIntent().getStringExtra("Quantity");
        strRatePerUnit = getIntent().getStringExtra("RatePerUnit");
        strMonthYear = getIntent().getStringExtra("MonthYear");

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

        inputValue = "RC ID: " + strRationCardNo + "\n" + "Scheme Name: " + strSchemeName + "\n" + "Commodity Name: " + strCommodityName + "\n" + "Quantity: " + strQuantity + "\n" + "Rate Per Unit: " + strRatePerUnit + "\n" + "Months, Year: " + strMonthYear;
        if (inputValue.length() > 0) {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;
            qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrimg.setImageBitmap(bitmap);

            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
        }
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
                Intent intent = new Intent(QRCodeActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.item_language:
                Intent intent2 = new Intent(QRCodeActivity.this, MainActivity.class);
                startActivity(intent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
