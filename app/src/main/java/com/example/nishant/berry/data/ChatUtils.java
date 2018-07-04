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
 * File Created on 03/07/18 7:22 PM by nishant
 * Last Modified on 03/07/18 7:22 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.databinding.FriendsMessageListItemBinding;
import com.example.nishant.berry.ui.adapter.FriendsInteractionViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.FriendsInteraction;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * ChatUtility class to display chat list
 */
class ChatUtils {

    private ChatCallback mChatCallback;

    ChatUtils() {
    }

    /**
     * Use this method to set chat callbacks
     * Must set it before calling getChatList() method to get firebase adapter
     *
     * @param callbacks Must be initiated by class which implements this callback
     *                  i.e {@link DataManager} class
     */
    void setChatCallbacks(@NonNull ChatCallback callbacks) {
        mChatCallback = callbacks;
    }

    /**
     * Call this method to get the user's chat list
     * This method sets callback for firebase adapter and list item click
     */
    void getChatList() {
        // Safety for null pointer exception
        if (mChatCallback == null) return;

        // Query for Interactions database
        Query query = DataManager.getCurrentUserInteractionRef()
                .orderByChild(IFirebaseConfig.TIMESTAMP);

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

                        final AllUsers[] users = {new AllUsers()};
                        // UserId of user with whom we are chatting
                        final String listUserId = getRef(position).getKey();
                        assert listUserId != null;

                        // Get basic information like name, avatar of user and update the
                        // list item view
                        DataManager.getUsersRef().child(listUserId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // Extract the data from FirebaseUtils helper
                                        users[0] = FirebaseUtils.extractValues(dataSnapshot);
                                        holder.bind(users[0]);

                                        // Set list item onCLickListener to open InteractionActivity
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mChatCallback.onChatListItemClick(listUserId, users[0].getName());
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });

                        // Database query to get the very last message sent or received by user
                        Query lastMessageQuery = DataManager.getCurrentUserMessageRef()
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
        // So that we can set adapter on RecyclerView in fragment/ activity
        // This will also help in start and stop the listening the adapter in onStart() and onStop()
        // methods
        mChatCallback.onChatAdapter(adapter);
    }

    /**
     * Interface for firebase adapter and list item click
     */
    interface ChatCallback {
        void onChatAdapter(FirebaseRecyclerAdapter adapter);

        void onChatListItemClick(String userId, String name);
    }
}
