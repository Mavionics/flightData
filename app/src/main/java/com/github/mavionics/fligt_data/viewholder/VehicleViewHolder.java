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
    private VehicleViewHolder.ClickListener mClickListener;

    public VehicleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        //listener set on ENTIRE ROW, you may set on individual components within a row.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public void bindToPost(Vehicles vehicle) {
        nameView.setText(vehicle.getName());
    }

    //Interface to send callbacks...
    public interface ClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(VehicleViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
