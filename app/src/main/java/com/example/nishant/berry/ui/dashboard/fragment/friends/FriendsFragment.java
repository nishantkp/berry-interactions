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
 * File Created on 15/08/18 7:03 PM by nishant
 * Last Modified on 23/07/18 10:44 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.AlertDialogFriendsListBinding;
import com.example.nishant.berry.databinding.FragmentFriendsBinding;
import com.example.nishant.berry.ui.adapter.FriendsAdapter;
import com.example.nishant.berry.ui.interaction.InteractionActivity;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.module.ActivityModule;
import com.example.nishant.berry.ui.profile.UserProfileActivity;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment
        extends Fragment
        implements FriendsContract.View, FriendsAdapter.OnClick {

    /* Dagger Injection */
    @Inject
    FriendsPresenter mPresenter;
    @Inject
    FriendsAdapter mFriendsAdapter;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    DividerItemDecoration mItemDecor;
    @Inject
    AlertDialog.Builder mDialogBuilder;

    private FragmentFriendsBinding mBinding;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance() {
        Bundle args = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity activity = getActivity();
        if (activity == null) return null;

        // Inject components with dagger
        DaggerFriendsComponent.builder()
                .dataManagerComponent(BerryApp.get(activity).getDataManagerApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .friendsModule(new FriendsModule(this))
                .build()
                .inject(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        mBinding = FragmentFriendsBinding.bind(view);

        // Adapter
        mBinding.friendsRv.setAdapter(mFriendsAdapter);

        mPresenter.attachView(this);

        // Setup recycler view
        mBinding.friendsRv.setLayoutManager(mLinearLayoutManager);
        mBinding.friendsRv.setHasFixedSize(true);

        // Add divider between two items
        mBinding.friendsRv.addItemDecoration(mItemDecor);
        return mBinding.getRoot();
    }

    @Override
    public void onFriendsList(List<AllUsers> friendsList) {
        mFriendsAdapter.updateData(friendsList, IConstants.DIFF_ONLINE_STATUS);
    }

    /**
     * Implement this method to start {@link InteractionActivity} when use click on
     * "send message" option from alert dialog
     *
     * @param id   user id
     * @param name user name
     */
    @Override
    public void onCreateInteractionActivity(String id, String name) {
        startActivity(
                InteractionActivity.getStarterIntent(getContext())
                        .putExtra(IConstants.KEY_USER_ID, id)
                        .putExtra(IConstants.KEY_USER_DISPLAY_NAME, name)
        );
    }

    /**
     * Implement this method to start {@link UserProfileActivity} when use click on
     * "view profile" option from alert dialog
     *
     * @param id   user id
     * @param name user name
     */
    @Override
    public void onCreateUserProfileActivity(String id, String name) {
        startActivity(
                UserProfileActivity.getStarterIntent(getContext())
                        .putExtra(IConstants.KEY_USER_ID, id)
        );
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
        mDialogBuilder.setView(alertView);
        final AlertDialog dialog = mDialogBuilder.create();
        dialog.show();

        // When user clicks on send message, start InteractionActivity
        binding.alertSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPresenter.onInteraction(id, name);
            }
        });

        // When user clicks on view profile, start UserProfileActivity
        binding.alertViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPresenter.onUserProfile(id, name);
            }
        });
    }
}
