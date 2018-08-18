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
 * File Created on 18/08/18 12:23 PM by nishant
 * Last Modified on 18/08/18 12:23 PM
 */

package com.example.nishant.berry.ui.dashboard.fragment.request;

import com.example.nishant.berry.ui.adapter.FriendRequestAdapter;
import com.example.nishant.berry.ui.dashboard.DashboardActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides {@link FriendRequestAdapter} object to {@link RequestComponent}
 */
@Module
public class RequestModule {

    @DashboardActivityScope
    @Provides
    public FriendRequestAdapter getFriendRequestAdapter(RequestPresenter requestPresenter) {
        return new FriendRequestAdapter(requestPresenter);
    }
}