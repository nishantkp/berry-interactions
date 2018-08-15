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
 * File Created on 06/08/18 11:17 AM by nishant
 * Last Modified on 06/08/18 11:17 AM
 */

package com.example.nishant.berry;

import com.example.nishant.berry.ui.model.User;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Credential validation Unit test
 * Validate @Email, @Password, @DisplayName
 */
public class CredentialValidationTest {

    // TODO: Email validation method for test needs to be updated as it can not mock android static method for EMAIL_MATCHER
//    @Test
//    public void emailValidation_ReturnsTrue() {
//        assertThat(User.isEmailValid("nishant@nishant.com"), is(true));
//    }

    @Test
    public void displayNameValidation_ReturnsTrue() {
        assertThat(User.isDisplayNameValid("Test Environment"), is(true));
    }

    @Test
    public void passwordValidation_ReturnsTrue() {
        assertThat(User.isDisplayNameValid("1234567890"), is(true));
    }
}
