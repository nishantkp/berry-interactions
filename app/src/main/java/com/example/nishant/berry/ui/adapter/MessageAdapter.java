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
 * File Created on 13/06/18 10:50 PM by nishant
 * Last Modified on 13/06/18 10:50 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.MessageListItemReceivedBinding;
import com.example.nishant.berry.databinding.MessageListItemSentBinding;
import com.example.nishant.berry.ui.interaction.InteractionActivity;
import com.example.nishant.berry.ui.model.Message;
import com.example.nishant.berry.ui.utils.ImageLoad;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

/**
 * RecyclerView adapter to display messages for {@link InteractionActivity}
 */
public class MessageAdapter
        extends RecyclerView.Adapter {
    private List<Message> mMessageList;
    private String mCurrentUserId;
    private String mInteractionUserThumbUrl;
    private static final int SENT_MESSAGE = 1;
    private static final int RECEIVED_MESSAGE = 2;

    public MessageAdapter(List<Message> messageList) {
        mMessageList = messageList;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENT_MESSAGE) {
            View sentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_item_sent, parent, false);
            return new SentMessageViewHolder(MessageListItemSentBinding.bind(sentView));
        } else {
            View receivedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_item_received, parent, false);
            return new ReceivedMessageViewHolder(MessageListItemReceivedBinding.bind(receivedView));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case SENT_MESSAGE:
                ((SentMessageViewHolder) holder).bind(message);
                break;
            case RECEIVED_MESSAGE:
                ((ReceivedMessageViewHolder) holder).bind(message);
                break;
        }
    }

    /**
     * Determine Item type whether message which is being displayed, is sent by current user or
     * it is received message
     *
     * @param position adapter position
     * @return view type
     */
    @Override
    public int getItemViewType(int position) {
        Message currentMessage = mMessageList.get(position);
        if (currentMessage.getFrom().equals(mCurrentUserId)) {
            return SENT_MESSAGE;
        } else {
            return RECEIVED_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList == null ? 0 : mMessageList.size();
    }

    /**
     * Method to update content of recycle view
     *
     * @param messageList message list
     */
    public void swapData(List<Message> messageList) {
        if (messageList == null || messageList.size() == 0) return;
        mMessageList = messageList;
        notifyDataSetChanged();
    }

    public void updateInteractionUserAvatar(String thumbUrl) {
        mInteractionUserThumbUrl = thumbUrl;
        notifyDataSetChanged();
    }

    public void clearData() {
        mMessageList.clear();
        notifyDataSetChanged();
    }

    /**
     * Received message ViewHolder
     */
    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private MessageListItemReceivedBinding messageListItemBinding;

        ReceivedMessageViewHolder(MessageListItemReceivedBinding binding) {
            super(binding.getRoot());
            messageListItemBinding = binding;
        }

        public void bind(Message message) {
            messageListItemBinding.setMessage(message);
            // Load interaction user avatar
            ImageLoad.load(mInteractionUserThumbUrl, messageListItemBinding.messageListItemAvatar);
        }
    }

    /**
     * Sent message ViewHolder
     */
    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private MessageListItemSentBinding mBinding;

        SentMessageViewHolder(MessageListItemSentBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Message message) {
            mBinding.setMessage(message);
        }
    }
}
