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

package com.example.nishant.berry.ui.dashboard.fragment.request;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.FragmentRequestBinding;
import com.example.nishant.berry.ui.adapter.FriendRequestAdapter;
import com.example.nishant.berry.ui.model.AllUsers;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Fragment displaying list of received friend requests
 */
public class RequestFragment
        extends Fragment
        implements RequestContract.View, FriendRequestAdapter.OnClick {

    // Log tag
    private static final String LOG_TAG = RequestFragment.class.getSimpleName();
    private FragmentRequestBinding mBinding;
    private RequestPresenter mPresenter;
    private FriendRequestAdapter mFriendRequestAdapter;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance() {
        Bundle args = new Bundle();
        RequestFragment fragment = new RequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        mBinding = FragmentRequestBinding.bind(view);

        mFriendRequestAdapter = new FriendRequestAdapter(this);
        mBinding.requestRv.setAdapter(mFriendRequestAdapter);

        mPresenter = new RequestPresenter();
        mPresenter.attachView(this);

        // Setup recycler view
        mBinding.requestRv.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        mBinding.requestRv.setHasFixedSize(true);

        // Add divider between two items
        DividerItemDecoration itemDecor =
                new DividerItemDecoration(mBinding.requestRv.getContext(), VERTICAL);
        mBinding.requestRv.addItemDecoration(itemDecor);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Provide implementation of this method either to Log error message or notify user about error
     * by means of toast, snack bar or so on...
     *
     * @param error error message
     */
    @Override
    public void onError(String error) {
        Log.d(LOG_TAG, error);
    }

    /**
     * Provide implementation of this method for what to do with all friend requests
     *
     * @param requests List of friend requests in form of List<AllUsers>
     */
    @Override
    public void onFriendReq(List<AllUsers> requests) {
        mFriendRequestAdapter.updateData(requests);
    }

    /**
     * Implement this {@link FriendRequestAdapter} callback when user clicks on accept friend
     * request button from the list
     *
     * @param userId user id
     */
    @Override
    public void onPositiveClick(String userId) {
        mPresenter.onPositiveClick(userId);
    }

    /**
     * Implement this {@link FriendRequestAdapter} callback when user clicks on cancel/ decline
     * friend request button from the list
     *
     * @param userId user id
     */
    @Override
    public void onNegativeClick(String userId) {
        mPresenter.onNegativeClick(userId);
    }
}
