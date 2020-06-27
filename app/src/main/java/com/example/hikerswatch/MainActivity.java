package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView2 = findViewById(R.id.textView2);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    if (addressList != null && addressList.size() > 0){
                        String address = "";

                        if (addressList.get(0).getThoroughfare() != null){
                            address += addressList.get(0).getThoroughfare() + "\r\n";
                        }
                        if (addressList.get(0).getLocality() != null){
                            address += addressList.get(0).getLocality() + "\r\n";
                        }
                        if (addressList.get(0).getAdminArea() != null){
                            address += addressList.get(0).getAdminArea() + "\r\n";
                        }
                        if (addressList.get(0).getPostalCode() != null){
                            address += addressList.get(0).getPostalCode();
                        }
                        textView2.setText("Latitude: " + location.getLatitude() + "\r\nLongitude: " + location.getLongitude() + "\r\nAltitude: " + location.getAltitude() + "\r\n\nGEO_Codes: " + address);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "GPS DISABLED", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(MainActivity.this, "!! GPS ENABLED | Getting Location Data", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(MainActivity.this, "!! GPS DISABLED !!", Toast.LENGTH_LONG).show();
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }
}