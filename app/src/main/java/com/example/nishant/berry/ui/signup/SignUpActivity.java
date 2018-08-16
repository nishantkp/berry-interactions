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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.databinding.ActivitySignUpBinding;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.model.User;
import com.example.nishant.berry.ui.module.ActivityModule;
import com.example.nishant.berry.ui.module.ProgressDialogModule;

import java.util.Objects;

import javax.inject.Inject;

public class SignUpActivity
        extends AppCompatActivity
        implements SignUpContract.View, SignUpContract.View.SignUpCallback {

    private ActivitySignUpBinding mBinding;

    @Inject
    SignUpPresenter mPresenter;

    @Inject
    ProgressDialog mProgressDialog;

    /**
     * Use this method get the intent to start {@link SignUpActivity}
     *
     * @param context Context of activity from which intent is started
     * @return Intent to start {@link SignUpActivity}
     */
    public static Intent getStarterIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        // Inject Dagger
        DaggerSignUpComponent.builder()
                .dataManagerComponent(BerryApp.get(this).getDataManagerApplicationComponent())
                .activityModule(new ActivityModule(this))
                .progressDialogModule(new ProgressDialogModule("Creating account..."))
                .build()
                .inject(this);

        // Setup app bar
        setSupportActionBar(mBinding.signUpToolBar.mainAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0f);

        mBinding.setUser(new User());
        mBinding.setCallback(this);

        mPresenter.attachView(this);
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void signUpSuccess() {
        startActivity(DashboardActivity.getStarterIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    @Override
    public void signUpError(String error) {
        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void invalidDisplayName(String error) {
        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidEmail(String error) {
        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidPassword(String error) {
        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
    }
}
