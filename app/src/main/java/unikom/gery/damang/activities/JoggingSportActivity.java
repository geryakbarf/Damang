package unikom.gery.damang.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import unikom.gery.damang.R;

public class JoggingSportActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    Marker marker;
    private GoogleMap mMap;

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
        setContentView(R.layout.activity_jogging_sport);
        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String newProvider = lManager.getBestProvider(new Criteria(),true);
        @SuppressLint("MissingPermission") Location location = lManager.getLastKnownLocation(newProvider);
        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Lokasi Anda"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition,17));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), SportActivity.class));
        finish();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String newProvider = lManager.getBestProvider(new Criteria(),true);
        location = lManager.getLastKnownLocation(newProvider);
        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Lokasi Anda"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition,20));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}