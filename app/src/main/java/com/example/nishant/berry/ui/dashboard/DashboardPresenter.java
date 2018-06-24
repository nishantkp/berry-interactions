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
 * Last Modified on 01/06/18 10:35 PM
 */

package com.example.nishant.berry.ui.dashboard;

import android.provider.ContactsContract;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Objects;

public class DashboardPresenter
        extends BasePresenter<DashboardContract.View>
        implements DashboardContract.Presenter {

    private FirebaseAuth mAuth;

    DashboardPresenter() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public DashboardContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(DashboardContract.View view) {
        super.attachView(view);
    }

    @Override
    public void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            getView().noActiveUser();
        }
    }

    /**
     * Sign out current user
     */
    @Override
    public void signOutUser() {
        // Update user's status offline and last_seen to firebase's timestamp before signing out
        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.USERS_OBJECT)
                .child(DataManager.getCurrentUserId());
        usersDatabaseReference.child(IFirebaseConfig.ONLINE).setValue(false);
        usersDatabaseReference.child(IFirebaseConfig.LAST_SEEN).setValue(ServerValue.TIMESTAMP);
        mAuth.signOut();
    }
}
