package com.appdeveloper.allenwang.mvp.ui.detail.core;

import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.model.MovieBrief;
import com.appdeveloper.allenwang.mvp.model.MovieDetails;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DetailScreenPresenterTest {

    private DetailScreenPresenter detailScreenPresenter;

    private static MovieBrief mockBrief =
            new MovieBrief("Spider Man","1987","123456","Scared","url");
    private static MovieDetails mockDetail = new MovieDetails(mockBrief.getImdbID());

    @Mock
    private ServiceApi service;

    @Mock
    private DetailScreenContract.View view;
    @Mock
    private Scheduler mainThreadScheduler = AndroidSchedulers.mainThread();
    @Mock
    private Scheduler ioScheduler = Schedulers.io();

    @BeforeClass
    public static void setUpRxSchedulers() {
        final Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new ScheduledThreadPoolExecutor(1) {
                    @Override
                    public void execute(@NonNull Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        detailScreenPresenter = new DetailScreenPresenter(
                service, view, ioScheduler, mainThreadScheduler);
    }

    @Test
    public void loadItemWithNull() {
        detailScreenPresenter.loadItem(null);
        verify(view).showError("Could not find the item");
    }

    @Test
    public void loadItemWithEmpty() {
        detailScreenPresenter.loadItem( "");
        verify(view).showError("Could not find the item");
    }

    @Test
    public void loadItemWithEmptyDoesNotCallShowResult() {
        detailScreenPresenter.loadItem( "");
        verify(view, never()).showDetailUi(any(MovieDetails.class));
    }

    @Test
    public void fetchValidDataShouldLoadIntoView() {
        when(service.getMovie(any(String.class)))
                .thenReturn(Observable.just(mockDetail));

        DetailScreenPresenter presenter = new DetailScreenPresenter(
                service,
                view,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );

        presenter.loadItem(mockBrief.getImdbID());

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).setProgressIndicator(true);
        inOrder.verify(view, times(1)).showDetailUi(mockDetail);
        inOrder.verify(view, times(1)).setProgressIndicator(false);
    }


    @Test
    public void fetchErrorShouldReturnErrorToView() {
        Exception exception = new Exception("ERROR");
        when(service.getMovie(any(String.class)))
                .thenReturn(Observable.<MovieDetails>error(exception));

        DetailScreenPresenter presenter = new DetailScreenPresenter(
                service,
                view,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
        presenter.loadItem(mockBrief.getImdbID());

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).setProgressIndicator(true);
        inOrder.verify(view, times(1)).showError(exception.getMessage());
        inOrder.verify(view, times(1)).setProgressIndicator(false);
        verify(view, never()).showDetailUi(null);
    }


}