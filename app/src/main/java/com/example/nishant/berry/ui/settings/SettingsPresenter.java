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
 * File Created on 03/06/18 1:21 AM by nishant
 * Last Modified on 03/06/18 1:21 AM
 */

package com.example.nishant.berry.ui.settings;

import android.support.annotation.NonNull;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsPresenter
        extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    SettingsPresenter() {
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseReference =
                FirebaseDatabase.getInstance().getReference().child(IFirebaseConfig.USERS_OBJECT).child(userId);
        retrieveDataFromFirebaseDatabase();
    }

    @Override
    public SettingsContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SettingsContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to retrieve data from Firebase database
     */
    @Override
    public void retrieveDataFromFirebaseDatabase() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get data from data snapshot
                String name = dataSnapshot.child(IFirebaseConfig.NAME).getValue().toString();
                String status = dataSnapshot.child(IFirebaseConfig.STATUS).getValue().toString();
                String image = dataSnapshot.child(IFirebaseConfig.IMAGE).getValue().toString();

                // Setup callbacks
                getView().setName(name);
                getView().setStatus(status);
                getView().setImage(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getView().onError(databaseError.getMessage());
            }
        });
    }
}
