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
 * File Created on 04/06/18 8:42 PM by nishant
 * Last Modified on 04/06/18 8:42 PM
 */

package com.example.nishant.berry.ui.allusers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.model.AllUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsersPresenter
        extends BasePresenter<AllUsersContract.View>
        implements AllUsersContract.Presenter {

    private Query mQuery;

    AllUsersPresenter() {

        // Create a query for Database reference pointing to "users"
        mQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.USERS_OBJECT);
    }

    @Override
    public AllUsersContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(AllUsersContract.View view) {
        super.attachView(view);

        // After attaching a view, setup firebase adapter
        setupFirebaseRecyclerAdapter(mQuery);
    }

    @Override
    public void setupFirebaseRecyclerAdapter(Query query) {
        FirebaseRecyclerOptions<AllUsers> options =
                new FirebaseRecyclerOptions.Builder<AllUsers>().setQuery(query, AllUsers.class)
                        .build();

        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> adapter
                = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllUsersViewHolder holder,
                                            final int position,
                                            @NonNull final AllUsers model) {
                holder.bind(model);

                // Set onclick listener on ViewHolder
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // set call back with user id parameter
                        getView().onListItemClick(getRef(position).getKey());
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
