package jp.co.pitta.sensorlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shingo on 2016/06/06.
 */
public class SensorTitleAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;

    ArrayList<SensorTitle> titleList;

    public SensorTitleAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTitleList(ArrayList<SensorTitle> titleList){
        this.titleList = titleList;
    }
    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public Object getItem(int position) {
        return titleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.sensor_title,parent,false);

        ((TextView)convertView.findViewById(R.id.sensor_tiltle)).setText(titleList.get(position).getTitle());

        return convertView;
    }
}
