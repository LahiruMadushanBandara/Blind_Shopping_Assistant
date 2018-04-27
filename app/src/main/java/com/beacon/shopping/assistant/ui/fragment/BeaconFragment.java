

package com.beacon.shopping.assistant.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.beacon.shopping.assistant.BuildConfig;
import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.TrackedBeacon;
import com.beacon.shopping.assistant.ui.activity.MainNavigationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by lahiru on 8/12/2017.
 */
public class BeaconFragment extends Fragment {

    protected boolean mNeedFab;
    protected Unbinder unbinder;
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }
    private OnTrackedBeaconSelectedListener mBeaconSelectedListener;

    public interface OnTrackedBeaconSelectedListener {
        void onBeaconSelected(TrackedBeacon beacon);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainNavigationActivity) {
            ((MainNavigationActivity) getActivity()).swappingFloatingIcon();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mBeaconSelectedListener = (OnTrackedBeaconSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTrackedBeaconSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof MainNavigationActivity) {
            if (mNeedFab) {
                ((MainNavigationActivity) getActivity()).swappingFabUp();
            } else {
                ((MainNavigationActivity) getActivity()).hideFab();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof MainNavigationActivity && mNeedFab) {
            ((MainNavigationActivity) getActivity()).swappingFabAway();
        }
    }

    public void setNeedFab(boolean mNeedFab) {
        this.mNeedFab = mNeedFab;
    }

    public class EmptyView {

        @BindView(R.id.empty_text)
        TextView text;

        public EmptyView(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void selectBeacon(TrackedBeacon trackedBeacon) {
        if (mBeaconSelectedListener != null) {
            mBeaconSelectedListener.onBeaconSelected(trackedBeacon);
        }
    }

}