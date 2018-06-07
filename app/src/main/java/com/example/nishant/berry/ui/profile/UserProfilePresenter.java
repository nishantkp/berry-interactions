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
 * File Created on 06/06/18 8:04 PM by nishant
 * Last Modified on 06/06/18 8:04 PM
 */

package com.example.nishant.berry.ui.profile;

import android.support.annotation.NonNull;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.ui.model.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Presenter that deals with getting data from firebase database to populate user profile layout
 */
public class UserProfilePresenter
        extends BasePresenter<UserProfileContract.View>
        implements UserProfileContract.Presenter {

    private DatabaseReference mDatabaseReference;

    UserProfilePresenter(String userId) {
        if (userId == null) return;
        // Database reference pointing to passed in userId
        mDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.USERS_OBJECT)
                .child(userId);
        getDataFromFirebaseDatabase();
    }

    @Override
    public UserProfileContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(UserProfileContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to get the data from firebase database about particular user
     */
    @Override
    public void getDataFromFirebaseDatabase() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the data from DataSnapshot
                String displayName = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.NAME).getValue()).toString();
                String status = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.STATUS).getValue()).toString();
                String imageUrl = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.IMAGE).getValue()).toString();

                UserProfile userProfile = new UserProfile();
                userProfile.setDisplayName(displayName);
                userProfile.setStatus(status);

                // Set callbacks to populate user profile layout
                getView().updateProfile(userProfile);
                getView().updateUserProfileAvatar(imageUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getView().onError("Error retrieving user profile!");
            }
        });
    }
}
