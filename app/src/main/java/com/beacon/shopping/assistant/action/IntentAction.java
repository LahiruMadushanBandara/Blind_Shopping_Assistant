

package com.beacon.shopping.assistant.action;

import android.content.Context;
import android.content.Intent;

import com.beacon.shopping.assistant.model.NotificationAction;

/**
 * Created by lahiru on 10/04/2018.
 */
public class IntentAction extends NoneAction {


    public IntentAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        Intent newIntent = new Intent(param);
        context.sendBroadcast(newIntent);
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "IntentAction, action: " + param;
    }
}