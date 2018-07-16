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
import com.example.nishant.berry.data.callbacks.OnFriendRequest;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.List;

/**
 * Presenter that responsible for displaying friends request, in Request tab in Dashboard
 */
public class RequestPresenter
        extends BasePresenter<RequestContract.View>
        implements RequestContract.Presenter {

    private DataManager mDataManager;

    RequestPresenter() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public RequestContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(RequestContract.View view) {
        super.attachView(view);

        // after attaching a view get all the friend requests
        getFriendRequestList();
    }

    /**
     * Implement this method to get the all friend request sent/received
     */
    @Override
    public void getFriendRequestList() {
        mDataManager.getFriendRequests(new OnFriendRequest() {
            @Override
            public void onData(List<AllUsers> data) {
                getView().onFriendReq(data);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Implement this method for what to do when user clicks on Accept friend request button
     *
     * @param id Id of user on which click action is performed
     */
    @Override
    public void onPositiveClick(String id) {
        mDataManager.acceptFriendRequest(id, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                /* User has successfully accepted friend request */
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Implement this method for what to do when user clicks on cancel/ decline friend request button
     *
     * @param id Id of a user on which click action is performed
     */
    @Override
    public void onNegativeClick(String id) {
        mDataManager.ignoreFriendRequest(id, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                /* User has successfully canceled or declined friend request */
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }
}
