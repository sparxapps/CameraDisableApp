package com.cameradisable;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CheckBox checkbox;
    TextView remove;
    DevicePolicyManager dPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            dPM = getDevicePolicyManager(MainActivity.this);
            checkbox = (CheckBox) findViewById(R.id.checkbox);
            remove = (TextView) findViewById(R.id.remove);

            if (!dPM.isAdminActive(getAdminName(this))) {
                // try to become active – must happen here in this activity, to get result
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        getAdminName(this));
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Using for enable/disable the camera.");
                startActivityForResult(intent, 100);
               
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dPM.removeActiveAdmin(getAdminName(MainActivity.this));
                    BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver(MainActivity.this);

                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
                    registerReceiver(broadcastReceiver, intentFilter);
                }
            });

            //dPM.setApplicationHidden(getAdminName(this),getPackageName(), true);


            checkbox.setChecked(dPM.getCameraDisabled(getAdminName(MainActivity.this)));

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        dPM.setCameraDisabled(getAdminName(MainActivity.this), true);
                    } else {
                        dPM.setCameraDisabled(getAdminName(MainActivity.this), false);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private ComponentName getActiveComponentName(DevicePolicyManager mDevicePolicyManager) {

        ComponentName componentName = null;

        List<ComponentName> activeComponentList = mDevicePolicyManager.getActiveAdmins();

        Iterator<ComponentName> iterator = activeComponentList.iterator();

        while (iterator.hasNext()) {

            componentName = (ComponentName) iterator.next();
        }
        return componentName;
    }

    /**
     * Sets whether the device's camera is disabled
     *
     * @param disable Set true to disable devices camera, false to enable device's
     *                camera
     */
    public void disableCamera(Context context, boolean disable) {
        DevicePolicyManager dPM = getDevicePolicyManager(context);
        ComponentName DeviceAdmin = getAdminName(context);
        if (dPM.isAdminActive(DeviceAdmin)) {
            boolean isCameraDisabled = dPM.getCameraDisabled(DeviceAdmin);
            // If the camera isn't already disabled and the user wants to
            // disable the camera (disable is true), disable the device's camera
            if (!isCameraDisabled && disable) {
                dPM.setCameraDisabled(DeviceAdmin, disable);
            }
            // If the camera is already disabled and the user wants to enable
            // the camera (disable is false), enable the device's camera
            if (isCameraDisabled && !disable) {
                dPM.setCameraDisabled(DeviceAdmin, disable);
            }
        }
    }


    private DevicePolicyManager getDevicePolicyManager(Context context) {
        DevicePolicyManager dPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dPM;
    }

    /**
     * @return Returns the DeviceAdminReceiver's component name
     */
    private ComponentName getAdminName(Context context) {
        ComponentName DeviceAdmin = new ComponentName(context,
                MyDevicePolicyReceiver.class);
        return DeviceAdmin;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                /////do something
                // dPM.setCameraDisabled(getAdminName(MainActivity.this), true);
            }
        }
    }
}
