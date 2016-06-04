package jp.co.megachips.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private final int maxSizeOfdisplayData = 1;

    private Spinner mSensorDelaySpinner;
    private Spinner[] mSensorNameSpinner     = new Spinner[maxSizeOfdisplayData];
    private TextView[] mSensorValueTextView  = new TextView[maxSizeOfdisplayData];

    private Button mSensorControlButton;

    private SensorManager mSensorManager;
    private List<Sensor> mSensorTypeList;
    private Sensor[] mSensor = new Sensor[maxSizeOfdisplayData];
    private final TriggerEventListener mTriggerListener = new TriggerListener();

    private String[] mInputSensorTypeName = new String[maxSizeOfdisplayData];
    private String mInputSensorDelay;

    private LinearLayout mSensorDisplayLayout;
    private Activity mActivity;
    private SensorDisplayBase mDisplay;

    String[] getListSensorTypeName(List<Sensor> sensorTypeList) {
        List<String> str = new ArrayList<>();

        for(int i = 0; i < sensorTypeList.size(); i++) {
            str.add(i, sensorTypeList.get(i).getStringType());
        }
        Collections.sort(str);
        str.add(0, "null");
        return str.toArray(new String[0]);
    }

    String[] getListSensorDelayName() {
        String[] str = {"Normal", "UI", "Game", "Fastest"};

        return str;
    }
    void addSpinnerMenu(Spinner spinner, String[] str) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void changeLayoutFile(String sensorType) {
        mSensorDisplayLayout.removeAllViews();

        switch(sensorType) {
            case Sensor.STRING_TYPE_ACCELEROMETER :
                getLayoutInflater().inflate(R.layout.android_sensor_accelerometer, mSensorDisplayLayout);
                mDisplay = new SensorDisplayAccel();
                break;
            case Sensor.STRING_TYPE_GAME_ROTATION_VECTOR :
                getLayoutInflater().inflate(R.layout.android_sensor_game_rotation_vector, mSensorDisplayLayout);
                mDisplay = new SensorDisplayGameRotationVector();
                break;
            case Sensor.STRING_TYPE_GEOMAGNETIC_ROTATION_VECTOR :
                getLayoutInflater().inflate(R.layout.android_sensor_geomagnetic_rotation_vector, mSensorDisplayLayout);
                mDisplay = new SensorDisplayGeomagneticRotationVector();
                break;
            case Sensor.STRING_TYPE_GRAVITY :
                getLayoutInflater().inflate(R.layout.android_sensor_gravity, mSensorDisplayLayout);
                mDisplay = new SensorDisplayGravity();
                break;
            case Sensor.STRING_TYPE_GYROSCOPE :
                getLayoutInflater().inflate(R.layout.android_sensor_gyroscope, mSensorDisplayLayout);
                mDisplay = new SensorDisplayGyro();
                break;
            case Sensor.STRING_TYPE_GYROSCOPE_UNCALIBRATED :
                getLayoutInflater().inflate(R.layout.android_sensor_gyroscope_uncalibrated, mSensorDisplayLayout);
                mDisplay = new SensorDisplayGyroUncalib();
                break;
            case Sensor.STRING_TYPE_LIGHT :
                getLayoutInflater().inflate(R.layout.android_sensor_light, mSensorDisplayLayout);
                mDisplay = new SensorDisplayLight();
                break;
            case Sensor.STRING_TYPE_LINEAR_ACCELERATION :
                getLayoutInflater().inflate(R.layout.android_sensor_linear_acceleration, mSensorDisplayLayout);
                mDisplay = new SensorDisplayLinearAccel();
                break;
            case Sensor.STRING_TYPE_MAGNETIC_FIELD :
                getLayoutInflater().inflate(R.layout.android_sensor_magnetic_field, mSensorDisplayLayout);
                mDisplay = new SensorDisplayMag();
                break;
            case Sensor.STRING_TYPE_MAGNETIC_FIELD_UNCALIBRATED :
                getLayoutInflater().inflate(R.layout.android_sensor_magnetic_field_uncalibrated, mSensorDisplayLayout);
                mDisplay = new SensorDisplayMagUncalib();
                break;
            case Sensor.STRING_TYPE_PRESSURE :
                getLayoutInflater().inflate(R.layout.android_sensor_pressure, mSensorDisplayLayout);
                mDisplay = new SensorDisplayPressure();
                break;
            case Sensor.STRING_TYPE_PROXIMITY :
                getLayoutInflater().inflate(R.layout.android_sensor_proximity, mSensorDisplayLayout);
                mDisplay = new SensorDisplayProximity();
                break;
            case Sensor.STRING_TYPE_ROTATION_VECTOR :
                getLayoutInflater().inflate(R.layout.android_sensor_rotation_vector, mSensorDisplayLayout);
                mDisplay = new SensorDisplayRotationVector();
                break;
            case Sensor.STRING_TYPE_SIGNIFICANT_MOTION :
                getLayoutInflater().inflate(R.layout.android_sensor_significant_motion, mSensorDisplayLayout);
                mDisplay = new SensorDisplaySignificantMotion();
                break;
            case Sensor.STRING_TYPE_STEP_COUNTER :
                getLayoutInflater().inflate(R.layout.android_sensor_step_counter, mSensorDisplayLayout);
                mDisplay = new SensorDisplayStepCounter();
                break;
            case Sensor.STRING_TYPE_STEP_DETECTOR :
                getLayoutInflater().inflate(R.layout.android_sensor_step_detector, mSensorDisplayLayout);
                mDisplay = new SensorDisplayStepDetector();
                break;
            default:
                getLayoutInflater().inflate(R.layout.other_sensor, mSensorDisplayLayout);
                mDisplay = new SensorDisplayOther();
                break;

        }

        mDisplay.setUI(mActivity);

    }

    Sensor searchSensor(String sensorTypeName, List<Sensor> sensorList) {
        Sensor sensor = null;

        for(int i = 0; i < sensorList.size(); i++) {
            if(sensorTypeName.equals(sensorList.get(i).getStringType())) {
                sensor = sensorList.get(i);
            }
        }

        return sensor;
    }

    int searchSensorDelay(String sensorDelay) {
        int delay = 0;

        if(sensorDelay.equals("Normal")) {
            delay = SensorManager.SENSOR_DELAY_NORMAL;
        } else if(sensorDelay.equals("UI")) {
            delay = SensorManager.SENSOR_DELAY_UI;
        } else if(sensorDelay.equals("Game")) {
            delay = SensorManager.SENSOR_DELAY_GAME;
        } else if(sensorDelay.equals("Fastest")) {
            delay = SensorManager.SENSOR_DELAY_FASTEST;
        }

        return delay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorDelaySpinner  = (Spinner)findViewById(R.id.spinner_sensor_delay);

        mSensorNameSpinner[0] = (Spinner)findViewById(R.id.spinner_sensor_name_0);

        mSensorValueTextView[0] = (TextView)findViewById(R.id.text_view_sensor_data_0);

        mSensorControlButton = (Button)findViewById(R.id.button_sensor_control);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensorTypeList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        mSensorDisplayLayout =  (LinearLayout)findViewById(R.id.layout_display_data);

        mActivity = this;

        for(int i = 0; i < maxSizeOfdisplayData; i++) {
            addSpinnerMenu(mSensorNameSpinner[i], getListSensorTypeName(mSensorTypeList));
        }

        addSpinnerMenu(mSensorDelaySpinner, getListSensorDelayName());

        for(int i = 0; i < maxSizeOfdisplayData; i++) {
            final int selectNumber = i;

            mSensorNameSpinner[i].setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Spinner spinner = (Spinner) arg0;
                    changeLayoutFile((String) spinner.getSelectedItem());
                    mInputSensorTypeName[selectNumber] = (String) spinner.getSelectedItem();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        mSensorDelaySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Spinner spinner = (Spinner) arg0;
                mInputSensorDelay = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        mSensorControlButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSensorControlButton.getText().equals(getString(R.string.sensor_start))) {
                    mSensorControlButton.setText(getString(R.string.sensor_stop));

                    for(int i = 0; i < maxSizeOfdisplayData; i++) {
                        mSensor[i] = searchSensor(mInputSensorTypeName[i], mSensorTypeList);

                        if (mSensor[i] != null) {
                            if (mSensor[i].getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
                                mSensorManager.requestTriggerSensor(mTriggerListener, mSensor[i]);
                            } else {
                                int sensorDelay = searchSensorDelay(mInputSensorDelay);
                                mSensorManager.registerListener(MainActivity.this, mSensor[i], sensorDelay);
                            }
                        }
                    }

                    for(int i = 0; i < maxSizeOfdisplayData; i++) {
                        mSensorValueTextView[i].setText("");
                    }

                } else {
                    mSensorControlButton.setText(getString(R.string.sensor_start));

                    for(int i = 0; i < maxSizeOfdisplayData; i++) {
                        if (mSensor[i] != null) {
                            if (mSensor[i].getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
                                mSensorManager.cancelTriggerSensor(mTriggerListener, mSensor[i]);
                            } else {
                                mSensorManager.unregisterListener(MainActivity.this, mSensor[i]);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mDisplay.display(event);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class TriggerListener extends TriggerEventListener {
        public void onTrigger(TriggerEvent event) {
            mDisplay.display(event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
