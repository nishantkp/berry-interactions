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
 * File Created on 11/06/18 11:09 PM by nishant
 * Last Modified on 11/06/18 11:09 PM
 */

package com.example.nishant.berry.ui.interaction;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BaseActivity;
import com.example.nishant.berry.databinding.ActivityInteractionBinding;
import com.example.nishant.berry.databinding.InteractionCustomBarBinding;

/**
 * Activity that handles all interactions between users
 */
public class InteractionActivity
        extends BaseActivity
        implements InteractionContract.View {

    private InteractionPresenter mPresenter;
    private ActivityInteractionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_interaction);

        // Setup support action bar
        setSupportActionBar(mBinding.interactionsAppBar.mainAppBar);

        // Attach view to presenter
        mPresenter = new InteractionPresenter(getIntent());
        mPresenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Implement this method to set custom actionbar
     *
     * @param displayName display name
     * @param avatarUrl   url of avatar thumbnail
     */
    @Override
    public void setActionBar(String displayName, String avatarUrl) {
        if (displayName == null) displayName = "Berry";
        ActionBar customActionBar = getSupportActionBar();
        View appBarView = getLayoutInflater().inflate(R.layout.interaction_custom_bar, null);
        InteractionCustomBarBinding customBarBinding = InteractionCustomBarBinding.bind(appBarView);

        if (customActionBar != null) {
            customActionBar.setDisplayHomeAsUpEnabled(true);
            customActionBar.setDisplayShowCustomEnabled(true);
            customActionBar.setCustomView(customBarBinding.getRoot());
            customBarBinding.customAppBarDisplayName.setText(displayName);
        }
    }
}
