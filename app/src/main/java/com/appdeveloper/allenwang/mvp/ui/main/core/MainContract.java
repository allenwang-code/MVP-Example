
package com.appdeveloper.allenwang.mvp.ui.main.core;

import android.support.annotation.NonNull;

import com.appdeveloper.allenwang.mvp.model.MovieBrief;

import java.util.List;


/**
 * This specifies the contract between the view and the presenter.
 */
public interface MainContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showMovies(List<MovieBrief> notes);

        void showDetailUi(MovieBrief item);

        void showError(String msg);
    }

    interface UserActionsListener {

        void loadItems(String query);

        void openItemDetail(@NonNull MovieBrief requestedItem);
    }
}
