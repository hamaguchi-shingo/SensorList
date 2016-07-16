package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentSensorListener, FragmentNumberDialog.OnNumberDialogDoneListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private boolean mStateSensorListFlag;

    private int mSamplingTime;
    private String mSamplingTimeUnit;

    private int mDelayTime;
    private String mDelayTimeUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSamplingTime = 1;
        mSamplingTimeUnit = "sec";

        mDelayTime = 0;
        mDelayTimeUnit = "sec";

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mToggle = new ActionBarDrawerToggle(
                MainActivity.this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) MainActivity.this);

        FragmentSensorList fragmentSensorList = new FragmentSensorList();
        Bundle args = new Bundle();
        //args.putInt("samplingTime2", mSelectSamplingTime);
        args.putInt("samplingTime", mSamplingTime);
        args.putString("unitTime", mSamplingTimeUnit);
        args.putInt("delayTime", mDelayTime);
        args.putString("unitDelayTime", mDelayTimeUnit);

        fragmentSensorList.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentSensorList, FragmentSensorList.class.getName());
        transaction.commit();

        mStateSensorListFlag = true;
        mToolbar.setTitle("Sensor List");
    }

    @Override
    public void onDone(String title, String unit, int value) {

        Bundle args;
        FragmentSensorList fragmentSensorList;

        if (title.equals("samplingTime")) {
            mSamplingTime = value;
            if (unit.equals("0") == true) {
                mSamplingTimeUnit = "msec";
            } else {
                mSamplingTimeUnit = "sec";
            }

            fragmentSensorList = new FragmentSensorList();
            args = new Bundle();
            //args.putInt("samplingTime2", mSelectSamplingTime);

        } else {
            mDelayTime = value;
            if (unit.equals("0") == true) {
                mDelayTimeUnit = "msec";
            } else {
                mDelayTimeUnit = "sec";
            }

            fragmentSensorList = new FragmentSensorList();
            args = new Bundle();
            //args.putInt("samplingTime2", mSelectSamplingTime);

        }

        args.putInt("samplingTime", mSamplingTime);
        args.putString("unitTime", mSamplingTimeUnit);
        args.putInt("delayTime", mDelayTime);
        args.putString("unitDelayTime", mDelayTimeUnit);

        fragmentSensorList.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentSensorList, FragmentSensorList.class.getName());
        transaction.commit();

    }

    @Override
    public void onFragmentSensorListEvent() {
        mToolbar.setTitle("Sensor List");
        mStateSensorListFlag = true;
    }

    @Override
    public void onFragmentSensorDisplayEvent(String sensorName) {
        mToggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle(sensorName);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggle.syncState();
        mStateSensorListFlag = false;
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggle.setDrawerIndicatorEnabled(true);
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mToggle = new ActionBarDrawerToggle(
                        MainActivity.this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                mToggle.syncState();
                mNavigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) MainActivity.this);

                FragmentSensorList fragmentSensorList = new FragmentSensorList();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                //args.putInt("samplingTime2", mSelectSamplingTime);
                args.putInt("samplingTime", mSamplingTime);
                args.putString("unitTime", mSamplingTimeUnit);
                args.putInt("delayTime", mDelayTime);
                args.putString("unitDelayTime", mDelayTimeUnit);

                fragmentSensorList.setArguments(args);
                transaction.replace(R.id.fragment_container, fragmentSensorList, FragmentSensorList.class.getName());
                transaction.commit();

                mStateSensorListFlag = true;
                mToolbar.setTitle("Sensor List");
            }
        });
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mStateSensorListFlag == false) {
                //super.onBackPressed();

                FragmentSensorList fragmentSensorList = new FragmentSensorList();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                //args.putInt("samplingTime2", mSelectSamplingTime);
                args.putInt("samplingTime", mSamplingTime);
                args.putString("unitTime", mSamplingTimeUnit);
                args.putInt("delayTime", mDelayTime);
                args.putString("unitDelayTime", mDelayTimeUnit);
                fragmentSensorList.setArguments(args);
                transaction.replace(R.id.fragment_container, fragmentSensorList, FragmentSensorList.class.getName());
                transaction.commit();

                mToolbar.setTitle("Sensor List");
                mToggle.setDrawerIndicatorEnabled(true);
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mToggle = new ActionBarDrawerToggle(
                        MainActivity.this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                mToggle.syncState();
                mNavigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) MainActivity.this);
                mStateSensorListFlag = true;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            FragmentNumberDialog myDiag = FragmentNumberDialog.newInstance(5, mSamplingTime, mSamplingTimeUnit, "samplingTime");
            myDiag.show(getFragmentManager(), "Diag");
        } else if (id == R.id.nav_batch) {
            FragmentNumberDialog myDiag = FragmentNumberDialog.newInstance(5, mDelayTime, mDelayTimeUnit, "delayTime");
            myDiag.show(getFragmentManager(), "Diag");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
