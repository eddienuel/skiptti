package com.skiptti.app.modules.skip.target;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skiptti.app.R;
import com.skiptti.app.control.ColorArcProgressBar;
import com.skiptti.app.dialogfragment.BluetoothListFragment;
import com.skiptti.app.fragments.BaseFragment;
import com.skiptti.app.model.Device;
import com.skiptti.app.services.UartService;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * @author emmanuel edwards
 */
public class TargetSkipFragment extends BaseFragment implements BluetoothListFragment.OnOptionSelectedListener{

    ColorArcProgressBar skipCountBar;
    View view;
    TextView selectedTarget;
    BluetoothAdapter mBtAdapter = null;
    private UartService mService = null;
    private float previousCount = 0;
    private int targetValue;
    public TargetSkipFragment() {
        // Required empty public constructor
        //This is a test comment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_target_skip_layout, container, false);
        targetValue = getArguments().getInt("target", 200);
        skipCountBar = (ColorArcProgressBar) view.findViewById(R.id.skip_count);
        selectedTarget = (TextView)view.findViewById(R.id.target_selected);
        skipCountBar.setMaxValues((float) targetValue);
        skipCountBar.setCurrentValues(0);
        selectedTarget.setText(String.valueOf(targetValue));
        view.setKeepScreenOn(true);

        BluetoothListFragment bluetoothListFragment = new BluetoothListFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        bluetoothListFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_TargetChooser);
        bluetoothListFragment.setOnOptionSelectedListener(this);
        bluetoothListFragment.show(manager, "hooks");

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 1);
        }
        service_init();
        return view;
    }

    /**
     * initializes the uart service
     */
    private void service_init() {
        Intent bindIntent = new Intent(getActivity(), UartService.class);
        getContext().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    /**
     * UART service connection callback listener
     * on service connected and on services disconnected
    * */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.d(this.getClass().getName(), "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Toast.makeText(getActivity(),"Unable to initialize Bluetooth", Toast.LENGTH_SHORT).show();
                Log.e(getClass().getName(), "Unable to initialize Bluetooth");
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };

    /**
     * intent filters to be used bu the uart service
     * @return an intent filter object
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    /**
     * broadcast receiver for all uart events from uart service
     */
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getAction();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (action != null ? action : ""){
                        case UartService.ACTION_GATT_CONNECTED:{

                            break;
                        }

                        case UartService.ACTION_GATT_DISCONNECTED:{
                            mService.close();
                            break;
                        }

                        case UartService.ACTION_GATT_SERVICES_DISCOVERED:{
                            mService.enableTXNotification();
                            break;
                        }

                        case UartService.ACTION_DATA_AVAILABLE:{
                            handleUartDataReceived(intent);
                            break;
                        }

                        case UartService.DEVICE_DOES_NOT_SUPPORT_UART:{
                            Toast.makeText(getActivity(), "Device doesn't support UART. Disconnecting", Toast.LENGTH_SHORT).show();
                            mService.disconnect();
                            break;
                        }
                    }
                }
            });
        }
    };

    private void handleUartDataReceived(Intent intent){
        final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
        try {
            String text = new String(txValue, "UTF-8");
            String[] splitString = text.split(" ");
            float skipCount = Float.valueOf(splitString[3]);
            Log.e("RECEIVED== ", text);
            processCount(skipCount);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSelect(Device device) {
        mService.connect(device.getBluetoothDevice().getAddress());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e("error", ignore.toString());
        }
        getActivity().unbindService(mServiceConnection);
        mService.stopSelf();
        mService= null;
    }

    private void processCount(float count){
        if(previousCount > count){
            endSkipSession(previousCount);
        }else if((int)count >= targetValue){
           endSkipSession(count);
        }else {
            previousCount = count;
            skipCountBar.setCurrentValues(count);
        }
    }

    private void endSkipSession(float finalCount){

        SkipFinishedDialogFragment dialogFragment = new SkipFinishedDialogFragment();
        FragmentManager manager = activity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        String message = String.format(Locale.UK,"You are amazing, you did %d skips. Good job!", (int)finalCount);
        bundle.putString("message", message);
        dialogFragment.setArguments(bundle);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_TargetChooser);
        dialogFragment.show(manager, "target");
    }
}
