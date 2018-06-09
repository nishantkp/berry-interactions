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
 * File Created on 04/06/18 8:57 PM by nishant
 * Last Modified on 04/06/18 8:57 PM
 */

package com.example.nishant.berry.ui.allusers;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.model.AllUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * View Holder for displaying each users in {@link AllUsersActivity}
 * This view holder is used in conjunction with FirebaseRecyclerAdapter
 */
public class AllUsersViewHolder extends RecyclerView.ViewHolder {
    private AllUsersListItemBinding mBinding;

    AllUsersViewHolder(AllUsersListItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    // Bind data to list-item view
    public void bind(final AllUsers users) {
        mBinding.setUsers(users);

        // load default avatar
        if (users.getImage().equals(IFirebaseConfig.DEFAULT_VALUE)) {
            mBinding.allUsersListItemAvatar.setImageResource(R.drawable.user_default_avatar);
        } else {
            // load avatar thumbnail into circular ImageView
            Picasso.get().load(users.getThumbnail())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.user_default_avatar)
                    .into(mBinding.allUsersListItemAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            /* This means we have user avatar stored locally, so no need to make network
                             * connection. It will update ImageView with locally stored user avatar
                             * Do nothing */
                        }

                        @Override
                        public void onError(Exception e) {
                            // If we don't have locally stored avatar, download it from database
                            Picasso.get().load(users.getThumbnail())
                                    .placeholder(R.drawable.user_default_avatar)
                                    .into(mBinding.allUsersListItemAvatar);
                        }
                    });
        }
    }
}
