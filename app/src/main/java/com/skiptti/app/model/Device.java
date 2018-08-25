package com.skiptti.app.model;


import android.bluetooth.BluetoothDevice;

import com.skiptti.app.R;


/**
 * Created by emmanuel on 09/10/2017.
 *
 */

public class Device {
    private String value;
    private String description;
    private String name;
    private int image;
    private BluetoothDevice bluetoothDevice;

    public Device() {
    }

    public Device(String value, String description, String name, int image) {
        this.value = value;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public Device(BluetoothDevice bluetoothDevice, int mRssi){
        this.bluetoothDevice = bluetoothDevice;
        this.name = bluetoothDevice.getName();
        this.description = bluetoothDevice.getAddress();
        this.image = R.mipmap.ic_skip_rope;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}
