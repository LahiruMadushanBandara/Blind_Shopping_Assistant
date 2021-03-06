

package com.beacon.shopping.assistant.action;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.NotificationAction;

import java.util.List;

/**
 * Created by lahiru on 03/01/2018.
 */
public class StartAppAction extends NoneAction {


    public StartAppAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        try {
            if (!launchApp(context, param)) {
                openApp(context, param);
            }
        } catch (Exception e) {
            return context.getString(R.string.action_start_application_error);
        }
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "StartAppAction, app_package: " + param;
    }

    private boolean launchApp(Context context, String packageName) {

        final PackageManager manager = context.getPackageManager();
        final Intent appLauncherIntent = new Intent(Intent.ACTION_MAIN);
        appLauncherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(appLauncherIntent, 0);
        if ((null != resolveInfos) && (!resolveInfos.isEmpty())) {
            for (ResolveInfo rInfo : resolveInfos) {
                String className = rInfo.activityInfo.name.trim();
                String targetPackageName = rInfo.activityInfo.packageName.trim();
                if (packageName.trim().equals(targetPackageName)) {
                    Intent intent = new Intent();
                    intent.setClassName(targetPackageName, className);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }
}
