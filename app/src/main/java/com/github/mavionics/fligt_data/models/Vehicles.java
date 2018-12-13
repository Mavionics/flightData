package com.github.mavionics.fligt_data.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;

// [START blog_user_class]
@IgnoreExtraProperties
public class Vehicles {

    private String uuid;
    private String name;
    private String owner;
    private GeoPoint position;
    private Timestamp timeStamp;

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

    public String getName() {
        return name;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getUuid(){
        return uuid;
    }
}
// [END blog_user_class]
