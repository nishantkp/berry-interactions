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
 * File Created on 27/06/18 6:53 PM by nishant
 * Last Modified on 27/06/18 6:53 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.adapter.AllUsersViewHolder;
import com.example.nishant.berry.ui.dashboard.fragment.friends.FriendsFragment;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.Friends;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

/**
 * FriendsUtility class to get the friends list of current user in {@link FriendsFragment}
 * with the help of FirebaseRecyclerAdapter
 */
class FriendsUtils implements FirebaseUtils.UsersObjectCallback {
    private FirebaseUtils mFirebaseUtils;
    private FriendsListCallback mFriendsListCallback;

    FriendsUtils() {
    }

    /**
     * Use this method set the callback for result when inquiring firebase database for friend list
     * of current user
     *
     * @param callback Must be initialized by class which implements the callback
     */
    void setFriendsListCallback(FriendsListCallback callback) {
        mFriendsListCallback = callback;

        // FirebaseUtils object
        mFirebaseUtils = new FirebaseUtils();
        mFirebaseUtils.setFirebaseUsersObjectCallbacks(this);
    }

    /**
     * Call this method to get the friends of current user
     * This method sets callback for FirebaseRecyclerAdapter
     */
    void getCurrentUserFriends() {
        // Safety for NullPointerException
        if (mFriendsListCallback == null) return;

        // Current user database query from Friends object
        Query query = DataManager.getCurrentUserFriendsRef();
        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query, Friends.class)
                        .build();

        FirebaseRecyclerAdapter<Friends, AllUsersViewHolder> adapter
                = new FirebaseRecyclerAdapter<Friends, AllUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllUsersViewHolder holder,
                                            final int position,
                                            @NonNull final Friends model) {
                // Get the user id
                String listUserId = getRef(position).getKey();
                assert listUserId != null;
                mFirebaseUtils.getUsersObject(listUserId, holder);
            }

            @NonNull
            @Override
            public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // get the View
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_list_item, parent, false);
                return new AllUsersViewHolder(AllUsersListItemBinding.bind(view.getRootView()));
            }
        };
        mFriendsListCallback.onFriendsFirebaseRecyclerAdapter(adapter);
    }

    /**
     * {@link FirebaseUtils}'s callback for user's detailed info
     *
     * @param model  AllUsers object
     * @param userId UserId of user
     * @param holder AllUserViewHolder to bind {@link AllUsers} model to view, and set onItemClick
     *               callback for that user
     */
    @Override
    public void onFirebaseUsersObject(final AllUsers model, final String userId, AllUsersViewHolder holder) {
        holder.bind(model);
        // Set item click callback
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriendsListCallback.onFriendsListItemClick(userId, model.getName());
            }
        });
    }

    /**
     * {@link FirebaseUtils}'s callback for error occurred while getting user's info from database
     *
     * @param error error message
     */
    @Override
    public void onFirebaseUsersObjectError(String error) {
        mFriendsListCallback.onFriendsListError(error);
    }

    /**
     * Callback interface for current user's friends
     */
    interface FriendsListCallback {
        void onFriendsListError(String error);

        void onFriendsListItemClick(String userId, String displayName);

        void onFriendsFirebaseRecyclerAdapter(FirebaseRecyclerAdapter adapter);
    }
}
