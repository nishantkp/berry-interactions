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

package com.example.nishant.berry.ui.dashboard.fragment.request;

import android.app.Activity;
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
import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.databinding.FragmentRequestBinding;
import com.example.nishant.berry.ui.adapter.FriendRequestAdapter;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.module.ActivityModule;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Fragment displaying list of received friend requests
 */
public class RequestFragment
        extends Fragment
        implements RequestContract.View {

    // Log tag
    private static final String LOG_TAG = RequestFragment.class.getSimpleName();

    /* Dagger Injection */
    @Inject
    RequestPresenter mPresenter;
    @Inject
    FriendRequestAdapter mFriendRequestAdapter;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    DividerItemDecoration mItemDecor;
    private FragmentRequestBinding mBinding;

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

        Activity activity = getActivity();
        if (activity == null) return null;

        // Inject dagger components
        DaggerRequestComponent.builder()
                .dataManagerComponent(BerryApp.get(activity).getDataManagerApplicationComponent())
                .activityModule(new ActivityModule(activity))
                .build()
                .inject(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        mBinding = FragmentRequestBinding.bind(view);

        mPresenter.attachView(this);
        mBinding.requestRv.setAdapter(mFriendRequestAdapter);

        // Setup recycler view
        mBinding.requestRv.setLayoutManager(mLinearLayoutManager);
        mBinding.requestRv.setHasFixedSize(true);

        // Add divider between two items
        mBinding.requestRv.addItemDecoration(mItemDecor);

        return mBinding.getRoot();
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
}
