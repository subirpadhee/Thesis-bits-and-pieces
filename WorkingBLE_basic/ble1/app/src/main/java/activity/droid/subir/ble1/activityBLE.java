package activity.droid.subir.ble1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

@TargetApi(21)
public class activityBLE extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private Button btnScanBLE;
    private ListView lvBLE;
    private ArrayAdapter adapterArray;
    private int j = 0;
    private final int SIZE_RESP = 30;
    private String[] arrayResp = new String[SIZE_RESP];
    private boolean dup = false;
    private BluetoothDevice newDev;
    private String IBEACON = "4D:D5:71:A7:5C:8B";//5F:10:CF:D4:C8:C4";//5B:98:CD:1F:FD:70";//7B:FE:F6:55:67:3A";//76:48:26:2F:C8:7D";//43.2A.95.1F.A7.D4";//74:26:32:D4:81:18";// "61:0B:FE:DA:BA:19"; //"6F:1B:88:86:E9:42";//BE:7D:2F:58:CA";"65:1F:29:7F:5E:45";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        btnScanBLE = (Button)findViewById(R.id.btnScanBLE);
        lvBLE = (ListView)findViewById(R.id.lvBLE);

        for(j=0; j<SIZE_RESP; j++)
            arrayResp[j] = "Default";

        j=0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                /*settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();*/
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                        //.setCallbackType(CALLBACK_TYPE_FIRST_MATCH)
                        .build();
                filters = new ArrayList<ScanFilter>();
                //ScanFilter fltr = new ScanFilter.Builder().setDeviceAddress("71:F7:E5:47:23:1D").build();
                //filters.add(fltr);
            }

            btnScanBLE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    for(j=0; j<SIZE_RESP; j++)
                        arrayResp[j] = "Default";
                    j = 0;
                    scanLeDevice(true);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy() {
        if (mGatt == null) {
            return;
        }
        mGatt.close();
        mGatt = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLEScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);
            mLEScanner.startScan(filters, settings, mScanCallback);
        } else
            mLEScanner.stopScan(mScanCallback);
    }


    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            BluetoothDevice btDevice = result.getDevice();

            if(j == SIZE_RESP)
                return;

            if(!(result.equals(null))) {
                /*if (result.getDevice().getName() == null){
                    if(result.getDevice().getUuids() != null)
                        arrayResp[j++] = "UUID: " + result.getDevice().getUuids().toString();
                    else
                        arrayResp[j++] = "DEVICE: " + result.getDevice().toString();// + " UUID: " + result.getDevice().getUuids().toString();
                }
                else
                    arrayResp[j++] = "DEVICE NAME: " + result.getDevice().getName().toString();*/

                int i= 0;
                dup = false;
                for(i = 0; i<j; i++)
                {
                    if(arrayResp[i].equals("DEVICE: " + result.getDevice().toString()))
                        dup = true;
                }
                if(!dup) {
                    arrayResp[j++] = "DEVICE: " + result.getDevice().toString();
                    }

                adapterArray = new ArrayAdapter(activityBLE.this, android.R.layout.simple_list_item_1, arrayResp);
                lvBLE.setAdapter(adapterArray);

                if(result.getDevice().toString().equals(IBEACON)) {
                    newDev = result.getDevice();
                    connectToDevice(newDev);

                    Toast.makeText(getApplicationContext(), newDev.toString(), Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(activityBLE.this, "RESULT NULL", Toast.LENGTH_SHORT).show();
            //connectToDevice(btDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Toast.makeText(activityBLE.this, "in 2", Toast.LENGTH_SHORT).show();
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    // Not required for api level > 21
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            mGatt = device.connectGatt(this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            Log.i("SERVICES; ", "Number of Services " + services.size());

            for(j=0; j< services.size(); j++){
                Log.i("SERVICE; ", services.get(j).toString());
                List<BluetoothGattCharacteristic> chars = services.get(j).getCharacteristics();
                for(int c=0; c < chars.size(); c++) {
                    Log.i("   CHARACTERISTIC : ", chars.get(c).toString());
                    Log.i("   UUID : ", chars.get(c).getUuid().toString());
                    Log.i("   PROPERTIES : ", ""+ chars.get(c).getProperties());
                    Log.i("   VALUE : ", ""+ chars.get(c).getValue());



                    //gatt.readCharacteristic(chars.get(c));
                }
            }
            //gatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            Log.i("GATT STATUS", ""+status);
            //gatt.disconnect();
        }
    };
}
