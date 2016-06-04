package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.widget.TextView;

/**
 * Created by shingo on 2016/06/02.
 */
public class SensorDisplaySignificantMotion extends SensorDisplayBase {

    TextView timestampTextView;
    TextView valueTextView[] = new TextView[1];

    @Override
    public void setUI(Activity activity) {
        timestampTextView = (TextView)activity.findViewById(R.id.significantMotionTimestampTextView);

        valueTextView[0] = (TextView)activity.findViewById(R.id.significantMotionTextView0);
    }

    @Override
    public void display(SensorEvent event){}

    @Override
    public void display(TriggerEvent event){
        timestampTextView.setText(String.valueOf(event.timestamp));
        for(int i = 0; i < valueTextView.length; i++) {
            valueTextView[i].setText(String.format("%.5f", event.values[i]));
        }
    }
}
