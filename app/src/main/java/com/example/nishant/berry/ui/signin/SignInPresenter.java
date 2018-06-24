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
 * File Created on 02/06/18 6:32 PM by nishant
 * Last Modified on 02/06/18 6:32 PM
 */

package com.example.nishant.berry.ui.signin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.ui.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInPresenter
        extends BasePresenter<SignInContract.View>
        implements SignInContract.Presenter {

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabaseReference;

    SignInPresenter() {
        mAuth = FirebaseAuth.getInstance();

        // Database reference pointing to users object
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(IFirebaseConfig.USERS_OBJECT);
    }

    @Override
    public SignInContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SignInContract.View view) {
        super.attachView(view);
    }

    @Override
    public void signInUser(String email, String password, SignInContract.View.SignInCallback callback) {

        // Validation check
        if (!User.isEmailValid(email)) {
            callback.invalidEmail("Enter valid email");
            return;
        }
        if (!User.isPasswordValid(password)) {
            callback.invalidPassword("Enter valid password");
            return;
        }

        // Show progress dialog
        getView().showProgressDialog();

        // SignIn user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            storeDeviceToken();
                        } else {
                            getView().cancelProgressDialog();
                            getView().signInError("Email or password is incorrect!");
                        }
                    }
                });
    }

    /**
     * Call this method to store token Id to user database, when user logs in
     */
    @Override
    public void storeDeviceToken() {
        // Get the device token
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        String userId = DataManager.getCurrentUserId();

        Map<String, Object> singInMap = new HashMap<>();
        singInMap.put(IFirebaseConfig.DEVICE_TOKEN_ID, deviceToken);
        singInMap.put(IFirebaseConfig.ONLINE, true);

        // Store token Id to users database
        mUserDatabaseReference.child(userId).updateChildren(singInMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    getView().cancelProgressDialog();
                    getView().signInError("Sign in error!");
                } else {
                    getView().cancelProgressDialog();
                    getView().signInSuccess();
                }
            }
        });
//                .child(IFirebaseConfig.DEVICE_TOKEN_ID)
//                .setValue(deviceToken)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isComplete() && task.isSuccessful()) {
//                            getView().cancelProgressDialog();
//                            getView().signInSuccess();
//                        } else {
//                            getView().cancelProgressDialog();
//                            getView().signInError("Sign in error!");
//                        }
//                    }
//                });
    }
}
