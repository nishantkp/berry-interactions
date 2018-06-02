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

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.ui.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInPresenter
        extends BasePresenter<SignInContract.View>
        implements SignInContract.Presenter {

    // Firebase
    private FirebaseAuth mAuth;

    SignInPresenter() {
        mAuth = FirebaseAuth.getInstance();
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

        // SignIn user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) getView().signInSuccess();
                        else getView().signInError("Email or password is incorrect!");
                    }
                });
    }
}
