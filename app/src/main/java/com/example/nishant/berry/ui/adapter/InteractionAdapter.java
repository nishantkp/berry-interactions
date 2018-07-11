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
 * File Created on 06/07/18 11:46 PM by nishant
 * Last Modified on 06/07/18 11:46 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.FriendsMessageListItemBinding;
import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.dashboard.fragment.chat.ChatFragment;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter that displays list of chats with different users
 * in {@link DashboardActivity}'s {@link ChatFragment}
 */
public class InteractionAdapter
        extends RecyclerView.Adapter<InteractionAdapter.InteractionViewHolder> {
    private List<AllUsers> mData;
    private OnClick mCallback;

    /**
     * Click callbacks when user clicks on recyclerView item
     */
    public interface OnClick {
        void onItemClick(String userId, String displayName);
    }

    public InteractionAdapter(OnClick callback) {
        mCallback = callback;
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public InteractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_message_list_item, parent, false);
        return new InteractionViewHolder(FriendsMessageListItemBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull InteractionViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull InteractionViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            // If the payload is empty that means, there is no change in data so
            // use adapter's default onBindView method
            onBindViewHolder(holder, position);
        } else {
            // Otherwise get the data from bundle and update particular view
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle != null) {
                String message = bundle.getString(IConstants.KEY_LAST_MESSAGE);
                holder.mBinding.chatListItemMessage.setText(message);
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
     * @param data List of users containing information about user name, last message sent/received
     *             and avatar url
     */
    public void updateData(List<AllUsers> data) {
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
                DiffUtil.calculateDiff(new AllUsersDiffCallback(mData, data));
        mData.clear();
        mData.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * Call this method to clear recycler view data
     */
    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class
     */
    class InteractionViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private FriendsMessageListItemBinding mBinding;

        InteractionViewHolder(FriendsMessageListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }

        /**
         * Call this method to bind the data to view
         *
         * @param allUsers AllUser object containing details about particular user
         *                 like id, name, status, image thumb url etc.
         */
        public void bind(AllUsers allUsers) {
            Context context = mBinding.getRoot().getContext();

            mBinding.setUser(allUsers);
            Log.i("actual status", "actual status : " + allUsers.getStatus());

            // If user has not seen the message, change the status color and typeface
            if (!allUsers.isMessageSeen()) {
                mBinding.chatListItemMessage.setTextColor(context.getResources().getColor(R.color.colorSecondary));
                mBinding.chatListItemMessage.setTypeface(null, Typeface.BOLD);
            } else {
                mBinding.chatListItemMessage.setTextAppearance(context, android.R.style.TextAppearance_Material_Body1);
            }
        }

        @Override
        public void onClick(View v) {
            AllUsers user = mData.get(getAdapterPosition());
            // Attach onCLick callback
            mCallback.onItemClick(user.getId(), user.getName());
        }
    }
}
