package com.appdeveloper.allenwang.mvp.ui.main.di;

import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.di.Named;
import com.appdeveloper.allenwang.mvp.ui.main.MainActivity;
import com.appdeveloper.allenwang.mvp.ui.main.core.MainContract;
import com.appdeveloper.allenwang.mvp.ui.main.core.MainPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class MainActivityModule {

    private MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @MainActivityScope
    @Provides
    MainContract.View provideMainView() {
        return activity;
    }

    @MainActivityScope
    @Provides
    MainPresenter provideMainPresenter(ServiceApi api, MainContract.View view,
                                       @Named("IoThread") Scheduler scheduler, @Named("MainThread") Scheduler androidScheduler) {
        return new MainPresenter(api, view, scheduler, androidScheduler);
    }

    @MainActivityScope
    @Provides
    MainActivity provideContext() {
        return activity;
    }
}
