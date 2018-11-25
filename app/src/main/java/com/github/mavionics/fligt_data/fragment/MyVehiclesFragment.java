package com.github.mavionics.fligt_data.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyVehiclesFragment extends PostListFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("vehicles")
                .child(getUid());
    }
}
