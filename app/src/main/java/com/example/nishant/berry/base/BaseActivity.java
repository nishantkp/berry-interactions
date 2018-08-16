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
 * Last Modified on 28/07/18 4:17 PM
 */

package com.example.nishant.berry.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nishant.berry.application.BerryApp;

import javax.inject.Inject;

/**
 * BaseActivity
 */
public class BaseActivity extends AppCompatActivity {
    @Inject
    BaseActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject BaseActivityPresenter
        DaggerBaseActivityComponent.builder()
                .dataManagerComponent(BerryApp.get(this).getDataManagerApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // When user is away from the screen, mark user offline
        mPresenter.markUserOffline();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When user is on the app screen, mark user online
        mPresenter.markUserOnline();
    }
}
