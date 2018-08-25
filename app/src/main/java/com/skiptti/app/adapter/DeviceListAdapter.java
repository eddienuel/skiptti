package com.skiptti.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.skiptti.app.R;
import com.skiptti.app.model.Device;

import java.util.List;

/**
 * Created by emmanuel on 24/02/2017.
 *
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private List<Device> devices;
    private Context context;

    public DeviceListAdapter(Context context, List<Device> devices){
        this.context = context;
        this.devices = devices;
    }

    private Context getContext(){
        return context;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View fileView = inflater.inflate(R.layout.item_view_bluetooth_row, parent, false);
        return new DeviceViewHolder(fileView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.getName().setText(device.getName());
        holder.getImage().setImageResource(device.getImage());
        holder.getDescription().setText(device.getDescription());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}
