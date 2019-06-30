package com.appdeveloper.allenwang.mvp;

import com.appdeveloper.allenwang.mvp.di.ApiModule;
import com.appdeveloper.allenwang.mvp.di.AppComponent;
import com.appdeveloper.allenwang.mvp.di.AppContextModule;
import com.appdeveloper.allenwang.mvp.di.DaggerAppComponent;
import com.appdeveloper.allenwang.mvp.di.NetworkModule;
import com.appdeveloper.allenwang.mvp.di.RxModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class Application extends android.app.Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initialiseLogger();
        initAppComponent();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .apiModule(new ApiModule())
                .networkModule(new NetworkModule())
                .rxModule(new RxModule())
                .build();
        appComponent.inject(this);
    }


    private void initialiseLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    // decide what to log in release version
                }
            });
        }
    }

    public static AppComponent getNetComponent() {
        return appComponent;
    }
}
