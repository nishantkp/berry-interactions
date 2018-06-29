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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IFirebaseConfig;
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
class RequestsUtils
        implements FriendRequestViewHolder.onButtonClick, FirebaseUtils.FriendReqUsersObjectCallback {

    private FriendRequestCallback mFriendReqCallback;
    private FirebaseUtils mFirebaseUtils;

    void setFriendRequestCallbacks(FriendRequestCallback callbacks) {
        mFriendReqCallback = callbacks;
        mFirebaseUtils = new FirebaseUtils();
        mFirebaseUtils.setFriendReqUserObjectCallbacks(this);
    }

    /**
     * Call this method to get the friend requests of current user
     * All the request, whether user has sent or received it
     */
    void getCurrentUsersFriendRequests() {
        // Safety to avoid NullPointerException
        if (mFriendReqCallback == null) return;

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
                        mFirebaseUtils.getUsersObject(listUserId, requestType, holder);
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
        mFriendReqCallback.onFriendReqFirebaseAdapter(adapter);
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
                    mFriendReqCallback.onFriendReqError("Unable to accept request!");
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
                    mFriendReqCallback.onFriendReqError("Unable to cancel request!");
                }
            }
        });
    }

    /**
     * {@link FirebaseUtils} callback to bind the {@link AllUsers} model to view
     *
     * @param model  {@link AllUsers} model containing details about user
     * @param userId id of user which is being displayed
     * @param holder FriendsRequestViewHolder instance to bind model to view
     */
    @Override
    public void onFirebaseUsersObject(AllUsers model, String userId, FriendRequestViewHolder holder) {
        holder.bind(model, userId);
    }

    /**
     * {@link FirebaseUtils} callback for error occurred while getting data from firebase database
     *
     * @param error error message
     */
    @Override
    public void onFirebaseUsersObjectError(String error) {
        mFriendReqCallback.onFriendReqError(error);
    }

    /**
     * FriendRequest callbacks which deals with error while retrieving data from firebase database
     * and provides FirebaseRecyclerAdapter to set it on RecyclerView to display list of requests
     */
    interface FriendRequestCallback {
        void onFriendReqError(String error);

        void onFriendReqFirebaseAdapter(FirebaseRecyclerAdapter adapter);
    }
}
