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
 * Last Modified on 15/08/18 6:05 PM
 */

package com.example.nishant.berry.ui.allusers;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.example.nishant.berry.R;
import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.base.BaseActivity;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.databinding.ActivityAllUsersBinding;
import com.example.nishant.berry.ui.adapter.FriendsAdapter;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.profile.UserProfileActivity;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Displays all users in database
 */
public class AllUsersActivity
        extends BaseActivity
        implements AllUsersContract.View, FriendsAdapter.OnClick {

    @Inject
    AllUsersPresenter mPresenter;
    @Inject
    FriendsAdapter mFriendsAdapter;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    DividerItemDecoration mItemDecor;
    private ActivityAllUsersBinding mBinding;

    /**
     * Use this method get the intent to start {@link AllUsersActivity}
     *
     * @param context Context of activity from which intent is started
     * @return Intent to start {@link AllUsersActivity}
     */
    public static Intent getStarterIntent(Context context) {
        return new Intent(context, AllUsersActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_users);

        // Inject Dagger
        DaggerAllUsersComponent.builder()
                .dataManagerComponent(BerryApp.get(this).getDataManagerApplicationComponent())
                .allUsersModule(new AllUsersModule(this))
                .build()
                .inject(this);

        // Setup app bar
        setSupportActionBar(mBinding.allUsersAppBar.mainAppBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All users");
        Objects.requireNonNull(getSupportActionBar()).setElevation(0f);

        mBinding.allUsersRv.setAdapter(mFriendsAdapter);

        // Setup presenter
        mPresenter.attachView(this);

        // Setup recycler view
        mBinding.allUsersRv.setLayoutManager(mLinearLayoutManager);
        mBinding.allUsersRv.setHasFixedSize(true);

        // Add divider between two items
        mBinding.allUsersRv.addItemDecoration(mItemDecor);
    }

    @Override
    public void onAllUsersData(List<AllUsers> data) {
        mFriendsAdapter.updateData(data, IConstants.DIFF_ALL);
    }

    @Override
    public void onError(String error) {
        // Error handling
    }

    /**
     * Start {@link UserProfileActivity}
     *
     * @param id user id
     */
    @Override
    public void onCreateProfileActivity(String id) {
        startActivity(UserProfileActivity.getStarterIntent(this)
                .putExtra(IConstants.KEY_USER_ID, id)
        );
    }

    /**
     * Implement this functionality to set behavior when user clicks on list item from
     * all users list
     *
     * @param id   id of a user on which click event occurred
     * @param name name of a user on which click event occured
     */
    @Override
    public void onItemClick(String id, String name) {
        mPresenter.onItemClick(id, name);
    }
}
