package com.github.mavionics.fligt_data.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mavionics.fligt_data.R;
import com.github.mavionics.fligt_data.models.Vehicles;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.vehicleItem) LinearLayout vehicleItem;
    @BindView(R.id.name) TextView nameView;

    public VehicleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(Vehicles vehicle) {
        nameView.setText(vehicle.name);
    }
}
