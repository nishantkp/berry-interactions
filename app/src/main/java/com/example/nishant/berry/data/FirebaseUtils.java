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

import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

class FirebaseUtils {
    private static FirebaseUtils sFirebaseUtils;

    // Singleton
    static FirebaseUtils getInstance() {
        if (sFirebaseUtils == null) {
            sFirebaseUtils = new FirebaseUtils();
        }
        return sFirebaseUtils;
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
    String getCurrentUserId() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    /**
     * Call this method to get root firebase database reference
     *
     * @return root database reference
     */
    DatabaseReference getRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Call this method to get root firebase storage reference
     *
     * @return root storage reference
     */
    StorageReference getStorageRootRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    /**
     * Call this method to get reference to objects in root database
     *
     * @param object object in root database
     * @return database reference
     */
    DatabaseReference getMainObjectRef(String object) {
        return getRootRef().child(object);
    }

    /**
     * Call this method to get reference to particular user in particular object in root ref
     *
     * @param userId id of a user
     * @param object object in root database
     * @return database reference
     */
    DatabaseReference getUserObjectRef(String userId, String object) {
        return getRootRef().child(object).child(userId);
    }

    /**
     * Call this method to get reference of current user from main objects in root ref
     *
     * @param object Main object in root ref
     * @return database reference for current user in users object
     */
    DatabaseReference getCurrentUserRefFromMainObject(String object) {
        return getMainObjectRef(object).child(getCurrentUserId());
    }

    /**
     * Call this method to get storage reference to user's avatar with file name of
     * {userId.jpg}
     *
     * @return firebase storage reference to store user's avatar
     */
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
    StorageReference getAvatarThumbnailStorageRef() {
        return getStorageRootRef()
                .child(IFirebaseConfig.AVATAR_STORAGE_DIR)
                .child(IFirebaseConfig.THUMBNAIL_STORAGE_DIR)
                .child(getCurrentUserId() + ".jpg");
    }

    /**
     * Call this method to sign out current user
     */
    void signOut() {
        getFirebaseAuth().signOut();
    }

    /**
     * Call this method to check whether current user is available or not
     *
     * @return true or false depending upon current user availability
     */
    boolean isCurrentUserAvailable() {
        return getFirebaseAuth().getCurrentUser() != null;
    }
}
