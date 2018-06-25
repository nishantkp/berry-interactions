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
 * File Created on 24/06/18 1:18 PM by nishant
 * Last Modified on 24/06/18 1:18 PM
 */

package com.example.nishant.berry.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Manager class, that deals with business logic
 */
public class DataManager {
    private static DataManager sDataManager;
    private static FirebaseUtils sFirebaseUtils;
    private SignInCallback mSignInCallback;
    private SignUpCallback mSignUpCallback;

    // Singleton
    public static DataManager getInstance(Context context) {
        if (sDataManager == null) {
            sDataManager = new DataManager();
            sFirebaseUtils = FirebaseUtils.getInstance();
        }
        return sDataManager;
    }

    /**
     * Call this method to get current user's ID
     *
     * @return user Id
     */
    public static String getCurrentUserId() {
        return sFirebaseUtils.getCurrentUserId();
    }

    /**
     * Call this method to get root firebase database reference
     *
     * @return root database reference
     */
    public static DatabaseReference getRootRef() {
        return sFirebaseUtils.getRootRef();
    }

    /**
     * Call this method to get reference to Users object on root database
     *
     * @return firebase database reference to Users object
     */
    public static DatabaseReference getUsersRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.USERS_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Users object in root ref
     *
     * @return database reference for current user in users object
     */
    public static DatabaseReference getCurrentUsersRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.USERS_OBJECT);
    }

    /**
     * Call this method to get reference to particular user in Users object in root ref
     *
     * @param userId user Id
     * @return database reference for a user whose userId is passed in
     */
    public static DatabaseReference getNewUserRef(String userId) {
        return sFirebaseUtils.getUserObjectRef(userId, IFirebaseConfig.USERS_OBJECT);
    }

    /**
     * Call this method to get database reference of user from Users object in root ref
     *
     * @return database reference for current user in users object
     */
    public static DatabaseReference getSecondUsersRef(String userId) {
        return null;
    }

    /**
     * Call this method to get reference to Friend Requests object on root database
     *
     * @return firebase database reference to Friend Requests object
     */
    public static DatabaseReference getFriendReqRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.FRIEND_REQUEST_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Friend requests object in root ref
     *
     * @return database reference for current user in Friend requests object
     */
    public static DatabaseReference getCurrentUserFriendsReqRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.FRIEND_REQUEST_OBJECT);
    }

    /**
     * Call this method to get reference to Friends object on root database
     *
     * @return firebase database reference to Friends object
     */
    public static DatabaseReference getFriendsRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.FRIENDS_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Friends object in root ref
     *
     * @return database reference for current user in Friends object
     */
    public static DatabaseReference getCurrentUserFriendsRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.FRIENDS_OBJECT);
    }

    /**
     * Call this method to get reference to Interactions object on root database
     *
     * @return firebase database reference to Interactions object
     */
    public static DatabaseReference getInteractionsRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.INTERACTIONS_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Interaction object in root ref
     *
     * @return database reference for current user in interaction object
     */
    public static DatabaseReference getCurrentUserInteractionRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.INTERACTIONS_OBJECT);
    }

    /**
     * Call this method to get reference to Notification object on root database
     *
     * @return firebase database reference to Notification object
     */
    public static DatabaseReference getNotificationRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.NOTIFICATION_OBJECT);
    }

    /**
     * Call this method to get reference to Message object on root database
     *
     * @return firebase database reference to Message object
     */
    public static DatabaseReference getMessageRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.MESSAGE_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Message object in root ref
     *
     * @return database reference for current user in Message object
     */
    public static DatabaseReference getCurrentUserMessageRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.MESSAGE_OBJECT);
    }

    /**
     * Call this method to get firebase storage reference to store user's avatar
     *
     * @return user's avatar storage reference
     */
    public static StorageReference getAvatarStorageRef() {
        return sFirebaseUtils.getAvatarStorageRef();
    }

    /**
     * Call this method to get firebase storage reference to store user's avatar thumbnail
     *
     * @return user's avatar thumbnail storage reference
     */
    public static StorageReference getAvatarThumbStorageRef() {
        return sFirebaseUtils.getAvatarThumbnailStorageRef();
    }

    /**
     * Use this method to sign out current user and
     * Update user's status offline and last_seen to firebase's timestamp before signing out
     */
    public static void signOutUser() {
        getCurrentUsersRef().child(IFirebaseConfig.ONLINE).setValue(false);
        getCurrentUsersRef().child(IFirebaseConfig.LAST_SEEN).setValue(ServerValue.TIMESTAMP);
        sFirebaseUtils.signOut();
    }

    /**
     * Call this method to check whether current user is available or not
     *
     * @return true if the current user is available/ false if it's not resent
     */
    public static boolean isCurrentUserAvailable() {
        return sFirebaseUtils.isCurrentUserAvailable();
    }

    /**
     * Set sign in callback for interface
     *
     * @param callback SignInCallBack must be initialized with the class which implements
     *                 call backs
     */
    public void setSignInCallback(SignInCallback callback) {
        mSignInCallback = callback;
    }

    /**
     * Set sign up callback for interface
     *
     * @param callback SignUpCallBack must be initialized with the class which implements
     *                 call backs
     */
    public void setSignUpCallback(SignUpCallback callback) {
        mSignUpCallback = callback;
    }

    /**
     * Call this method to login user to it's account
     *
     * @param email    email of user
     * @param password password provided by user
     */
    public void loginUser(String email, String password) {
        sFirebaseUtils.getFirebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the device token
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            String userId = getCurrentUserId();

                            Map<String, Object> singInMap = new HashMap<>();
                            singInMap.put(IFirebaseConfig.DEVICE_TOKEN_ID, deviceToken);
                            singInMap.put(IFirebaseConfig.ONLINE, true);

                            // Store token Id to users database
                            getUsersRef().child(userId).updateChildren(singInMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        mSignInCallback.signInError("Sign in error!");
                                    } else {
                                        mSignInCallback.signInSuccess();
                                    }
                                }
                            });
                        } else {
                            mSignInCallback.signInError("Sign in error!");
                        }
                    }
                });
    }

    /**
     * Call this method to register user in firebase
     *
     * @param displayName display name of user
     * @param email       email
     * @param password    password
     */
    public void signUpUser(final String displayName, String email, String password) {
        // Register user with email, password and cancel progress dialog
        sFirebaseUtils.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {

                            //Store data
                            storeDataToFirebaseDatabase(displayName);
                        } else {
                            mSignUpCallback.signUpError("Error creating account!");
                        }
                    }
                });
    }

    /**
     * Store user data to database and sets callbacks for success and error
     *
     * @param displayName display name
     */
    private void storeDataToFirebaseDatabase(String displayName) {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();

        // Value Map for Firebase database
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(IFirebaseConfig.NAME, displayName);
        userMap.put(IFirebaseConfig.STATUS, IFirebaseConfig.DEFAULT_STATUS);
        userMap.put(IFirebaseConfig.IMAGE, IFirebaseConfig.DEFAULT_VALUE);
        userMap.put(IFirebaseConfig.THUMBNAIL, IFirebaseConfig.DEFAULT_VALUE);
        userMap.put(IFirebaseConfig.DEVICE_TOKEN_ID, deviceToken);
        userMap.put(IFirebaseConfig.ONLINE, true);

        // Set the values to Firebase database
        DataManager.getCurrentUsersRef().setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            mSignUpCallback.signUpSuccess();
                        } else {
                            mSignUpCallback.signUpError("Error creating account!");
                        }
                    }
                });
    }

    /**
     * SignIn Callbacks when user signs in
     */
    public interface SignInCallback {
        void signInSuccess();

        void signInError(String message);
    }

    /**
     * SignUp Callbacks when user signs up
     */
    public interface SignUpCallback {
        void signUpSuccess();

        void signUpError(String message);
    }
}