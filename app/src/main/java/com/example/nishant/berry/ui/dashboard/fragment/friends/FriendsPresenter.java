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
 * File Created on 10/06/18 5:34 PM by nishant
 * Last Modified on 10/06/18 5:34 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.friends;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Presenter that responsible for displaying friends list in Friends tab in
 * {@link DashboardActivity}
 */
public class FriendsPresenter
        extends BasePresenter<FriendsContract.View>
        implements FriendsContract.Presenter, DataManager.FriendsListCallback {

    // DataManager
    private DataManager mDataManager;

    FriendsPresenter() {
        mDataManager = new DataManager();
        mDataManager.setFriendsListCallbacks(this);
    }

    @Override
    public FriendsContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(FriendsContract.View view) {
        super.attachView(view);

        // After attaching a view, setup firebase adapter
        setupFirebaseRecyclerAdapter();
    }

    @Override
    public void setupFirebaseRecyclerAdapter() {
        mDataManager.getCurrentUserFriendList();
    }

    /**
     * Implement this {@link DataManager} callback for error handling while getting data
     * from Firebase database
     *
     * @param error error message
     */
    @Override
    public void onError(String error) {
    }

    /**
     * Implement this {@link DataManager} callback for getting data from firebase
     * by calling startListening()/ stopListening() methods on FirebaseRecyclerAdapter
     * And ultimately set the adapter on RecyclerView to actually see the list
     *
     * @param adapter FirebaseRecyclerAdapter instance
     */
    @Override
    public void onFriendsAdapter(FirebaseRecyclerAdapter adapter) {
        getView().getFirebaseRecyclerAdapter(adapter);
    }

    /**
     * Implement this {@link DataManager} callback for click event on friends list
     *
     * @param userId      Id of a friend on which click action is performed
     * @param displayName Name of a friend
     */
    @Override
    public void onListItemClick(String userId, String displayName) {
        getView().onListItemClick(userId, displayName);
    }
}
