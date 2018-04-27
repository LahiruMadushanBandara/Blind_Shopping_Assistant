/*
 *

 */

package com.beacon.shopping.assistant;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.beacon.shopping.assistant.data.DataManager;
import com.beacon.shopping.assistant.injection.component.ApplicationComponent;
import com.beacon.shopping.assistant.injection.component.DaggerApplicationComponent;
import com.beacon.shopping.assistant.injection.module.ApplicationModule;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.model.ActionRegion;
import com.beacon.shopping.assistant.model.IManagedBeacon;
import com.beacon.shopping.assistant.model.RegionName;
import com.beacon.shopping.assistant.model.TrackedBeacon;
import com.beacon.shopping.assistant.util.BeaconUtil;
import com.beacon.shopping.assistant.util.Constants;
import com.beacon.shopping.assistant.util.PreferencesUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * Created by lahiru on 18/02/18.
 */
public class BeaconLocatorApp extends Application implements BootstrapNotifier, RangeNotifier {

    ApplicationComponent applicationComponent;
    List<Region> mRegions = new ArrayList<>();
    List<TrackedBeacon> mBeacons = new ArrayList<>();
    private BackgroundPowerSaver mBackgroundPowerSaver;
    private BeaconManager mBeaconManager;
    private DataManager mDataManager;
    private RegionBootstrap mRegionBootstrap;

    TextToSpeech t1;

    public static BeaconLocatorApp from(@NonNull Context context) {
        return (BeaconLocatorApp) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mBeaconManager = BeaconLocatorApp.from(this).getComponent().beaconManager();
        mDataManager = BeaconLocatorApp.from(this).getComponent().dataManager();

        initBeaconManager();
        enableBackgroundScan(PreferencesUtil.isBackgroundScan(this));




        //text to speech
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



    }

    private void initBeaconManager() {
        mBeaconManager.setBackgroundMode(PreferencesUtil.isBackgroundScan(this));

        if (PreferencesUtil.isEddystoneLayoutUID(this)) {
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        }
        if (PreferencesUtil.isEddystoneLayoutURL(this)) {
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        }
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        //konkakt?
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconManager.setBackgroundBetweenScanPeriod(PreferencesUtil.getBackgroundScanInterval(this));

        mBeaconManager.setBackgroundScanPeriod(10000L);          // default is 10000L
        mBeaconManager.setForegroundBetweenScanPeriod(0L);      // default is 0L
        mBeaconManager.setForegroundScanPeriod(1100L);          // Default is 1100L

        //mBeaconManager.setMaxTrackingAge(10000);
        //mBeaconManager.setRegionExitPeriod(12000L);

        /*
        RangedBeacon.setMaxTrackingAge() only controls the period of time ranged beacons will continue to be
        returned after the scanning service stops detecting them.
        It has no affect on when monitored regions trigger exits. It is set to 5 seconds by default.

        Monitored regions are exited whenever a scan period finishes and the BeaconManager.setRegionExitPeriod()
        has passed since the last detection.
        By default, this is 10 seconds, but you can customize it.

        Using the defaults, the library will stop sending ranging updates five seconds after a beacon was last seen,
         and then send a region exit 10 seconds after it was last seen.
        You are welcome to change these two settings to meet your needs, but the BeaconManager.setRegionExitPeriod()
        should generally be the same or longer than the RangedBeacon.setMaxTrackingAge().
         */

        mBackgroundPowerSaver = new BackgroundPowerSaver(this);
        mBeaconManager.addRangeNotifier(this);

        try {
            if (mBeaconManager.isAnyConsumerBound()) {
                mBeaconManager.updateScanPeriods();
            }
        } catch (RemoteException e) {
            Log.e(Constants.TAG, "update scan periods error", e);
        }
    }





    public void enableBackgroundScan(boolean enable) {
        if (enable) {
            Log.d(Constants.TAG, "Enable Background Scan");
            enableRegions();
            //loadTrackedBeacons();
        } else {
            Log.d(Constants.TAG, "Disable Background Scan");
            disableRegions();
        }
    }

    private void disableRegions() {
        if (mRegionBootstrap != null) {
            mRegionBootstrap.disable();
        }
    }

    /**
     * consider to use as a cache of beacons
     */
    private void loadTrackedBeacons() {
        mBeacons = mDataManager.getAllBeacons();
    }

    private void enableRegions() {
        mRegions = getAllEnabledRegions();
        if (mRegions.size() > 0) {
            mRegionBootstrap = new RegionBootstrap(this, mRegions);
        } else {
            Log.d(Constants.TAG, "Ignore Background scan, no regions");
        }
    }

    public List<Region> getAllEnabledRegions() {
        List<Region> regions = new ArrayList<>();
        List<ActionBeacon> actions = mDataManager.getAllEnabledBeaconActions();
        for (ActionBeacon action : actions) {
            regions.add(ActionRegion.parseRegion(action));
        }
        return regions;
    }

    @Override
    public void didEnterRegion(Region region) {
        RegionName regName = RegionName.parseString(region.getUniqueId());

        if (regName.isApplicationRegion()) {
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_NEAR_YOU) {
                try {
                    mBeaconManager.startRangingBeaconsInRegion(region);

                } catch (RemoteException e) {
                    Log.e(Constants.TAG, "Error start ranging region: " + regName, e);
                }
            }
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_ENTERS_REGION) {
                Intent intent = new Intent();
                intent.setAction(Constants.NOTIFY_BEACON_ENTERS_REGION);
                intent.putExtra("REGION", (Parcelable)region);
                getApplicationContext().sendOrderedBroadcast(intent, null);
            }
        }
    }

    @Override
    public void didExitRegion(Region region) {

        RegionName regName = RegionName.parseString(region.getUniqueId());

        if (regName.isApplicationRegion()) {
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_NEAR_YOU) {
                try {
                    mBeaconManager.stopRangingBeaconsInRegion(region);

                    // set "far" proximity
                    mDataManager.updateBeaconDistance(regName.getBeaconId(), 99);
                } catch (RemoteException e) {
                    Log.e(Constants.TAG, "Error stop ranging region: " + regName, e);
                }
            }
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_LEAVES_REGION) {
                Intent intent = new Intent();
                intent.setAction(Constants.NOTIFY_BEACON_LEAVES_REGION);
                intent.putExtra("REGION", (Parcelable) region);
                getApplicationContext().sendOrderedBroadcast(intent, null);
            }
        }
    }




    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.d(Constants.TAG, "Region State  " + i + " region " + region);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons != null && beacons.size() > 0 && region != null) {
            RegionName regName = RegionName.parseString(region.getUniqueId());
            if (regName.isApplicationRegion()) {
                if (regName.getEventType() == ActionBeacon.EventType.EVENT_NEAR_YOU) {
                    Iterator<Beacon> iterator = beacons.iterator();
                    while (iterator.hasNext()) {
                        Beacon beacon = iterator.next();
                        TrackedBeacon tracked = mDataManager.getBeacon(regName.getBeaconId());
                        mDataManager.updateBeaconDistance(regName.getBeaconId(), beacon.getDistance());
                        if (tracked != null && BeaconUtil.isInProximity(IManagedBeacon.ProximityType.FAR, tracked.getDistance())) {
                            if (BeaconUtil.isInProximity(IManagedBeacon.ProximityType.NEAR, beacon.getDistance())
                                    || BeaconUtil.isInProximity(IManagedBeacon.ProximityType.IMMEDIATE, beacon.getDistance())) {

                                Intent intent = new Intent();
                                intent.setAction(Constants.NOTIFY_BEACON_NEAR_YOU_REGION);
                                intent.putExtra("REGION", (Parcelable)region);
                                getApplicationContext().sendOrderedBroadcast(intent, null);

                                //text to speech
                                String toSpeak = "You are Near to the Item";
                                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    }
                }
            }
        }
    }




//    public void notifyBeacon(Collection<Beacon> beacons, Region region) {
//
//            RegionName regName = RegionName.parseString(region.getUniqueId());
//
//
//                    Iterator<Beacon> iterator = beacons.iterator();
//
//                        Beacon beacon = iterator.next();
//                        TrackedBeacon tracked = mDataManager.getBeacon(regName.getBeaconId());
//                        mDataManager.updateBeaconDistance(regName.getBeaconId(), beacon.getDistance());
//                        if (tracked != null && BeaconUtil.isInProximity(IManagedBeacon.ProximityType.FAR, tracked.getDistance())) {
//
//
//                                //text to speech
//                                String toSpeak = "You are Near to the Item";
//                                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
//                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//                        }
//    }








    @Override
    public void onLowMemory() {
        super.onLowMemory();
        enableBackgroundScan(false);
    }


}