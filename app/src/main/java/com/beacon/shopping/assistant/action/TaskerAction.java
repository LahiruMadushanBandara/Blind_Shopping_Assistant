

package com.beacon.shopping.assistant.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.NotificationAction;
import com.beacon.shopping.assistant.util.Constants;

import net.dinglisch.android.tasker.TaskerIntent;

/**
 * Created by lahiru on 03/01/2018.
 */
public class TaskerAction extends NoneAction {


    public TaskerAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {

        switch (TaskerIntent.testStatus(context)) {
            case OK:
                final TaskerIntent newIntent = new TaskerIntent(param);
                final BroadcastReceiver broadcastRec = new BroadcastReceiver() {
                    @Override
                    public void onReceive(final Context context, final Intent recIntent) {
                        if (recIntent.getBooleanExtra(TaskerIntent.EXTRA_SUCCESS_FLAG, false)) {

                        }
                        context.unregisterReceiver(this);
                    }
                };
                context.registerReceiver(broadcastRec, newIntent.getCompletionFilter());
                context.sendBroadcast(newIntent);
                break;
            case NotEnabled:
                Log.w(Constants.TAG, "Tasker is not enabled");
                return context.getString(R.string.tasker_disabled);
            case AccessBlocked:
                Log.w(Constants.TAG, "Taskers access is blocked");
                return context.getString(R.string.tasker_external_access_denided);
            case NotInstalled:
                Log.w(Constants.TAG, "Tasker is not installed");
                return context.getString(R.string.tasker_not_installed);
            default:
                break;
        }
       return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "TaskerAction, param(s): " + param;
    }
}
