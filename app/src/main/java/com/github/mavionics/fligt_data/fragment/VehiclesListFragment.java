package com.github.mavionics.fligt_data.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mavionics.fligt_data.R;
import com.github.mavionics.fligt_data.activities.FlightActivity;
import com.github.mavionics.fligt_data.activities.MainActivity;
import com.github.mavionics.fligt_data.models.Vehicles;
import com.github.mavionics.fligt_data.viewholder.VehicleViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class VehiclesListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    // [START define_database_reference]
    private FirebaseFirestore mDatabase;
    // [END define_database_reference]

    private FirestoreRecyclerAdapter<Vehicles, VehicleViewHolder> mAdapter;
    private LinearLayoutManager mManager;

    private RecyclerView mRecycler;


    public VehiclesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_vehicles, container, false);
        ButterKnife.bind(getActivity(), rootView);
        mRecycler = rootView.findViewById(R.id.vehicleList);
        mDatabase = FirebaseFirestore.getInstance();
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query vehiclesQuery = mDatabase.collection("vehicles").whereEqualTo("owner",getUid());

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Vehicles>()
                .setQuery(vehiclesQuery, Vehicles.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Vehicles, VehicleViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull VehicleViewHolder viewHolder, int position, @NonNull Vehicles model) {
                viewHolder.bindToPost(model);
            }

            @Override
            public VehicleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                VehicleViewHolder viewHolder = new VehicleViewHolder(inflater.inflate(R.layout.item_vehicle, viewGroup, false));
                viewHolder.setOnClickListener(new VehicleViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "Starting FlightActivity");

                        if (ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getActivity(),
                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                        PackageManager.PERMISSION_GRANTED) {
                            ((MainActivity) getActivity()).showPhoneStatePermission();
                        }else {

                            Intent intent = new Intent(getActivity(), FlightActivity.class);
                            String message = mAdapter.getItem(position).getName();

                            intent.putExtra("VEHICLE_NAME", message);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getActivity(), mAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder;
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
