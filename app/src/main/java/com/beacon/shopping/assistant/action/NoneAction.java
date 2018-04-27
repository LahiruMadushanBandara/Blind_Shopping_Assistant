

package com.beacon.shopping.assistant.action;

import android.content.Context;
import android.content.Intent;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.NotificationAction;
import com.beacon.shopping.assistant.util.Constants;

/**
 * Created by lahiru on 10/04/2018.
 */
public class NoneAction extends Action {

    protected final String param;
    protected final NotificationAction notification;

    public NoneAction(String param, NotificationAction notification) {
        this.param = param;
        this.notification = notification;
    }


    @Override
    public String execute(Context context) {
        if (isParamRequired() && isParamEmpty()) {
            return context.getString(R.string.action_action_param_is_required);
        }
        //empty
        sendAlarm(context);
        return null;
    }

    @Override
    public boolean isParamRequired() {
        return false;
    }

    protected void sendAlarm(Context context) {
        if (isNotificationRequired()) {
            Intent newIntent = new Intent(Constants.ALARM_NOTIFICATION_SHOW);
            newIntent.putExtra("NOTIFICATION", notification);
            context.sendBroadcast(newIntent);
        }
    }

    protected boolean isParamEmpty() {
        return param == null || param.isEmpty();
    }

    protected boolean isNotificationRequired() {
        return notification != null && notification.isEnabled();
    }

    @Override
    public String toString() {
        return "NoneAction, action: " + param;
    }
}