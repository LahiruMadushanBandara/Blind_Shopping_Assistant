

package com.beacon.shopping.assistant.viewModel;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.View;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.DetectedBeacon;
import com.beacon.shopping.assistant.model.IManagedBeacon;
import com.beacon.shopping.assistant.ui.fragment.BeaconFragment;
import com.beacon.shopping.assistant.util.BeaconUtil;
import android.speech.tts.TextToSpeech;

/**
 * Created by lahiru on 11/03/18.
 */
public class BeaconViewModel extends BaseObservable {

    protected IManagedBeacon mManagedBeacon;
    protected BeaconFragment mFragment;

    public BeaconViewModel(@NonNull BeaconFragment fragment, @NonNull IManagedBeacon managedBeacon) {
        this.mManagedBeacon = managedBeacon;
        this.mFragment = fragment;
    }

    public String getRssi() {
        return String.format("%d", mManagedBeacon.getRssi());
    }

    public String getTxPower() {
        return String.format("%d", mManagedBeacon.getTxPower());
    }

    public String getId() {
        return mManagedBeacon.getId();
    }

    public String getDistance() {
        return BeaconUtil.getRoundedDistanceString(mManagedBeacon.getDistance());
    }

    public String getName() {
        return (mManagedBeacon.getBluetoothName() == null || mManagedBeacon.getBluetoothName().isEmpty()) ? mManagedBeacon.getBluetoothAddress() :
                mManagedBeacon.getBluetoothName();
    }

    public String getSeenSince() {
        return DateUtils.getRelativeTimeSpanString(mManagedBeacon.getTimeLastSeen(), System.currentTimeMillis(), 0L).toString();
    }

    private boolean isLostBeacon() {
        return ((System.currentTimeMillis() - mManagedBeacon.getTimeLastSeen()) / 1000L > 5L);
    }

    public String getBeaconType() {
        return mManagedBeacon.getBeaconType().getString();
    }

    public String getProximity() {
        if (isLostBeacon()) {
            return getSeenSince();
        }
        return mFragment.getString(BeaconUtil.getProximityResourceId(BeaconUtil.getProximity(mManagedBeacon.getDistance())));
    }

    public int getProximityColor() {
        if (isLostBeacon()) {
            return ContextCompat.getColor(mFragment.getActivity(), R.color.hn_orange_dark);
        }
        return ContextCompat.getColor(mFragment.getActivity(), android.R.color.tab_indicator_text);
    }


    public int visibilityMajor() {
        return (mManagedBeacon.isEddyStoneURL()) ? View.GONE : View.VISIBLE;
    }

    public int visibilityMinor() {
        return (mManagedBeacon.isEddyStoneURL() || mManagedBeacon.isEddyStoneUID()) ? View.GONE : View.VISIBLE;
    }

    public String getUuid() {
        if (mManagedBeacon.getType() == DetectedBeacon.TYPE_EDDYSTONE_URL) {
            return mManagedBeacon.getEddystoneURL();
        }
        return mManagedBeacon.getUUID();
    }

    public String getMajor() {
        return mManagedBeacon.getMajor();
    }

    public String getMinor() {
        return mManagedBeacon.getMinor();
    }

    public String getNameUuid() {

        switch (mManagedBeacon.getType()) {
            case DetectedBeacon.TYPE_EDDYSTONE_URL:
                return mFragment.getString(R.string.mv_text_url);
            case DetectedBeacon.TYPE_EDDYSTONE_UID:
                return mFragment.getString(R.string.mv_text_namespace);
            default:
                return mFragment.getString(R.string.mv_text_uuid);
        }
    }

    public String getNameMajor() {

        switch (mManagedBeacon.getType()) {
            case DetectedBeacon.TYPE_EDDYSTONE_UID:
                return mFragment.getString(R.string.mv_text_instance);
            default:
                return mFragment.getString(R.string.mv_text_major);
        }
    }

    public String getNameMinor() {
        return mFragment.getString(R.string.mv_text_minor);
    }


    public View.OnClickListener onClickBeacon() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBeacon();
            }
        };
    }

    protected void clickBeacon() {
        // do abstract?
    }
}
