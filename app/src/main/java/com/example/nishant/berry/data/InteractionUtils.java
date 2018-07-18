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
 * File Created on 17/07/18 7:33 PM by nishant
 * Last Modified on 17/07/18 7:33 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnInteraction;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.ui.model.AllUsers;
import com.example.nishant.berry.ui.model.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class that deals with getting chat between current user and user defined by
 * interactionUserId
 */
final class InteractionUtils {

    private static final int NUMBER_OF_MESSAGES_PER_PAGE = 20;
    private static FirebaseUtils sFirebaseUtils;
    private String mLastMessageKey = "";
    private String mPreviousMessageKey = "";
    private int mCurrentPage = 1;
    private int mItemPosition = 0;
    private DatabaseReference mRootRef;
    private List<Message> mMessageList = new ArrayList<>();

    InteractionUtils() {
        sFirebaseUtils = new FirebaseUtils();
        mRootRef = DataManager.getRootRef();
    }

    /**
     * Call this method to get the list of messages
     *
     * @param interactionUserId Id if a user with whom current user had chat with
     * @param callback          Callback for message list and error
     */
    void getMessageList(@NonNull final String interactionUserId, @NonNull final OnInteraction callback) {
        // Initialize interaction database, so that we can get list of user with whom current user
        // had chat with
        initInteractionDatabase(interactionUserId, callback);

        Query query = DataManager.getMessageRef()
                .child(DataManager.getCurrentUserId())
                .child(interactionUserId)
                .limitToLast(mCurrentPage * NUMBER_OF_MESSAGES_PER_PAGE);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                mItemPosition++;
                if (mItemPosition == 1) {
                    mLastMessageKey = dataSnapshot.getKey();
                    mPreviousMessageKey = mLastMessageKey;
                }
                mMessageList.add(message);
                callback.onInteractions(mMessageList);
                updateMessageSeenStatus(dataSnapshot.getKey(), interactionUserId);
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

    /**
     * Update the message seen values : true or false
     *
     * @param key               push key for messages
     * @param interactionUserId id of user with whom current user is chatting
     */
    private void updateMessageSeenStatus(String key, String interactionUserId) {
        if (key == null) return;
        DatabaseReference seenMessageRef =
                DataManager.getMessageRef()
                        .child(DataManager.getCurrentUserId())
                        .child(interactionUserId)
                        .child(key)
                        .child(IFirebaseConfig.MESSAGE_SEEN);
        seenMessageRef.setValue(true);
    }

    /**
     * Call this method to get information about chat user
     *
     * @param interactionUserId Id of user with whom current user is chatting
     * @param callback          DataCallback for user information and error
     */
    void getChatUserInfo(@NonNull final String interactionUserId, @NonNull final OnUsersData callback) {
        sFirebaseUtils.getUsersObject(interactionUserId, new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId) {
                callback.onData(model, userId);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Call this method to update database with message sent by current user
     *
     * @param interactionUserId Id of user with whom current user is chatting
     * @param message           Message sent by current user
     * @param callback          DataCallback for task success/ failure
     */
    void onInteraction(@NonNull final String interactionUserId,
                       @NonNull final String message,
                       @NonNull final OnTaskCompletion callback) {

        String currentUserId = DataManager.getCurrentUserId();
        String currentUserRef = IFirebaseConfig.MESSAGE_OBJECT + "/" + currentUserId + "/" + interactionUserId;
        String interactionUserRef = IFirebaseConfig.MESSAGE_OBJECT + "/" + interactionUserId + "/" + currentUserId;

        // Get the push ID
        DatabaseReference messagePushRef = DataManager.getMessageRef()
                .child(currentUserId)
                .child(interactionUserId)
                .push();
        String messagePushId = messagePushRef.getKey();

        // Values to store at each user-reference
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put(IFirebaseConfig.MESSAGE_DATA, message);
        messageMap.put(IFirebaseConfig.MESSAGE_SEEN, false);
        messageMap.put(IFirebaseConfig.MESSAGE_TYPE, "text");
        messageMap.put(IFirebaseConfig.MESSAGE_TIME, ServerValue.TIMESTAMP);
        messageMap.put(IFirebaseConfig.MESSAGE_FROM, currentUserId);

        // Map to update user-references
        Map<String, Object> messageUserMap = new HashMap<>();
        messageUserMap.put(currentUserRef + "/" + messagePushId, messageMap);
        messageUserMap.put(interactionUserRef + "/" + messagePushId, messageMap);

        // Update interaction database time stamp
        mRootRef.child(IFirebaseConfig.INTERACTIONS_OBJECT).child(currentUserId).child(interactionUserId).child(IFirebaseConfig.TIMESTAMP).setValue(ServerValue.TIMESTAMP);
        mRootRef.child(IFirebaseConfig.INTERACTIONS_OBJECT).child(interactionUserId).child(currentUserId).child(IFirebaseConfig.TIMESTAMP).setValue(ServerValue.TIMESTAMP);

        // Update the database
        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                // If there an error updating children, set error callback
                if (databaseError != null)
                    callback.onError(databaseError.getMessage());
                else
                    callback.onSuccess();
            }
        });
    }

    /**
     * Call this method to initialize interaction database
     *
     * @param interactionUserId Id of user with current user is chatting
     * @param callback          DataCallback for error
     */
    private void initInteractionDatabase(@NonNull final String interactionUserId,
                                         @NonNull final OnInteraction callback) {
        final String currentUserId = DataManager.getCurrentUserId();
        DataManager.getInteractionsRef().child(DataManager.getCurrentUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(interactionUserId)) {

                            Map<String, Object> interactionMap = new HashMap<>();
                            interactionMap.put(IFirebaseConfig.MESSAGE_SEEN, false);
                            interactionMap.put(IFirebaseConfig.TIMESTAMP, ServerValue.TIMESTAMP);

                            // Create entries for interactions database
                            Map<String, Object> interactionUserMap = new HashMap<>();
                            interactionUserMap.put(IFirebaseConfig.INTERACTIONS_OBJECT + "/" + currentUserId + "/" + interactionUserId, interactionMap);
                            interactionUserMap.put(IFirebaseConfig.INTERACTIONS_OBJECT + "/" + interactionUserId + "/" + currentUserId, interactionMap);

                            mRootRef.updateChildren(interactionUserMap,
                                    new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            // If there an error updating children, log the error message
                                            if (databaseError != null) {
                                                callback.onError(databaseError.getMessage());
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());
                    }
                });
    }

    /**
     * Call this method to add more messages to list
     *
     * @param interactionUserId Id of user with whom current user od chatting
     * @param callback          DataCallback for list of messages, list offset to view messages in
     *                          ListView/ RecyclerView properly and error
     */
    void loadMoreMessages(@NonNull final String interactionUserId, @NonNull final OnInteraction callback) {
        mItemPosition = 0;
        mCurrentPage++;
        updateMoreMessageToList(interactionUserId, callback);
    }

    /**
     * Helper method to add more messages to list
     *
     * @param interactionUserId Id of user with whom current user od chatting
     * @param callback          DataCallback for list of messages, list offset to view messages in
     *                          ListView/ RecyclerView properly and error
     */
    private void updateMoreMessageToList(@NonNull final String interactionUserId,
                                         @NonNull final OnInteraction callback) {
        DatabaseReference messageRef = mRootRef.child(IFirebaseConfig.MESSAGE_OBJECT)
                .child(DataManager.getCurrentUserId())
                .child(interactionUserId);

        Query query = messageRef.orderByKey()
                .endAt(mLastMessageKey)
                .limitToLast(NUMBER_OF_MESSAGES_PER_PAGE);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);

                // If our previous message key is not same as current message key, add that message
                // to message list
                if (!mPreviousMessageKey.equals(dataSnapshot.getKey())) {
                    mMessageList.add(mItemPosition++, message);
                } else {
                    mPreviousMessageKey = mLastMessageKey;
                }

                if (mItemPosition == 1) mLastMessageKey = dataSnapshot.getKey();
                callback.onInteractions(mMessageList);
                callback.listOffset(NUMBER_OF_MESSAGES_PER_PAGE - 1);
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
