
package com.appdeveloper.allenwang.mvp.ui.main.core;

import android.support.annotation.NonNull;

import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.model.MovieBrief;
import com.appdeveloper.allenwang.mvp.model.Search;
import com.appdeveloper.allenwang.mvp.ui.main.MainActivity;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

import static dagger.internal.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates the
 * UI as required.
 */
public class MainPresenter implements MainContract.UserActionsListener {

    private final ServiceApi service;
    private final MainContract.View view;
    Scheduler scheduler;
    Scheduler androidSchedulers;

    public MainPresenter(@NonNull ServiceApi serviceApi, @NonNull MainContract.View notesView,
                         @NonNull Scheduler scheduler, @NonNull Scheduler androidSchedulers) {
        service = checkNotNull(serviceApi, "notesRepository cannot be null");
        view = checkNotNull(notesView, "notesView cannot be null!");
        this.scheduler =  checkNotNull(scheduler, "scheduler cannot be null");
        this.androidSchedulers =  checkNotNull(androidSchedulers, "androidSchedulers cannot be null");
    }

    @Override
    public void loadItems(String query) {
        if(query == null || query.isEmpty()) {
            view.showError("Could not find the item");
            return;
        }

        // Disposal can be collected
        view.setProgressIndicator(true);
        service.getMovies(query)
                .subscribeOn(scheduler)
                .unsubscribeOn(scheduler)
                .observeOn(androidSchedulers)
                .subscribe(new Observer<Search>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                         this.d = d;
                    }

                    @Override
                    public void onNext(Search search) {
                        if (search == null) {
                            view.showError("Could not find the item");
                        } else if (search.getError() != null) {
                            view.showError(search.getError());
                        } else {
                            view.showMovies(search.getSearch());
                        }
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

    @Override
    public void openItemDetail(@NonNull MovieBrief requestedItem) {
        checkNotNull(requestedItem, "requestedNote cannot be null!");
        view.showDetailUi(requestedItem);
    }

}
