package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by shingo on 2016/06/02.
 */
public class SensorDisplayGyro extends SensorDisplayBase {

    ViewGroup mViewGroup;
    TableRow mTimestampTableRow;
    TableRow mSensorDataTableRow[];

    @Override
    public void setUI(View view, LayoutInflater inflater) {

        mViewGroup = (ViewGroup)view.findViewById(R.id.tableLayout);

        inflater.inflate(R.layout.table_sensor_raw, mViewGroup);
        mTimestampTableRow = (TableRow)mViewGroup.getChildAt(0);

        ((TextView)mTimestampTableRow.getChildAt(0)).setText("timestamp");
        ((TextView)mTimestampTableRow.getChildAt(2)).setText("ns");

        String values[] = {"x-axis", "y-axis", "z-axis"};
        String unit[]   = {"rad/s", "rad/s", "rad/s"};

        mSensorDataTableRow = new TableRow[values.length];

        for(int i = 0; i < values.length; i++) {
            inflater.inflate(R.layout.table_sensor_raw, mViewGroup);
            mSensorDataTableRow[i] = (TableRow) mViewGroup.getChildAt(i + 1);
            ((TextView) mSensorDataTableRow[i].getChildAt(0)).setText(values[i]);
            ((TextView) mSensorDataTableRow[i].getChildAt(2)).setText(unit[i]);
        }

    }

    @Override
    public void display(SensorEvent event){
        ((TextView)mTimestampTableRow.getChildAt(1)).setText(String.valueOf(event.timestamp));
        for(int i = 0; i < mSensorDataTableRow.length; i++){
            ((TextView)mSensorDataTableRow[i].getChildAt(1)).setText(String.format("%.5f", event.values[i]));
        }
    }

    @Override
    public void display(TriggerEvent event){}
}
