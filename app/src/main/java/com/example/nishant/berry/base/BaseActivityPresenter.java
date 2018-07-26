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
 * File Created on 25/07/18 7:33 PM by nishant
 * Last Modified on 25/07/18 7:33 PM
 */

package com.example.nishant.berry.base;

import android.util.Log;

import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;

/**
 * Presenter for {@link BaseActivity} to mark user online/ offline
 */
class BaseActivityPresenter {
    private DataManager mDataManager;
    private static final String LOG_TAG = BaseActivityPresenter.class.getSimpleName();

    BaseActivityPresenter() {
        mDataManager = DataManager.getInstance();
    }

    /**
     * This method will mark user online in Firebase database
     * Advise : Call this method on onResume() method of activity because onResume() method will
     * always be called
     */
    void markUserOnline() {
        mDataManager.markUserOnline(new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                // Task successful
            }

            @Override
            public void onError(String error) {
                Log.d(LOG_TAG, error);
            }
        });
    }

    /**
     * This method will mark user offline in Firebase database
     * Advise : Call this method on onPause() method of activity because onPause() method will
     * always be called
     */
    void markUserOffline() {
        mDataManager.markUserOffline(new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                // Task successful
            }

            @Override
            public void onError(String error) {
                Log.d(LOG_TAG, error);
            }
        });
    }
}
