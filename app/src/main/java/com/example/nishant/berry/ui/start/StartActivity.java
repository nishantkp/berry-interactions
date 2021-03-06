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
 * File Created on 02/06/18 12:55 AM by nishant
 * Last Modified on 01/06/18 11:00 PM
 */

package com.example.nishant.berry.ui.start;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nishant.berry.R;
import com.example.nishant.berry.databinding.ActivityStartBinding;
import com.example.nishant.berry.ui.signin.SignInActivity;
import com.example.nishant.berry.ui.signup.SignUpActivity;

/**
 * Start activity responsible for start page and navigates user to sign-in or sign-up page
 * depending on user's preference
 */
public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding mBinding;

    /**
     * Use this method get the intent to start {@link StartActivity}
     *
     * @param context Context of activity from which intent is started
     * @return Intent to start {@link StartActivity}
     */
    public static Intent getStarterIntent(Context context) {
        return new Intent(context, StartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        mBinding.setActivity(this);
    }

    /**
     * When user clicks on account registration button, start {@link SignUpActivity}
     */
    public void onRegistrationButtonClick() {
        startActivity(SignUpActivity.getStarterIntent(this));
    }

    /**
     * When user click on Sign In button, start {@link SignInActivity}
     */
    public void onSignInButtonClick() {
        startActivity(SignInActivity.getStarterIntent(this));
    }
}
