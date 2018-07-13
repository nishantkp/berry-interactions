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
 * File Created on 12/07/18 7:47 PM by nishant
 * Last Modified on 12/07/18 7:47 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that deals with login and signUp of user, saving status
 */
final class AccountUtils {
    private static FirebaseUtils sFirebaseUtils;

    AccountUtils() {
        sFirebaseUtils = new FirebaseUtils();
    }

    //--------------------------------------------------------------------------------------------//
    //-----------------------------------------SignIn---------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    /**
     * Call this method to log user into it's account
     *
     * @param email    email of user
     * @param password password provided by user
     * @param callback callback for success and errors
     */
    public void signInUser(@NonNull final String email,
                           @NonNull final String password,
                           @NonNull final DataCallback.OnTaskCompletion callback) {
        sFirebaseUtils.getFirebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUserDatabase(callback);
                        } else {
                            callback.onError("Sign in error!");
                        }
                    }
                });
    }

    /**
     * Call this method to update user's database with device token and online status
     *
     * @param callback callback for success and errors
     */
    private void updateUserDatabase(@NonNull final DataCallback.OnTaskCompletion callback) {
        // Get the device token
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        String userId = DataManager.getCurrentUserId();

        Map<String, Object> singInMap = new HashMap<>();
        singInMap.put(IFirebaseConfig.DEVICE_TOKEN_ID, deviceToken);
        singInMap.put(IFirebaseConfig.ONLINE, true);

        // Store token Id to users database
        DataManager.getUsersRef().child(userId).updateChildren(singInMap,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            callback.onError("Sign in error!");
                        } else {
                            callback.onSuccess();
                        }
                    }
                });
    }

    //--------------------------------------------------------------------------------------------//
    //-----------------------------------------SignUp---------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    /**
     * Call this method to register user in firebase
     *
     * @param displayName display name of user
     * @param email       email
     * @param password    password
     * @param callback    callbacks for success and failure
     */
    void signUpUser(@NonNull final String displayName,
                    @NonNull String email,
                    @NonNull String password,
                    @NonNull final DataCallback.OnTaskCompletion callback) {
        // Register user with email, password and cancel progress dialog
        sFirebaseUtils.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            //Store data
                            storeDataToFirebaseDatabase(displayName, callback);
                        } else {
                            callback.onError("Error creating account!");
                        }
                    }
                });
    }

    /**
     * Store user data to database and sets callbacks for success and error
     *
     * @param displayName display name
     * @param callback    callbacks for success and failure
     */
    private void storeDataToFirebaseDatabase(@NonNull String displayName,
                                             @NonNull final DataCallback.OnTaskCompletion callback) {
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
                            callback.onSuccess();
                        } else {
                            callback.onError("Error creating account!");
                        }
                    }
                });
    }

    //--------------------------------------------------------------------------------------------//
    //-----------------------------------------Status---------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    /**
     * Call this method for saving status to firebase database
     *
     * @param status   User's status
     * @param callback callbacks for success and failure
     */
    void saveUserStatus(@NonNull String status,
                        @NonNull final DataCallback.OnTaskCompletion callback) {
        DataManager.getCurrentUsersRef().child(IFirebaseConfig.STATUS).setValue(status)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("Error while saving changes!");
                        }
                    }
                });
    }
}
