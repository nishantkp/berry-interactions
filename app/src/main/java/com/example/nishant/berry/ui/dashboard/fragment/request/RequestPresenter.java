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
 * File Created on 19/06/18 6:13 PM by nishant
 * Last Modified on 19/06/18 6:13 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.request;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Presenter that responsible for displaying friends request, in Request tab in Dashboard
 */
public class RequestPresenter
        extends BasePresenter<RequestContract.View>
        implements RequestContract.Presenter, DataManager.FriendReqCallback {

    // DataManager
    private DataManager mDataManager;

    RequestPresenter() {
        mDataManager = new DataManager();
        mDataManager.setFriendRequestCallbacks(this);
    }

    @Override
    public RequestContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(RequestContract.View view) {
        super.attachView(view);

        // after attaching a view setup firebase recycler adapter
        setupFirebaseRecyclerAdapter();
    }

    /**
     * Implement this method to setup Firebase recycler adapter
     */
    @Override
    public void setupFirebaseRecyclerAdapter() {
        mDataManager.getCurrentUsersFriendReq();
    }

    /**
     * {@link DataManager} callback for error while getting data from firebase database
     *
     * @param error message
     */
    @Override
    public void onError(String error) {
        getView().onError(error);
    }

    /**
     * {@link DataManager} callback for FirebaseRecyclerAdapter, so that we can use set this adapter
     * on RecyclerView for list of friend requests
     * Also we have to call startListening()/ stopListening() methods in onStart()/onPause() to
     * actually get the data from firebase
     *
     * @param adapter FirebaseRecyclerAdapter instance
     */
    @Override
    public void onReqAdapter(FirebaseRecyclerAdapter adapter) {
        getView().setFirebaseAdapterWithRecyclerView(adapter);
    }
}
