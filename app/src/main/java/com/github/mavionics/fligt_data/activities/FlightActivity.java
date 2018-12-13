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
import com.github.mavionics.fligt_data.communication.FlightStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.HashMap;
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
    private String mVehicleUuid;
    private LocationManager mLocationManager;
    private GeoPoint mPosition;
    private Timestamp mLastUpdated;

    // Init
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateVehicleStatus();
            searchForConnection();
            if(mHandler != null){
                mHandler.postDelayed(this, 5000);
            }
        }
    };
    private DocumentReference mVehicleDocument;

    private void searchForConnection() {



        mDatabase.collection("calls")
                .whereEqualTo("vehicleId", mVehicleUuid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> calls = new HashMap<>();
                                calls.put("vehicleSDP","My sdp");
                                mStatus = FlightStatus.connecting;
                                mDatabase.collection("calls")
                                        .document(document.getId()).update(calls);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @BindView(R.id.vehicleName) TextView mNameView;
    @BindView(R.id.vehiclePosition) TextView mPositionView;
    @BindView(R.id.vehicleLastUpdated) TextView mLastUpdatedView;
    @BindString(R.string.emptyPosition) String mPositionNotFound;
    @BindString(R.string.Firebase_Vehicle_UUID) String mFIREBASE_VEHICLE_UUID;
    @BindString(R.string.Firebase_Vehicle_Name) String mFIREBASE_VEHICLE_NAME;

    private DecimalFormat mDecimalFormat;
    private FlightStatus mStatus = FlightStatus.offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mVehicleUuid = intent.getStringExtra(mFIREBASE_VEHICLE_UUID);
        String name = intent.getStringExtra(mFIREBASE_VEHICLE_NAME);
        mNameView.setText(name);
        mPositionView.setText("");
        mLastUpdatedView.setText("");

        Log.d(TAG, "onCreate: vehicle uuid: " + mVehicleUuid);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mDecimalFormat = new DecimalFormat("#.000");

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location not found, please check permissions.",
                    Toast.LENGTH_LONG).show();
            finish();
        }else{
            mDatabase = FirebaseFirestore.getInstance();

            mVehicleDocument = mDatabase.collection("vehicles").document(mVehicleUuid);

            if (mHandler != null) {
                // Update location every second
                mHandler.postDelayed(mRunnable, 0);
            }
            mStatus = FlightStatus.online;

        }
    }

    @Override
    public void finish(){
        super.finish();
        Log.d(TAG, "finish: ");
        mHandler = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        Map<String, Object> vehicle = new HashMap<>();
        mStatus = FlightStatus.offline;
        vehicle.put("status",mStatus.toString());
        mVehicleDocument.update(vehicle);
        finish();
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "updateLocation: permission not granted");
            Toast.makeText(this, "Location not found, please check permissions.",
                    Toast.LENGTH_LONG).show();
            finish();
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

    private void updateVehicleStatus(){

        mLocation = getLastKnownLocation();
        Map<String, Object> vehicle = new HashMap<>();

        if (mLocation != null) {
            Log.d(TAG, "updateLocation: position not found");
            mPosition = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
            vehicle.put("position", mPosition);
            vehicle.put("status", mStatus.toString());
            mPositionView.setText(GeoToString(mPosition));
        }else{
            mPositionView.setText(mPositionNotFound);
        }

        mLastUpdated = Timestamp.now();
        vehicle.put("timestamp", mLastUpdated);
        mLastUpdatedView.setText(mLastUpdated.toDate().toString());

        mVehicleDocument.update(vehicle);
    }
    private String GeoToString(GeoPoint position){
        return "lat: " + mDecimalFormat.format(position.getLatitude()) +
                ", lon: " + mDecimalFormat.format(position.getLongitude());
    }

}
