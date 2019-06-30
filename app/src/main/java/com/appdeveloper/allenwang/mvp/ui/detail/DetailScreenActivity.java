package com.appdeveloper.allenwang.mvp.ui.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdeveloper.allenwang.mvp.Application;
import com.appdeveloper.allenwang.mvp.R;
import com.appdeveloper.allenwang.mvp.Utils;
import com.appdeveloper.allenwang.mvp.model.MovieBrief;
import com.appdeveloper.allenwang.mvp.model.MovieDetails;
import com.appdeveloper.allenwang.mvp.ui.detail.core.DetailScreenContract;
import com.appdeveloper.allenwang.mvp.ui.detail.core.DetailScreenPresenter;
import com.appdeveloper.allenwang.mvp.ui.detail.di.DaggerDetailActivityComponent;
import com.appdeveloper.allenwang.mvp.ui.detail.di.DetailActivityModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailScreenActivity extends AppCompatActivity implements DetailScreenContract.View{

    @BindView(R.id.detail_title_textView)
    TextView titleTextView;

    @BindView(R.id.detail_actors_textView)
    TextView actorsTextView;

    @BindView(R.id.detail_year_textView)
    TextView yearTextView;

    @BindView(R.id.detail_director_textView)
    TextView producerTextView;

    @BindView(R.id.detail_progressBar)
    ProgressBar progressBar;

    @Inject
    DetailScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        ButterKnife.bind(this);

        DaggerDetailActivityComponent.builder()
                .appComponent(Application.getNetComponent())
                .detailActivityModule(new DetailActivityModule(this))
                .build()
                .inject(this);

        MovieBrief movie = (MovieBrief) getIntent().getSerializableExtra("item");
        presenter.loadItem(movie.getImdbID());
    }

    @Override
    public void setProgressIndicator(boolean active) {
        progressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showDetailUi(MovieDetails item) {
        titleTextView.setText(item.getTitle());
        actorsTextView.setText(item.getActors());
        yearTextView.setText(item.getYear());
        producerTextView.setText(item.getDirector());
    }

    @Override
    public void showError(String msg) {
        Utils.popUpAlert(this, msg);
    }
}
