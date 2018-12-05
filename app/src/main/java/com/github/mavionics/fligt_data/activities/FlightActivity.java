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

    // Init
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateLocation();
            if(mHandler != null){
                mHandler.postDelayed(this, 500);
            }
        }
    };

    @BindView(R.id.vehicleName) TextView mNameView;
    @BindView(R.id.vehiclePosition) TextView mPositionView;
    @BindView(R.id.vehicleLastUpdated) TextView mLastUpdatedView;

    private DecimalFormat mDecimalFormat;

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


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mDecimalFormat = new DecimalFormat("#.000");

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            showPhoneStatePermission();
        }else{
            mLocation = getLastKnownLocation();

            if(mLocation == null){
                Log.d(TAG, "UpdateLocation: location null");
                Toast.makeText(this, "Location not found, please check permissions.",
                        Toast.LENGTH_LONG).show();
            }
                onPermissionRequested();
        }
    }

    @Override
    protected void onPermissionRequested(){
        if (mHandler != null) {
            // Update location every second
            mHandler.postDelayed(mRunnable, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mHandler = null;
        finish();
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "updateLocation: permission not granted");
            return null;
        }
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void updateLocation(){

        mLocation = getLastKnownLocation();

        if(mLocation == null){
            Log.d(TAG, "UpdateLocation: location null");
            return;
        }

        mDatabase = FirebaseFirestore.getInstance();

        mDatabase.collection("vehicles")
                .whereEqualTo("owner",getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                Map<String, Object> vehicle = document.getData();
                                if(vehicle.get("name").equals(mVehicleName)
                                        && mLocation != null){

                                    mPosition = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
                                    mLastUpdated = Timestamp.now();
                                    vehicle.put("position", mPosition);
                                    vehicle.put("timestamp", mLastUpdated);
                                    // Add a new document with a generated ID

                                    Log.d(TAG, "Updating vehicle location: " + vehicle.toString());
                                    mDatabase.collection("vehicles").document(document.getId()).update(vehicle);
                                    mPositionView.setText(GeoToString(mPosition));
                                    mLastUpdatedView.setText(mLastUpdated.toDate().toString());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private String GeoToString(GeoPoint position){
        return "lat: " + mDecimalFormat.format(position.getLatitude()) +
                ", lon: " + mDecimalFormat.format(position.getLongitude());
    }

}
