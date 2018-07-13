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
 * File Created on 07/07/18 5:03 PM by nishant
 * Last Modified on 07/07/18 5:03 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;

import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.List;

/**
 * DiffUtils class to calculate difference between old batch and new batch of data
 * Class used with {@link InteractionAdapter}, {@link FriendsAdapter}
 */
final class AllUsersDiffCallback extends DiffUtil.Callback {
    /* Identifier for Last message send/received by user to update the last message only*/
    private static int LAST_MESSAGE = 1;
    /* Identifier for whether user is online or not to display green dot */
    private static int ONLINE_STATUS = 2;
    private List<AllUsers> mOldList;
    private List<AllUsers> mNewList;
    private int mIdentifier;

    AllUsersDiffCallback(List<AllUsers> oldList, List<AllUsers> newList, int identifier) {
        mOldList = oldList;
        mNewList = newList;
        mIdentifier = identifier;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getId().equals(mNewList.get(
                newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final AllUsers oldUser = mOldList.get(oldItemPosition);
        final AllUsers newUser = mNewList.get(newItemPosition);
        if (mIdentifier == LAST_MESSAGE)
            return oldUser.getStatus().equals(newUser.getStatus());
        if (mIdentifier == ONLINE_STATUS)
            return oldUser.getOnlineStatus() == newUser.getOnlineStatus();
        return oldUser.getStatus().equals(newUser.getStatus())
                && oldUser.getOnlineStatus() == newUser.getOnlineStatus()
                && oldUser.getThumbnail().equals(newUser.getThumbnail());
    }

    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        AllUsers oldUser = mOldList.get(oldItemPosition);
        AllUsers newUser = mNewList.get(newItemPosition);
        Bundle bundle = new Bundle();
        if (!oldUser.getStatus().equals(newUser.getStatus())) {
            bundle.putString(IConstants.KEY_LAST_MESSAGE, newUser.getStatus());
        }
        if (oldUser.getOnlineStatus() != newUser.getOnlineStatus()) {
            bundle.putInt(IConstants.KEY_ONLINE_STATUS, newUser.getOnlineStatus());
        }
        if (!oldUser.getThumbnail().equals(newUser.getThumbnail())) {
            bundle.putString(IConstants.KEY_THUMBNAIL, newUser.getThumbnail());
        }
        if (bundle.size() == 0) return null;
        return bundle;
    }
}
