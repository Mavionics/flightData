package com.github.mavionics.fligt_data.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;

// [START blog_user_class]
@IgnoreExtraProperties
public class Vehicles {

    public String name;
    public String owner;
    public GeoPoint position;
    public Timestamp timeStamp;

    public Vehicles() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Vehicles(String name, String owner,
                    GeoPoint position, Timestamp timeStamp) {
        this.name = name;
        this.owner = owner;
        this.position = position;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "name: " + name +
                " ,owner: " + owner +
                " ,position: " + position;
    }
}
// [END blog_user_class]
