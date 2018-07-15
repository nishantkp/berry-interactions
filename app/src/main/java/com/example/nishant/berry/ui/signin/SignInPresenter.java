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
 * File Created on 02/06/18 6:32 PM by nishant
 * Last Modified on 02/06/18 6:32 PM
 */

package com.example.nishant.berry.ui.signin;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.ui.model.User;

public class SignInPresenter
        extends BasePresenter<SignInContract.View>
        implements SignInContract.Presenter {

    // Data Manager
    private DataManager mDataManager;

    SignInPresenter() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public SignInContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SignInContract.View view) {
        super.attachView(view);
    }

    @Override
    public void signInUser(String email, String password, SignInContract.View.SignInCallback callback) {

        // Validation check
        if (!User.isEmailValid(email)) {
            callback.invalidEmail("Enter valid email");
            return;
        }
        if (!User.isPasswordValid(password)) {
            callback.invalidPassword("Enter valid password");
            return;
        }

        // Show progress dialog
        getView().showProgressDialog();
        mDataManager.loginUser(email, password, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                getView().cancelProgressDialog();
                getView().signInSuccess();
            }

            @Override
            public void onError(String error) {
                getView().cancelProgressDialog();
                getView().signInError(error);
            }
        });
    }
}
