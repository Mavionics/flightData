package com.github.mavionics.fligt_data;

import android.app.Application;
import android.content.res.Configuration;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    public class MyCustomApplication extends Application {
        // Called when the application is starting, before any other application objects have been created.
        // Overriding this method is totally optional!
        @Override
        public void onCreate() {
            super.onCreate();

            CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
            Fabric.with(this, new Crashlytics.Builder().core(core).build());
        }

        // Called by the system when the device configuration changes while your component is running.
        // Overriding this method is totally optional!
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
        }

        // This is called when the overall system is running low on memory,
        // and would like actively running processes to tighten their belts.
        // Overriding this method is totally optional!
        @Override
        public void onLowMemory() {
            super.onLowMemory();
        }
    }

}
