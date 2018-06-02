package com.example.nishant.berry.ui.signup;

import android.support.annotation.NonNull;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.ui.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPresenter
        extends BasePresenter<SignUpContract.View>
        implements SignUpContract.Presenter {

    private FirebaseAuth mAuth;

    SignUpPresenter() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public SignUpContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SignUpContract.View view) {
        super.attachView(view);
    }

    /**
     * SignUp user
     * validate display name, email, password. If everything is OK then register user with Firebase
     *
     * @param displayName display name
     * @param email       email address
     * @param password    password
     * @param callback    callback for invalid display name, password and email
     */
    @Override
    public void signUpUser(String displayName, String email, String password, SignUpContract.View.SignUpCallback callback) {

        // Validation check
        if (!User.isDisplayNameValid(displayName)) {
            callback.invalidDisplayName("Enter valid display name!");
            return;
        }
        if (!User.isEmailValid(email)) {
            callback.invalidEmail("Enter valid email address!");
            return;
        }
        if (!User.isPasswordValid(password)) {
            callback.invalidPassword("Password must be of minimum length 7!");
            return;
        }

        // Register user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            getView().signUpSuccess();
                        } else {
                            getView().signUpError("Error creating account!");
                        }
                    }
                });
    }
}
