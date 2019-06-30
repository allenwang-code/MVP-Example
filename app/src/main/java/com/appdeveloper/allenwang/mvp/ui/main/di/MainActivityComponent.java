package com.appdeveloper.allenwang.mvp.ui.main.di;

import com.appdeveloper.allenwang.mvp.di.AppComponent;
import com.appdeveloper.allenwang.mvp.ui.main.MainActivity;

import dagger.Component;

@MainActivityScope
@Component(dependencies = AppComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
