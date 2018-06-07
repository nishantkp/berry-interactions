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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mFriendReqDatabaseReference;
    private String mCurrentState;
    private String mCurrentUserId;
    private String mNewUserId;

    UserProfilePresenter(String userId) {
        if (userId == null) return;
        mNewUserId = userId;
        // Database reference pointing to passed in userId
        mUsersDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.USERS_OBJECT)
                .child(userId);

        // Database reference pointing to friend_request object
        mFriendReqDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.FRIEND_REQUEST_OBJECT);

        // Current firebase user id
        mCurrentUserId = FirebaseAuth.getInstance().getUid();

        mCurrentState = "not_friends";
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
        if (!mCurrentState.equals("not_friends")) return;
        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
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

    /**
     * When user presses send friend request button, this method will be executed
     * i.e if user1 sends friend request to user2
     * <p>
     * friend_request:
     * <p>
     * user1_key{
     * user2_key{
     * request_type : sent
     * }
     * }
     * <p>
     * user2_key{
     * user1_key{
     * request_type: received
     * }
     * }
     */
    @Override
    public void sendFriendRequestButtonClick() {
        mFriendReqDatabaseReference.child(mCurrentUserId)
                .child(mNewUserId)
                .child(IFirebaseConfig.FRIEND_REQUEST_TYPE)
                .setValue(IFirebaseConfig.FRIEND_REQUEST_SENT)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFriendReqDatabaseReference.child(mNewUserId)
                                    .child(mCurrentUserId)
                                    .child(IFirebaseConfig.FRIEND_REQUEST_TYPE)
                                    .setValue(IFirebaseConfig.FRIEND_REQUEST_RECEIVED)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            getView().friendRequestSentSuccessfully("Friend request sent!");
                                        }
                                    });
                        } else {
                            getView().onError("Unable to send friend request!");
                        }
                    }
                });
    }
}
