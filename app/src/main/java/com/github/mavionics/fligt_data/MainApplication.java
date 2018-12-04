package com.github.mavionics.fligt_data;

import android.app.Application;

import com.github.mavionics.fligt_data.modules.ApplicationComponent;
import com.github.mavionics.fligt_data.modules.DaggerApplicationComponent;
import com.github.mavionics.fligt_data.modules.DataModule;
import com.github.mavionics.fligt_data.modules.Rx;

public class MainApplication extends Application {

    private ApplicationComponent component;
    private Boolean isPersistent = false;

    @Override public void onCreate() {
        super.onCreate();
        inject();
    }

    private void inject(){
        component = DaggerApplicationComponent.builder()
                .dataModule(new DataModule(this))
                .rx(new Rx())
                .build();
        component.inject(this);
    }

    public ApplicationComponent getApplicationComponent(){
        return component;
    }

}
