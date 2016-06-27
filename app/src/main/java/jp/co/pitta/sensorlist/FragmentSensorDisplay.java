package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by shingo on 2016/06/10.
 */
public class FragmentSensorDisplay extends Fragment implements SensorEventListener {

    View mView;
    LayoutInflater mInflater;
    SensorManager mSensorManager;
    Sensor mSensor;
    SensorDisplayBase mSensorDisplay;
    private FragmentSensorListener mListener;
    FragmentSensorDisplay value;
    TriggerEventListener mTriggerListener = new TriggerListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        value = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.table_sensor, container, false);
        mView.setFocusableInTouchMode(true);
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mListener.onFragmentSensorListEvent();
                    return true;
                }
                return false;
            }
        });

        mInflater = inflater;

        int type = 0;
        String strType = getArguments().getString("sensorName");
        int selectItem = getArguments().getInt("samplingTime");

        SensorManager sensorManager = (SensorManager) mView.getContext().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorTypeList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        mSensor = null;
        for (int i = 0; i < sensorTypeList.size(); i++) {
            if (strType.equals(sensorTypeList.get(i).getName())) {
                mSensor = sensorTypeList.get(i);
                type = sensorTypeList.get(i).getType();
                break;
            }
        }

        mSensorDisplay = getSensorDisplay(type);
        mSensorDisplay.setUI(mView, mInflater);

        int sensorDelay;
        switch (selectItem) {
            case 0:
                sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
                break;
            case 1:
                sensorDelay = SensorManager.SENSOR_DELAY_UI;
                break;
            case 2:
                sensorDelay = SensorManager.SENSOR_DELAY_GAME;
                break;
            case 3:
                sensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
                break;
            default:
                sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
                break;
        }

        if (mSensor.getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
            mSensorManager.requestTriggerSensor(mTriggerListener, mSensor);
        } else {
            mSensorManager.registerListener(this, mSensor, sensorDelay);
        }

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSensor.getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
            mSensorManager.cancelTriggerSensor(mTriggerListener, mSensor);
        } else {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mSensorDisplay.display(event);
    }

    class TriggerListener extends TriggerEventListener {
        public void onTrigger(TriggerEvent event) {
            mSensorDisplay.display(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    SensorDisplayBase getSensorDisplay(int type) {

        SensorDisplayBase sensorDisplay;

        switch (type) {

            case Sensor.TYPE_ACCELEROMETER:
                sensorDisplay = new SensorDisplayAccel();
                break;

            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                sensorDisplay = new SensorDisplayGameRotationVector();
                break;

            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                sensorDisplay = new SensorDisplayGeomagneticRotationVector();
                break;

            case Sensor.TYPE_GRAVITY:
                sensorDisplay = new SensorDisplayGravity();
                break;

            case Sensor.TYPE_GYROSCOPE:
                sensorDisplay = new SensorDisplayGyro();
                break;

            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                sensorDisplay = new SensorDisplayGyroUncalib();
                break;

            case Sensor.TYPE_LIGHT:
                sensorDisplay = new SensorDisplayLight();
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensorDisplay = new SensorDisplayLinearAccel();
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorDisplay = new SensorDisplayMag();
                break;

            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                sensorDisplay = new SensorDisplayMagUncalib();
                break;

            case Sensor.TYPE_PRESSURE:
                sensorDisplay = new SensorDisplayPressure();
                break;

            case Sensor.TYPE_PROXIMITY:
                sensorDisplay = new SensorDisplayProximity();
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                sensorDisplay = new SensorDisplayRotationVector();
                break;

            case Sensor.TYPE_SIGNIFICANT_MOTION:
                sensorDisplay = new SensorDisplaySignificantMotion();
                break;

            case Sensor.TYPE_STEP_COUNTER:
                sensorDisplay = new SensorDisplayStepCounter();
                break;

            case Sensor.TYPE_STEP_DETECTOR:
                sensorDisplay = new SensorDisplayStepDetector();
                break;

            default:
                sensorDisplay = new SensorDisplayOther();
                break;
        }

        return sensorDisplay;
    }
}
