package unikom.gery.damang.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import unikom.gery.damang.GBApplication;
import unikom.gery.damang.R;
import unikom.gery.damang.devices.DeviceManager;
import unikom.gery.damang.impl.GBDevice;
import unikom.gery.damang.sqlite.dml.HeartRateHelper;
import unikom.gery.damang.sqlite.table.Sport;

public class SportActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack;
    private TextView btnViewAll;
    private ConstraintLayout cvNoData;
    private CardView btnOtherSport;
    private RecyclerView rvSport;
    private ArrayList<Sport> arrayList;
    private HeartRateHelper heartRateHelper;
    private DeviceManager deviceManager;
    private List<GBDevice> deviceList;
    private GBDevice device;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change statusbar color
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sport);

        rvSport = findViewById(R.id.rvSportData);
        btnBack = findViewById(R.id.btnBack);
        btnViewAll = findViewById(R.id.btnViewAll);
        btnOtherSport = findViewById(R.id.btnSportOther);
        cvNoData = findViewById(R.id.cvNoData);
        heartRateHelper = HeartRateHelper.getInstance(getApplicationContext());
        arrayList = heartRateHelper.getSportData();
        deviceManager = ((GBApplication) getApplication()).getDeviceManager();
        deviceList = deviceManager.getDevices();

        btnBack.setOnClickListener(this);
        btnOtherSport.setOnClickListener(this);
        setView();
    }

    private void setView() {
        if (arrayList.size() > 0) {
            btnViewAll.setVisibility(View.VISIBLE);
            cvNoData.setVisibility(View.GONE);
            rvSport.setVisibility(View.VISIBLE);
        } else {
            btnViewAll.setVisibility(View.INVISIBLE);
            rvSport.setVisibility(View.GONE);
            cvNoData.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkDevice() {
        boolean status = false;
        if (deviceList.size() <= 0)
            return false;
        else {
            for (int i = 0; i < deviceList.size(); i++) {
                device = deviceList.get(i);
                if (device.isConnected()) {
                    status = true;
                }
            }
        }
        return status;
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
            finish();
        else if (view == btnOtherSport) {
            if (checkDevice()) {
                startActivity(new Intent(getApplicationContext(), OtherSportActivity.class));
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Harap hubungkan dahulu sistem dengan perangkat wearable device", Toast.LENGTH_SHORT).show();
        }
    }
}