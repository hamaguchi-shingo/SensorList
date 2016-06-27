package jp.co.pitta.sensorlist;

import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by shingo on 2016/06/02.
 */
abstract class SensorDisplayBase {
    abstract void setUI(  View mView,  LayoutInflater inflater );
    abstract void display( SensorEvent event );
    abstract void display(TriggerEvent event );
}
