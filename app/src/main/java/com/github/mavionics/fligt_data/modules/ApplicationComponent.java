package com.github.mavionics.fligt_data.modules;

import com.github.mavionics.fligt_data.MainApplication;
import com.github.mavionics.fligt_data.activities.FlightActivity;
import com.github.mavionics.fligt_data.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                DataModule.class,
                Rx.class
        }
)

public interface ApplicationComponent {
    void inject(MainApplication mainApplication);

    void inject(FlightActivity splashActivity);

    void inject(MainActivity mainActivity);
}
