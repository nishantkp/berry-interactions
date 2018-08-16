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

package com.example.nishant.berry.ui.status;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.base.BaseActivity;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.ActivityStatusBinding;
import com.example.nishant.berry.ui.model.User;
import com.example.nishant.berry.ui.module.ActivityModule;
import com.example.nishant.berry.ui.module.ProgressDialogModule;

import java.util.Objects;

public class StatusActivity
        extends BaseActivity
        implements StatusContract.View {

    private ActivityStatusBinding mBinding;
    private ProgressDialog mProgressDialog;

    /**
     * Use this method get the intent to start {@link StatusActivity}
     *
     * @param context Context of activity from which intent is started
     * @return Intent to start {@link StatusActivity}
     */
    public static Intent getStarterIntent(Context context) {
        return new Intent(context, StatusActivity.class);
    }

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

        // Get the presenter from dagger component
        StatusComponent component = DaggerStatusComponent.builder()
                .dataManagerComponent(BerryApp.get(this).getDataManagerApplicationComponent())
                .activityModule(new ActivityModule(this))
                .progressDialogModule(new ProgressDialogModule("Saving changes..."))
                .build();
        StatusPresenter presenter = component.provideStatusPresenter();
        mProgressDialog = component.provideDialog();
        presenter.attachView(this);
        mBinding.setPresenter(presenter);
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
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
