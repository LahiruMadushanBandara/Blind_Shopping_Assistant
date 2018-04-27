

package com.beacon.shopping.assistant.viewModel;

import android.support.annotation.NonNull;

import com.beacon.shopping.assistant.model.IManagedBeacon;
import com.beacon.shopping.assistant.model.TrackedBeacon;
import com.beacon.shopping.assistant.ui.fragment.BeaconFragment;

/**
 * Created by lahiru on 11/03/18.
 */
public class DetectedBeaconViewModel extends BeaconViewModel {

    public DetectedBeaconViewModel(@NonNull BeaconFragment fragment, @NonNull IManagedBeacon managedBeacon) {
        super(fragment, managedBeacon);
    }

    protected void clickBeacon() {
        mFragment.selectBeacon(new TrackedBeacon(mManagedBeacon));
    }
}
