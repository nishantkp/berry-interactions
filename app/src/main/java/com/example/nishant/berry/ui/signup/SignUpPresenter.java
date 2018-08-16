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
 * Last Modified on 23/07/18 10:44 PM
 */

package com.example.nishant.berry.ui.signup;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.ui.model.User;

/**
 * Presenter that deals with user registration
 */
public class SignUpPresenter
        extends BasePresenter<SignUpContract.View>
        implements SignUpContract.Presenter {

    private DataManager mDataManager;

    SignUpPresenter(DataManager dataManager) {
        mDataManager = dataManager;
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

        // Show progress dialog
        getView().showProgressDialog();
        mDataManager.signUpUser(displayName, email, password, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                getView().cancelProgressDialog();
                getView().signUpSuccess();
            }

            @Override
            public void onError(String error) {
                getView().cancelProgressDialog();
                getView().signUpError(error);
            }
        });
    }
}
