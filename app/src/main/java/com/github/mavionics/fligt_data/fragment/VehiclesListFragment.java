package com.github.mavionics.fligt_data.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mavionics.fligt_data.R;
import com.github.mavionics.fligt_data.models.Vehicles;
import com.github.mavionics.fligt_data.viewholder.VehicleViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VehiclesListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    // [START define_database_reference]
    private FirebaseFirestore mDatabase;
    // [END define_database_reference]

    private FirestoreRecyclerAdapter<Vehicles, VehicleViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public VehiclesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_vehicles, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseFirestore.getInstance();
        // [END create_database_reference]

        mRecycler = rootView.findViewById(R.id.vehicleList);
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
                return new VehicleViewHolder(inflater.inflate(R.layout.item_vehicle, viewGroup, false));
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
