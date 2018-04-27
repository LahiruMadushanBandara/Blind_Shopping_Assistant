

package com.beacon.shopping.assistant.action;

import android.content.Context;

/**
 * Created by lahiru on 10/04/2018.
 */
public interface IAction {
    boolean isParamRequired();
    String execute(Context context);
}
