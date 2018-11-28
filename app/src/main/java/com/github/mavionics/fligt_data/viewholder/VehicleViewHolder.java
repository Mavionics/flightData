package com.github.mavionics.fligt_data.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mavionics.fligt_data.R;
import com.github.mavionics.fligt_data.models.Vehicles;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView;
    public LinearLayout vehicleItem;

    public VehicleViewHolder(View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.name);
        vehicleItem = itemView.findViewById(R.id.vehicleItem);
    }

    public void bindToPost(Vehicles vehicle) {
        nameView.setText(vehicle.name);
    }
}
