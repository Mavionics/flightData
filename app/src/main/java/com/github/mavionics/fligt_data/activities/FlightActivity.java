package com.github.mavionics.fligt_data.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mavionics.fligt_data.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;

public class FlightActivity extends BaseActivity {

    private FirebaseFirestore db;
    private Location location;
    private String TAG = "FlightActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        Intent intent = getIntent();
        final String message =
                intent.getStringExtra("VEHICLE_NAME");

        Log.d(TAG, "onCreate: " + message);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showPhoneStatePermission();
            return;
        }

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        // Access a Cloud Firestore instance from your Activity

        db = FirebaseFirestore.getInstance();

        db.collection("vehicles")
                .whereEqualTo("owner",getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> vehicle = document.getData();
                                Log.d(TAG, "vehicle name: " + vehicle.get("name"));
                                if(vehicle.get("name").equals(message)){
                                    Log.d(TAG, "onComplete: matches activity name");
                                    vehicle.put("position", new GeoPoint(location.getLatitude(), location.getLongitude()));
                                    vehicle.put("timestamp", Timestamp.now());
                                    // Add a new document with a generated ID
                                    db.collection("vehicles").document(document.getId()).update(vehicle);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}
