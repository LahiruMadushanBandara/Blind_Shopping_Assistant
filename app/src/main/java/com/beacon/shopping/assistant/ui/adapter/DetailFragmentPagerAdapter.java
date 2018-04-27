

package com.beacon.shopping.assistant.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.ui.fragment.BeaconActionPageFragment;
import com.beacon.shopping.assistant.ui.fragment.BeaconDetailPageFragment;
import com.beacon.shopping.assistant.ui.fragment.BeaconEventPageFragment;
import com.beacon.shopping.assistant.ui.fragment.BeaconNotificationPageFragment;
import com.beacon.shopping.assistant.ui.fragment.PageBeaconFragment;
import com.beacon.shopping.assistant.util.Constants;

/**
 * Created by lahiru on 25/12/2017.
 */
public class DetailFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private int tabTitleResources[] = new int[]{R.string.tab_title_beacon_info, R.string.tab_title_beacon_event, R.string.tab_title_beacon_action,
            R.string.tab_title_beacon_notification};
    private Context mContext;
    private ActionBeacon mActionBeacon;

    public DetailFragmentPagerAdapter(FragmentManager fm, ActionBeacon beacon, Context context) {
        super(fm);
        this.mContext = context;
        this.mActionBeacon = beacon;
    }

    @Override
    public int getCount() {
        return tabTitleResources.length;
    }

    @Override
    public PageBeaconFragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PAGE, position + 1);

        PageBeaconFragment frg = BeaconDetailPageFragment.newInstance(position + 1);
        switch (position) {
            case 0:
                frg = BeaconDetailPageFragment.newInstance(position + 1);
                break;
            case 1:
                frg = BeaconEventPageFragment.newInstance(position + 1);
                break;
            case 2:
                frg = BeaconActionPageFragment.newInstance(position + 1);
                break;
            case 3:
                frg = BeaconNotificationPageFragment.newInstance(position + 1);
                break;
        }

        if (mActionBeacon != null) {
            args.putParcelable(Constants.ARG_ACTION_BEACON, mActionBeacon);
            frg.setArguments(args);
        }
        return frg;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mContext.getString(tabTitleResources[position]);
    }
}
