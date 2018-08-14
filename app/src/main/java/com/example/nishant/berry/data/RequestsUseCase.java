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
public final class RequestsUseCase {
    // Log tag
    private static final String LOG_TAG = RequestsUseCase.class.getSimpleName();
    private FbUsersUseCase mFbUsersUseCase;

    RequestsUseCase(FbUsersUseCase fbUsersUseCase) {
        mFbUsersUseCase = fbUsersUseCase;
    }

    /**
     * Call this method to get the list of friend request by current user
     * whether sent/received
     *
     * @param callback DataCallback for list of users and error
     */
    void getCurrentUsersFriendReq(@NonNull final OnUsersList callback) {
        // Firebase query for Friend request object
        Query reqQuery = mFbUsersUseCase.getCurrentUserFriendsReqRef();

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
        mFbUsersUseCase.getUsersObject(key, new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId) {
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
     * <p>
     * i.e if user1 accept the request from user2 friends table would look like
     * Firebase Object Structure:
     * <p>
     * friends{
     * user1_key{
     * user2_key{
     * date: currentDate
     * }
     * }
     * user2_key{
     * user1_key{
     * date: currentDate
     * }
     * }
     * }
     *
     * @param userId   Id of a user who has sent current user a request or to whom current user
     *                 has sent a request
     * @param callback DataCallback for task success/ failure
     */
    void acceptFriendRequest(String userId, @NonNull final OnTaskCompletion callback) {
        String currentUserId = mFbUsersUseCase.getCurrentUserId();
        // Get the current date and time
        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        // HashMap for updating friends object and friend requests object
        Map<String, Object> friendsMap = new HashMap<>();
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + currentUserId + "/" + userId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + userId + "/" + currentUserId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + userId, null);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + userId + "/" + currentUserId, null);

        mFbUsersUseCase.getRootRef().updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
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
        String currentUserId = mFbUsersUseCase.getCurrentUserId();
        // HashMap to delete friend requests from friend requests table
        Map<String, Object> cancelMap = new HashMap<>();
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + userId, null);
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + userId + "/" + currentUserId, null);

        mFbUsersUseCase.getRootRef().updateChildren(cancelMap, new DatabaseReference.CompletionListener() {
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

    /**
     * Call this method to send user a friend request
     * This method also update the Notifications object, so that firebase function gets invoked and
     * it will send notification to respective device.
     * <p>
     * Firebase Object Structure:
     * friend_request:
     * <p>
     * user1_key{
     * user2_key{
     * request_type : sent
     * }
     * }
     * <p>
     * user2_key{
     * user1_key{
     * request_type: received
     * }
     * }
     *
     * @param userId   Id of user to whom current user wants to send request
     * @param callback DataCallback for task success/ failure
     */
    void sendFriendRequest(String userId, @NonNull final OnTaskCompletion callback) {
        DatabaseReference rootRef = mFbUsersUseCase.getRootRef();
        String currentUserId = mFbUsersUseCase.getCurrentUserId();

        // Get the notification ID
        DatabaseReference notificationReference = rootRef.child(IFirebaseConfig.NOTIFICATION_OBJECT).child(userId).push();
        String notificationId = notificationReference.getKey();

        // HashMap for notification details
        HashMap<String, String> notificationMap = new HashMap<>();
        notificationMap.put(IFirebaseConfig.NOTIFICATION_FROM, currentUserId);
        notificationMap.put(IFirebaseConfig.NOTIFICATION_TYPE, IFirebaseConfig.NOTIFICATION_TYPE_REQUEST);

        // Update database
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + userId + "/" + IFirebaseConfig.FRIEND_REQUEST_TYPE, IFirebaseConfig.FRIEND_REQUEST_SENT);
        requestMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + userId + "/" + currentUserId + "/" + IFirebaseConfig.FRIEND_REQUEST_TYPE, IFirebaseConfig.FRIEND_REQUEST_RECEIVED);
        requestMap.put(IFirebaseConfig.NOTIFICATION_OBJECT + "/" + userId + "/" + notificationId, notificationMap);
        rootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    callback.onError("Unable to send friend request");
                } else {
                    callback.onSuccess();
                }
            }
        });
    }

    /**
     * Call this method to unfriend a user
     *
     * @param userId   Id of user to whom current user wants to unfriend
     * @param callback DataCallback for task success/ failure
     */
    void unfriendUser(String userId, @NonNull final OnTaskCompletion callback) {
        DatabaseReference rootRef = mFbUsersUseCase.getRootRef();
        String currentUserId = mFbUsersUseCase.getCurrentUserId();

        // HashMap to unfriend user
        Map<String, Object> unfriendMap = new HashMap<>();
        unfriendMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + currentUserId + "/" + userId, null);
        unfriendMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + userId + "/" + currentUserId, null);

        rootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    callback.onError("Error while unfriend!");
                    return;
                }
                callback.onSuccess();
            }
        });
    }
}
