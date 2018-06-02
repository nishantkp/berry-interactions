package com.example.nishant.berry.ui.signup;

import com.example.nishant.berry.base.MvpView;

public interface SignUpContract {
    interface View extends MvpView {
        void signUpSuccess();

        void signUpError(String error);

        interface SignUpCallback {
            void invalidDisplayName(String error);

            void invalidEmail(String error);

            void invalidPassword(String error);
        }
    }

    interface Presenter {
        void signUpUser(String displayName,
                        String email,
                        String password,
                        SignUpContract.View.SignUpCallback callback);
    }
}
