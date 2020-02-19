package kr.co.chience.dataapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

<<<<<<< HEAD
import kr.co.chience.dataapp.R;
import kr.co.chience.dataapp.model.Data;

public class DataAdapter extends BaseAdapter {
=======
import kr.co.chience.dataapp.model.Data;
import kr.co.chience.dataapp.R;

public class DataAdapter extends BaseAdapter {

>>>>>>> origin/master
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
            scanViewHolder.cdc = convertView.findViewById(R.id.textview_cdc);
            scanViewHolder.mic = convertView.findViewById(R.id.textview_mic);
            scanViewHolder.voc = convertView.findViewById(R.id.textview_voc);
            scanViewHolder.co2 = convertView.findViewById(R.id.textview_co2);
            scanViewHolder.temp = convertView.findViewById(R.id.textview_temp);
            scanViewHolder.att = convertView.findViewById(R.id.textview_att);
            scanViewHolder.humInt = convertView.findViewById(R.id.textview_humInt);
            scanViewHolder.humDec = convertView.findViewById(R.id.textview_humDec);
            convertView.setTag(scanViewHolder);
        } else {
            scanViewHolder = (ScanViewHolder) convertView.getTag();
        }

        scanViewHolder.cdc.setText("CDC :" + mItems.get(position).getCdc());
        scanViewHolder.mic.setText("MIC :" + mItems.get(position).getMic());
        scanViewHolder.voc.setText("VOC :" + mItems.get(position).getVoc());
        scanViewHolder.co2.setText("CO2 :" + mItems.get(position).getCo2());
        scanViewHolder.temp.setText("TEMP :" + mItems.get(position).getTemp());
        scanViewHolder.att.setText("ATT :" + mItems.get(position).getAtt());
        scanViewHolder.humInt.setText("HUM1 :" + mItems.get(position).getHumInt());
        scanViewHolder.humDec.setText("HUM2 :" + mItems.get(position).getHumDec());
<<<<<<< HEAD

=======
>>>>>>> origin/master
        return convertView;
    }

    public class ScanViewHolder {
        public TextView cdc;
        public TextView mic;
        public TextView voc;
        public TextView co2;
        public TextView temp;
        public TextView att;
        public TextView humInt;
        public TextView humDec;
    }

<<<<<<< HEAD

=======
>>>>>>> origin/master
}
