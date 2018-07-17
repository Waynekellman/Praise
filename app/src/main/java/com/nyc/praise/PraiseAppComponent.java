package com.nyc.praise;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Wayne Kellman on 7/16/18.
 */
@Singleton @Component(modules = {AndroidModule.class, GpsModule.class})
interface PraiseAppComponent {

     GPSHelper provideGps();

}
