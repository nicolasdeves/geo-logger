package com.nicolas.geo_logger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private DatabaseManager databaseManager;

    private TextView latitudeTv, longitudeTv, countTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        databaseManager = new DatabaseManager(this);
    }

    public void initComponents() {
        latitudeTv = findViewById(R.id.tv_latitude);
        longitudeTv = findViewById(R.id.tv_longitude);
        countTv = findViewById(R.id.tv_count);

        Button viewPositionsButton = findViewById(R.id.button_view_positions);
        viewPositionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Button cleanDataButton = findViewById(R.id.button_clean);
        cleanDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseManager.cleanData();
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitudeTv.setText(String.format("%.5f", location.getLatitude()));
        longitudeTv.setText(String.format("%.5f", location.getLongitude()));
        countTv.setText(String.valueOf(databaseManager.getQuantity()));

        databaseManager.insertLocation(location.getLatitude(), location.getLongitude());
    }
}