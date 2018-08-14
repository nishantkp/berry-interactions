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

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.nishant.berry.data.callbacks.OnInteraction;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUserProfile;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.model.AllUsers;

/**
 * Data Manager class, that deals with business logic
 */
public class DataManager implements DataContract {
    private final FbUsersUseCase mFbUsersUseCase;
    private final FriendsUseCase mFriendsUseCase;
    private final RequestsUseCase mRequestsUseCase;
    private final SettingsUseCase mSettingsUseCase;
    private final ChatUseCase mChatUseCase;
    private final AccountUseCase mAccountUseCase;
    private final SearchUseCase mSearchUseCase;
    private final ProfileUseCase mProfileUseCase;
    private final InteractionUseCase mInteractionUseCase;

    // Lazy Initialization pattern
    private static class StaticHolder {
        static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return StaticHolder.INSTANCE;
    }

    /**
     * Private constructor so no one can make object of a data manager
     */
    public DataManager() {
        mFbUsersUseCase = new FbUsersUseCase();
        mFriendsUseCase = new FriendsUseCase(mFbUsersUseCase);
        mRequestsUseCase = new RequestsUseCase(mFbUsersUseCase);
        mSettingsUseCase = new SettingsUseCase(mFbUsersUseCase);
        mChatUseCase = new ChatUseCase(mFbUsersUseCase);
        mAccountUseCase = new AccountUseCase(mFbUsersUseCase);
        mSearchUseCase = new SearchUseCase(mFbUsersUseCase);
        mProfileUseCase = new ProfileUseCase(mFbUsersUseCase);
        mInteractionUseCase = new InteractionUseCase(mFbUsersUseCase);
    }

    /**
     * Call this method to mark user online (i.e if the user is currently on app screen)
     */
    @Override
    public void markUserOnline(@NonNull final OnTaskCompletion callback) {
        mFbUsersUseCase.markUserOnline(callback);
    }

    /**
     * Call this method to mark user offline (i.e if the user is away from app screen)
     */
    @Override
    public void markUserOffline(@NonNull final OnTaskCompletion callback) {
        mFbUsersUseCase.markUserOffline(callback);
    }

    /**
     * Use this method to sign out current user
     */
    @Override
    public void signOutUser() {
        mFbUsersUseCase.signOut();
    }

    /**
     * Call this method to check whether current user is available or not
     *
     * @param callback callbacks for success and failure
     */
    @Override
    public void checkCurrentUserAvailability(@NonNull OnTaskCompletion callback) {
        mFbUsersUseCase.checkCurrentUserAvailability(callback);
    }

    /**
     * Call this method to login user to it's account
     *
     * @param email    email of user
     * @param password password provided by user
     * @param callback callback for success and errors
     */
    @Override
    public void loginUser(@NonNull String email,
                          @NonNull String password,
                          @NonNull OnTaskCompletion callback) {
        mAccountUseCase.signInUser(email, password, callback);
    }

    /**
     * Call this method to register user in firebase
     *
     * @param displayName display name of user
     * @param email       email
     * @param password    password
     * @param callback    callbacks for success and failure
     */
    @Override
    public void signUpUser(@NonNull String displayName,
                           @NonNull String email,
                           @NonNull String password,
                           @NonNull OnTaskCompletion callback) {
        mAccountUseCase.signUpUser(displayName, email, password, callback);
    }

    /**
     * Call this method for saving status to firebase database
     *
     * @param status   User's status
     * @param callback callbacks for success and failure
     */
    @Override
    public void saveUserStatus(@NonNull String status,
                               @NonNull OnTaskCompletion callback) {
        mAccountUseCase.saveUserStatus(status, callback);
    }

    /**
     * Call this method to get all the list of all registered users
     *
     * @param callback DataCallback for list of users and error
     */
    @Override
    public void getAllRegisteredUsers(@NonNull OnUsersList callback) {
        mFbUsersUseCase.getAllRegisteredUsers(callback);
    }

    /**
     * Call this method to get detail information about current user like name, status, avatar etc..
     * User must implement UserObjectCallback before using this particular method to get the
     *
     * @param callback Callback for detail user info, user id and error
     */
    @Override
    public void getCurrentUserInfo(@NonNull final OnUsersData callback) {
        mFbUsersUseCase.getUsersObject(mFbUsersUseCase.getCurrentUserId(), callback);
    }

    /**
     * Call this method to store user avatar and thumbnail to firebase storage
     *
     * @param avatarUri     uri of user avatar
     * @param thumbnailByte user avatar thumbnail in form of byte array
     */
    @Override
    public void storeAvatar(Uri avatarUri,
                            final byte[] thumbnailByte,
                            final @NonNull OnTaskCompletion callback) {
        mSettingsUseCase.storeAvatarToFirebaseDatabase(avatarUri, thumbnailByte, callback);
    }

    /**
     * Call this method to get the list of users with whom current user has interactions with
     * This method with give List<AllUsers> in callback
     * {@link AllUsers} object consists of user name, last message sent/received, thumbnail avatar url
     *
     * @param callback DataCallback for list of interactions and error
     */
    @Override
    public void getChatList(@NonNull final OnUsersList callback) {
        mChatUseCase.getUsersInteraction(callback);
    }

    /**
     * Call this method to get the list of current user's friends
     *
     * @param callback DataCallback for list of friends and error
     */
    @Override
    public void fetchFriends(@NonNull final OnUsersList callback) {
        mFriendsUseCase.getAllFriends(callback);
    }

    /**
     * Call this method to find the user from user's object in firebase database
     *
     * @param searchString Input text by user
     * @param limit        number of search item you want to see in result
     * @param callback     DataCallback for list of users
     */
    @Override
    public void findUser(@NonNull String searchString, int limit, @NonNull OnUsersList callback) {
        mSearchUseCase.findUser(searchString, limit, callback);
    }

    /**
     * Call this method to get all the friend requests send or received by user in List<AllUsers>
     * format
     *
     * @param callback DataCallback for list of users and error dealing with firebase database
     */
    @Override
    public void getFriendRequests(@NonNull OnUsersList callback) {
        mRequestsUseCase.getCurrentUsersFriendReq(callback);
    }

    /**
     * Call this method to accept the friend request
     *
     * @param userId   id of user whose friend request we are accepting
     * @param callback DataCallback for task completion
     */
    @Override
    public void acceptFriendRequest(@NonNull String userId, @NonNull OnTaskCompletion callback) {
        mRequestsUseCase.acceptFriendRequest(userId, callback);
    }

    /**
     * Call this method to cancel/decline friend request
     *
     * @param userId   id of user
     * @param callback DataCallback for task completion
     */
    @Override
    public void ignoreFriendRequest(@NonNull String userId, @NonNull OnTaskCompletion callback) {
        mRequestsUseCase.ignoreFriendRequest(userId, callback);
    }

    /**
     * Call this method to unfriend a user
     *
     * @param userId   Id of user which current user wants to unfriend
     * @param callback DataCallback for task completion
     */
    @Override
    public void unfriendUser(String userId, @NonNull OnTaskCompletion callback) {
        mRequestsUseCase.unfriendUser(userId, callback);
    }

    /**
     * Call this method to send friend request to particular user
     *
     * @param userId   Id of user to whom current user wants to send friend request
     * @param callback DataCallback fot task completion
     */
    @Override
    public void sendFriendRequest(String userId, @NonNull OnTaskCompletion callback) {
        mRequestsUseCase.sendFriendRequest(userId, callback);
    }

    /**
     * Call this method to get the user's profile
     *
     * @param userId   Id of user in which profile we are interested in
     * @param callback DataCallbacks for friend request sent/ received/ already friend and user's
     *                 info
     */
    @Override
    public void getUserProfile(String userId, @NonNull OnUserProfile callback) {
        mProfileUseCase.getUserProfile(userId, callback);
    }


    /**
     * Call this method to get the list of messages send/received by current user and
     * Id specified in method signature
     *
     * @param interactionUserId Id of user with whom current user is chatting
     * @param callback          DataCallback for list of messages and error
     */
    @Override
    public void getMessageList(@NonNull String interactionUserId, @NonNull OnInteraction callback) {
        mInteractionUseCase.getMessageList(interactionUserId, callback);
    }

    /**
     * Call this method to update message database
     *
     * @param interactionUserId Id of user with whom current user is chatting
     * @param message           Message which current user wants to send
     * @param callback          DataCallback for success/ failure
     */
    @Override
    public void onInteraction(@NonNull String interactionUserId,
                              @NonNull String message,
                              @NonNull OnTaskCompletion callback) {
        mInteractionUseCase.onInteraction(interactionUserId, message, callback);
    }

    /**
     * Call this method to load more messages i.e second page
     * When user scrolls the screen or swipe the screen to refresh
     *
     * @param interactionUserId Id of a user with whom current user if chatting
     * @param callback          DataCallback for message list, offset for ListView/ RecyclerView
     *                          and error
     */
    @Override
    public void loadMoreMessages(@NonNull String interactionUserId, @NonNull OnInteraction callback) {
        mInteractionUseCase.loadMoreMessages(interactionUserId, callback);
    }

    /**
     * Call this method to get the basic information about user just by passing Id
     *
     * @param userId   Id of a user whose information we are interested in
     * @param callback DataCallback for user info and error
     */
    @Override
    public void getUserInfoFromId(String userId, @NonNull OnUsersData callback) {
        mFbUsersUseCase.getUsersObject(userId, callback);
    }
}