package com.example.tabtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabtest.BLE.BluetoothLeService;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;


public class Home1BT extends ListFragment {

    private final static String TAG = Home1BT.class.getSimpleName();

    //public BluetoothAdapter bluetoothAdapter = new BluetoothAdapter();
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE_BT = 1;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private Home1BT.LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private boolean mScanning;
    private Handler mHandler;

    private String mDeviceName;
    private String mDeviceAddress;
    private boolean mConnected = false;

    // Threshold buttons
    private TextView uvi_label;
    private EditText uvi_value;
    private Button uvi_sendbutton;
    private ImageView uvi_status;

    private TextView steps_label;
    private EditText steps_value;
    private Button steps_sendbutton;
    private ImageView steps_status;

    private TextView co2_label;
    private EditText co2_value;
    private Button co2_sendbutton;
    private ImageView co2_status;

//    private TextView voc_label;
//    private EditText voc_value;
//    private Button voc_sendbutton;
//    private TextView voc_status;
//
//    private TextView temp_label;
//    private EditText temp_value;
//    private Button temp_sendbutton;
//    private TextView temp_status;

    private TextView uviDur_label;
    private EditText uviDur_value;
    private Button uviDur_sendbutton;
    private ImageView uviDur_status;

//    private TextView sun_label;
//    private EditText sun_value;
//    private Button sun_sendbutton;
//    private TextView sun_status;

    // other buttons
    private Button initSerialBt;
    private Button getAllData;




    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                getActivity().finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                // displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                if (intent.hasExtra(BluetoothLeService.EXTRA_DATA)) {
                    String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    Log.e("DEBUG", "Data read: " + data);
                    // TODO: handle data
                    if (data.equals("setUviThresh")) {
                        uvi_status.setVisibility(View.VISIBLE);
                    } else if (data.equals("setStepGoal")) {
                        steps_status.setVisibility(View.VISIBLE);
                    } else if (data.equals("setCo2Thresh")) {
                        co2_status.setVisibility(View.VISIBLE);
                    } else if (data.equals("setUviDurationThresh")) {
                        uviDur_status.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("DEBUG", "Data is available to read.");
                    if(mBluetoothLeService != null) {
                        mBluetoothLeService.readCustomCharacteristic();
                    }
                }
            }
        }
    };


    @SuppressLint({"CutPasteId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home1_bt, container, false);

        final TextView out = (TextView) rootView.findViewById(R.id.out);
        final Button button1 = (Button) rootView.findViewById(R.id.button1);
        final Button button2 = (Button) rootView.findViewById(R.id.button2);
        //final Button button3 = (Button) rootView.findViewById(R.id.button3);

        // Threshold buttons
        uvi_label = (TextView) rootView.findViewById(R.id.sendlabel1);
        uvi_value = (EditText) rootView.findViewById(R.id.sendcontent1);
        uvi_sendbutton = (Button) rootView.findViewById(R.id.sendbutton1);
        //uvi_status = (TextView) rootView.findViewById(R.id.sendstatus1);
        uvi_status = (ImageView) rootView.findViewById(R.id.sendstatus1);

        steps_label = (TextView) rootView.findViewById(R.id.sendlabel2);
        steps_value = (EditText) rootView.findViewById(R.id.sendcontent2);
        steps_sendbutton = (Button) rootView.findViewById(R.id.sendbutton2);
        steps_status = (ImageView) rootView.findViewById(R.id.sendstatus2);

        co2_label = (TextView) rootView.findViewById(R.id.sendlabel3);
        co2_value = (EditText) rootView.findViewById(R.id.sendcontent3);
        co2_sendbutton = (Button) rootView.findViewById(R.id.sendbutton3);
        co2_status = (ImageView) rootView.findViewById(R.id.sendstatus3);

//        voc_label = (TextView) rootView.findViewById(R.id.sendlabel4);
//        voc_value = (EditText) rootView.findViewById(R.id.sendcontent4);
//        voc_sendbutton = (Button) rootView.findViewById(R.id.sendbutton4);
//        voc_status = (TextView) rootView.findViewById(R.id.sendstatus4);
//
//        temp_label = (TextView) rootView.findViewById(R.id.sendlabel5);
//        temp_value = (EditText) rootView.findViewById(R.id.sendcontent5);
//        temp_sendbutton = (Button) rootView.findViewById(R.id.sendbutton5);
//        temp_status = (TextView) rootView.findViewById(R.id.sendstatus5);
//
        uviDur_label = (TextView) rootView.findViewById(R.id.sendlabel6);
        uviDur_value = (EditText) rootView.findViewById(R.id.sendcontent6);
        uviDur_sendbutton = (Button) rootView.findViewById(R.id.sendbutton6);
        uviDur_status = (ImageView) rootView.findViewById(R.id.sendstatus6);

//        sun_label = (TextView) rootView.findViewById(R.id.sendlabel7);
//        sun_value = (EditText) rootView.findViewById(R.id.sendcontent7);
//        sun_sendbutton = (Button) rootView.findViewById(R.id.sendbutton7);
//        sun_status = (TextView) rootView.findViewById(R.id.sendstatus7);

        // other buttons
        initSerialBt = (Button) rootView.findViewById(R.id.button3);
        getAllData = (Button) rootView.findViewById(R.id.button4);

        mHandler = new Handler();
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, LENGTH_SHORT).show();
            getActivity().finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.error_bluetooth_not_supported, LENGTH_SHORT).show();
            out.setText(R.string.error_bluetooth_not_supported);
            getActivity().finish();
            return rootView;
        }




        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                scanLeDevice(false);
            }
        });


//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                mBluetoothAdapter.disable();
//                //out.append("TURN_OFF BLUETOOTH");
//                Toast.makeText(getActivity().getApplicationContext(), "TURNING_OFF BLUETOOTH", Toast.LENGTH_LONG);
//
//            }
//        });


        uvi_sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onClickSend(arg0);
            }
        });
        steps_sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onClickSend(arg0);
            }
        });
        co2_sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onClickSend(arg0);
            }
        });
        uviDur_sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Home1BT.this.onClickSend(arg0);
            }
        });

        // other buttons
        initSerialBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onClickSend(arg0);
            }
        });
        getAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onClickSend(arg0);
            }
        });

//      uviDur_sendbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                onClickSend(arg0);
//            }
//        });

//        voc_sendbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                onClickSend(arg0);
//            }
//        });
//        temp_sendbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                onClickSend(arg0);
//            }
//        });
//        sun_sendbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                onClickSend(arg0);
//            }
//        });



        Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
        getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);

        return rootView;
    }








    @Override
    public void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        // Initializes list view adapter.
        mLeDeviceListAdapter = new Home1BT.LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);

        if(!mConnected) scanLeDevice(true);

        // Set check mark
        uvi_status.setVisibility(View.INVISIBLE);
        steps_status.setVisibility(View.INVISIBLE);
        co2_status.setVisibility(View.INVISIBLE);
        uviDur_status.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            getActivity().finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        Log.e(TAG, "Connected to: " + device.toString());
//        final Intent intent = new Intent(this, Home1BT.class);
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//        startActivity(intent);
        mDeviceName = device.getName();
        mDeviceAddress = device.getAddress();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.i(TAG, "Connect request result=" + result);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = Home1BT.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Home1BT.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new Home1BT.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (Home1BT.ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }





    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }



    private void updateConnectionState(final int resourceId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mConnectionState.setText(resourceId);
            }
        });
    }



    public void onClickSend(View v){
        Log.e("DEBUG", "onClickSend()");

        switch (v.getId()) {
            case R.id.sendbutton1:
                if(mBluetoothLeService != null) {
                    mBluetoothLeService.writeCustomCharacteristic("setUviThresh:" + uvi_value.getText());
                    uvi_status.setVisibility(View.INVISIBLE);
                }
            case R.id.sendbutton2:
                if(mBluetoothLeService != null) {
                    mBluetoothLeService.writeCustomCharacteristic("setStepGoal:" + steps_value.getText());
                    steps_status.setVisibility(View.INVISIBLE);
                }
            case R.id.sendbutton3:
                if(mBluetoothLeService != null) {
                    mBluetoothLeService.writeCustomCharacteristic("setCo2Thresh:" + co2_value.getText());
                    co2_status.setVisibility(View.INVISIBLE);
                }
//            case R.id.sendbutton4:
//                if(mBluetoothLeService != null) {
//                    mBluetoothLeService.writeCustomCharacteristic("setVOCThresh:" + voc_value.getText());
//                }
//            case R.id.sendbutton5:
//                if(mBluetoothLeService != null) {
//                    mBluetoothLeService.writeCustomCharacteristic("setTempThresh:" + temp_value.getText());
//                }
            case R.id.sendbutton6:
                if(mBluetoothLeService != null) {
                    mBluetoothLeService.writeCustomCharacteristic("setUviDurationThresh:" + uviDur_value.getText());
                    uviDur_status.setVisibility(View.INVISIBLE);
                }
            case R.id.button3:
                if(mBluetoothLeService != null) {
                    mBluetoothLeService.writeCustomCharacteristic("initBtSerial");
                }
                case R.id.button4:
                if(mBluetoothLeService != null) {
                    mBluetoothLeService.writeCustomCharacteristic("dataWanted_all");
                }
//            case R.id.sendbutton7:
//                if(mBluetoothLeService != null) {
//                    mBluetoothLeService.writeCustomCharacteristic("setSunscreenFactor:" + sun_value.getText());
//                }

        }

    }

}
