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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnFriendRequest;
import com.example.nishant.berry.data.callbacks.OnFriendRequestUserData;
import com.example.nishant.berry.databinding.FriendRequestListItemBinding;
import com.example.nishant.berry.ui.adapter.FriendRequestViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.FriendRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class which deals with displaying friend request and performing action when
 * accept/cancel/decline button is pressed
 */
final class RequestsUtils
        implements FriendRequestViewHolder.onButtonClick {

    // Log tag
    private static final String LOG_TAG = RequestsUtils.class.getSimpleName();
    private static FirebaseUtils sFirebaseUtils;

    RequestsUtils() {
        sFirebaseUtils = new FirebaseUtils();
    }

    /**
     * Call this method to get the friend requests of current user
     * All the request, whether user has sent or received it
     *
     * @param callback DataCallback for error and firebase adapter to display list of friend
     *                 requests whether sent or received
     */
    void getCurrentUsersFriendRequests(@NonNull final OnFriendRequest callback) {

        // Firebase query for Friend request object
        Query reqQuery = DataManager.getCurrentUserFriendsReqRef();

        FirebaseRecyclerOptions<FriendRequest> options =
                new FirebaseRecyclerOptions.Builder<FriendRequest>().setQuery(reqQuery, FriendRequest.class)
                        .build();

        // Adapter
        FirebaseRecyclerAdapter<FriendRequest, FriendRequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<FriendRequest, FriendRequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendRequestViewHolder holder,
                                                    int position,
                                                    @NonNull FriendRequest model) {
                        final String listUserId = getRef(position).getKey();
                        final String requestType = model.getRequest_type();
                        if (listUserId == null) return;
                        sFirebaseUtils.getUsersObject(listUserId, requestType, holder,
                                new OnFriendRequestUserData() {
                                    @Override
                                    public void onData(AllUsers model, String userId, FriendRequestViewHolder holder) {
                                        // bind the {@link AllUsers} model to view
                                        holder.bind(model, userId);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        callback.onError(error);
                                    }
                                });
                    }

                    @NonNull
                    @Override
                    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {

                        // Inflate the view
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.friend_request_list_item, parent, false);

                        return new FriendRequestViewHolder(FriendRequestListItemBinding.bind(view),
                                RequestsUtils.this);
                    }
                };
        // Set call back for firebase recycler adapter, so that in fragment/activity we can start
        // and stop listing to adapter in onStart() and onPause() methods respectively
        // By doing so we can get data from Firebase Database
        callback.onAdapter(adapter);
    }

    /**
     * {@link FriendRequestViewHolder} callback when user clicks on accept friend request button
     * from list
     *
     * @param listUserId Id of a user who has sent current user a request or to whom current user
     *                   has sent a request
     */
    @Override
    public void onPositiveClick(String listUserId) {
        String currentUserId = DataManager.getCurrentUserId();
        // Get the current date and time
        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        // HashMap for updating friends object and friend requests object
        Map<String, Object> friendsMap = new HashMap<>();
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + currentUserId + "/" + listUserId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + listUserId + "/" + currentUserId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + listUserId, null);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + listUserId + "/" + currentUserId, null);

        DataManager.getRootRef().updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d(LOG_TAG, "Unable to accept request!");
                }
            }
        });
    }

    /**
     * {@link FriendRequestViewHolder} callback when user clicks on cancel/decline friend request
     * button from list
     *
     * @param listUserId Id of a user who has sent current user a request or to whom current user
     *                   has sent a request
     */
    @Override
    public void onNegativeClick(String listUserId) {
        String currentUserId = DataManager.getCurrentUserId();
        // HashMap to delete friend requests from friend requests table
        Map<String, Object> cancelMap = new HashMap<>();
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + listUserId, null);
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + listUserId + "/" + currentUserId, null);

        DataManager.getRootRef().updateChildren(cancelMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d(LOG_TAG, "Unable to cancel request!");
                }
            }
        });
    }
}
