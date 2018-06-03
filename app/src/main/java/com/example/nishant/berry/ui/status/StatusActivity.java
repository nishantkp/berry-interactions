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
 * File Created on 03/06/18 1:52 AM by nishant
 * Last Modified on 03/06/18 1:52 AM
 */

package com.example.nishant.berry.ui.status;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.ActivityStatusBinding;
import com.example.nishant.berry.ui.model.User;

import java.util.Objects;

public class StatusActivity
        extends AppCompatActivity
        implements StatusContract.View {

    private ActivityStatusBinding mBinding;
    private StatusPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_status);

        User user = new User();
        mBinding.setUser(user);

        // If Intent is not null and has key for status, get the string and set it to EditText by
        // setting the status value for User object. FYI it's two way data-binding
        // So when user first opens the status activity, they can see the existing status in
        // EditText field
        if (getIntent() != null && getIntent().hasExtra(IConstants.KEY_STATUS_INTENT)) {
            user.setStatus(getIntent().getStringExtra(IConstants.KEY_STATUS_INTENT));
        }

        // Setup action bar
        setSupportActionBar(mBinding.statusAppBar.mainAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Account Status");
        Objects.requireNonNull(getSupportActionBar()).setElevation(0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new StatusPresenter();
        mPresenter.attachView(this);
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Saving changes...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
