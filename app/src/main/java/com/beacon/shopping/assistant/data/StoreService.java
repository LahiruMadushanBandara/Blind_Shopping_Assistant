

package com.beacon.shopping.assistant.data;

import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.model.TrackedBeacon;

import java.util.List;

/**
 * Created by lahiru on 20/12/2017.
 */
public interface StoreService {

    boolean createBeacon(final TrackedBeacon beacon);

    boolean updateBeacon(final TrackedBeacon beacon);

    boolean deleteBeacon(final String id, boolean cascade);

    TrackedBeacon getBeacon(final String id);

    List<TrackedBeacon> getBeacons();

    boolean updateBeaconDistance(final String id, double distance);

    boolean updateBeaconAction(ActionBeacon beacon);

    boolean createBeaconAction(ActionBeacon beacon);

    List<ActionBeacon> getBeaconActions(final String beaconId);

    boolean deleteBeaconAction(final int id);

    boolean deleteBeaconActions(final String beaconId);

    List<ActionBeacon> getAllEnabledBeaconActions();

    boolean updateBeaconActionEnable(final int id, boolean enable);

    List<ActionBeacon> getEnabledBeaconActionsByEvent(final int eventType, final String beaconId);

    boolean isBeaconExists(String id);
}
