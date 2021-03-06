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
 * File Created on 17/06/18 5:23 PM by nishant
 * Last Modified on 17/06/18 5:23 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.chat;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.interaction.InteractionActivity;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.List;

/**
 * Presenter that deals with setting up the FirebaseRecyclerAdapter and querying the Firebase
 * database to get the appropriate values
 */
public class ChatPresenter
        extends BasePresenter<ChatContract.View>
        implements ChatContract.Presenter {

    ChatPresenter() {
    }

    @Override
    public ChatContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(ChatContract.View view) {
        super.attachView(view);
        getCurrentUsersChatList();
    }

    /**
     * Provide implementation of this method to get current user's chat list
     */
    @Override
    public void getCurrentUsersChatList() {
        DataManager.getInstance().getChatList(new OnUsersList() {
            @Override
            public void onData(List<AllUsers> data) {
                getView().onInteractionData(data);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Provide implementation of this method to start {@link InteractionActivity}
     *
     * @param userId      user id
     * @param displayName user name
     */
    @Override
    public void onItemClick(String userId, String displayName) {
        getView().onCreateInteractionActivity(userId, displayName);
    }
}
