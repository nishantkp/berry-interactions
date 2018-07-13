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
 * File Created on 02/06/18 8:48 PM by nishant
 * Last Modified on 02/06/18 8:48 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.AlertDialogFriendsListBinding;
import com.example.nishant.berry.databinding.FragmentFriendsBinding;
import com.example.nishant.berry.ui.adapter.FriendsAdapter;
import com.example.nishant.berry.ui.interaction.InteractionActivity;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.UserProfile;
import com.example.nishant.berry.ui.profile.UserProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment
        extends Fragment
        implements FriendsContract.View, FriendsAdapter.OnClick {

    private FriendsPresenter mPresenter;
    private FragmentFriendsBinding mBinding;
    private FriendsAdapter mFriendsAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        mBinding = FragmentFriendsBinding.bind(view);

        // Adapter
        mFriendsAdapter = new FriendsAdapter(this);
        mBinding.friendsRv.setAdapter(mFriendsAdapter);

        mPresenter = new FriendsPresenter();
        mPresenter.attachView(this);

        // Setup recycler view
        mBinding.friendsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.friendsRv.setHasFixedSize(true);

        // Add divider between two items
        DividerItemDecoration itemDecor =
                new DividerItemDecoration(mBinding.friendsRv.getContext(), VERTICAL);
        mBinding.friendsRv.addItemDecoration(itemDecor);

        return mBinding.getRoot();
    }

    @Override
    public void onFriendsList(List<AllUsers> friendsList) {
        mFriendsAdapter.updateData(friendsList, IConstants.DIFF_ONLINE_STATUS);
    }

    @Override
    public void onError(String error) {
        // Error handling
    }

    /**
     * When user clicks on list item from friends list, this method will execute
     *
     * @param id   user id of a person, on which click action is performed
     * @param name name of user
     */
    @Override
    public void onItemClick(final String id, final String name) {
        // Create a custom dialog from layout resource file
        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_friends_list, null);
        AlertDialogFriendsListBinding binding = AlertDialogFriendsListBinding.bind(alertView);

        // AlertDialog builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // When user clicks on send message, start InteractionActivity
        binding.alertSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(
                        new Intent(getContext(), InteractionActivity.class)
                                .putExtra(IConstants.KEY_USER_ID, id)
                                .putExtra(IConstants.KEY_USER_DISPLAY_NAME, name)
                );
            }
        });

        // When user clicks on view profile, start UserProfileActivity
        binding.alertViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(
                        new Intent(getContext(), UserProfileActivity.class)
                                .putExtra(IConstants.KEY_USER_ID, id)
                );
            }
        });
    }
}
