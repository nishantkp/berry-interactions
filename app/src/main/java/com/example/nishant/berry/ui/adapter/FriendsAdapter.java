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
 * File Created on 11/07/18 8:22 PM by nishant
 * Last Modified on 11/07/18 8:22 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.allusers.AllUsersActivity;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.dashboard.fragment.friends.FriendsFragment;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.utils.ImageLoad;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom RecyclerViewAdapter to display current user's friends
 * in {@link DashboardActivity}'s {@link FriendsFragment} and in {@link AllUsersActivity}
 */
public class FriendsAdapter
        extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    private final OnClick mCallback;
    private final List<AllUsers> mData;

    /**
     * Click callback when use clicks on recycler view item
     */
    public interface OnClick {
        void onItemClick(String id, String name);
    }

    public FriendsAdapter(OnClick onClickCallback) {
        mCallback = onClickCallback;
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_users_list_item, parent, false);
        return new FriendsViewHolder(AllUsersListItemBinding.bind(view.getRootView()));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            // If the payload is empty that means, there is no change in data so
            // use adapter's default onBindView method
            onBindViewHolder(holder, position);
        } else {
            // Otherwise get the data from bundle and update particular view
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle != null) {
                if (bundle.containsKey(IConstants.KEY_LAST_MESSAGE)) {
                    String message = bundle.getString(IConstants.KEY_LAST_MESSAGE);
                    holder.mBinding.allUsersListItemStatus.setText(message);
                }
                if (bundle.containsKey(IConstants.KEY_ONLINE_STATUS)) {
                    int onlineStatus = bundle.getInt(IConstants.KEY_ONLINE_STATUS);
                    holder.mBinding.allUsersOnlineStatus.setVisibility(onlineStatus);
                }
                if (bundle.containsKey(IConstants.KEY_THUMBNAIL)) {
                    String thumbUrl = bundle.getString(IConstants.KEY_THUMBNAIL);
                    ImageLoad.setImageResource(holder.mBinding.allUsersListItemAvatar, thumbUrl);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * Call this method to update the RecyclerView with new batch of data or specific data
     *
     * @param data       List of users containing information about user name, last message sent/received
     *                   and avatar url
     * @param identifier Identifier to determine which data needs to be checked in diffUtils
     *                   between old and new batch of data
     */
    public void updateData(List<AllUsers> data, int identifier) {
        // If data set is empty, that means we are loading data for the first time
        if (mData.isEmpty()) {
            mData.addAll(data);
            notifyDataSetChanged();
            return;
        }
        // DiffUtils callbacks for calculating difference between new batch of data and old data
        // So we can update only specific views rather that updating whole list with
        // notifyDataSetChanged()
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new AllUsersDiffCallback(mData, data, identifier));
        mData.clear();
        mData.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * Call this method to clear the data from recycler view
     */
    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class
     */
    class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AllUsersListItemBinding mBinding;

        FriendsViewHolder(AllUsersListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }

        /**
         * Call this method to bind the data to view
         *
         * @param model AllUser object containing details about particular user
         *              like id, name, status, image thumb url etc.
         */
        void bind(AllUsers model) {
            mBinding.setUsers(model);
        }

        @Override
        public void onClick(View v) {
            AllUsers user = mData.get(getAdapterPosition());
            mCallback.onItemClick(user.getId(), user.getName());
        }
    }
}
