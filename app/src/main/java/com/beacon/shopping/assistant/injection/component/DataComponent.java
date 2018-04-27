

package com.beacon.shopping.assistant.injection.component;

import com.beacon.shopping.assistant.data.DataManager;
import com.beacon.shopping.assistant.injection.UserScope;
import com.beacon.shopping.assistant.injection.module.DataModule;

import dagger.Component;

@UserScope
@Component(dependencies = ApplicationComponent.class, modules = {DataModule.class})
public interface DataComponent {

    void inject(DataManager dataManager);

}