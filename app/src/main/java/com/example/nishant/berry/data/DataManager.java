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

import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.firebase.database.DatabaseReference;

/**
 * Data Manager class, that deals with business logic
 */
public class DataManager {
    private static DataManager sDataManager;
    private static FirebaseUtils sFirebaseUtils;

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
    public static DatabaseReference getCurretUsersRef() {
        return sFirebaseUtils.getCurrentUserRefFromUsersObject();
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
     * Call this method to get reference to Friends object on root database
     *
     * @return firebase database reference to Friends object
     */
    public static DatabaseReference getFriendsRef() {
        return sFirebaseUtils.getMainObjectRef(IFirebaseConfig.FRIENDS_OBJECT);
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
}
