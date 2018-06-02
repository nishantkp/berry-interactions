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
 * Last Modified on 02/06/18 12:51 AM
 */

package com.example.nishant.berry.ui.signup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.ActivitySignUpBinding;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.model.User;

public class SignUpActivity
        extends AppCompatActivity
        implements SignUpContract.View, SignUpContract.View.SignUpCallback {

    private ActivitySignUpBinding mBinding;
    private SignUpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mBinding.setUser(new User());
        mBinding.setCallback(this);

        mPresenter = new SignUpPresenter();
        mPresenter.attachView(this);
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void signUpSuccess() {
        startActivity(
                new Intent(SignUpActivity.this, DashboardActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    @Override
    public void signUpError(String error) {
        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
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
