

package com.beacon.shopping.assistant.injection.module;

import android.app.Application;
import android.content.Context;

import com.beacon.shopping.assistant.action.ActionExecutor;
import com.beacon.shopping.assistant.data.DataManager;

import org.altbeacon.beacon.BeaconManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {
    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }


    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new DataManager(this.application);
    }

    @Provides
    @Singleton
    ActionExecutor provideActionExecutor() {
        return new ActionExecutor(this.application);
    }

    @Provides
    @Singleton
    public BeaconManager provideBeaconManager() {
        BeaconManager manager = BeaconManager.getInstanceForApplication(application);


        //manager.setDebug(true);
        return manager;
    }

}