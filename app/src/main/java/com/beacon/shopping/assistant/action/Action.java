

package com.beacon.shopping.assistant.action;

import android.content.Context;

/**
 * Created by lahiru on 10/04/2018.
 */
abstract class Action implements IAction {
    @Override
    abstract public String execute(Context context);

}