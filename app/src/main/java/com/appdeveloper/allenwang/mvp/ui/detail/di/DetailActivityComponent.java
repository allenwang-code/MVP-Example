package com.appdeveloper.allenwang.mvp.ui.detail.di;

import com.appdeveloper.allenwang.mvp.di.AppComponent;
import com.appdeveloper.allenwang.mvp.ui.detail.DetailScreenActivity;

import dagger.Component;

@DetailActivityScope
@Component(dependencies = AppComponent.class, modules = DetailActivityModule.class)
public interface DetailActivityComponent {
    void inject(DetailScreenActivity activity);
}
