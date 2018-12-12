package com.github.mavionics.fligt_data.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mavionics.fligt_data.R;
import com.github.mavionics.fligt_data.services.FlightService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlightActivity extends BaseActivity {

    private FirebaseFirestore mDatabase;
    private Location mLocation;
    private String TAG = "FlightActivity";
    private String mVehicleName;
    private LocationManager mLocationManager;
    private GeoPoint mPosition;
    private Timestamp mLastUpdated;


    @BindView(R.id.vehicleName) TextView mNameView;
    @BindView(R.id.vehiclePosition) TextView mPositionView;
    @BindView(R.id.vehicleLastUpdated) TextView mLastUpdatedView;
    @BindString(R.string.emptyPosition) String mPositionNotFound;
    @BindString(R.string.STARTFOREGROUND_ACTION) String mACTION_STARTFOREGROUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mVehicleName = intent.getStringExtra("VEHICLE_NAME");
        mNameView.setText(mVehicleName);
        mPositionView.setText("");
        mLastUpdatedView.setText("");

        Log.d(TAG, "onCreate: vehicle name: " + mVehicleName);


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location not found, please check permissions.",
                    Toast.LENGTH_LONG).show();
            finish();
        }else{
            Intent startIntent = new Intent(FlightActivity.this, FlightService.class);
            startIntent.setAction(mACTION_STARTFOREGROUND);
            startService(startIntent);
        }
    }

    @Override
    public void finish(){
        super.finish();
        Log.d(TAG, "finish: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        finish();
    }


}
