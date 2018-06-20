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
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.FragmentChatBinding;
import com.example.nishant.berry.databinding.FragmentRequestBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Fragment displaying list of received friend requests
 */
public class RequestFragment
        extends Fragment
        implements RequestContract.View {

    private FragmentRequestBinding mBinding;
    private RequestPresenter mPresenter;
    private FirebaseRecyclerAdapter mAdapter;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        mBinding = FragmentRequestBinding.bind(view);

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
        mAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.stopListening();
    }

    @Override
    public void setFirebaseAdapterWithRecyclerView(FirebaseRecyclerAdapter adapter) {
        mAdapter = adapter;
        mBinding.requestRv.setAdapter(adapter);
    }
}
