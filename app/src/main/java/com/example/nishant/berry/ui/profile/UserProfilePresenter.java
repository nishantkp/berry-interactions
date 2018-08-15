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
 * Last Modified on 15/08/18 6:42 PM
 */

package com.example.nishant.berry.ui.profile;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUserProfile;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.UserProfile;

/**
 * Presenter that deals with getting data from firebase database to populate user profile layout
 */
public class UserProfilePresenter
        extends BasePresenter<UserProfileContract.View>
        implements UserProfileContract.Presenter {

    private int mCurrentState;
    private String mNewUserId;
    private UserProfile mUserProfile;
    private DataManager mDataManager;

    UserProfilePresenter(String userId, DataManager dataManager) {
        if (userId == null) return;
        mDataManager = dataManager;
        mNewUserId = userId;
        mUserProfile = new UserProfile();

        // Get user's profile
        getDataFromFirebaseDatabase();
    }

    @Override
    public UserProfileContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(UserProfileContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to get the data from firebase database about particular user
     */
    @Override
    public void getDataFromFirebaseDatabase() {
        mDataManager.getUserProfile(mNewUserId, new OnUserProfile() {
            @Override
            public void onFriendRequestReceived() {
                mCurrentState = IFirebaseConfig.REQ_RECEIVED;
                mUserProfile.setFriendReqButtonText("accept friend request");
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_VISIBLE);
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onFriendRequestSent() {
                mCurrentState = IFirebaseConfig.REQ_SENT;
                mUserProfile.setFriendReqButtonText("cancel friend request");
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onSendFriendRequest() {
                mCurrentState = IFirebaseConfig.NOT_FRIEND;
                mUserProfile.setFriendReqButtonText("send friend request");
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onFriend() {
                mCurrentState = IFirebaseConfig.FRIENDS;
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                mUserProfile.setFriendReqButtonText("unfriend");
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onData(AllUsers model) {
                mUserProfile.setDisplayName(model.getName());
                mUserProfile.setStatus(model.getStatus());
                mUserProfile.setAvatar(model.getImage());
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * When user presses send friend request button, this method will be executed
     */
    @Override
    public void sendFriendRequestButtonClick() {
        switch (mCurrentState) {
            case IFirebaseConfig.NOT_FRIEND:
                sendRequest();
                break;
            case IFirebaseConfig.REQ_SENT:
                cancelRequest();
                break;
            case IFirebaseConfig.REQ_RECEIVED:
                acceptRequest();
                break;
            case IFirebaseConfig.FRIENDS:
                removeFriend();
                break;
        }
    }

    /**
     * Provide implementation of this method to send friend request
     */
    @Override
    public void sendRequest() {
//        // As soon as user hits send request button, disable the button
//        // so user can't be able to press another time and no new requests are made to
//        // firebase database
//        mUserProfile.setFriendReqButtonEnabled(false);
//        getView().updateProfile(mUserProfile);
        mDataManager.sendFriendRequest(mNewUserId, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                mCurrentState = IFirebaseConfig.REQ_SENT;
                mUserProfile.setFriendReqButtonText("cancel friend request");
                getView().updateProfile(mUserProfile);
                getView().friendRequestSentSuccessfully("Friend request sent!");
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Provide implementation of this method to cancel friend request
     */
    @Override
    public void cancelRequest() {
        mDataManager.ignoreFriendRequest(mNewUserId, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                mCurrentState = IFirebaseConfig.NOT_FRIEND;
                mUserProfile.setFriendReqButtonEnabled(true);
                mUserProfile.setFriendReqButtonText("send friend request");
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Provide implementation of this method to accept the friend request
     */
    @Override
    public void acceptRequest() {
        mDataManager.acceptFriendRequest(mNewUserId, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                mCurrentState = IFirebaseConfig.FRIENDS;
                mUserProfile.setFriendReqButtonText("unfriend");
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * Provide implementation of this method to remove friend
     */
    @Override
    public void removeFriend() {
        mDataManager.unfriendUser(mNewUserId, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                mCurrentState = IFirebaseConfig.NOT_FRIEND;
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                mUserProfile.setFriendReqButtonText("send friend request");
                getView().updateProfile(mUserProfile);
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
            }
        });
    }

    /**
     * This method will be called when user clicks on decline request button
     */
    @Override
    public void onDeclineFriendRequestBtnClick() {
        cancelRequest();
        mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
        getView().updateProfile(mUserProfile);
    }
}
