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
 * File Created on 11/06/18 11:28 PM by nishant
 * Last Modified on 11/06/18 11:28 PM
 */

package com.example.nishant.berry.ui.interaction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.ui.model.InteractionActionBar;
import com.example.nishant.berry.ui.model.Message;
import com.example.nishant.berry.ui.utils.GetTimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InteractionPresenter
        extends BasePresenter<InteractionContract.View>
        implements InteractionContract.Presenter {

    private static final int NUMBER_OF_MESSAGES_PER_PAGE = 20;
    private String mInteractionUserId;
    private String mCurrentUserId;
    private String mDisplayName;
    private String mLastSeen;
    private String mAvatarThumbUrl;
    private String mOnlineStatus;
    private String mLastMessageKey = "";
    private String mPreviousMessageKey = "";
    private DatabaseReference mRootRef;
    private DatabaseReference mUsersRootRef;
    private DatabaseReference mInteractionsRootRef;
    private int mCurrentPage = 1;
    private int mItemPosition = 0;
    private List<Message> mMessageList = new ArrayList<>();

    InteractionPresenter(Intent receivedIntent) {
        // Extract the userId and user displayName from intent
        mInteractionUserId = receivedIntent.hasExtra(IConstants.KEY_USER_ID) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_ID) : null;
        mDisplayName = receivedIntent.hasExtra(IConstants.KEY_USER_DISPLAY_NAME) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_DISPLAY_NAME) : null;
        if (mInteractionUserId == null) return;

        // Current user Id
        mCurrentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Root reference to databases
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRootRef = mRootRef.child(IFirebaseConfig.USERS_OBJECT);
        mInteractionsRootRef = mRootRef.child(IFirebaseConfig.INTERACTIONS_OBJECT);
    }

    @Override
    public InteractionContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(InteractionContract.View view) {
        super.attachView(view);
        getView().setUpRecyclerView();
        updateMessageList();
        extractBasicInfoDatabase();
        initInteractionDatabase();
    }

    /**
     * Call this method to extract the basic information about user from database
     * like display name, thumb avatar url, user online status etc
     */
    @Override
    public void extractBasicInfoDatabase() {
        mUsersRootRef.child(mInteractionUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAvatarThumbUrl = dataSnapshot.child(IFirebaseConfig.THUMBNAIL).getValue().toString();
                mLastSeen = dataSnapshot.child(IFirebaseConfig.LAST_SEEN).getValue().toString();
                boolean online = (boolean) dataSnapshot.child(IFirebaseConfig.ONLINE).getValue();
                if (online) {
                    mOnlineStatus = "Online";
                } else {
                    // Get the last seen time in human readable format
                    mOnlineStatus = GetTimeAgo.getTimeAgo(Long.parseLong(mLastSeen));
                }

                // Create a model for setting views in custom actionbar
                InteractionActionBar model = new InteractionActionBar();
                model.setAvatarUrl(mAvatarThumbUrl);
                model.setName(mDisplayName);
                model.setOnlineStatus(mOnlineStatus);

                // Set call back for setting up action bar
                getView().setActionBar(model);
                // Callback for updating RecyclerView with interaction user avatar
                getView().interactionUserAvatar(mAvatarThumbUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getView().setActionBar(
                        new InteractionActionBar("Berry", "default", "Hey It's Berry")
                );
            }
        });
    }

    /**
     * Call this method to initialize Interactions database
     */
    @Override
    public void initInteractionDatabase() {
        mInteractionsRootRef.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(mInteractionUserId)) {

                    Map<String, Object> interactionMap = new HashMap<>();
                    interactionMap.put(IFirebaseConfig.MESSAGE_SEEN, false);
                    interactionMap.put(IFirebaseConfig.TIMESTAMP, ServerValue.TIMESTAMP);

                    // Create entries for interactions database
                    Map<String, Object> interactionUserMap = new HashMap<>();
                    interactionUserMap.put(IFirebaseConfig.INTERACTIONS_OBJECT + "/" + mCurrentUserId + "/" + mInteractionUserId, interactionMap);
                    interactionUserMap.put(IFirebaseConfig.INTERACTIONS_OBJECT + "/" + mInteractionUserId + "/" + mCurrentUserId, interactionMap);

                    mRootRef.updateChildren(interactionUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            // If there an error updating children, log the error message
                            if (databaseError != null) {
                                Log.d("CHAT_LOG", databaseError.getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method will be invoked when user clicks send button from @{@link InteractionActivity}
     * layout
     *
     * @param message message user want to send
     */
    @Override
    public void onInteractions(String message) {
        if (TextUtils.isEmpty(message)) return;

        String currentUserRef = IFirebaseConfig.MESSAGE_OBJECT + "/" + mCurrentUserId + "/" + mInteractionUserId;
        String interactionUserRef = IFirebaseConfig.MESSAGE_OBJECT + "/" + mInteractionUserId + "/" + mCurrentUserId;

        // Get the push ID
        DatabaseReference messagePushRef = mRootRef.child(IFirebaseConfig.MESSAGE_OBJECT)
                .child(mCurrentUserId)
                .child(mInteractionUserId)
                .push();
        String messagePushId = messagePushRef.getKey();

        // Values to store at each user-reference
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put(IFirebaseConfig.MESSAGE_DATA, message);
        messageMap.put(IFirebaseConfig.MESSAGE_SEEN, false);
        messageMap.put(IFirebaseConfig.MESSAGE_TYPE, "text");
        messageMap.put(IFirebaseConfig.MESSAGE_TIME, ServerValue.TIMESTAMP);
        messageMap.put(IFirebaseConfig.MESSAGE_FROM, mCurrentUserId);

        // Map to update user-references
        Map<String, Object> messageUserMap = new HashMap<>();
        messageUserMap.put(currentUserRef + "/" + messagePushId, messageMap);
        messageUserMap.put(interactionUserRef + "/" + messagePushId, messageMap);

        // Update interaction database time stamp
        mRootRef.child(IFirebaseConfig.INTERACTIONS_OBJECT).child(mCurrentUserId).child(mInteractionUserId).child(IFirebaseConfig.TIMESTAMP).setValue(ServerValue.TIMESTAMP);
        mRootRef.child(IFirebaseConfig.INTERACTIONS_OBJECT).child(mInteractionUserId).child(mCurrentUserId).child(IFirebaseConfig.TIMESTAMP).setValue(ServerValue.TIMESTAMP);

        // Update the database
        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                // If there an error updating children, log the error message
                // If the task is successful set callBack to clear the EditText field
                if (databaseError != null) {
                    Log.d("CHAT_LOG", databaseError.getMessage());
                } else {
                    getView().clearEditTextField();
                }
            }
        });
    }

    /**
     * Get the messages from database and set callback to update recycler view
     */
    @Override
    public void updateMessageList() {
        DatabaseReference messageRef = mRootRef.child(IFirebaseConfig.MESSAGE_OBJECT)
                .child(mCurrentUserId)
                .child(mInteractionUserId);
        Query query = messageRef.limitToLast(mCurrentPage * NUMBER_OF_MESSAGES_PER_PAGE);

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
                getView().updateMessageList(mMessageList);
                updateMessageSeenStatus(dataSnapshot.getKey());
                getView().onSwipeRefreshComplete();
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

            }
        });
    }

    /**
     * Call this method to refresh message list
     */
    @Override
    public void swipeMessageRefresh() {
        mItemPosition = 0;
        mCurrentPage++;
        updateMoreMessageToList();
    }

    /**
     * Call this method to add more messages to list when user swipe message list to load more
     * previous messages
     */
    @Override
    public void updateMoreMessageToList() {
        DatabaseReference messageRef = mRootRef.child(IFirebaseConfig.MESSAGE_OBJECT)
                .child(mCurrentUserId)
                .child(mInteractionUserId);

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
                getView().updateMessageList(mMessageList);
                getView().setLayoutManagerOffset(NUMBER_OF_MESSAGES_PER_PAGE - 1);
                getView().onSwipeRefreshComplete();
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

            }
        });
    }

    /**
     * Update the message seen values : true or false
     *
     * @param key push key for messages
     */
    @Override
    public void updateMessageSeenStatus(String key) {
        if (key == null) return;
        DatabaseReference seenMessageRef =
                FirebaseDatabase.getInstance().getReference()
                        .child(IFirebaseConfig.MESSAGE_OBJECT)
                        .child(mCurrentUserId)
                        .child(mInteractionUserId)
                        .child(key)
                        .child(IFirebaseConfig.MESSAGE_SEEN);
        seenMessageRef.setValue(true);
    }
}
