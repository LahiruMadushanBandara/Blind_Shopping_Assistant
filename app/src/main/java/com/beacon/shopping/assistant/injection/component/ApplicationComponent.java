

package com.beacon.shopping.assistant.injection.component;


import android.app.Application;

import com.beacon.shopping.assistant.action.ActionExecutor;
import com.beacon.shopping.assistant.data.DataManager;
import com.beacon.shopping.assistant.injection.module.ApplicationModule;

import org.altbeacon.beacon.BeaconManager;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by lahiru on 18/02/18.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    DataManager dataManager();

    BeaconManager beaconManager();

    ActionExecutor actionExecutor();

}