package kr.co.chience.dataapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import java.util.List;
import java.util.Vector;

import kr.co.chience.dataapp.adapter.DataAdapter;
import kr.co.chience.dataapp.model.Data;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    Button button_start, button_stop;

    Vector<Data> datas;
    DataAdapter dataAdapter;
    ListView listView;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothGatt mBluetoothGatt;
    BluetoothDevice mBluetoothDevice;

    String proximity = "aabbccdd";
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_start = findViewById(R.id.button_start);
        button_stop = findViewById(R.id.button_end);
        listView = findViewById(R.id.listview);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        datas = new Vector<>();

        button_start.setOnClickListener(this);
        button_stop.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                startScan();
                break;
            case R.id.button_end:
                stopScan();
                break;
        }
    }

    private void startScan() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode((ScanSettings.SCAN_MODE_LOW_LATENCY));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
        }
        ScanSettings settings = builder.build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner.startScan(null, settings, mScanCallback);
        }
    }

    private void stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
    }


    /*Scanning*/
    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //BLE 알림이 발견되면 콜백
            try {
                ScanRecord scanRecord = result.getScanRecord();
                processScan(result.getDevice(), scanRecord.getBytes(), result.getRssi());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            //배치 결과가 전달 될 때 콜백
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            //스캔을 시작 할 수 없을 때의 콜백
        }
    };

    /*Name, Address, Rssi, UUID */
    private String processScan(BluetoothDevice device, final byte[] scanRecord, final int rssi) {
        List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

        String deviceUuid = null;
        String deviceName = device.getName();
        String deviceAddress = device.getAddress();
        String deviceRssi = String.valueOf(rssi);

        for (ADStructure structure : structures) {
            if (structure instanceof IBeacon) {
                IBeacon iBeacon = (IBeacon) structure;
                deviceUuid = iBeacon.getUUID().toString();

                if (deviceUuid.startsWith(proximity)) {
                    if (listView.getCount() == 0) {
                        addValue(deviceUuid);
                    } else {
                        setValue(deviceUuid);
                    }
                }

            }

        }
        return deviceAddress;
    }

    /*ListView Add*/
    private void addValue(String uuid) {

        String cdc, mic, voc, co2, temp, att = null, humInt, humDec;

        cdc = hexStringToInteger(uuid.substring(9, 11));
        mic = hexStringToInteger(uuid.substring(11, 13));
        voc = hexStringToInteger(uuid.substring(14, 16)) + hexStringToInteger(uuid.substring(16, 18));
        co2 = hexStringToInteger(uuid.substring(19, 21)) + hexStringToInteger(uuid.substring(21, 23));
        temp = hexStringToInteger(uuid.substring(24, 26));

        if (uuid.substring(26, 28).equals("ff")) {
            att = "-" + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32));
        } else if (uuid.substring(26, 28).equals("0")){
            att = "+" + hexStringToInteger(uuid.substring(26, 28)) + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32));
        }

        humInt = hexStringToInteger(uuid.substring(32, 34));
        humDec = hexStringToInteger(uuid.substring(34, 36));

        datas.add(0, new Data(cdc, mic, voc, co2, temp, att, humInt, humDec));
        dataAdapter = new DataAdapter(datas, getLayoutInflater());
        listView.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void setValue(String uuid) {

        String cdc, mic, voc, co2, temp, att = null, humInt, humDec;

        cdc = hexStringToInteger(uuid.substring(9, 11));
        mic = hexStringToInteger(uuid.substring(11, 13));
        voc = hexStringToInteger(uuid.substring(14, 16)) + hexStringToInteger(uuid.substring(16, 18));
        co2 = hexStringToInteger(uuid.substring(19, 21)) + hexStringToInteger(uuid.substring(21, 23));
        temp = hexStringToInteger(uuid.substring(24, 26));

        if (uuid.substring(26, 28).equals("ff")) {
            att = "-" + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32));
        } else if (uuid.substring(26, 28).equals("0")){
            att = "+" + hexStringToInteger(uuid.substring(26, 28)) + hexStringToInteger(uuid.substring(28, 30)) + hexStringToInteger(uuid.substring(30, 32));
        }

        humInt = hexStringToInteger(uuid.substring(32, 34));
        humDec = hexStringToInteger(uuid.substring(34, 36));

        datas.set(0, new Data(cdc, mic, voc, co2, temp, att, humInt, humDec));
        dataAdapter = new DataAdapter(datas, getLayoutInflater());
        listView.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    public String hexStringToInteger(String hex) {
        return String.valueOf(Integer.parseInt(hex, 16));
    }


}
