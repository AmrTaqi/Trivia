package com.example.trivia.di.modules;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Context mContext;

    public AppModule(Context context) {
        this.mContext = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return mContext;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPrefs(Context context) {
        return context.getSharedPreferences("PREFERENCES_ID", context.MODE_PRIVATE);
    }
}
