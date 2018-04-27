

package com.beacon.shopping.assistant.injection.module;

import android.content.Context;

import com.beacon.shopping.assistant.data.DbStoreService;
import com.beacon.shopping.assistant.data.StoreService;
import com.beacon.shopping.assistant.injection.UserScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lahiru on 20/12/2017.
 */
@Module
public class DataModule {
    private Context mContext;

    public DataModule(Context context) {
        mContext = context;
    }

    @Provides
    @UserScope
    StoreService provideStoreService() {
        return new DbStoreService(mContext);
    }
}
