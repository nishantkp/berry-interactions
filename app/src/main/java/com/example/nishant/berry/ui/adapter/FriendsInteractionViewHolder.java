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
 * File Created on 17/06/18 11:55 PM by nishant
 * Last Modified on 17/06/18 11:55 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.style.TextAppearanceSpan;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.FriendsMessageListItemBinding;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.utils.ImageLoad;

public class FriendsInteractionViewHolder extends RecyclerView.ViewHolder {
    private FriendsMessageListItemBinding mBinding;

    public FriendsInteractionViewHolder(FriendsMessageListItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(AllUsers allUsers) {
        Context context = mBinding.getRoot().getContext();

        mBinding.setUser(allUsers);

        if (!allUsers.isMessageSeen()) {
            mBinding.chatListItemMessage.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            mBinding.chatListItemMessage.setTypeface(null, Typeface.BOLD);
        } else {
            mBinding.chatListItemMessage.setTextAppearance(context, android.R.style.TextAppearance_Material_Body1);
        }
    }
}
