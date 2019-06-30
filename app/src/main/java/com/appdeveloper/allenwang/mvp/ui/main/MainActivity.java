package com.appdeveloper.allenwang.mvp.ui.main;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.appdeveloper.allenwang.mvp.Application;
import com.appdeveloper.allenwang.mvp.Utils;
import com.appdeveloper.allenwang.mvp.model.MovieBrief;
import com.appdeveloper.allenwang.mvp.ui.detail.DetailScreenActivity;
import com.appdeveloper.allenwang.mvp.R;
import com.appdeveloper.allenwang.mvp.ui.main.core.MainContract;
import com.appdeveloper.allenwang.mvp.ui.main.core.MainPresenter;
import com.appdeveloper.allenwang.mvp.ui.main.di.DaggerMainActivityComponent;
import com.appdeveloper.allenwang.mvp.ui.main.di.MainActivityModule;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.main_searchView)
    EditText searchView;

    @BindView(R.id.main_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.main_progressBar)
    ProgressBar progressBar;

    @Inject
    MainPresenter presenter;

    private MainRecyclerviewAdapter adapter;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DaggerMainActivityComponent.builder()
                .appComponent(Application.getNetComponent())
                .mainActivityModule(new MainActivityModule((this)))
                .build()
                .inject(this);

        setAdapter();
        setTextChangeListener();
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    @Override
    public void setProgressIndicator(boolean active) {
        progressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showMovies(List<MovieBrief> movieBriefs) {
        adapter.updateGameDatas(movieBriefs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showDetailUi(MovieBrief item) {
        Intent i = new Intent(this, DetailScreenActivity.class);
        i.putExtra("item", item);
        startActivity(i);
    }

    @Override
    public void showError(String msg) {
        Utils.popUpAlert(this, msg);
    }

    private void setAdapter() {
        adapter = new MainRecyclerviewAdapter(this, new MainRecyclerviewAdapter.ItemListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(MovieBrief clickedData) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1500){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                presenter.openItemDetail(clickedData);
            }
        });
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void setTextChangeListener() {
        Observable<String> obs = RxTextView.textChanges(searchView)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence s) throws Exception {
                        return s.length() > 3;
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                });

        disposable = obs.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                presenter.loadItems(s);
            }
        });
    }

}
