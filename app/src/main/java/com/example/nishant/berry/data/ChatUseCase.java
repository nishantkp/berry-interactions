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
 * File Created on 15/08/18 7:03 PM by nishant
 * Last Modified on 15/08/18 2:47 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.model.AllUsers;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ChatUtility class to display chat list
 */
@Singleton
public final class ChatUseCase {
    // TODO: Use stack instead linkedList to store interaction data : <<<FUTURE UPDATE>>>
    private static List<AllUsers> mData = new LinkedList<>();
    private FbUsersUseCase mFbUsersUseCase;

    @Inject
    public ChatUseCase(FbUsersUseCase fbUsersUseCase) {
        mFbUsersUseCase = fbUsersUseCase;
    }

    /**
     * Call this method to get the all users interactions, i.e list of all the users
     * with whom current has had interaction with
     *
     * @param callback DataCallback for error handling and list of all interactions
     */
    void getUsersInteraction(@NonNull final OnUsersList callback) {
        // Query for Interactions database
        Query query = mFbUsersUseCase.getCurrentUserInteractionRef()
                .orderByChild(IFirebaseConfig.TIMESTAMP);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get all Ids from interaction database
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    getUsersInfo(data.getKey(), callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Call this method to get information about particular user
     *
     * @param id       id of user with whom current user had interaction with
     * @param callback DataCallback for Error message
     *                 Error we get while querying users database
     */
    private void getUsersInfo(final String id,
                              @NonNull final OnUsersList callback) {
        // Use FbUsersUseCase method to get information about particular user
        mFbUsersUseCase.getUsersObject(id, new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId) {
                model.setId(id);
                getLastMessage(model, callback);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Call this method to get the last message sent/ received by user and update index of user
     * in the list accordingly
     *
     * @param users    User object containing UserId, name and thumbnail url of user avatar
     * @param callback DataCallback for error and list of data
     *                 Error we get while querying message reference for last massage send/ receive
     *                 by user
     */
    private void getLastMessage(final AllUsers users,
                                @NonNull final OnUsersList callback) {
        // Query for Message object to get the last message for particular user
        final Query lastMessageQuery = mFbUsersUseCase.getCurrentUserMessageRef()
                .child(users.getId())
                .limitToLast(1);

        lastMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Get the last message and message seen from DataSnapshot
                String lastMessage = (String) dataSnapshot.child(IFirebaseConfig.MESSAGE_DATA).getValue();
                boolean messageSeen = (boolean) dataSnapshot.child(IFirebaseConfig.MESSAGE_SEEN).getValue();
                users.setStatus(lastMessage);
                users.setMessageSeen(messageSeen);

                // If user doesn't exists in the list, the add the user first
                if (!mData.contains(users)) {
                    mData.add(users);
                } else {
                    int index = mData.indexOf(users);
                    mData.remove(index);
                    mData.add(mData.size(), users);
                }
                callback.onData(mData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }
}
