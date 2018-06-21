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
 * File Created on 11/06/18 11:09 PM by nishant
 * Last Modified on 11/06/18 11:09 PM
 */

package com.example.nishant.berry.ui.interaction;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.nishant.berry.R;
import com.example.nishant.berry.base.BaseActivity;
import com.example.nishant.berry.databinding.ActivityInteractionBinding;
import com.example.nishant.berry.databinding.InteractionCustomBarBinding;
import com.example.nishant.berry.ui.adapter.MessageAdapter;
import com.example.nishant.berry.ui.model.Interaction;
import com.example.nishant.berry.ui.model.InteractionActionBar;
import com.example.nishant.berry.ui.model.Message;

import java.util.List;

/**
 * Activity that handles all interactions between users
 */
public class InteractionActivity
        extends BaseActivity
        implements InteractionContract.View {

    private InteractionPresenter mPresenter;
    private ActivityInteractionBinding mBinding;
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_interaction);
        mBinding.interactionsBottomBar.setData(new Interaction());

        // Setup support action bar
        setSupportActionBar(mBinding.interactionsAppBar.mainAppBar);

        // Attach view to presenter
        mPresenter = new InteractionPresenter(getIntent());
        mPresenter.attachView(this);
        mBinding.interactionsBottomBar.setPresenter(mPresenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Implement this method to set custom actionbar
     *
     * @param model InteractionActionBar model which has user name, online status and user avatar
     *              download url
     */
    @Override
    public void setActionBar(InteractionActionBar model) {
        final ActionBar customActionBar = getSupportActionBar();
        View appBarView = getLayoutInflater().inflate(R.layout.interaction_custom_bar, null);
        final InteractionCustomBarBinding customBarBinding = InteractionCustomBarBinding.bind(appBarView);

        if (customActionBar == null) return;
        customActionBar.setDisplayHomeAsUpEnabled(true);
        customActionBar.setDisplayShowCustomEnabled(true);
        customActionBar.setCustomView(customBarBinding.getRoot());
        customBarBinding.setUser(model);
    }

    @Override
    public void updateMessageList(List<Message> messageList) {
        mAdapter.swapData(messageList);
        mBinding.interactionsMessageList.scrollToPosition(messageList.size() - 1);
    }

    /**
     * This method will be called when user hits send and message is successfully updated to database
     * Provide implementation of this method to clear message editText
     */
    @Override
    public void clearEditTextField() {
        mBinding.interactionsBottomBar.interactionBottomEditText.getText().clear();
    }

    /**
     * Provide implementation of RecyclerView
     */
    @Override
    public void setUpRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mBinding.interactionsMessageList.setHasFixedSize(true);
        mBinding.interactionsMessageList.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MessageAdapter(null);
        mBinding.interactionsMessageList.setAdapter(mAdapter);

        // Attach a swipe refresh listener
        mBinding.interactionSwipeRefresh
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.swipeMessageRefresh();
                    }
                });
    }

    /**
     * This method will invoke when, previous messages are successfully loaded
     * i.e, use this method to hide progressbar
     */
    @Override
    public void onSwipeRefreshComplete() {
        mBinding.interactionSwipeRefresh.setRefreshing(false);
    }

    /**
     * This method will provide interaction user's thumbnail avatar url
     * i.e update recycler adapter to display avatar thumbnail
     *
     * @param url url of thumbnail
     */
    @Override
    public void interactionUserAvatar(String url) {
        mAdapter.updateInteractionUserAvatar(url);
    }

    /**
     * This method will invoked to set position offset
     * i.e when user refreshes message list to load previous message, use this method to provide
     * offset to layout manager
     *
     * @param position offset position
     */
    @Override
    public void setLayoutManagerOffset(int position) {
        mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
    }
}
