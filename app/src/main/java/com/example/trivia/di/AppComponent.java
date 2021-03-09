package com.example.trivia.di;


import com.example.trivia.MainActivity;
import com.example.trivia.di.modules.AppModule;
import com.example.trivia.util.Prefs;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Prefs getPrefs();
    void inject(MainActivity mainActivity);
}
