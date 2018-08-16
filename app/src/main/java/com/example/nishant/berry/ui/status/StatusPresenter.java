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

package com.example.nishant.berry.ui.status;

import android.text.TextUtils;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.ui.settings.SettingsActivity;

import javax.inject.Inject;

/**
 * Presenter which deals with saving current user's status
 */
public class StatusPresenter
        extends BasePresenter<StatusContract.View>
        implements StatusContract.Presenter {

    private DataManager mDataManager;

    @Inject
    StatusPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public StatusContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(StatusContract.View view) {
        super.attachView(view);
    }

    /**
     * When user click on save button from {@link SettingsActivity}
     * layout, save the status
     *
     * @param status user status from EditText field
     */
    @Override
    public void onStatusSaveClick(String status) {
        getView().showProgressDialog();
        if (TextUtils.isEmpty(status)) {
            status = "Welcome to berry!";
        }

        // Save user's status
        mDataManager.saveUserStatus(status, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                getView().cancelProgressDialog();
            }

            @Override
            public void onError(String error) {
                getView().cancelProgressDialog();
                getView().onError(error);
            }
        });
    }
}
