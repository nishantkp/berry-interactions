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
 * File Created on 10/06/18 7:39 PM by nishant
 * Last Modified on 10/06/18 6:36 PM
 */

package com.example.nishant.berry.ui.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.allusers.AllUsersActivity;
import com.example.nishant.berry.ui.dashboard.fragment.friends.FriendsFragment;
import com.example.nishant.berry.ui.model.AllUsers;

/**
 * View Holder for displaying each users in {@link AllUsersActivity} and {@link FriendsFragment}
 * This view holder is used in conjunction with FirebaseRecyclerAdapter
 */
public class AllUsersViewHolder extends RecyclerView.ViewHolder {
    private AllUsersListItemBinding mBinding;

    public AllUsersViewHolder(AllUsersListItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    // Bind data to list-item view
    public void bind(final AllUsers users) {
        mBinding.setUsers(users);
    }
}
