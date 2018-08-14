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
 * File Created on 15/07/18 2:57 PM by nishant
 * Last Modified on 15/07/18 2:57 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.model.AllUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class the deals with finding user from User's database in Firebase Database
 */
public final class SearchUseCase {
    private FbUsersUseCase mFbUsersUseCase;

    public SearchUseCase(FbUsersUseCase fbUsersUseCase) {
        mFbUsersUseCase = fbUsersUseCase;
    }

    /**
     * Call this method to find user from user's object in Firebase database
     *
     * @param searchString Data entered by user
     * @param limit        Number of users you want to see in search results
     * @param callback     Data callback for list of users and error
     */
    void findUser(String searchString, int limit, @NonNull final OnUsersList callback) {
        // Firebase database Query of Users object
        Query databaseQuery = mFbUsersUseCase.getUsersRef()
                .orderByChild(IFirebaseConfig.NAME)
                .startAt(searchString.toUpperCase())
                .endAt(searchString.toLowerCase() + "\uf8ff")
                .limitToFirst(limit);

        //Get data from user's object depending upon query
        databaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AllUsers> usersList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AllUsers user = data.getValue(AllUsers.class);
                    if (user != null) {
                        user.setId(data.getKey());
                        user.setOnline(false);
                        // If user doesn't exists in the list add it
                        if (!usersList.contains(user)) usersList.add(user);
                        callback.onData(usersList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }
}
