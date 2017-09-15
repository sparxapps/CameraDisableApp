package com.cameradisable;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sis116 on 01-Sep-17.
 */

public class MyDevicePolicyReceiver extends DeviceAdminReceiver {

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "disabled dpm", Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "enabled dpm", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "disable dpm request", Toast.LENGTH_SHORT).show();
        return super.onDisableRequested(context, intent);
    }


    private DevicePolicyManager getDevicePolicyManager(Context context) {
        DevicePolicyManager dPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dPM;
    }

    private ComponentName getAdminName(Context context) {
        ComponentName DeviceAdmin = new ComponentName(context,
                MyDevicePolicyReceiver.class);
        return DeviceAdmin;
    }
}