

package com.beacon.shopping.assistant.ui.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beacon.shopping.assistant.BeaconLocatorApp;
import com.beacon.shopping.assistant.data.DataManager;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.util.Constants;

/**
 * Created by lahiru on 20/12/2017.
 */
public abstract class PageBeaconFragment extends PreferenceFragmentCompat {

    protected DataManager mDataManager;
    protected ActionBeacon mActionBeacon;
    protected int mPage;

    abstract public void onCreatePreferences(Bundle savedInstanceState, String rootKey);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = BeaconLocatorApp.from(getActivity()).getComponent().dataManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);

        readArguments();
        setData();

        return fragmentView;
    }

    protected abstract void setData();

    protected void readArguments() {
        if (getArguments() != null) {
            mPage = getArguments().getInt(Constants.ARG_PAGE);
            mActionBeacon = getArguments().getParcelable(Constants.ARG_ACTION_BEACON);
        }
    }

    protected boolean updateActionBeacon() {
        return mDataManager.updateBeaconAction(mActionBeacon);
    }


}
