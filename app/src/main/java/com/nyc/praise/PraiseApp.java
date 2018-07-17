package com.nyc.praise;

import android.app.Application;

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
