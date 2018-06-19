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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.databinding.FriendsMessageListItemBinding;
import com.example.nishant.berry.ui.adapter.FriendsInteractionViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.FriendsInteraction;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Presenter that deals with setting up the FirebaseRecyclerAdapter and querying the Firebase
 * database to get the appropriate values
 */
public class ChatPresenter
        extends BasePresenter<ChatContract.View>
        implements ChatContract.Presenter {

    private Query mQuery;
    private String mCurrentUserId;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mMessageDatabaseReference;

    ChatPresenter() {
        // Current user Id
        mCurrentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Query for Interactions database
        mQuery = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.INTERACTIONS_OBJECT)
                .child(mCurrentUserId)
                .orderByChild(IFirebaseConfig.TIMESTAMP);

        // Users database reference
        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.USERS_OBJECT);

        // Message database reference
        mMessageDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.MESSAGE_OBJECT)
                .child(mCurrentUserId);
    }

    @Override
    public ChatContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(ChatContract.View view) {
        super.attachView(view);
        setupRecyclerAdapter(mQuery);
    }

    /**
     * Provide implementation of Firebase Recycler Adapter
     *
     * @param query Firebase Query
     */
    @Override
    public void setupRecyclerAdapter(Query query) {
        FirebaseRecyclerOptions<FriendsInteraction> options =
                new FirebaseRecyclerOptions.Builder<FriendsInteraction>()
                        .setQuery(query, FriendsInteraction.class)
                        .build();

        // Firebase recycler adapter
        FirebaseRecyclerAdapter<FriendsInteraction, FriendsInteractionViewHolder> adapter =
                new FirebaseRecyclerAdapter<FriendsInteraction, FriendsInteractionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendsInteractionViewHolder holder,
                                                    int position,
                                                    @NonNull FriendsInteraction model) {
                        // Single element array
                        final AllUsers[] users = {new AllUsers()};
                        // UserId of user with whom we are chatting
                        final String listUserId = getRef(position).getKey();
                        assert listUserId != null;

                        // Get basic information like name, avatar of user and update the
                        // list item view
                        mUsersDatabaseReference.child(listUserId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String displayName = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.NAME).getValue()).toString();
                                        String thumbnail = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.THUMBNAIL).getValue()).toString();

                                        users[0].setName(displayName);
                                        users[0].setThumbnail(thumbnail);
                                        holder.bind(users[0]);

                                        // Set list item onCLickListener to open InteractionActivity
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getView().onListItemClick(listUserId, displayName);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        // Database query to get the very last message send or received by user
                        Query lastMessageQuery = mMessageDatabaseReference
                                .child(listUserId)
                                .limitToLast(1);
                        // Get the last message and update the list item view
                        lastMessageQuery.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                String lastMessage = Objects.requireNonNull(dataSnapshot
                                        .child(IFirebaseConfig.MESSAGE_DATA)
                                        .getValue()).toString();
                                boolean messageSeen = (boolean) dataSnapshot.child(IFirebaseConfig.MESSAGE_SEEN).getValue();

                                users[0].setStatus(lastMessage);
                                users[0].setMessageSeen(messageSeen);
                                holder.bind(users[0]);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FriendsInteractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                           int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.friends_message_list_item, parent, false);
                        return new FriendsInteractionViewHolder(FriendsMessageListItemBinding.bind(view));
                    }
                };

        // Set call back for firebase recycler adapter
        // So that we can set adapter on RecyclerView in fragment
        // This will also help in start and stop the listening the adapter in onStart() and onStop()
        // methods
        getView().getFirebaseRecyclerAdapter(adapter);
    }
}
