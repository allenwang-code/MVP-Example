package com.appdeveloper.allenwang.mvp.ui.main.core;

import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.model.MovieBrief;
import com.appdeveloper.allenwang.mvp.model.Search;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private MainPresenter presenter;

    @Mock
    private ServiceApi service;
    @Mock
    private MainContract.View view;
    @Mock
    private Scheduler mainThreadScheduler = AndroidSchedulers.mainThread();
    @Mock
    private Scheduler ioScheduler = Schedulers.io();

    private static String TITLE =  "Title";
    private static List<MovieBrief> MOVIES =  Lists.newArrayList(
            new MovieBrief(TITLE, "1", "1", "1","URL1"),
            new MovieBrief(TITLE, "2", "2", "2","URL2"));
    private static Search SEARCH = new Search(MOVIES);

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
        presenter = new MainPresenter(service, view, ioScheduler, mainThreadScheduler);
    }

    @Test
    public void loadItemWithNull() {
        presenter.loadItems(null);
        verify(view).showError("Could not find the item");
    }

    @Test
    public void loadItemWithEmpty() {
        presenter.loadItems( "");
        verify(view).showError("Could not find the item");
    }

    @Test
    public void loadItemWithEmptyDoesNotCallShowResult() {
        presenter.loadItems( "");
        verify(view, never()).showMovies(any(List.class));
    }

    @Test
    public void fetchValidDataShouldLoadIntoView() {
        when(service.getMovies(any(String.class)))
                .thenReturn(Observable.just(SEARCH));

        MainPresenter mainPresenter = new MainPresenter(
                service,
                view,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );

        mainPresenter.loadItems(TITLE);

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).setProgressIndicator(true);
        inOrder.verify(view, times(1)).showMovies(MOVIES);
        inOrder.verify(view, times(1)).setProgressIndicator(false);
    }


    @Test
    public void fetchErrorShouldReturnErrorToView() {
        Exception exception = new Exception("ERROR");
        when(service.getMovies(any(String.class)))
                .thenReturn(Observable.<Search>error(exception));

        MainPresenter mainPresenter = new MainPresenter(
                service,
                view,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
        mainPresenter.loadItems("abc");

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).setProgressIndicator(true);
        inOrder.verify(view, times(1)).showError(exception.getMessage());
        inOrder.verify(view, times(1)).setProgressIndicator(false);
        verify(view, never()).showMovies(null);
    }

    @Test
    public void openItemDetail() {
        MovieBrief m = new MovieBrief(TITLE, "1", "1", "1","URL1");
        presenter.openItemDetail(m);
        verify(view).showDetailUi(m);
    }
}