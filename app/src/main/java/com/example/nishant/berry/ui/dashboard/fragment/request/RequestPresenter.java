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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
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

import java.util.Objects;

/**
 * Presenter that responsible for displaying friends request, in Request tab in Dashboard
 */
public class RequestPresenter
        extends BasePresenter<RequestContract.View>
        implements RequestContract.Presenter, FriendRequestViewHolder.onButtonClick {

    private DatabaseReference mUsersDatabaseReference;
    private Query mRequestQuery;
    private String mCurrentUserId;

    RequestPresenter() {
        // Current user Id
        mCurrentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //Database reference to Users object
        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.USERS_OBJECT);

        // Firebase query for Friend request object
        mRequestQuery = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.FRIEND_REQUEST_OBJECT)
                .child(mCurrentUserId);
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

    @Override
    public void setupFirebaseRecyclerAdapter(Query query) {
        FirebaseRecyclerOptions<FriendRequest> options =
                new FirebaseRecyclerOptions.Builder<FriendRequest>().setQuery(query, FriendRequest.class)
                        .build();

        FirebaseRecyclerAdapter<FriendRequest, FriendRequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<FriendRequest, FriendRequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendRequestViewHolder holder,
                                                    int position,
                                                    @NonNull FriendRequest model) {
                        final String listUserId = getRef(position).getKey();
                        final String requestType = model.getRequest_type();
                        if (listUserId == null) return;
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
                                        holder.bind(user, mCurrentUserId, listUserId);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @NonNull
                    @Override
                    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        // Inflate the view
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.friend_request_list_item, parent, false);

                        return new FriendRequestViewHolder(FriendRequestListItemBinding.bind(view), RequestPresenter.this);
                    }
                };

        getView().setFirebaseAdapterWithRecyclerView(adapter);
    }

    @Override
    public void onPositiveClick(String currentUserId, String listUserId) {
        Log.i("button click", "POSITIVE : current id : " + currentUserId + "list user id: " + listUserId);
    }

    @Override
    public void onNegativeClick(String currentUserId, String listUserId) {
        Log.i("button click", "NEGATIVE current id : " + currentUserId + "list user id: " + listUserId);
    }
}
