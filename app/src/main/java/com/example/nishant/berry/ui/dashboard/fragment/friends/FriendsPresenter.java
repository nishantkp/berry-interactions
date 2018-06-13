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
 * File Created on 10/06/18 5:34 PM by nishant
 * Last Modified on 10/06/18 5:34 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.friends;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.adapter.AllUsersViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.Friends;
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

public class FriendsPresenter
        extends BasePresenter<FriendsContract.View>
        implements FriendsContract.Presenter {

    private Query mQuery;
    private DatabaseReference mUsersDatabaseReference;

    FriendsPresenter() {
        mQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.FRIENDS_OBJECT)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        // provide offline feature
        mQuery.keepSynced(true);

        // Users database reference
        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child(IFirebaseConfig.USERS_OBJECT);
        // provide offline feature
        mUsersDatabaseReference.keepSynced(true);
    }

    @Override
    public FriendsContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(FriendsContract.View view) {
        super.attachView(view);

        // After attaching a view, setup firebase adapter
        setupFirebaseRecyclerAdapter(mQuery);
    }

    @Override
    public void setupFirebaseRecyclerAdapter(Query query) {
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
                mUsersDatabaseReference.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String displayName = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.NAME).getValue()).toString();
                        String status = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.STATUS).getValue()).toString();
                        String thumbnail = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.THUMBNAIL).getValue()).toString();
                        boolean onlineStatus = (boolean) dataSnapshot.child(IFirebaseConfig.ONLINE).getValue();

                        AllUsers users = new AllUsers();
                        users.setName(displayName);
                        users.setStatus(status);
                        users.setThumbnail(thumbnail);
                        users.setOnline(onlineStatus);
                        holder.bind(users);

                        // Set onclick listener on ViewHolder
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // set call back with user id parameter
                                getView().onListItemClick(getRef(holder.getAdapterPosition()).getKey(), displayName);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        /*Do nothing for now*/
                        /*TODO Set error message callBack*/
                    }
                });
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
        getView().getFirebaseRecyclerAdapter(adapter);
    }
}
