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
 * File Created on 05/08/18 6:15 PM by nishant
 * Last Modified on 05/08/18 6:15 PM
 */

package com.example.nishant.berry;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.nishant.berry.ui.dashboard.DashboardActivity;
import com.example.nishant.berry.ui.signin.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * {@link SignInActivity} test
 * Write email address and password
 * perform click login button and check if {@link DashboardActivity} opens or not
 */
@RunWith(AndroidJUnit4.class)
public class SignInActivityBasicTest {

    @Rule
    public IntentsTestRule<SignInActivity> mActivityTestRule
            = new IntentsTestRule<>(SignInActivity.class);

    @Test
    public void writeEmailPassword_openDashboardActivity() {
        // write email address
        onView(withId(R.id.sign_in_email_text)).perform(typeText("nishant@nishant.com"));

        // write password
        onView(withId(R.id.sign_in_password_text)).perform(typeText("1234567890"));

        // perform click action on login button
        onView(withId(R.id.sign_in_btn)).perform(click());

        // Check dialog pops up or not
        onView(withText("Authenticating...")).check(matches(isDisplayed()));

        // Wait for 1000 millis
        waitTime(1000);

        // Check if the DashboardActivity opens up or not
        intended(hasComponent(DashboardActivity.class.getName()));
    }

    /**
     * Put Main thread to sleep
     *
     * @param i time in millis
     */
    public void waitTime(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
