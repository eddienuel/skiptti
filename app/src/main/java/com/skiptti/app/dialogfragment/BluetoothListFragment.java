package com.skiptti.app.dialogfragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skiptti.app.activity.MainApplicationActivity;
import com.skiptti.app.adapter.DeviceListAdapter;
import com.skiptti.app.adapter.ItemClickSupport;
import com.skiptti.app.model.Device;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.skiptti.app.R;

/**
 * Created by emmanuel on 24/10/2017.
 *
 */

public class BluetoothListFragment extends DialogFragment{
    RecyclerView deviceListView;
    DeviceListAdapter adapter;
    List<Device> devices = new ArrayList<>();
    private OnOptionSelectedListener listener;
    private BluetoothAdapter mBluetoothAdapter;
    MainApplicationActivity activity;


    public BluetoothListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainApplicationActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            dismiss();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            dismiss();
        }
        return inflater.inflate(R.layout.dialog_fragment_bluetooth_view, container);
    }

    @Override
    @SuppressWarnings("all")
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        deviceListView = (RecyclerView) view.findViewById(R.id.hooks_list);
        devices = new ArrayList<>();
        setup();

        ItemClickSupport.addTo(deviceListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.onSelect(devices.get(position));
                dismiss();
            }
        });
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener listener){
        this.listener = listener;
    }


    private void setup(){
        if(devices != null) {
            Set<BluetoothDevice> bluetoothDeviceSet = mBluetoothAdapter.getBondedDevices();
            if(bluetoothDeviceSet != null) {
                for (BluetoothDevice bleDevice : bluetoothDeviceSet) {
                    devices.add(new Device(bleDevice, 0));
                }
            }
            adapter = new DeviceListAdapter(getActivity(), devices);
            deviceListView.setAdapter(adapter);
            deviceListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addDevice(device, rssi);
                }
            });
        }
    };

    private void addDevice(BluetoothDevice bluetoothDevice, int rssi){
        for (Device device : devices) {
            if (device.getDescription().equals(bluetoothDevice.getAddress())) {
                return;
            }
        }
        devices.add(new Device(bluetoothDevice, rssi));
        adapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    public interface OnOptionSelectedListener{
        void onSelect(Device device);
    }
}
