

package com.beacon.shopping.assistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.beacon.shopping.assistant.BeaconLocatorApp;
import com.beacon.shopping.assistant.action.ActionExecutor;
import com.beacon.shopping.assistant.action.IAction;
import com.beacon.shopping.assistant.data.DataManager;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.model.RegionName;
import com.beacon.shopping.assistant.util.Constants;

import org.altbeacon.beacon.Region;

import java.util.List;


/**
 * Created by lahiru on 02/01/2018.
 */
public class BeaconRegionReceiver extends BroadcastReceiver {

    ActionExecutor mActionExecutor;
    DataManager mDataManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO
        if (intent.hasExtra("REGION")) {
            Region region = intent.getParcelableExtra("REGION");
            if (region != null) {
                RegionName regionName = RegionName.parseString(region.getUniqueId());

                mDataManager = BeaconLocatorApp.from(context).getComponent().dataManager();
                List<ActionBeacon> actions = mDataManager.getEnabledBeaconActionsByEvent(regionName.getEventType(), regionName.getBeaconId());
                if (actions.size() > 0) {

                    mActionExecutor = BeaconLocatorApp.from(context).getComponent().actionExecutor();
                    for (ActionBeacon actionBeacon : actions) {
                        // load action from db
                        IAction action = ActionExecutor.actionBuilder(actionBeacon.getActionType(), actionBeacon.getActionParam(), actionBeacon.getNotification());
                        if (action != null) {
                            String resMessage = mActionExecutor.storeAndExecute(action);
                            if (resMessage != null ) {
                                Toast.makeText(context, resMessage, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.w(Constants.TAG, "Action not found for " + actionBeacon);
                        }
                    }
                }
            }
        }
    }

}
