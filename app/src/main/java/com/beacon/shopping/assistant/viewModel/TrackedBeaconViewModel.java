

package com.beacon.shopping.assistant.viewModel;

import android.support.annotation.NonNull;
import android.view.View;

import com.beacon.shopping.assistant.model.IManagedBeacon;
import com.beacon.shopping.assistant.ui.fragment.BeaconFragment;
import com.beacon.shopping.assistant.ui.fragment.TrackedBeaconsFragment;

/**
 * Created by lahiru on 11/03/18.
 */
public class TrackedBeaconViewModel extends BeaconViewModel {

    public TrackedBeaconViewModel(@NonNull BeaconFragment fragment, @NonNull IManagedBeacon managedBeacon) {
        super(fragment, managedBeacon);
    }

    public View.OnClickListener onClickBeaconDelete() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrackedBeaconsFragment) mFragment).removeBeacon(mManagedBeacon.getId());
            }
        };
    }

    public View.OnClickListener onClickBeaconAdd() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrackedBeaconsFragment) mFragment).newBeaconAction(mManagedBeacon.getId());
            }
        };
    }


}
