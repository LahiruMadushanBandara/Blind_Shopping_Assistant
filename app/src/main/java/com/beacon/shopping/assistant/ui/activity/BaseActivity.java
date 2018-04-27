/*

 *
 */

package com.beacon.shopping.assistant.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.beacon.shopping.assistant.BeaconLocatorApp;
import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.util.Constants;
import com.beacon.shopping.assistant.util.PreferencesUtil;

import java.util.Locale;


/**
 * Created by lahiru on 19/02/18.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    public static int glCount = 0;
    String text;
    TextToSpeech tts;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
                return true;
            case R.id.action_settings:
                launchSettingsActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showNotification()  {
        tts= new TextToSpeech(this,this);
        tts.setLanguage(Locale.US);
        tts.setPitch((float) 0.6);
        tts.setSpeechRate(1);
    }

    private void speakOut() {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    //txt
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "This language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                speakOut();
            }
        } else {
            Toast.makeText(this, "This language is not supported", Toast.LENGTH_SHORT).show();
        }
    }




    protected void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, Constants.REQ_GLOBAL_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQ_GLOBAL_SETTING) {
            //TODO settings
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    protected Fragment checkFragmentInstance(int id, Object instanceClass) {

        Fragment fragment = getFragmentInstance(id);
        if (fragment != null && instanceClass.equals(fragment.getClass())) {
            return fragment;
        }
        return null;
    }

    protected Fragment getFragmentInstance(int id) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment fragment = fragmentManager.findFragmentById(id);
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        glCount++;
        if (glCount == 1) {
            BeaconLocatorApp.from(this).enableBackgroundScan(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        glCount--;
        if (glCount <= 0) {
            BeaconLocatorApp.from(this).enableBackgroundScan(PreferencesUtil.isBackgroundScan(this));
        }
    }
}