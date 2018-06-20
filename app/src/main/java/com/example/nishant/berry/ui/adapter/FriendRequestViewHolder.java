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
 * File Created on 19/06/18 6:30 PM by nishant
 * Last Modified on 19/06/18 6:30 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.nishant.berry.databinding.FriendRequestListItemBinding;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.utils.ImageLoad;

/**
 * RecyclerView ViewHolder to bind friend_request_list_item with AllUsers object
 */
public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

    private String mCurrentUserId;
    private String mListUserId;
    private onButtonClick mClick;
    private FriendRequestListItemBinding mBinding;

    /**
     * Callback for positive button click and negative button clicks
     * i.e,
     * onPositiveClick() -> when user clicks on "Accept request" button
     * onNegativeClick() -> when user clicks on "decline" or "cancel request" button
     */
    public interface onButtonClick {
        void onPositiveClick(String currentUserId, String listUserId);

        void onNegativeClick(String currentUserId, String listUserId);
    }

    public FriendRequestViewHolder(FriendRequestListItemBinding binding, final onButtonClick click) {
        super(binding.getRoot());
        mBinding = binding;
        mClick = click;
    }

    /**
     * Bind AllUsers object to friend_request_list_item layout
     *
     * @param allUsers AllUsers object
     */
    public void bind(AllUsers allUsers, String currentUserId, String listUserId) {
        mBinding.setUser(allUsers);
        mBinding.setViewHolder(this);
        mCurrentUserId = currentUserId;
        mListUserId = listUserId;
    }

    /**
     * Binding adapter lo load avatar into ImageView
     *
     * @param view ImageView
     * @param url  url of a avatar
     */
    @BindingAdapter({"app:loadImage"})
    public static void setImage(ImageView view, String url) {
        ImageLoad.load(url, view);
    }

    /**
     * When user clicks on "Accept" button, setup call back with current userId and
     * Id of user on which click action is performed
     */
    public void onPositiveButtonClick() {
        mClick.onPositiveClick(mCurrentUserId, mListUserId);
    }

    /**
     * When user clicks on "decline" or "cancel request" button, setup callback
     * with current userId and Id of user on which click action is performed
     */
    public void onNegativeButtonClick() {
        mClick.onNegativeClick(mCurrentUserId, mListUserId);
    }
}
