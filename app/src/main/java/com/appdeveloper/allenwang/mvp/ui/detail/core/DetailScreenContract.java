
package com.appdeveloper.allenwang.mvp.ui.detail.core;

import com.appdeveloper.allenwang.mvp.model.MovieDetails;


/**
 * This specifies the contract between the view and the presenter.
 */
public interface DetailScreenContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showDetailUi(MovieDetails item);

        void showError(String msg);
    }

    interface UserActionsListener {
        void loadItem(String id);
    }
}
