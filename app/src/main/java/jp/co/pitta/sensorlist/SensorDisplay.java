package jp.co.pitta.sensorlist;

import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by shingo on 2016/07/09.
 */
public class SensorDisplay {

    ViewGroup mViewGroup;
    TableRow mTimestampTableRow;
    TableRow mSensorDataTableRow[] = null;
    LayoutInflater mInflater;

    public SensorDisplay( View view, LayoutInflater inflater ) {
        mViewGroup = (ViewGroup)view.findViewById(R.id.tableLayout);
        mInflater  = inflater;
    }

    public void init() {
        mSensorDataTableRow = null;
    }

    public void setUnitTimestamp( String timestampUnit ) {
        mInflater.inflate(R.layout.table_sensor_raw, mViewGroup);
        mTimestampTableRow = (TableRow)mViewGroup.getChildAt(0);

        ((TextView)mTimestampTableRow.getChildAt(0)).setText("timestamp");
        ((TextView)mTimestampTableRow.getChildAt(2)).setText(timestampUnit);
    }

    public void setUnitTitleName( String ...args ) {

        if( mSensorDataTableRow == null ) {
            mSensorDataTableRow = new TableRow[args.length];
        }

        for( int i = 0; i < args.length; i++ ) {
            mInflater.inflate(R.layout.table_sensor_raw, mViewGroup);
            mSensorDataTableRow[i] = (TableRow) mViewGroup.getChildAt(i + 1);
            ((TextView) mSensorDataTableRow[i].getChildAt(0)).setText(args[i]);
        }

    }

    public void setUnitName( String ...args ) {

        if( mSensorDataTableRow == null ) {
            mSensorDataTableRow = new TableRow[args.length];
        }

        for( int i = 0; i < args.length; i++ ) {
            mInflater.inflate(R.layout.table_sensor_raw, mViewGroup);
            mSensorDataTableRow[i] = (TableRow) mViewGroup.getChildAt(i + 1);
            ((TextView) mSensorDataTableRow[i].getChildAt(2)).setText(args[i]);
        }

    }

    public void display(SensorEvent event){

        if(mSensorDataTableRow == null) {
            mSensorDataTableRow = new TableRow[event.values.length];
            for(int i = 0; i < event.values.length; i++){
                mInflater.inflate(R.layout.table_sensor_raw, mViewGroup);
                mSensorDataTableRow[i] = (TableRow)mViewGroup.getChildAt(i + 1);
                String str = "values["+ String.valueOf(i) + "]";
                ((TextView)mSensorDataTableRow[i].getChildAt(0)).setText(str);
            }
        }

        ((TextView)mTimestampTableRow.getChildAt(1)).setText(String.valueOf(event.timestamp));
        for(int i = 0; i < mSensorDataTableRow.length; i++){
            ((TextView)mSensorDataTableRow[i].getChildAt(1)).setText(String.format("%.5f", event.values[i]));
        }

    }

    public void display(TriggerEvent event){
        if(mSensorDataTableRow == null) {
            mSensorDataTableRow = new TableRow[event.values.length];
            for(int i = 0; i < event.values.length; i++){
                mInflater.inflate(R.layout.table_sensor_raw, mViewGroup);
                mSensorDataTableRow[i] = (TableRow)mViewGroup.getChildAt(i + 1);
                String str = "values["+ String.valueOf(i) + "]";
                ((TextView)mSensorDataTableRow[i].getChildAt(0)).setText(str);
            }
        }

        ((TextView)mTimestampTableRow.getChildAt(1)).setText(String.valueOf(event.timestamp));
        for(int i = 0; i < event.values.length; i++){
            ((TextView)mSensorDataTableRow[i].getChildAt(1)).setText(String.format("%.5f", event.values[i]));
        }
    }
}
