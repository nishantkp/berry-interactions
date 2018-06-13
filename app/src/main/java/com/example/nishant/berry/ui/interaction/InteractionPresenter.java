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
 * File Created on 11/06/18 11:28 PM by nishant
 * Last Modified on 11/06/18 11:28 PM
 */

package com.example.nishant.berry.ui.interaction;

import android.content.Intent;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;

public class InteractionPresenter
        extends BasePresenter<InteractionContract.View>
        implements InteractionContract.Presenter {

    private String mUserId;
    private String mDisplayName;

    InteractionPresenter(Intent receivedIntent) {
        // Extract the userId and user displayName from intent
        mUserId = receivedIntent.hasExtra(IConstants.KEY_USER_ID) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_ID) : null;
        mDisplayName = receivedIntent.hasExtra(IConstants.KEY_USER_DISPLAY_NAME) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_DISPLAY_NAME) : null;
    }

    @Override
    public InteractionContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(InteractionContract.View view) {
        super.attachView(view);
        getView().setActionBar(mDisplayName, null);
    }
}
