package jp.co.megachips.sensorlist;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;

/**
 * Created by shingo on 2016/06/02.
 */
abstract class SensorDisplayBase {
    abstract void setUI( Activity activity );
    abstract void display( SensorEvent event );
    abstract void display(TriggerEvent event );
}
