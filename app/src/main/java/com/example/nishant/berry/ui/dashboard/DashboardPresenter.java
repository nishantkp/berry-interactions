package com.example.nishant.berry.ui.dashboard;

import com.example.nishant.berry.base.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardPresenter
        extends BasePresenter<DashboardContract.View>
        implements DashboardContract.Presenter {

    private FirebaseAuth mAuth;

    DashboardPresenter() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public DashboardContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(DashboardContract.View view) {
        super.attachView(view);
    }

    @Override
    public void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            getView().noActiveUser();
        }
    }
}
