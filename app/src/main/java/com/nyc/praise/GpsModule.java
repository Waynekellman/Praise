package com.nyc.praise;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Wayne Kellman on 7/16/18.
 */
@Module
public class GpsModule {
    @Provides GPSHelper provideGpsHelper(@App Context context){
        return new GPSHelper(context);
    }
}
