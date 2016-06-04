package jp.co.megachips.sensorlist;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.widget.TextView;

/**
 * Created by shingo on 2016/06/02.
 */
public class SensorDisplayGyroUncalib extends SensorDisplayBase {

    TextView timestampTextView;
    TextView valueTextView[] = new TextView[6];

    @Override
    public void setUI(Activity activity) {
        timestampTextView = (TextView)activity.findViewById(R.id.gyroUncalibTimestampTextView);

        valueTextView[0] = (TextView)activity.findViewById(R.id.gyroUncalibTextView0);
        valueTextView[1] = (TextView)activity.findViewById(R.id.gyroUncalibTextView1);
        valueTextView[2] = (TextView)activity.findViewById(R.id.gyroUncalibTextView2);
        valueTextView[3] = (TextView)activity.findViewById(R.id.gyroUncalibTextView3);
        valueTextView[4] = (TextView)activity.findViewById(R.id.gyroUncalibTextView4);
        valueTextView[5] = (TextView)activity.findViewById(R.id.gyroUncalibTextView5);
    }

    @Override
    public void display(SensorEvent event){
        timestampTextView.setText(String.valueOf(event.timestamp));
        for(int i = 0; i < valueTextView.length; i++) {
            valueTextView[i].setText(String.format("%.5f", event.values[i]));
        }
    }

    @Override
    public void display(TriggerEvent event){}
}
