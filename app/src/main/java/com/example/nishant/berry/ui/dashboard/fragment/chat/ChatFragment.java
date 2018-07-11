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
 * File Created on 02/06/18 8:46 PM by nishant
 * Last Modified on 02/06/18 8:46 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.FragmentChatBinding;


import com.example.nishant.berry.R;
import com.example.nishant.berry.ui.adapter.InteractionAdapter;
import com.example.nishant.berry.ui.interaction.InteractionActivity;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * A Fragment that deals with displaying a list of friends, with whom current user had
 * conversation/ chat/ interaction
 */
public class ChatFragment
        extends Fragment
        implements ChatContract.View, InteractionAdapter.OnClick {
    private FragmentChatBinding mBinding;
    private ChatPresenter mPresenter;
    private InteractionAdapter mInteractionAdapter;

    // Tag for logs
    private static final String LOG_TAG = ChatFragment.class.getSimpleName();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mBinding = FragmentChatBinding.bind(view);
        mInteractionAdapter = new InteractionAdapter(this);
        mBinding.chatRv.setAdapter(mInteractionAdapter);

        mPresenter = new ChatPresenter();
        mPresenter.attachView(this);

        // Setup recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBinding.chatRv.setLayoutManager(layoutManager);
        mBinding.chatRv.setHasFixedSize(true);

        // Adds divider between two list items
        DividerItemDecoration itemDecor =
                new DividerItemDecoration(mBinding.chatRv.getContext(), VERTICAL);
        mBinding.chatRv.addItemDecoration(itemDecor);

        return mBinding.getRoot();
    }

    @Override
    public void onInteractionData(List<AllUsers> data) {
        mInteractionAdapter.updateData(data);
    }

    /**
     * Error while getting data from firebase database for user's interactions
     *
     * @param error error message
     */
    @Override
    public void onError(String error) {
        Log.d(LOG_TAG, error);
    }

    /**
     * Implement this method to open {@link InteractionActivity} when user clicks on
     * list item
     *
     * @param userId      id of user with whom current user want to chat
     * @param displayName name of a user with whom current user want to chat
     */
    @Override
    public void onItemClick(String userId, String displayName) {
        startActivity(
                new Intent(getContext(), InteractionActivity.class)
                        .putExtra(IConstants.KEY_USER_ID, userId)
                        .putExtra(IConstants.KEY_USER_DISPLAY_NAME, displayName)
        );
    }
}
