package com.nyc.praise;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Wayne Kellman on 7/16/18.
 */

public class PraiseApp extends Application {

    private PraiseAppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerPraiseAppComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();


    }

    public PraiseAppComponent component(){
        return appComponent;
    }

}
