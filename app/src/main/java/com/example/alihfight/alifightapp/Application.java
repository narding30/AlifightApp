package com.example.alihfight.alifightapp;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
