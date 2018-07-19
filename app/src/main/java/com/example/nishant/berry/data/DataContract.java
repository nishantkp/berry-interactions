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
 * File Created on 03/07/18 10:52 PM by nishant
 * Last Modified on 03/07/18 10:52 PM
 */

package com.example.nishant.berry.data;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.nishant.berry.data.callbacks.OnInteraction;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUserProfile;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;

/**
 * DataManager contract class
 */
interface DataContract {
    void loginUser(@NonNull String email,
                   @NonNull String password,
                   @NonNull OnTaskCompletion callback);

    void signUpUser(@NonNull String displayName,
                    @NonNull String email,
                    @NonNull String password,
                    @NonNull OnTaskCompletion callback);

    void saveUserStatus(@NonNull String status, @NonNull OnTaskCompletion callback);

    void getAllRegisteredUsers(@NonNull OnUsersList callback);

    void getCurrentUserInfo(@NonNull OnUsersData callback);

    void storeAvatar(Uri avatarUri,
                     byte[] thumbnailByte,
                     @NonNull OnTaskCompletion callback);

    void getChatList(@NonNull OnUsersList callback);

    void fetchFriends(@NonNull OnUsersList callback);

    void findUser(@NonNull String searchString, int limit, @NonNull OnUsersList callback);

    void getFriendRequests(@NonNull OnUsersList callback);

    void acceptFriendRequest(@NonNull String userId, @NonNull OnTaskCompletion callback);

    void ignoreFriendRequest(@NonNull String userId, @NonNull OnTaskCompletion callback);

    void getUserProfile(String userId, @NonNull final OnUserProfile callback);

    void unfriendUser(String userId, @NonNull final OnTaskCompletion callback);

    void sendFriendRequest(String userId, @NonNull final OnTaskCompletion callback);

    void getMessageList(@NonNull final String interactionUserId, @NonNull final OnInteraction callback);

    void onInteraction(@NonNull final String interactionUserId,
                       @NonNull final String message,
                       @NonNull final OnTaskCompletion callback);

    void loadMoreMessages(@NonNull final String interactionUserId, @NonNull final OnInteraction callback);

    void getUserInfoFromId(String userId, @NonNull final OnUsersData callback);
}
