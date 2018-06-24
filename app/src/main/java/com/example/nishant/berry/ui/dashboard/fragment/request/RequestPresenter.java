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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.databinding.FriendRequestListItemBinding;
import com.example.nishant.berry.ui.adapter.FriendRequestViewHolder;
import com.example.nishant.berry.ui.adapter.FriendsInteractionViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.FriendRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Presenter that responsible for displaying friends request, in Request tab in Dashboard
 */
public class RequestPresenter
        extends BasePresenter<RequestContract.View>
        implements RequestContract.Presenter, FriendRequestViewHolder.onButtonClick {

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mRootReference;
    private Query mRequestQuery;

    RequestPresenter() {
        mRootReference = FirebaseDatabase.getInstance().getReference();

        //Database reference to Users object
        mUsersDatabaseReference = mRootReference.child(IFirebaseConfig.USERS_OBJECT);

        // Firebase query for Friend request object
        mRequestQuery = mRootReference
                .child(IFirebaseConfig.FRIEND_REQUEST_OBJECT)
                .child(DataManager.getCurrentUserId());
    }

    @Override
    public RequestContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(RequestContract.View view) {
        super.attachView(view);

        // after attaching a view setup firebase recycler adapter
        setupFirebaseRecyclerAdapter(mRequestQuery);
    }

    /**
     * Implement this method to setup Firebase recycler adapter
     *
     * @param query Database friend request object query
     */
    @Override
    public void setupFirebaseRecyclerAdapter(Query query) {
        FirebaseRecyclerOptions<FriendRequest> options =
                new FirebaseRecyclerOptions.Builder<FriendRequest>().setQuery(query, FriendRequest.class)
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

                        // Get information about user who has sent the request or whom current
                        // user has sent the request from Firebase's Users database
                        mUsersDatabaseReference.child(listUserId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        AllUsers user = new AllUsers();
                                        String name = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.NAME).getValue()).toString();
                                        String status = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.STATUS).getValue()).toString();
                                        String thumbnail = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.THUMBNAIL).getValue()).toString();
                                        user.setName(name);
                                        user.setStatus(status);
                                        user.setThumbnail(thumbnail);
                                        user.setFriendRequestType(requestType);

                                        // Update list item layout
                                        holder.bind(user, DataManager.getCurrentUserId(), listUserId);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                RequestPresenter.this);
                    }
                };
        // Set call back for firebase recycler adapter, so that in fragment we can start and stop
        // listing to adapter in onStart() and onPause() methods respectively
        // By doing so we can get data from Firebase Database
        getView().setFirebaseAdapterWithRecyclerView(adapter);
    }

    /**
     * Provide implementation of this method for what to do when user clicks on "Accept" button
     *
     * @param currentUserId current user's Id
     * @param listUserId    Id of a user who had sent current user a friend a request
     */
    @Override
    public void onPositiveClick(String currentUserId, String listUserId) {
        // Get the current date and time
        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

        // HashMap for updating friends object and friend requests object
        Map<String, Object> friendsMap = new HashMap<>();
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + currentUserId + "/" + listUserId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIENDS_OBJECT + "/" + listUserId + "/" + currentUserId + "/" + IFirebaseConfig.FRIEND_SINCE, currentDateTime);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + listUserId, null);
        friendsMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + listUserId + "/" + currentUserId, null);

        mRootReference.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    getView().onError("Unable to accept request!");
                }
            }
        });
    }

    /**
     * Provide implementation of this method for what to do when user clicks on "decline" or
     * "cancel request" button
     *
     * @param currentUserId current user's Id
     * @param listUserId    Id of a user to whom current user has sent friend request or
     *                      user who has sent current user a friend request
     */
    @Override
    public void onNegativeClick(String currentUserId, String listUserId) {
        // HashMap to delete friend requests from friend requests table
        Map<String, Object> cancelMap = new HashMap<>();
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + currentUserId + "/" + listUserId, null);
        cancelMap.put(IFirebaseConfig.FRIEND_REQUEST_OBJECT + "/" + listUserId + "/" + currentUserId, null);

        mRootReference.updateChildren(cancelMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    getView().onError("Unable to cancel request!");
                }
            }
        });
    }
}
