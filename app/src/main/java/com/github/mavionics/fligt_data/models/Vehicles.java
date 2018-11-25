package com.github.mavionics.fligt_data.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Vehicles {

    public String name;
    public String owner;
    public String position;
    public String timeStamp;

    public Vehicles() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Vehicles(String name, String owner,
                    String position, String timeStamp) {
        this.name = name;
        this.owner = owner;
        this.position = position;
        this.timeStamp = timeStamp;
    }

}
// [END blog_user_class]
