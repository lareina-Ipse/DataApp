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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

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

    String uuid;

    String cdc, mic, voc, co2, temp, att, hum;

    TextView textView_cdc, textView_mic, textView_voc, textView_co2, textView_temp, textView_att, textView_hum;

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
        listView.setOnItemClickListener(this);

        button_start.setOnClickListener(this);
        button_stop.setOnClickListener(this);

        textView_cdc = findViewById(R.id.textview_cdc);
        textView_mic = findViewById(R.id.textview_mic);
        textView_voc = findViewById(R.id.textview_voc);
        textView_co2 = findViewById(R.id.textview_co2);
        textView_temp = findViewById(R.id.textview_temp);
        textView_att = findViewById(R.id.textview_att);
        textView_hum = findViewById(R.id.textview_hum);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Data data = datas.get(position);
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(data.getAddress());
        message();
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

    public void message() {
        Log.e(TAG, "UUID ::::::: " + uuid);

        String cdc1, mic1, voc1, co1, temp1, att1, hum1;

        cdc1 = uuid.substring(9, 11);
        mic1 = uuid.substring(11, 13);
        voc1 = uuid.substring(14, 18);
        co1 = uuid.substring(19, 23);
        temp1 = uuid.substring(24, 26);
        att1 = uuid.substring(26, 32);
        hum1 = uuid.substring(32, 36);


        cdc = String.valueOf(Long.parseLong(uuid.substring(9, 11), 16));
        mic = String.valueOf(Long.parseLong(uuid.substring(11, 13), 16));
        voc = String.valueOf(Long.parseLong(uuid.substring(14, 16), 16)) + String.valueOf(Long.parseLong(uuid.substring(16, 18), 16));
        co2 = String.valueOf(Long.parseLong(uuid.substring(19, 21), 16)) + String.valueOf(Long.parseLong(uuid.substring(21, 23), 16));
        temp = String.valueOf(Long.parseLong(uuid.substring(24, 26), 16));
        att = String.valueOf(Long.parseLong(uuid.substring(26, 28), 16) + String.valueOf(Long.parseLong(uuid.substring(28, 30), 16) + String.valueOf(Long.parseLong(uuid.substring(30, 32), 16))));
        hum = String.valueOf(Long.parseLong(uuid.substring(32, 34), 16) + String.valueOf(Long.parseLong(uuid.substring(34, 36), 16)));


        textView_cdc.setText("조도: " + cdc1 + ", "+ cdc);
        textView_mic.setText("음량: " + mic1 + ", "+ mic);
        textView_voc.setText("유기화합물: " + voc1 + ", " + voc);
        textView_co2.setText("이산화탄소: " + co1 + ", " + co2);
        textView_temp.setText("온도: " + temp1 + ", " + temp);
        textView_att.setText("고도: " + att1 + ", " + att);
        textView_hum.setText("습도: " + hum1 + ", " +hum);


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

                if (listView.getCount() != 0) {
                    break;
                }

                if (deviceUuid.startsWith(proximity)) {
                    addValue(deviceName, deviceAddress, deviceRssi, deviceUuid);
                    Log.e(TAG, "Device Uuid" + deviceUuid);
                    uuid = deviceUuid;
                }

            }

        }
        return deviceAddress;
    }

    /*ListView Add*/
    private void addValue(String name, String address, String rssi, String UUID) {
        datas.add(0, new Data(name, address, rssi, UUID));
        dataAdapter = new DataAdapter(datas, getLayoutInflater());
        listView.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }


}
