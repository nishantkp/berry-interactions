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
 * File Created on 02/06/18 12:55 AM by nishant
 * Last Modified on 01/06/18 8:16 PM
 */

package com.example.nishant.berry.ui.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.ActivitySignInBinding;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.model.User;
import com.example.nishant.berry.ui.signup.SignUpActivity;

import java.util.Objects;

public class SignInActivity
        extends AppCompatActivity
        implements SignInContract.View, SignInContract.View.SignInCallback {

    private ActivitySignInBinding mBinding;
    private SignInPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        // Setup app bar
        setSupportActionBar(mBinding.signInToolBar.mainAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0f);

        mPresenter = new SignInPresenter();
        mPresenter.attachView(this);
        mBinding.setUser(new User());
        mBinding.setPresenter(mPresenter);
        mBinding.setCallback(this);
    }

    @Override
    public void signInSuccess() {
        startActivity(
                new Intent(SignInActivity.this, DashboardActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    @Override
    public void signInError(String error) {
        Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void invalidEmail(String error) {
        Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void invalidPassword(String error) {
        Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}
