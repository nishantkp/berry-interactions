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

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Presenter that deals with getting data from firebase database to populate user profile layout
 */
public class UserProfilePresenter
        extends BasePresenter<UserProfileContract.View>
        implements UserProfileContract.Presenter {

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mFriendReqDatabaseReference;
    private DatabaseReference mFriendsDatabaseReference;
    private DatabaseReference mNotificationDatabaseReference;
    private int mCurrentState;
    private String mCurrentUserId;
    private String mNewUserId;
    private UserProfile mUserProfile;

    UserProfilePresenter(String userId) {
        if (userId == null) return;
        mNewUserId = userId;
        mUserProfile = new UserProfile();
        // Database reference pointing to passed in userId
        mUsersDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.USERS_OBJECT)
                .child(userId);

        // Database reference pointing to friend_request object
        mFriendReqDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.FRIEND_REQUEST_OBJECT);

        // Database reference pointing to friends object
        mFriendsDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.FRIENDS_OBJECT);

        // Database reference pointing to notifications object
        mNotificationDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(IFirebaseConfig.NOTIFICATION_OBJECT);

        // Current firebase user id
        mCurrentUserId = FirebaseAuth.getInstance().getUid();

        mCurrentState = IFirebaseConfig.NOT_FRIEND;
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
        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the data from DataSnapshot
                String displayName = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.NAME).getValue()).toString();
                String status = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.STATUS).getValue()).toString();
                String imageUrl = Objects.requireNonNull(dataSnapshot.child(IFirebaseConfig.IMAGE).getValue()).toString();

                mUserProfile.setDisplayName(displayName);
                mUserProfile.setStatus(status);
                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);

                // Update layout to display accept friend request
                updateButtonTextToAcceptFriendRequest();

                // Update layout to display unfriend, if user is already a friend
                updateButtonTextToUnfriend();

                // Set callbacks to populate user profile layout
                getView().updateProfile(mUserProfile);
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
     */
    @Override
    public void sendFriendRequestButtonClick() {
        switch (mCurrentState) {
            case IFirebaseConfig.NOT_FRIEND:
                sendRequest();
                break;
            case IFirebaseConfig.REQ_SENT:
                cancelRequest();
                break;
            case IFirebaseConfig.REQ_RECEIVED:
                acceptRequest();
                break;
            case IFirebaseConfig.FRIENDS:
                removeFriend();
                break;
        }
    }

    /**
     * Call this method to send friend request
     * <p>
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
    public void sendRequest() {
        // As soon as user hits send request button, disable the button
        // so user can't be able to press another time and no new requests are made to
        // firebase database
        mUserProfile.setFriendReqButtonEnabled(false);
        getView().updateProfile(mUserProfile);

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
                                            // Update friend request button text and create notification
                                            createNotification();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            unableToSendFriendRequestError();
                                        }
                                    });
                        } else {
                            unableToSendFriendRequestError();
                        }
                    }
                });
    }

    /**
     * Call this method to cancel friend request
     */
    @Override
    public void cancelRequest() {
        // Disable the button so user can't be able to press another time and
        // no new requests are made to firebase database
        mUserProfile.setFriendReqButtonEnabled(false);
        getView().updateProfile(mUserProfile);

        mFriendReqDatabaseReference.child(mCurrentUserId)
                .child(mNewUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFriendReqDatabaseReference.child(mNewUserId)
                                    .child(mCurrentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // When we successfully updated database,
                                            // change the current state, and set button text to "send request"
                                            mCurrentState = IFirebaseConfig.NOT_FRIEND;
                                            mUserProfile.setFriendReqButtonEnabled(true);
                                            mUserProfile.setFriendReqButtonText("send friend request");
                                            getView().updateProfile(mUserProfile);
                                        }
                                    });
                        } else {
                            mUserProfile.setFriendReqButtonEnabled(true);
                            getView().updateProfile(mUserProfile);
                            getView().onError("Unable to cancel request!");
                        }
                    }
                });
    }

    /**
     * Call this method to accept the friend request
     * i.e if user1 accept the request from user2 friends table would look like
     * <p>
     * friends{
     * user1_key{
     * user2_key: currentDate
     * }
     * user2_key{
     * user1_key: currentDate
     * }
     * }
     */
    @Override
    public void acceptRequest() {
        // Get the current date and time
        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

        mFriendsDatabaseReference.child(mCurrentUserId).child(mNewUserId).setValue(currentDateTime)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFriendsDatabaseReference.child(mNewUserId).child(mCurrentUserId)
                                    .setValue(currentDateTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Remove references from friend_requests table
                                        removeRefFromFriendRequestTable();
                                    } else {
                                        getView().onError("Error accepting friend request");
                                    }
                                }
                            });
                        } else {
                            getView().onError("Error accepting friend request");
                        }
                    }
                });
    }

    /**
     * Call this method after accepting friend request to remove any references of
     * friend request from friend_requests table
     */
    @Override
    public void removeRefFromFriendRequestTable() {
        mFriendReqDatabaseReference.child(mCurrentUserId)
                .child(mNewUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFriendReqDatabaseReference.child(mNewUserId)
                                    .child(mCurrentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // When we successfully updated database,
                                                // change the current state, and set button text to "unfriend"
                                                mCurrentState = IFirebaseConfig.FRIENDS;
                                                mUserProfile.setFriendReqButtonText("unfriend");
                                                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                                                mUserProfile.setFriendReqButtonEnabled(true);
                                                getView().updateProfile(mUserProfile);
                                            }
                                        }
                                    });
                        } else {
                            getView().onError("Unable to cancel request!");
                            mUserProfile.setFriendReqButtonEnabled(true);
                            getView().updateProfile(mUserProfile);
                        }
                    }
                });
    }

    /**
     * Call this method to update btn text for accepting the friend request
     * Go to friend_requests/ currentUserId
     * go to userId of user on whose profile right now we are on
     * get the value of request_type parameter. If type is "received" change btn text to
     * "accept friend request" / if type is "sent" change btn text to "cancel friend request"
     */
    @Override
    public void updateButtonTextToAcceptFriendRequest() {
        mFriendReqDatabaseReference.child(mCurrentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(mNewUserId)) {
                            // Get the request_type from user on whose profile right now we are at
                            String reqType = Objects.requireNonNull(dataSnapshot.child(mNewUserId)
                                    .child(IFirebaseConfig.FRIEND_REQUEST_TYPE)
                                    .getValue())
                                    .toString();

                            switch (reqType) {
                                case IFirebaseConfig.FRIEND_REQUEST_RECEIVED:
                                    // If the request type is "received", change the button text to
                                    // accept the friend request
                                    mCurrentState = IFirebaseConfig.REQ_RECEIVED;
                                    mUserProfile.setFriendReqButtonText("accept friend request");
                                    mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_VISIBLE);
                                    getView().updateProfile(mUserProfile);
                                    break;
                                case IFirebaseConfig.FRIEND_REQUEST_SENT:
                                    // If the request type is "sent", change the button text to
                                    // cancel the friend request
                                    mCurrentState = IFirebaseConfig.REQ_SENT;
                                    mUserProfile.setFriendReqButtonText("cancel friend request");
                                    mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                                    getView().updateProfile(mUserProfile);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        getView().onError("Error retrieving user profile!");
                    }
                });
    }

    /**
     * Call this method to update button text to "unfriend"
     * Go to friends/ currentUserId
     * check if the id of person on whose profile we are on right now, exists or not
     */
    @Override
    public void updateButtonTextToUnfriend() {
        mFriendsDatabaseReference.child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mNewUserId)) {
                    mCurrentState = IFirebaseConfig.FRIENDS;
                    mUserProfile.setFriendReqButtonText("unfriend");
                    mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                    getView().updateProfile(mUserProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getView().onError("Error retrieving user profile!");
            }
        });
    }

    /**
     * Call this to unfriend the user
     */
    @Override
    public void removeFriend() {
        mFriendsDatabaseReference.child(mCurrentUserId).child(mNewUserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFriendsDatabaseReference.child(mNewUserId).child(mCurrentUserId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // When we successfully updated database,
                                                // change the current state, and set button text to "send request"
                                                mCurrentState = IFirebaseConfig.NOT_FRIEND;
                                                mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
                                                mUserProfile.setFriendReqButtonText("send friend request");
                                            } else {
                                                getView().onError("Error!");
                                            }
                                            mUserProfile.setFriendReqButtonEnabled(true);
                                            getView().updateProfile(mUserProfile);
                                        }
                                    });
                        } else {
                            getView().onError("Error!");
                        }
                    }
                });
    }

    /**
     * This method will be called when user clicks on decline request button
     */
    @Override
    public void onDeclineFriendRequestBtnClick() {
        cancelRequest();
        mUserProfile.setDeclineFriendReqButtonVisibility(IConstants.VIEW_GONE);
        getView().updateProfile(mUserProfile);
    }

    /**
     * Call this method to store detail about notification to database
     * and update the friend request button text to "cancel friend request"
     */
    @Override
    public void createNotification() {
        // HashMap for notification details
        HashMap<String, String> notificationMap = new HashMap<>();
        notificationMap.put(IFirebaseConfig.NOTIFICATION_FROM, mCurrentUserId);
        notificationMap.put(IFirebaseConfig.NOTIFICATION_TYPE, IFirebaseConfig.NOTIFICATION_TYPE_REQUEST);

        mNotificationDatabaseReference.child(mNewUserId)
                .push()
                .setValue(notificationMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // When we successfully updated database,
                            // change the current state, and set button text to "cancel request"
                            mCurrentState = IFirebaseConfig.REQ_SENT;
                            mUserProfile.setFriendReqButtonEnabled(true);
                            mUserProfile.setFriendReqButtonText("cancel friend request");
                            getView().updateProfile(mUserProfile);
                            getView().friendRequestSentSuccessfully("Friend request sent!");
                        } else {
                            unableToSendFriendRequestError();
                        }
                    }
                });
    }

    /**
     * Call this method to show error message about "not able to send friend request"
     * and enable friend request button
     */
    @Override
    public void unableToSendFriendRequestError() {
        mUserProfile.setFriendReqButtonEnabled(true);
        getView().updateProfile(mUserProfile);
        getView().onError("Unable to send friend request!");
    }
}
