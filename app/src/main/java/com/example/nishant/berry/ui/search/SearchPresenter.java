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

package com.example.nishant.berry.ui.search;

import android.text.TextUtils;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter responsible for querying database as per user search query
 */
public class SearchPresenter
        extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter {

    private DataManager mDataManager;

    @Inject
    SearchPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public SearchContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SearchContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to find user from firebase database
     *
     * @param searchQuery search query entered by user
     */
    @Override
    public void findUserFromQuery(String searchQuery) {
        // If user has not given any search query, return from the method
        if (TextUtils.isEmpty(searchQuery)) {
            return;
        }

        // Get the user's list which contains 15 users with the help of DataManager
        mDataManager.findUser(searchQuery, 15, new OnUsersList() {
            @Override
            public void onData(List<AllUsers> data) {
                getView().onData(data);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Call this method when user clicks on list item
     *
     * @param id   user id
     * @param name user name
     */
    @Override
    public void onItemClick(String id, String name) {
        getView().onCreateUserProfileActivity(id, name);
    }
}
