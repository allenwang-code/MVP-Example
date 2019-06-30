package com.appdeveloper.allenwang.mvp.ui.detail.di;

import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.di.Named;
import com.appdeveloper.allenwang.mvp.ui.detail.DetailScreenActivity;
import com.appdeveloper.allenwang.mvp.ui.detail.core.DetailScreenContract;
import com.appdeveloper.allenwang.mvp.ui.detail.core.DetailScreenPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class DetailActivityModule {

    private DetailScreenActivity activity;

    public DetailActivityModule(DetailScreenActivity activity) {
        this.activity = activity;
    }

    @DetailActivityScope
    @Provides
    DetailScreenContract.View provideMainView() {
        return activity;
    }

    @DetailActivityScope
    @Provides
    DetailScreenPresenter provideMainPresenter(ServiceApi serviceApi, DetailScreenContract.View view, @Named("IoThread")
                                                Scheduler scheduler, @Named("MainThread") Scheduler androidScheduler) {
        return new DetailScreenPresenter(serviceApi, view, scheduler, androidScheduler);
    }

    @DetailActivityScope
    @Provides
    DetailScreenActivity provideContext() {
        return activity;
    }
}
