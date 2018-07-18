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

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.callbacks.OnInteraction;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUserProfile;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.model.AllUsers;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;

/**
 * Data Manager class, that deals with business logic
 */
public class DataManager implements DataContract {
    private static FirebaseUtils sFirebaseUtils;
    private static FriendsUtils sFriendsUtils;
    private static RequestsUtils sRequestsUtils;
    private static SettingsUtils sSettingsUtils;
    private static ChatUtils sChatUtils;
    private static AccountUtils sAccountUtils;
    private static SearchUtils sSearchUtils;
    private static ProfileUtils sProfileUtils;
    private static InteractionUtils sInteractionUtils;

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
    private DataManager() {
        sFirebaseUtils = new FirebaseUtils();
        sFriendsUtils = new FriendsUtils();
        sRequestsUtils = new RequestsUtils();
        sSettingsUtils = new SettingsUtils();
        sChatUtils = new ChatUtils();
        sAccountUtils = new AccountUtils();
        sSearchUtils = new SearchUtils();
        sProfileUtils = new ProfileUtils();
        sInteractionUtils = new InteractionUtils();
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
    public static DatabaseReference getCurrentUsersRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.USERS_OBJECT);
    }

    /**
     * Call this method to get reference to particular user in Users object in root ref
     *
     * @param userId user Id
     * @return database reference for a user whose userId is passed in
     */
    public static DatabaseReference getNewUserRef(String userId) {
        return sFirebaseUtils.getUserObjectRef(userId, IFirebaseConfig.USERS_OBJECT);
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
     * Call this method to get reference of current user from Friend requests object in root ref
     *
     * @return database reference for current user in Friend requests object
     */
    public static DatabaseReference getCurrentUserFriendsReqRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.FRIEND_REQUEST_OBJECT);
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
     * Call this method to get reference of current user from Friends object in root ref
     *
     * @return database reference for current user in Friends object
     */
    public static DatabaseReference getCurrentUserFriendsRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.FRIENDS_OBJECT);
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
     * Call this method to get reference of current user from Interaction object in root ref
     *
     * @return database reference for current user in interaction object
     */
    public static DatabaseReference getCurrentUserInteractionRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.INTERACTIONS_OBJECT);
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

    /**
     * Call this method to get reference of current user from Message object in root ref
     *
     * @return database reference for current user in Message object
     */
    public static DatabaseReference getCurrentUserMessageRef() {
        return sFirebaseUtils.getCurrentUserRefFromMainObject(IFirebaseConfig.MESSAGE_OBJECT);
    }

    /**
     * Call this method to get firebase storage reference to store user's avatar
     *
     * @return user's avatar storage reference
     */
    public static StorageReference getAvatarStorageRef() {
        return sFirebaseUtils.getAvatarStorageRef();
    }

    /**
     * Call this method to get firebase storage reference to store user's avatar thumbnail
     *
     * @return user's avatar thumbnail storage reference
     */
    public static StorageReference getAvatarThumbStorageRef() {
        return sFirebaseUtils.getAvatarThumbnailStorageRef();
    }

    /**
     * Use this method to sign out current user and
     * Update user's status offline and last_seen to firebase's timestamp before signing out
     */
    public static void signOutUser() {
        getCurrentUsersRef().child(IFirebaseConfig.ONLINE).setValue(false);
        getCurrentUsersRef().child(IFirebaseConfig.LAST_SEEN).setValue(ServerValue.TIMESTAMP);
        sFirebaseUtils.signOut();
    }

    /**
     * Call this method to check whether current user is available or not
     *
     * @return true if the current user is available/ false if it's not resent
     */
    public static boolean isCurrentUserAvailable() {
        return sFirebaseUtils.isCurrentUserAvailable();
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
        sAccountUtils.signInUser(email, password, callback);
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
        sAccountUtils.signUpUser(displayName, email, password, callback);
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
        sAccountUtils.saveUserStatus(status, callback);
    }

    /**
     * Call this method to get all the list of all registered users
     *
     * @param callback DataCallback for list of users and error
     */
    @Override
    public void getAllRegisteredUsers(@NonNull OnUsersList callback) {
        sFirebaseUtils.getAllRegisteredUsers(callback);
    }

    /**
     * Call this method to get detail information about current user like name, status, avatar etc..
     * User must implement UserObjectCallback before using this particular method to get the
     *
     * @param callback Callback for detail user info, user id and error
     */
    @Override
    public void getCurrentUserInfo(@NonNull final OnUsersData callback) {
        sFirebaseUtils.getUsersObject(getCurrentUserId(), callback);
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
        sSettingsUtils.storeAvatarToFirebaseDatabase(avatarUri, thumbnailByte, callback);
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
        sChatUtils.getUsersInteraction(callback);
    }

    /**
     * Call this method to get the list of current user's friends
     *
     * @param callback DataCallback for list of friends and error
     */
    @Override
    public void fetchFriends(@NonNull final OnUsersList callback) {
        sFriendsUtils.getAllFriends(callback);
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
        sSearchUtils.findUser(searchString, limit, callback);
    }

    /**
     * Call this method to get all the friend requests send or received by user in List<AllUsers>
     * format
     *
     * @param callback DataCallback for list of users and error dealing with firebase database
     */
    @Override
    public void getFriendRequests(@NonNull OnUsersList callback) {
        sRequestsUtils.getCurrentUsersFriendReq(callback);
    }

    /**
     * Call this method to accept the friend request
     *
     * @param userId   id of user whose friend request we are accepting
     * @param callback DataCallback for task completion
     */
    @Override
    public void acceptFriendRequest(@NonNull String userId, @NonNull OnTaskCompletion callback) {
        sRequestsUtils.acceptFriendRequest(userId, callback);
    }

    /**
     * Call this method to cancel/decline friend request
     *
     * @param userId   id of user
     * @param callback DataCallback for task completion
     */
    @Override
    public void ignoreFriendRequest(@NonNull String userId, @NonNull OnTaskCompletion callback) {
        sRequestsUtils.ignoreFriendRequest(userId, callback);
    }

    /**
     * Call this method to unfriend a user
     *
     * @param userId   Id of user which current user wants to unfriend
     * @param callback DataCallback for task completion
     */
    @Override
    public void unfriendUser(String userId, @NonNull OnTaskCompletion callback) {
        sRequestsUtils.unfriendUser(userId, callback);
    }

    /**
     * Call this method to send friend request to particular user
     *
     * @param userId   Id of user to whom current user wants to send friend request
     * @param callback DataCallback fot task completion
     */
    @Override
    public void sendFriendRequest(String userId, @NonNull OnTaskCompletion callback) {
        sRequestsUtils.sendFriendRequest(userId, callback);
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
        sProfileUtils.getUserProfile(userId, callback);
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
        sInteractionUtils.getMessageList(interactionUserId, callback);
    }

    /**
     * Call this method to get the info about user with whom current user is chatting
     *
     * @param interactionUserId Id of user with whom current user is chatting
     * @param callback          DataCallback for user info and error
     */
    @Override
    public void getChatUserInfo(@NonNull String interactionUserId, @NonNull OnUsersData callback) {
        sInteractionUtils.getChatUserInfo(interactionUserId, callback);
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
        sInteractionUtils.onInteraction(interactionUserId, message, callback);
    }

    /**
     * Call this method to load more messages i.e second page
     * When user scrolls the screen or swipe the screen to refresh
     *
     * @param interactionUserId If of a user with whom current user if chatting
     * @param callback          DataCallback for message list, offset for ListView/ RecyclerView
     *                          and error
     */
    @Override
    public void loadMoreMessages(@NonNull String interactionUserId, @NonNull OnInteraction callback) {
        sInteractionUtils.loadMoreMessages(interactionUserId, callback);
    }
}