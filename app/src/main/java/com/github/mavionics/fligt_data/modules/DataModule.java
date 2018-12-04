package com.github.mavionics.fligt_data.modules;

import com.github.mavionics.fligt_data.MainApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    MainApplication application;

    public DataModule(MainApplication mainApplication) {
        this.application = mainApplication;
    }

    @Provides
    MainApplication provideMainApplication() {
        return application;
    }
}
