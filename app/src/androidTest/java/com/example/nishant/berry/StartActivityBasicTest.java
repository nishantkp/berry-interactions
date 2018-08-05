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
 * File Created on 05/08/18 5:33 PM by nishant
 * Last Modified on 05/08/18 5:33 PM
 */

package com.example.nishant.berry;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.nishant.berry.ui.signin.SignInActivity;
import com.example.nishant.berry.ui.signup.SignUpActivity;
import com.example.nishant.berry.ui.start.StartActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * {@link com.example.nishant.berry.ui.start.StartActivity} basic test
 * Perform click actions on login and register buttons
 */
@RunWith(AndroidJUnit4.class)
public class StartActivityBasicTest {

    @Rule
    public IntentsTestRule<StartActivity> mActivityTestRule
            = new IntentsTestRule<>(StartActivity.class);

    @Test
    public void clickRegisterButton_openSignUpActivity() {

        // Find the view and perform click
        onView(withId(R.id.start_registration_btn)).perform(click());

        // Check SignUpActivity
        intended(hasComponent(SignUpActivity.class.getName()));
    }

    @Test
    public void clickSignInButton_openSignInActivity() {

        // Find the view and perform click
        onView(withId(R.id.start_sign_in_button)).perform(click());

        // Check SignInActivity
        intended(hasComponent(SignInActivity.class.getName()));
    }
}
