

package com.beacon.shopping.assistant.model;

import java.util.List;

/**
 * Created by lahiru on 29/12/2017.
 */
public interface ITriggerable {
    List<ActionBeacon> getActions();

    void addAction(ActionBeacon action);

    void addActions(List<ActionBeacon> actions);
}
