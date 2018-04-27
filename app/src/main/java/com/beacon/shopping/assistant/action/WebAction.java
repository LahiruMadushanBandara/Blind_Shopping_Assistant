

package com.beacon.shopping.assistant.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.NotificationAction;

/**
 * Created by lahiru on 03/01/2018.
 */
public class WebAction extends NoneAction {

    public WebAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        try {
            Uri uri = Uri.parse(param);
            Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        } catch (Exception e) {
            return context.getString(R.string.action_urlaction_error);
        }
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "WebAction, url: " + param;
    }
}
