

package com.beacon.shopping.assistant.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.beacon.shopping.assistant.R;


/**
 * Created by lahiru on 18/02/18.
 */
public final class DialogBuilder {

    private DialogBuilder() {
    }

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

}
