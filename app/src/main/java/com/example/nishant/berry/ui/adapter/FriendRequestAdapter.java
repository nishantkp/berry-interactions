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
 * File Created on 15/07/18 5:44 PM by nishant
 * Last Modified on 15/07/18 5:44 PM
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
import com.example.nishant.berry.databinding.FriendRequestListItemBinding;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.dashboard.fragment.request.RequestFragment;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.utils.ImageLoad;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom recyclerView adapter that deals with displaying list of friend requests
 * in {@link DashboardActivity}'s {@link RequestFragment}
 * <p>
 * This adapter also sets callback for button click on list item for accept/cancel/decline
 * friend request
 */
public class FriendRequestAdapter
        extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {
    private OnClick mCallback;
    private List<AllUsers> mReqList = new ArrayList<>();

    public FriendRequestAdapter(OnClick callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_request_list_item, parent, false);

        return new FriendRequestViewHolder(FriendRequestListItemBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        holder.bind(mReqList.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            // Otherwise get the data from bundle and update particular view
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle != null) {
                if (bundle.containsKey(IConstants.KEY_LAST_MESSAGE)) {
                    String message = bundle.getString(IConstants.KEY_LAST_MESSAGE);
                    holder.mBinding.friendReqListItemMessage.setText(message);
                }
                if (bundle.containsKey(IConstants.KEY_THUMBNAIL)) {
                    String thumbUrl = bundle.getString(IConstants.KEY_THUMBNAIL);
                    ImageLoad.setImageResource(holder.mBinding.friendReqListItemAvatar, thumbUrl);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mReqList == null || mReqList.isEmpty() ? 0 : mReqList.size();
    }

    public void updateData(List<AllUsers> data) {
        // If data set is empty, that means we are loading data for the first time
        if (mReqList.isEmpty()) {
            mReqList.addAll(data);
            notifyDataSetChanged();
            return;
        }
        // DiffUtils callbacks for calculating difference between new batch of data and old data
        // So we can update only specific views rather that updating whole list with
        // notifyDataSetChanged()
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new AllUsersDiffCallback(mReqList, data, IConstants.DIFF_ALL));
        mReqList.clear();
        mReqList.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * OnClick callbacks for positive and negative button clicks
     * <p>
     * i.e,
     * onPositiveClick : when user clicks on accept friend request button
     * onNegativeClick : when user clicks on cancel or decline friend request button
     */
    public interface OnClick {
        void onPositiveClick(String userId);

        void onNegativeClick(String userId);
    }

    /**
     * ViewHolder class
     */
    class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private final FriendRequestListItemBinding mBinding;

        FriendRequestViewHolder(FriendRequestListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        /**
         * Set {@link AllUsers} object on the list item
         *
         * @param model {@link AllUsers} model
         */
        void bind(AllUsers model) {
            mBinding.setUser(model);
            mBinding.friendReqListItemAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onPositiveClick(mReqList.get(getAdapterPosition()).getId());
                }
            });
            mBinding.friendReqListItemCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onNegativeClick(mReqList.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
