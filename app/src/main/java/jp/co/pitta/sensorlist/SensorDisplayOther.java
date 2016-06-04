package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by shingo on 2016/06/02.
 */
public class SensorDisplayOther extends SensorDisplayBase {

    Activity mActivity;
    ViewGroup mViewGroup;
    TableRow mTimestampTableRow;
    TableRow mSensorDataTableRow[];
    boolean initFlag;

    @Override
    public void setUI(Activity activity) {
        mActivity  = activity;
        mViewGroup = (ViewGroup)mActivity.findViewById(R.id.tableLayout);

        mActivity.getLayoutInflater().inflate(R.layout.table_raw, mViewGroup);
        mTimestampTableRow = (TableRow)mViewGroup.getChildAt(0);

        ((TextView)mTimestampTableRow.getChildAt(0)).setText("timestamp");
        ((TextView)mTimestampTableRow.getChildAt(2)).setText("ns");

        initFlag = false;
    }

    @Override
    public void display(SensorEvent event){

        if(initFlag == false) {
            mSensorDataTableRow = new TableRow[event.values.length];
            for(int i = 0; i < event.values.length; i++){
                mActivity.getLayoutInflater().inflate(R.layout.table_raw, mViewGroup);
                mSensorDataTableRow[i] = (TableRow)mViewGroup.getChildAt(i + 1);
                String str = "values["+ String.valueOf(i) + "]";
                ((TextView)mSensorDataTableRow[i].getChildAt(0)).setText(str);
            }

            initFlag = true;
        }

        ((TextView)mTimestampTableRow.getChildAt(1)).setText(String.valueOf(event.timestamp));
        for(int i = 0; i < event.values.length; i++){
            ((TextView)mSensorDataTableRow[i].getChildAt(1)).setText(String.format("%.5f", event.values[i]));
        }

    }

    @Override
    public void display(TriggerEvent event){}
}
