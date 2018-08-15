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
 * Last Modified on 15/08/18 2:47 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnUserProfile;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.ui.model.AllUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Utility class that is responsible for getting user profile and friend request status from
 * firebase database
 */
@Singleton
public final class ProfileUseCase {
    private FbUsersUseCase mFbUsersUseCase;

    @Inject
    public ProfileUseCase(FbUsersUseCase fbUsersUseCase) {
        mFbUsersUseCase = fbUsersUseCase;
    }

    /**
     * Call this method to get the user profile
     * <p>
     * This method also sets callback for whether the current user has sent/ received friend
     * request to this particular user(i.e specified by userId) or not
     * Callback also provide detail info about particular user like name, thumbnail, online status etc.
     *
     * @param userId   Id of user whose profile we are interested in
     * @param callback DataCallback for user info, friend request status and error
     */
    void getUserProfile(String userId, @NonNull final OnUserProfile callback) {
        mFbUsersUseCase.getUsersObject(userId, new OnUsersData() {
            @Override
            public void onData(final AllUsers model, final String userId) {
                model.setId(userId);
                callback.onData(model);
                // Query friend requests database to get the send/received friend request
                queryFriendRequestDatabase(userId, callback);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Call this method to query friend-request database to find sent/received friend request
     * If we don't find user into friend request database query friends database to check whether
     * user is friend or not and set callbacks accordingly
     *
     * @param userId   Id of a user on whose profile current user is on
     * @param callback DataCallback for friend request status and error
     */
    private void queryFriendRequestDatabase(final String userId, @NonNull final OnUserProfile callback) {
        mFbUsersUseCase.getFriendReqRef().child(mFbUsersUseCase.getCurrentUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(userId)) {
                            String reqType = Objects.requireNonNull(dataSnapshot.child(userId)
                                    .child(IFirebaseConfig.FRIEND_REQUEST_TYPE)
                                    .getValue())
                                    .toString();
                            switch (reqType) {
                                case IFirebaseConfig.FRIEND_REQUEST_RECEIVED:
                                    callback.onFriendRequestReceived();
                                    break;
                                case IFirebaseConfig.FRIEND_REQUEST_SENT:
                                    callback.onFriendRequestSent();
                                    break;
                            }
                        } else {
                            // There is no child with id present in friend-requests database
                            // so query friends database to check whether this user is friend or not
                            queryFriendsDatabase(userId, callback);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError("Error retrieving user profile info");
                    }
                });
    }

    /**
     * Call this method to query friends database to check whether user is friend or not
     *
     * @param userId   Id of user for which we are querying friends database
     * @param callback DataCallback for friends status and error
     */
    private void queryFriendsDatabase(final String userId, @NonNull final OnUserProfile callback) {
        mFbUsersUseCase.getFriendsRef().child(mFbUsersUseCase.getCurrentUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(userId)) {
                            // If we find the user id, that means user is already friend and set
                            // callback accordingly
                            callback.onFriend();
                        } else {
                            // Otherwise user is not friend
                            callback.onSendFriendRequest();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError("Error retrieving user profile");
                    }
                });
    }
}
