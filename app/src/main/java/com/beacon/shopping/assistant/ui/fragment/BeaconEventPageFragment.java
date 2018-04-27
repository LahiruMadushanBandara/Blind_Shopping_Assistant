

package com.beacon.shopping.assistant.ui.fragment;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.ActionBeacon;

/**
 * Created by lahiru on 20/12/2017.
 */
public class BeaconEventPageFragment extends PageBeaconFragment {


    public static BeaconEventPageFragment newInstance(int page) {
        BeaconEventPageFragment detailFragment = new BeaconEventPageFragment();
        return detailFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.preferences_beacon_event);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        String key = preference.getKey();
        if (key.equals("be_event_enter_region")) {
            mActionBeacon.setEventType(ActionBeacon.EventType.EVENT_ENTERS_REGION);
        } else if (key.equals("be_event_leaves_region")) {
            mActionBeacon.setEventType(ActionBeacon.EventType.EVENT_LEAVES_REGION);
        } else {
            mActionBeacon.setEventType(ActionBeacon.EventType.EVENT_NEAR_YOU);
        }

        setData();

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    protected void setData() {
        switch (mActionBeacon.getEventType()) {
            case EVENT_ENTERS_REGION:
                ((CheckBoxPreference) findPreference("be_event_enter_region")).setChecked(true);
                ((CheckBoxPreference) findPreference("be_event_leaves_region")).setChecked(false);
                ((CheckBoxPreference) findPreference("be_event_near_you")).setChecked(false);
                break;
            case EVENT_LEAVES_REGION:
                ((CheckBoxPreference) findPreference("be_event_leaves_region")).setChecked(true);
                ((CheckBoxPreference) findPreference("be_event_enter_region")).setChecked(false);
                ((CheckBoxPreference) findPreference("be_event_near_you")).setChecked(false);
                break;
            default:
                ((CheckBoxPreference) findPreference("be_event_near_you")).setChecked(true);
                ((CheckBoxPreference) findPreference("be_event_leaves_region")).setChecked(false);
                ((CheckBoxPreference) findPreference("be_event_enter_region")).setChecked(false);
        }
    }

}
