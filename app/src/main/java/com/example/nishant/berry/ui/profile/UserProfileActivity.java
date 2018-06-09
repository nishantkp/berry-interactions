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
 * File Created on 06/06/18 7:38 PM by nishant
 * Last Modified on 06/06/18 7:38 PM
 */

package com.example.nishant.berry.ui.profile;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.ActivityUserProfileBinding;
import com.example.nishant.berry.ui.model.UserProfile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class UserProfileActivity
        extends AppCompatActivity
        implements UserProfileContract.View {

    private UserProfilePresenter mPresenter;
    private ActivityUserProfileBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        // Extract the userId from intent
        String userId = null;
        if (getIntent().hasExtra(IConstants.KEY_USER_ID)) {
            userId = getIntent().getStringExtra(IConstants.KEY_USER_ID);
        }

        // Setup presenter
        mPresenter = new UserProfilePresenter(userId);
        mPresenter.attachView(this);

        // Attach presenter to binding
        mBinding.setPresenter(mPresenter);
    }

    /**
     * Implement this method to deal with error message
     *
     * @param errorMessage message
     */
    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Use this method to populate profile details
     *
     * @param profile UserProfile object
     */
    @Override
    public void updateProfile(UserProfile profile) {
        mBinding.setProfile(profile);
    }

    /**
     * Use this method to update user avatar
     *
     * @param url url of user avatar
     */
    @Override
    public void updateUserProfileAvatar(final String url) {
        Picasso.get().load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.user_default_avatar)
                .into(mBinding.profileAvatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        /* This means we have user avatar stored locally, so no need to make network
                         * connection. It will update ImageView with locally stored user avatar
                         * Do nothing */
                    }

                    @Override
                    public void onError(Exception e) {
                        // If we don't have locally stored avatar, download it from database
                        Picasso.get().load(url)
                                .placeholder(R.drawable.user_default_avatar)
                                .into(mBinding.profileAvatar);
                    }
                });
    }

    /**
     * Use this method to notify user about friend request sent successfully
     *
     * @param message success message
     */
    @Override
    public void friendRequestSentSuccessfully(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
