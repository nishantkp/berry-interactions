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
 * File Created on 20/06/18 10:27 PM by nishant
 * Last Modified on 20/06/18 10:27 PM
 */

package com.example.nishant.berry.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.EditText;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BaseActivity;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.ActivitySearchBinding;
import com.example.nishant.berry.ui.adapter.FriendsAdapter;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.SearchUser;
import com.example.nishant.berry.ui.profile.UserProfileActivity;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class SearchActivity
        extends BaseActivity
        implements SearchContract.View, FriendsAdapter.OnClick {

    /* Tag for log messages */
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private static SearchPresenter mPresenter;
    private ActivitySearchBinding mBinding;
    private FriendsAdapter mFriendsAdapter;

    /**
     * Binding adapter for {@link SearchActivity} EditText field
     *
     * @param view        EditText view
     * @param searchQuery Text entered by user
     */
    @BindingAdapter({"app:performSearchPerQuery"})
    public static void searchUser(EditText view, String searchQuery) {
        mPresenter.findUserFromQuery(searchQuery);
    }

    /**
     * Use this method get the intent to start {@link SearchActivity}
     *
     * @param context Context of activity from which intent is started
     * @return Intent to start {@link SearchActivity}
     */
    public static Intent getStarterIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mFriendsAdapter = new FriendsAdapter(this);
        mBinding.searchRv.setAdapter(mFriendsAdapter);

        // set presenter
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
        mBinding.setSearch(new SearchUser());

        // set layout manager on recycler view
        mBinding.searchRv.setLayoutManager(new LinearLayoutManager(this));

        // Add divider between two items
        DividerItemDecoration itemDecor =
                new DividerItemDecoration(mBinding.searchRv.getContext(), VERTICAL);
        mBinding.searchRv.addItemDecoration(itemDecor);
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
     * {@link SearchPresenter} callback for list of users
     *
     * @param data User's list from search query entered by user
     */
    @Override
    public void onData(List<AllUsers> data) {
        mFriendsAdapter.updateData(data, IConstants.DIFF_ALL);
    }

    /**
     * {@link SearchPresenter} callback for error while retrieving data from database
     *
     * @param error error message
     */
    @Override
    public void onError(String error) {
        Log.d(LOG_TAG, error);
    }

    /**
     * Implement this functionality to set behavior when user clicks on list item from
     * search users list
     *
     * @param id   id of a user on which click event occurred
     * @param name name of a user on which click event occured
     */
    @Override
    public void onItemClick(String id, String name) {
        startActivity(UserProfileActivity.getStarterIntent(this)
                .putExtra(IConstants.KEY_USER_ID, id));
    }
}
