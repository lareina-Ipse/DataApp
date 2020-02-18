package kr.co.chience.dataapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

public class DataAdapter extends BaseAdapter {

    private Vector<Data> mItems;
    private LayoutInflater mInflater;

    public DataAdapter(Vector<Data> mItems, LayoutInflater mInflater) {
        this.mItems = mItems;
        this.mInflater = mInflater;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScanViewHolder scanViewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_data, parent, false);
            scanViewHolder = new ScanViewHolder();
            scanViewHolder.name = convertView.findViewById(R.id.textview_name);
            scanViewHolder.address = convertView.findViewById(R.id.textview_address);
            scanViewHolder.rssi = convertView.findViewById(R.id.textview_rssi);
            scanViewHolder.uuid = convertView.findViewById(R.id.textview_uuid);
            convertView.setTag(scanViewHolder);
        } else {
            scanViewHolder = (ScanViewHolder) convertView.getTag();
        }

        scanViewHolder.name.setText("Name :" + mItems.get(position).getName());
        scanViewHolder.address.setText("Address :" + mItems.get(position).getAddress());
        scanViewHolder.rssi.setText("Nssi :" + mItems.get(position).getRssi());
        scanViewHolder.uuid.setText("UUID :" + mItems.get(position).getUuid());

        return convertView;
    }

    public class ScanViewHolder {
        public TextView name;
        public TextView address;
        public TextView rssi;
        public TextView uuid;
    }

}
