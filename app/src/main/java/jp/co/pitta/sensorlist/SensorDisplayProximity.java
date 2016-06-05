package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by shingo on 2016/06/02.
 */
public class SensorDisplayProximity extends SensorDisplayBase {

    Activity mActivity;
    ViewGroup mViewGroup;
    TableRow mTimestampTableRow;
    TableRow mSensorDataTableRow[];

    @Override
    public void setUI(Activity activity) {

        mActivity  = activity;
        mViewGroup = (ViewGroup)mActivity.findViewById(R.id.tableLayout);

        mActivity.getLayoutInflater().inflate(R.layout.table_raw, mViewGroup);
        mTimestampTableRow = (TableRow)mViewGroup.getChildAt(0);

        ((TextView)mTimestampTableRow.getChildAt(0)).setText("timestamp");
        ((TextView)mTimestampTableRow.getChildAt(2)).setText("ns");

        String values[] = {"values[0]"};
        String unit[]   = {"cm"};

        mSensorDataTableRow = new TableRow[values.length];

        for(int i = 0; i < values.length; i++) {
            mActivity.getLayoutInflater().inflate(R.layout.table_raw, mViewGroup);
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
