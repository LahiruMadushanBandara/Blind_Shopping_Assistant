

package com.beacon.shopping.assistant.action;

import android.content.Context;
import android.media.AudioManager;

import com.beacon.shopping.assistant.model.NotificationAction;
import com.beacon.shopping.assistant.util.PreferencesUtil;

/**
 * Created by lahiru on 03/01/2018.
 */
public class SilentOffAction extends NoneAction {


    public SilentOffAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        int old_mode = PreferencesUtil.getSilentModeProfile(context);
        if (ringerMode != AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        return super.execute(context);
    }

    @Override
    public String toString() {
        return "SilentOffAction, param: " + param;
    }
}
