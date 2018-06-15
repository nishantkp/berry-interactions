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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.MessageListItemBinding;
import com.example.nishant.berry.ui.interaction.InteractionActivity;
import com.example.nishant.berry.ui.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

/**
 * RecyclerView adapter to display messages for {@link InteractionActivity}
 */
public class MessageAdapter
        extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessageList;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Message> messageList) {
        mMessageList = messageList;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        return new MessageViewHolder(MessageListItemBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(mMessageList.get(position));
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

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private MessageListItemBinding messageListItemBinding;

        MessageViewHolder(MessageListItemBinding binding) {
            super(binding.getRoot());
            messageListItemBinding = binding;
        }

        public void bind(Message message) {
            String currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            messageListItemBinding.setMessage(message);
            Context context = messageListItemBinding.getRoot().getContext();

            // If current userId is same as "from" userId, that means current user has sent
            // the message so change the background color of message TextView to blue
            if (currentUserId.equals(message.getFrom())) {
                messageListItemBinding.messageListItemMessage
                        .setBackgroundResource(R.drawable.current_user_message_background);
                messageListItemBinding.messageListItemMessage
                        .setTextColor(context.getResources().getColor(R.color.secondaryTextColor));
            } else {
                // Otherwise set it to default
                messageListItemBinding.messageListItemMessage
                        .setBackgroundResource(R.drawable.message_background);
                messageListItemBinding.messageListItemMessage
                        .setTextColor(context.getResources().getColor(R.color.primaryTextColor));
            }
        }
    }
}
