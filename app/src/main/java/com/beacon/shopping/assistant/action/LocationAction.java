

package com.beacon.shopping.assistant.action;


import android.content.Context;
import android.content.Intent;

import com.beacon.shopping.assistant.model.NotificationAction;
import com.beacon.shopping.assistant.util.Constants;

/**
 * Created by lahiru on 10/04/2018.
 */
public class LocationAction extends NoneAction {

    public LocationAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        Intent newIntent = new Intent(Constants.GET_CURRENT_LOCATION);
        context.sendBroadcast(newIntent);
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return false;
    }

    @Override
    public String toString() {
        return "LocationAction, param: " + param;
    }
}
