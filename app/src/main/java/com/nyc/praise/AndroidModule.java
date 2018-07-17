package com.nyc.praise;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Wayne Kellman on 7/16/18.
 */

@Module
public class AndroidModule {
    private Application application;

    public AndroidModule(Application application) {
        this.application = application;
    }

    @Provides @App
    Context provideAppContext(){
        return application;
    }

}
