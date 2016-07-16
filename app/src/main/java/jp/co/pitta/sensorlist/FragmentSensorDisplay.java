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
    SensorManager mSensorManager;
    Sensor mSensor;
    //SensorDisplayBase mSensorDisplay;
    SensorDisplay mSensorDisplay;

    private FragmentSensorListener mListener;
    FragmentSensorDisplay value;
    TriggerEventListener mTriggerListener = new TriggerListener();

    private int mSamplingTime;
    private String mSamplingTimeUnit;

    private int mDelayTime;
    private String mDelayTimeUnit;


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

        mSensorDisplay = new SensorDisplay(mView, inflater);


        int type = 0;
        String strType = getArguments().getString("sensorName");
        mSamplingTime = getArguments().getInt("samplingTime");
        mSamplingTimeUnit = getArguments().getString("unitTime");

        mDelayTime = getArguments().getInt("delayTime");
        mDelayTimeUnit = getArguments().getString("unitDelayTime");

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

        setUnitTable(type);
        // mSensorDisplay = getSensorDisplay(type);
        //mSensorDisplay.setUI(mView, mInflater);

        int sensorDelay;
        int sensorBatchDelay;

        if (mSamplingTimeUnit.equals("msec")) {
            sensorDelay = mSamplingTime * 1000;
        } else {
            sensorDelay = mSamplingTime * 1000 * 1000;
        }

        if (mDelayTimeUnit.equals("msec")) {
            sensorBatchDelay = mDelayTime * 1000;
        } else {
            sensorBatchDelay = mDelayTime * 1000 * 1000;
        }

        if (mSensor.getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
            mSensorManager.requestTriggerSensor(mTriggerListener, mSensor);
        } else {
            mSensorManager.registerListener(this, mSensor, sensorDelay, sensorBatchDelay);
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

    void setUnitTable(int type) {
        mSensorDisplay.setUnitTimestamp("ns");
        mSensorDisplay.init();
        switch (type) {

            case Sensor.TYPE_ACCELEROMETER:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis");
                mSensorDisplay.setUnitName("m/s^2", "m/s^2", "m/s^2");
                break;

            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                break;

            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                break;

            case Sensor.TYPE_GRAVITY:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis");
                mSensorDisplay.setUnitName("m/s^2", "m/s^2", "m/s^2");
                break;

            case Sensor.TYPE_GYROSCOPE:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis");
                mSensorDisplay.setUnitName("rad/s", "rad/s", "rad/s");
                break;

            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis", "x-drift", "y-drift", "z-drift");
                mSensorDisplay.setUnitName("rad/s", "rad/s", "rad/s", "rad/s", "rad/s", "rad/s");
                break;

            case Sensor.TYPE_LIGHT:
                mSensorDisplay.setUnitTitleName("value[0]");
                mSensorDisplay.setUnitName("lux");
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis");
                mSensorDisplay.setUnitName("m/s^2", "m/s^2", "m/s^2");
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis");
                mSensorDisplay.setUnitName("uT", "uT", "uT");
                break;

            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                mSensorDisplay.setUnitTitleName("x-axis", "y-axis", "z-axis", "x-bias", "y-bias", "z-bias");
                mSensorDisplay.setUnitName("uT", "uT", "uT", "", "", "");
                break;

            case Sensor.TYPE_PRESSURE:
                mSensorDisplay.setUnitTitleName("value[0]");
                mSensorDisplay.setUnitName("hPa");
                break;

            case Sensor.TYPE_PROXIMITY:
                mSensorDisplay.setUnitTitleName("value[0]");
                mSensorDisplay.setUnitName("cm");
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                break;

            case Sensor.TYPE_SIGNIFICANT_MOTION:
                break;

            case Sensor.TYPE_STEP_COUNTER:
                break;

            case Sensor.TYPE_STEP_DETECTOR:
                break;

            default:
                break;
        }
    }

}
