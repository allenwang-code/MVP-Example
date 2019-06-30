
package com.appdeveloper.allenwang.mvp.ui.detail.core;

import android.support.annotation.NonNull;

import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.model.MovieDetails;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

import static dagger.internal.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI ({@link com.appdeveloper.allenwang.mvp.ui.detail.DetailScreenActivity}), retrieves the data and updates the
 * UI as required.
 */
public class DetailScreenPresenter implements DetailScreenContract.UserActionsListener {

    private final ServiceApi service;
    private final DetailScreenContract.View view;
    Scheduler scheduler;
    Scheduler androidSchedulers;

    public DetailScreenPresenter(
            @NonNull ServiceApi serviceApi, @NonNull DetailScreenContract.View v,
            @NonNull Scheduler scheduler, @NonNull Scheduler androidSchedulers) {
        service = checkNotNull(serviceApi, "serviceApi cannot be null");
        this.view = checkNotNull(v, "v cannot be null!");
        this.scheduler =  checkNotNull(scheduler, "scheduler cannot be null");
        this.androidSchedulers =  checkNotNull(androidSchedulers, "androidSchedulers cannot be null");
    }

    @Override
    public void loadItem(String id) {
        if(id == null || id.isEmpty()) {
            view.showError("Could not find the item");
            return;
        }

        view.setProgressIndicator(true);
        service.getMovie(id)
                .subscribeOn(scheduler)
                .unsubscribeOn(scheduler)
                .observeOn(androidSchedulers)
                .subscribe(new Observer<MovieDetails>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(MovieDetails details) {
                        view.showDetailUi(details);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                        view.setProgressIndicator(false);
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        view.setProgressIndicator(false);
                        d.dispose();
                    }
                });
    }

}
