package com.example.nishant.berry.ui.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.example.nishant.berry.BR;

/**
 * User object user with dataBinding for SignUp and SignIn
 */
public class User extends BaseObservable {
    private String displayName;
    private String email;
    private String password;

    @Bindable
    public String getDisplayName() {
        return displayName;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        notifyPropertyChanged(BR.displayName);
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    /**
     * Email validation
     *
     * @param email email address
     * @return true or false
     */
    public static boolean isEmailValid(String email) {
        return !(TextUtils.isEmpty(email)
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * Display name validation
     *
     * @param displayName display name
     * @return true or false
     */
    public static boolean isDisplayNameValid(String displayName) {
        return !TextUtils.isEmpty(displayName);
    }

    /**
     * Password validation
     * password's length should be greater than 6
     *
     * @param password password
     * @return true or false
     */
    public static boolean isPasswordValid(String password) {
        return !(TextUtils.isEmpty(password) || password.length() < 7);
    }
}
