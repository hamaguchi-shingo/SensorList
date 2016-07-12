package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shingo on 2016/06/10.
 */
public class FragmentSensorList extends Fragment {

    private View mView;
    private FragmentSensorListener mListener;
    private ListView mListView;
    private List<Sensor> mSensorTypeList;

    private int mSamplingTime;
    private String mSamplingTimeUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (FragmentSensorListener) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sensor_list, container, false);

        mSamplingTime = getArguments().getInt("samplingTime");
        mSamplingTimeUnit = getArguments().getString("unitTime");

        ArrayList<SensorTitle> list = new ArrayList<>();
        SensorTitleAdapter adapter = new SensorTitleAdapter(mView.getContext().getApplicationContext());
        adapter.setTitleList(list);

        mListView = (ListView) mView.findViewById(R.id.listView);
        ;
        mListView.setAdapter(adapter);

        SensorManager sensorManager = (SensorManager) mView.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensorTypeList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<Integer> idList = new ArrayList<>();

        for (int i = 0; i < mSensorTypeList.size(); i++) {
            SensorTitle sensorTitle = new SensorTitle();
            sensorTitle.setTitle(mSensorTypeList.get(i).getName());
            list.add(sensorTitle);
            idList.add(mSensorTypeList.get(i).getType());
            adapter.notifyDataSetChanged();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView listView = (ListView) parent;
                SensorTitle item = (SensorTitle) listView.getItemAtPosition(position);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragmentSensorDisplay = new FragmentSensorDisplay();

                Bundle args = new Bundle();
                args.putString("sensorName", item.getTitle());
                fragmentSensorDisplay.setArguments(args);
                mListener.onFragmentSensorDisplayEvent(item.getTitle());

                args.putInt("samplingTime", mSamplingTime);
                args.putString("unitTime", mSamplingTimeUnit);

                fragmentSensorDisplay.setArguments(args);

                transaction.replace(R.id.fragment_container, fragmentSensorDisplay, FragmentSensorDisplay.class.getName());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return mView;
    }

}
