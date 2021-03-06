/*

 *
 */

package com.beacon.shopping.assistant.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.view.View;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.ui.activity.BeaconActionActivity;
import com.beacon.shopping.assistant.ui.fragment.TrackedBeaconsFragment;
import com.beacon.shopping.assistant.util.BeaconUtil;
import com.beacon.shopping.assistant.util.Constants;

/**
 * Created by lahiru on 19/02/18.
 */
public class ActionBeaconViewModel extends BaseObservable {

    protected ActionBeacon mActionBeacon;
    protected TrackedBeaconsFragment mFragment;

    public ActionBeaconViewModel(@NonNull TrackedBeaconsFragment fragment, @NonNull ActionBeacon actionBeacon) {
        this.mActionBeacon = actionBeacon;
        this.mFragment = fragment;
    }

    public String getName() {
        return mActionBeacon.getName();
    }

    public String getEventName() {
        return mFragment.getString(BeaconUtil.getEventTypeResourceId(mActionBeacon.getEventType()));
    }

    public String getActionNames() {
        switch (mActionBeacon.getActionType()) {
            case ACTION_WEB:
                return mFragment.getString(R.string.mv_action_type_open_web);
            case ACTION_URL:
                return mFragment.getString(R.string.mv_action_type_open_url);
            case ACTION_INTENT_ACTION:
                return mFragment.getString(R.string.mv_action_type_broadcast_intent);
            case ACTION_START_APP:
                return mFragment.getString(R.string.mv_action_type_start_app);
            case ACTION_GET_LOCATION:
                return mFragment.getString(R.string.mv_action_type_get_location);
            case ACTION_SET_SILENT_ON:
                return mFragment.getString(R.string.mv_action_type_set_silent_on);
            case ACTION_SET_SILENT_OFF:
                return mFragment.getString(R.string.mv_action_type_set_silent_off);
            case ACTION_TASKER:
                return mFragment.getString(R.string.mv_action_type_tasker);
        }
        return mFragment.getString(R.string.mv_action_type_none);
    }

    public boolean isEnabled() {
        return mActionBeacon.isEnabled();
    }

    public String getEnableStatus() {
        return mActionBeacon.isEnabled() ? mFragment.getString(R.string.mv_action_status_enable) : mFragment.getString(R.string.mv_action_status_disabled);
    }

    public View.OnClickListener onClickEdit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActionDetailsActivity();
            }
        };
    }

    protected void launchActionDetailsActivity() {
        Intent intent = BeaconActionActivity.getStartIntent(mFragment.getActivity());
        intent.putExtra(Constants.ARG_ACTION_BEACON, mActionBeacon);
        mFragment.startActivityForResult(intent, Constants.REQ_UPDATED_ACTION_BEACON);
    }


    public View.OnClickListener onClickDelete() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.removeBeaconAction(mActionBeacon.getBeaconId(), mActionBeacon.getId());
            }
        };
    }

    public View.OnClickListener onClickEnable() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.enableBeaconAction(mActionBeacon.getBeaconId(), mActionBeacon.getId(), !mActionBeacon.isEnabled());
            }
        };
    }
}
