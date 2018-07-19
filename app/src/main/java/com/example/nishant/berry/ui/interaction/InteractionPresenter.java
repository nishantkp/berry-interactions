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
 * File Created on 11/06/18 11:28 PM by nishant
 * Last Modified on 11/06/18 11:28 PM
 */

package com.example.nishant.berry.ui.interaction;

import android.content.Intent;
import android.text.TextUtils;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnInteraction;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.InteractionActionBar;
import com.example.nishant.berry.ui.model.Message;
import com.example.nishant.berry.ui.utils.GetTimeAgo;

import java.util.List;

/**
 * Presenter that deals with getting messages and relevant information from firebase database
 * by dealing with {@link DataManager}
 */
public class InteractionPresenter
        extends BasePresenter<InteractionContract.View>
        implements InteractionContract.Presenter {

    private String mInteractionUserId;
    private DataManager mDataManager;

    InteractionPresenter(Intent receivedIntent) {
        // Extract the userId and user displayName from intent
        mInteractionUserId = receivedIntent.hasExtra(IConstants.KEY_USER_ID) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_ID) : null;
        /*
        String displayName = receivedIntent.hasExtra(IConstants.KEY_USER_DISPLAY_NAME) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_DISPLAY_NAME) : null;
        */
        if (mInteractionUserId == null) return;
        mDataManager = DataManager.getInstance();
    }

    @Override
    public InteractionContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(InteractionContract.View view) {
        super.attachView(view);
        getView().setUpRecyclerView();
        updateMessageList();
        extractBasicInfoDatabase();
    }

    /**
     * Call this method to extract the basic information about user from database
     * like display name, thumb avatar url, user online status etc
     */
    @Override
    public void extractBasicInfoDatabase() {
        mDataManager.getChatUserInfo(mInteractionUserId, new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId) {
                String onlineStatus;
                if (model.isOnline()) {
                    onlineStatus = "Online";
                } else {
                    // Get the last seen time in human readable format
                    onlineStatus = GetTimeAgo.getTimeAgo(model.getLast_seen());
                }

                // Create a model for setting views in custom actionbar
                InteractionActionBar actionBarModel = new InteractionActionBar();
                actionBarModel.setAvatarUrl(model.getThumbnail());
                actionBarModel.setName(model.getName());
                actionBarModel.setOnlineStatus(onlineStatus);

                // Set callback for setting up action bar
                getView().setActionBar(actionBarModel);
                // Callback for updating RecyclerView with interaction user avatar
                getView().interactionUserAvatar(model.getThumbnail());
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * This method will be invoked when user clicks send button from @{@link InteractionActivity}
     * layout
     *
     * @param message message user want to send
     */
    @Override
    public void onInteractions(String message) {
        if (TextUtils.isEmpty(message)) return;

        mDataManager.onInteraction(mInteractionUserId, message, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                getView().clearEditTextField();
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Get the messages from database and set callback to update recycler view
     */
    @Override
    public void updateMessageList() {
        mDataManager.getMessageList(mInteractionUserId, new OnInteraction() {
            @Override
            public void onInteractions(List<Message> messageList) {
                getView().updateMessageList(messageList);
                getView().onSwipeRefreshComplete();
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }

            @Override
            public void listOffset(int position) {
                /* Not needed for now */
            }
        });
    }

    /**
     * Call this method to refresh message list i.e swipe refresh layout
     */
    @Override
    public void swipeMessageRefresh() {
        mDataManager.loadMoreMessages(mInteractionUserId, new OnInteraction() {
            @Override
            public void onInteractions(List<Message> messageList) {
                getView().updateMessageList(messageList);
                getView().onSwipeRefreshComplete();
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }

            @Override
            public void listOffset(int position) {
                getView().setLayoutManagerOffset(position);
            }
        });
    }
}
