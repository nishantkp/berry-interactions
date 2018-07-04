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
 * File Created on 04/06/18 8:42 PM by nishant
 * Last Modified on 04/06/18 8:42 PM
 */

package com.example.nishant.berry.ui.allusers;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataCallback;
import com.example.nishant.berry.data.DataManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Presenter to display all users in database
 */
public class AllUsersPresenter
        extends BasePresenter<AllUsersContract.View>
        implements AllUsersContract.Presenter {

    private Query mQuery;
    private DataManager mDataManager;

    AllUsersPresenter() {
        // Create a query for Database reference pointing to "users"
        mQuery = DataManager.getUsersRef();
        // Adds offline functionality
        mQuery.keepSynced(true);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public AllUsersContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(AllUsersContract.View view) {
        super.attachView(view);

        // After attaching a view, setup firebase adapter
        setupFirebaseRecyclerAdapter(mQuery);
    }

    @Override
    public void setupFirebaseRecyclerAdapter(Query query) {
        // Get all user's from firebase database
        mDataManager.getAllUsersFromDatabase(query, new DataCallback.OnAllUsersList() {
            @Override
            public void onListItemClick(String listUserId) {
                getView().onListItemClick(listUserId);
            }

            @Override
            public void onFirebaseAdapter(FirebaseRecyclerAdapter adapter) {
                // FirebaseRecyclerAdapter which we can use in {@link AllUsersActivity} for
                // setting up on RecyclerView
                // Also we have to call startListening() and stopListening() method form
                // activity to actually get the data
                getView().getFirebaseRecyclerAdapter(adapter);
            }
        });
    }
}
