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
 * File Created on 10/06/18 8:14 PM by nishant
 * Last Modified on 10/06/18 8:14 PM
 */

package com.example.nishant.berry.ui.utils;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Class for updating database with current user status, whether user is online or not
 */
public class OnlineStatus implements LoginStats {
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mAuth;

    public OnlineStatus() {
        mAuth = FirebaseAuth.getInstance();
        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.USERS_OBJECT)
                .child(Objects.requireNonNull(mAuth.getUid()))
                .child(IFirebaseConfig.ONLINE);
    }

    @Override
    public void onlineUser() {
        mUsersDatabaseReference.setValue(true);
    }

    @Override
    public void offlineUser() {
        mUsersDatabaseReference.setValue(false);
    }
}
