package com.example.nishant.berry.ui.dashboard;

import com.example.nishant.berry.base.MvpView;

public interface DashboardContract {
    interface View extends MvpView {
        void noActiveUser();
    }

    interface Presenter {
        void checkCurrentUser();
    }
}
