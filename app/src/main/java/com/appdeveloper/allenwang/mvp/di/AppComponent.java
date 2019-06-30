package com.appdeveloper.allenwang.mvp.di;


import com.appdeveloper.allenwang.mvp.Application;
import com.appdeveloper.allenwang.mvp.data.ServiceApi;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Scheduler;

@Singleton
@Component(modules = {ApiModule.class, NetworkModule.class, RxModule.class, AppContextModule.class})
public interface AppComponent {
    void inject(Application application);

    ServiceApi serviceApi();

    @Named("IoThread")
    Scheduler IoThreadScheduler();

    @Named("MainThread")
    Scheduler MainThreadScheduler();
}
