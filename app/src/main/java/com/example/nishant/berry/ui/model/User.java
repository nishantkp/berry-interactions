/*
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * File Created on 15/08/18 7:03 PM by nishant
 * Last Modified on 03/06/18 2:35 AM
 */

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
    private String status;

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

    @Bindable
    public String getStatus() {
        return status;
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

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
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
