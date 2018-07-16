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
 * File Created on 28/06/18 6:42 PM by nishant
 * Last Modified on 28/06/18 6:42 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.adapter.AllUsersViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.FriendRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class which deals with displaying friend request and performing action when
 * accept/cancel/decline button is pressed
 */
final class RequestsUtils {

    // Log tag
    private static final String LOG_TAG = RequestsUtils.class.getSimpleName();
    private static FirebaseUtils sFirebaseUtils;

    RequestsUtils() {
        sFirebaseUtils = new FirebaseUtils();
    }

    /**
     * Call this method to get the list of friend request by current user
     * whether sent/received
     *
     * @param callback DataCallback for list of users and error
     */
    void getCurrentUsersFriendReq(@NonNull final OnUsersList callback) {
        // Firebase query for Friend request object
        Query reqQuery = DataManager.getCurrentUserFriendsReqRef();

        reqQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AllUsers> reqList = new ArrayList<>();

                // If data Snapshot does't exists that means user has dealt with all the friend requests
                // and there is no friend request object for current user so set empty list on callback
                if (!dataSnapshot.exists()) {
                    callback.onData(new ArrayList<AllUsers>());
                    return;
                }

                // Otherwise loop through data snapshot to get the details about current user
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    FriendRequest object = data.getValue(FriendRequest.class);
                    if (object != null)
                        getUsersObject(data.getKey(), object.getRequest_type(), reqList, callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Call this method to get the detail about user from it's id
     *
     * @param key          id of a user to whom current user has sent a request
     *                     or from received a request
     * @param request_type request type : sent or received
     * @param reqList      list of users to whom current user has sent/ from whom received requests
     * @param callback     DataCallback for list of users and error
     */
    private void getUsersObject(final String key,
                                final String request_type,
                                final List<AllUsers> reqList,
                                @NonNull final OnUsersList callback) {
        sFirebaseUtils.getUsersObject(key, null, new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId, AllUsersViewHolder holder) {
                model.setId(userId);
                model.setFriendRequestType(request_type);
                if (!reqList.contains(model)) {
                    reqList.add(model);
                }
                callback.onData(reqList);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Call this method to accept the friend request
     * This method will update the Friends object with userIds and simultaneously delete the
     * friend request references from FriendRequest object from firebase
     *
     * @param userId   Id of a user who has sent current user a request or to whom current user
     *                 has sent a request
     * @param callback DataCallback for task success/ failure
     */
    void acceptFriendRequest(String userId, @NonNull final OnTaskCompletion callback) {
        String currentUserId = DataManager.getCurrentUserId();
        // Get the current date and time
        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        // HashMap for updating friends object and friend requests object
        Map<String, Object> friendsMap = new HashMap<>();
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + currentUserId + "/" + userId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + userId + "/" + currentUserId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + userId, null);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + userId + "/" + currentUserId, null);

        DataManager.getRootRef().updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    callback.onError(databaseError.getMessage());
                    Log.d(LOG_TAG, "Unable to accept request!");
                } else {
                    callback.onSuccess();
                }
            }
        });
    }

    /**
     * Call this method to cancel or decline the friend request
     * This method will remove references of friend requests from FriendRequest object in firebase
     * database
     *
     * @param userId   Id of a user who has sent current user a request or to whom current user
     *                 has sent a request
     * @param callback DataCallback for task success/ failure
     */
    void ignoreFriendRequest(String userId, @NonNull final OnTaskCompletion callback) {
        String currentUserId = DataManager.getCurrentUserId();
        // HashMap to delete friend requests from friend requests table
        Map<String, Object> cancelMap = new HashMap<>();
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + userId, null);
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + userId + "/" + currentUserId, null);

        DataManager.getRootRef().updateChildren(cancelMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    callback.onError(databaseError.getMessage());
                    Log.d(LOG_TAG, "Unable to cancel request!");
                } else {
                    callback.onSuccess();
                }
            }
        });
    }
}
