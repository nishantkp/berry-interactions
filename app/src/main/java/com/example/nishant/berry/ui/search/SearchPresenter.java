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
 * File Created on 20/06/18 11:01 PM by nishant
 * Last Modified on 20/06/18 11:01 PM
 */

package com.example.nishant.berry.ui.search;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.adapter.AllUsersViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Presenter responsible for querying database as per user search query
 */
public class SearchPresenter
        extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter {

    SearchPresenter() {
    }

    @Override
    public SearchContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SearchContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to set FirebaseRecycler Adapter and set callback for the same
     * So in activity we can start and stop listening to firebase adapter
     *
     * @param searchQuery search query entered by user
     */
    @Override
    public void setFirebaseAdapter(String searchQuery) {
        // If user has not given any search query, return from the method
        if (TextUtils.isEmpty(searchQuery)) {
            getView().setRecyclerView(null);
            return;
        }

        // Firebase database Query of Users object
        Query databaseQuery = DataManager.getUsersRef()
                .orderByChild(IFirebaseConfig.NAME)
                .startAt(searchQuery.toUpperCase())
                .endAt(searchQuery.toLowerCase() + "\uf8ff")
                .limitToFirst(15);

        FirebaseRecyclerOptions<AllUsers> options =
                new FirebaseRecyclerOptions.Builder<AllUsers>().setQuery(databaseQuery, AllUsers.class)
                        .build();

        // Setup recycler adapter
        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AllUsersViewHolder holder,
                                                    final int position,
                                                    @NonNull final AllUsers model) {
                        // Set the online status to false so user can not find other users are online
                        // or not while performing a search
                        model.setOnline(false);
                        holder.bind(model);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Set callback to start ProfileActivity with userId
                                getView().onListItemSelected(getRef(holder.getAdapterPosition()).getKey());
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
        // Set callback to setup recycler view in activity
        getView().setRecyclerView(adapter);
    }
}
