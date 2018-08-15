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
 * File Created on 24/06/18 1:23 PM by nishant
 * Last Modified on 24/06/18 1:23 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.model.AllUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Firebase Utility class
 */
@Singleton
public final class FbUsersUseCase {
    private List<AllUsers> mAllUsersList = new LinkedList<>();

    @Inject
    public FbUsersUseCase() {
    }

    /**
     * Call this method to get firebase Auth instance
     *
     * @return Firebase Auth instance
     */
    FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    /**
     * Call this method get the current user's ID
     *
     * @return user Id
     */
    @NonNull
    String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    /**
     * Call this method to get root firebase database reference
     *
     * @return root database reference
     */
    @NonNull
    DatabaseReference getRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Call this method to get reference to Users object on root database
     *
     * @return firebase database reference to Users object
     */
    @NonNull
    DatabaseReference getUsersRef() {
        return getMainObjectRef(IFirebaseConfig.USERS_OBJECT);
    }

    /**
     * Call this method to get root firebase storage reference
     *
     * @return root storage reference
     */
    @NonNull
    private StorageReference getStorageRootRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    /**
     * Call this method to get reference to objects in root database
     *
     * @param object object in root database
     * @return database reference
     */
    @NonNull
    private DatabaseReference getMainObjectRef(@NonNull String object) {
        return getRootRef().child(object);
    }

    /**
     * Call this method to get reference to particular user in particular object in root ref
     *
     * @param userId id of a user
     * @param object object in root database
     * @return database reference
     */
    @NonNull
    DatabaseReference getUserObjectRef(@NonNull String userId, @NonNull String object) {
        return getRootRef().child(object).child(userId);
    }

    /**
     * Call this method to get reference of current user from main objects in root ref
     *
     * @param object Main object in root ref
     * @return database reference for current user in users object
     */
    @NonNull
    DatabaseReference getCurrentUserRefFromMainObject(@NonNull String object) {
        return getMainObjectRef(object).child(getCurrentUserId());
    }

    /**
     * Call this method to get reference of current user from Friend requests object in root ref
     *
     * @return database reference for current user in Friend requests object
     */
    @NonNull
    DatabaseReference getCurrentUserFriendsReqRef() {
        return getCurrentUserRefFromMainObject(IFirebaseConfig.FRIEND_REQUEST_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Users object in root ref
     *
     * @return database reference for current user in users object
     */
    @NonNull
    DatabaseReference getCurrentUsersRef() {
        return getCurrentUserRefFromMainObject(IFirebaseConfig.USERS_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Interaction object in root ref
     *
     * @return database reference for current user in interaction object
     */
    @NonNull
    DatabaseReference getCurrentUserInteractionRef() {
        return getCurrentUserRefFromMainObject(IFirebaseConfig.INTERACTIONS_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Message object in root ref
     *
     * @return database reference for current user in Message object
     */
    @NonNull
    DatabaseReference getCurrentUserMessageRef() {
        return getCurrentUserRefFromMainObject(IFirebaseConfig.MESSAGE_OBJECT);
    }

    /**
     * Call this method to get reference of current user from Friends object in root ref
     *
     * @return database reference for current user in Friends object
     */
    @NonNull
    DatabaseReference getCurrentUserFriendsRef() {
        return getCurrentUserRefFromMainObject(IFirebaseConfig.FRIENDS_OBJECT);
    }

    /**
     * Call this method to get reference to Message object on root database
     *
     * @return firebase database reference to Message object
     */
    @NonNull
    DatabaseReference getMessageRef() {
        return getMainObjectRef(IFirebaseConfig.MESSAGE_OBJECT);
    }

    /**
     * Call this method to get reference to Interactions object on root database
     *
     * @return firebase database reference to Interactions object
     */
    @NonNull
    DatabaseReference getInteractionsRef() {
        return getMainObjectRef(IFirebaseConfig.INTERACTIONS_OBJECT);
    }

    /**
     * Call this method to get reference to Friend Requests object on root database
     *
     * @return firebase database reference to Friend Requests object
     */
    @NonNull
    DatabaseReference getFriendReqRef() {
        return getMainObjectRef(IFirebaseConfig.FRIEND_REQUEST_OBJECT);
    }

    /**
     * Call this method to get reference to Friends object on root database
     *
     * @return firebase database reference to Friends object
     */
    @NonNull
    DatabaseReference getFriendsRef() {
        return getMainObjectRef(IFirebaseConfig.FRIENDS_OBJECT);
    }

    /**
     * Call this method to get storage reference to user's avatar with file name of
     * {userId.jpg}
     *
     * @return firebase storage reference to store user's avatar
     */
    @NonNull
    StorageReference getAvatarStorageRef() {
        return getStorageRootRef()
                .child(IFirebaseConfig.AVATAR_STORAGE_DIR)
                .child(getCurrentUserId() + ".jpg");
    }

    /**
     * Call this method to get storage reference to user's avatar thumbnail with
     * file name of {userId.jpg}
     *
     * @return firebase storage reference to store user's avatar thumbnail
     */
    @NonNull
    StorageReference getAvatarThumbnailStorageRef() {
        return getStorageRootRef()
                .child(IFirebaseConfig.AVATAR_STORAGE_DIR)
                .child(IFirebaseConfig.THUMBNAIL_STORAGE_DIR)
                .child(getCurrentUserId() + ".jpg");
    }

    /**
     * Call this method to sign out current user
     * Update user's status offline and last_seen to firebase's timestamp before signing out
     */
    void signOut() {
        getCurrentUserRefFromMainObject(IFirebaseConfig.USERS_OBJECT)
                .child(IFirebaseConfig.ONLINE).setValue(false);
        getCurrentUserRefFromMainObject(IFirebaseConfig.USERS_OBJECT)
                .child(IFirebaseConfig.LAST_SEEN).setValue(ServerValue.TIMESTAMP);
        getFirebaseAuth().signOut();
    }

    /**
     * Call this method to mark user online in Firebase Database's users object
     */
    void markUserOnline(@NonNull final OnTaskCompletion callback) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            callback.onError("User Unavailable");
            return;
        }

        // Update current user's database with online value to true
        getCurrentUsersRef().child(IFirebaseConfig.ONLINE).setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) callback.onSuccess();
                        else callback.onError("Error updating user online status");
                    }
                });
    }

    /**
     * Call this method to mark user offline in Firebase Database'e users object
     */
    void markUserOffline(@NonNull final OnTaskCompletion callback) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            callback.onError("User Unavailable");
            return;
        }

        // Update current user's database with online value to false
        getCurrentUsersRef().child(IFirebaseConfig.ONLINE).setValue(false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) callback.onSuccess();
                        else callback.onError("Error updating user online status");
                    }
                });

        // Update current user's database with last_seen value to Firebase server's timestamp
        getCurrentUsersRef().child(IFirebaseConfig.LAST_SEEN).setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) callback.onSuccess();
                        else callback.onError("Error updating user online status");
                    }
                });
    }

    /**
     * Call this method to check whether current user is available or not
     *
     * @param callback Success/Failure callbacks
     */
    void checkCurrentUserAvailability(@NonNull final OnTaskCompletion callback) {
        if (getFirebaseAuth().getCurrentUser() != null) {
            callback.onSuccess();
        } else {
            callback.onError("User Unavailable");
        }
    }

    /**
     * Call this method to get information about any User from users object
     * Must use setFirebaseUsersObjectCallbacks() along side with the method in order to get the
     * results
     *
     * @param userId   ID of user, whose information we are interested in
     * @param callback Users info callback for detail user info and error
     */
    void getUsersObject(@NonNull final String userId,
                        @NonNull final OnUsersData callback) {
        // Database reference for particular reference
        DatabaseReference reference = getMainObjectRef(IFirebaseConfig.USERS_OBJECT).child(userId);

        // Enable offline functionality
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Setup callbacks
                callback.onData(extractValues(dataSnapshot), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Call this method to get list of last 50 registered user in app
     *
     * @param callback Callback for list of user and error
     */
    void getAllRegisteredUsers(@NonNull final OnUsersList callback) {
        Query query = getUsersRef().limitToLast(50);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AllUsers user = data.getValue(AllUsers.class);
                    if (user != null) {
                        user.setId(data.getKey());
                        if (!mAllUsersList.contains(user)) mAllUsersList.add(user);
                        else {
                            int index = mAllUsersList.indexOf(user);
                            mAllUsersList.set(index, user);
                        }
                    }
                }
                callback.onData(mAllUsersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Call this method to extract data from DataSnapshot
     *
     * @param dataSnapshot DataSnapshot object from firebase database containing detailed
     *                     information about user
     * @return {@link AllUsers} object
     */
    private AllUsers extractValues(@NonNull DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(AllUsers.class);
    }
}
