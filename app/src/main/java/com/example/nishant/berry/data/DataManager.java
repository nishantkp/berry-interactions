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
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.databinding.AllUsersListItemBinding;
import com.example.nishant.berry.ui.adapter.AllUsersViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Manager class, that deals with business logic
 */
public class DataManager
        implements FirebaseUtils.UsersObjectCallback, SettingsUtils.AvatarStorageCallback,
        FriendsUtils.FriendsListCallback, RequestsUtils.FriendRequestCallback,
        ChatUtils.ChatCallback {
    private static FirebaseUtils sFirebaseUtils;
    private UserObjectCallback mUserObjectCallback;
    private AvatarStorageCallback mAvatarStoreCallback;
    private FriendsListCallback mFriendsListCallback;
    private FriendReqCallback mFriendReqCallback;
    private ChatListCallback mChatListCallback;

    // Lazy Initialization pattern
    private static class StaticHolder {
        static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        sFirebaseUtils = FirebaseUtils.getInstance();
        return StaticHolder.INSTANCE;
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
     * Set user object callback
     *
     * @param callbacks UserObjectCallback must be initialized with the class which implements
     *                  callback
     */
    public void setUserObjectCallbacks(UserObjectCallback callbacks) {
        mUserObjectCallback = callbacks;
    }

    /**
     * Set avatar store callback
     *
     * @param callbacks AvatarStoreCallback must be initialized with the class which implements
     *                  callback
     */
    public void setAvatarStoreCallbacks(AvatarStorageCallback callbacks) {
        mAvatarStoreCallback = callbacks;
    }

    /**
     * Set friends list callback
     *
     * @param callbacks FriendsListCallback must be initialized with the class which implements
     *                  callback
     */
    public void setFriendsListCallbacks(FriendsListCallback callbacks) {
        mFriendsListCallback = callbacks;
    }

    /**
     * Set friend request callback
     *
     * @param callbacks FriendRequestCallback must be initialized with the class which implements
     *                  callback
     */
    public void setFriendRequestCallbacks(FriendReqCallback callbacks) {
        mFriendReqCallback = callbacks;
    }

    /**
     * Set current user's chat list callback
     *
     * @param callbacks ChatListCallback must be initialized with the class which implements
     *                  callback
     */
    public void setChatListCallbacks(ChatListCallback callbacks) {
        mChatListCallback = callbacks;
    }

    /**
     * Call this method to login user to it's account
     *
     * @param email    email of user
     * @param password password provided by user
     * @param callback callback for success and errors
     */
    public void loginUser(@NonNull String email,
                          @NonNull String password,
                          @NonNull final DataCallback.OnTaskCompletion callback) {
        FirebaseUtils.getInstance().getFirebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the device token
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            String userId = getCurrentUserId();

                            Map<String, Object> singInMap = new HashMap<>();
                            singInMap.put(IFirebaseConfig.DEVICE_TOKEN_ID, deviceToken);
                            singInMap.put(IFirebaseConfig.ONLINE, true);

                            // Store token Id to users database
                            getUsersRef().child(userId).updateChildren(singInMap,
                                    new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError,
                                                               @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                callback.onError("Sign in error!");
                                            } else {
                                                callback.onSuccess();
                                            }
                                        }
                                    });
                        } else {
                            callback.onError("Sign in error!");
                        }
                    }
                });
    }

    /**
     * Call this method to register user in firebase
     *
     * @param displayName display name of user
     * @param email       email
     * @param password    password
     * @param callback    callbacks for success and failure
     */
    public void signUpUser(@NonNull final String displayName,
                           @NonNull String email,
                           @NonNull String password,
                           @NonNull final DataCallback.OnTaskCompletion callback) {
        // Register user with email, password and cancel progress dialog
        sFirebaseUtils.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            //Store data
                            storeDataToFirebaseDatabase(displayName, callback);
                        } else {
                            callback.onError("Error creating account!");
                        }
                    }
                });
    }

    /**
     * Store user data to database and sets callbacks for success and error
     *
     * @param displayName display name
     * @param callback    callbacks for success and failure
     */
    private void storeDataToFirebaseDatabase(@NonNull String displayName,
                                             @NonNull final DataCallback.OnTaskCompletion callback) {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();

        // Value Map for Firebase database
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(IFirebaseConfig.NAME, displayName);
        userMap.put(IFirebaseConfig.STATUS, IFirebaseConfig.DEFAULT_STATUS);
        userMap.put(IFirebaseConfig.IMAGE, IFirebaseConfig.DEFAULT_VALUE);
        userMap.put(IFirebaseConfig.THUMBNAIL, IFirebaseConfig.DEFAULT_VALUE);
        userMap.put(IFirebaseConfig.DEVICE_TOKEN_ID, deviceToken);
        userMap.put(IFirebaseConfig.ONLINE, true);

        // Set the values to Firebase database
        DataManager.getCurrentUsersRef().setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("Error creating account!");
                        }
                    }
                });
    }

    /**
     * Call this method for saving status to firebase database
     *
     * @param status   User's status
     * @param callback callbacks for success and failure
     */
    public void saveUserStatus(@NonNull String status,
                               @NonNull final DataCallback.OnTaskCompletion callback) {
        getCurrentUsersRef().child(IFirebaseConfig.STATUS).setValue(status)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("Error while saving changes!");
                        }
                    }
                });
    }

    /**
     * Call this method to get all the users from database
     * This method sets callback for list item click and setting up firebase adapter
     *
     * @param query    Firebase query
     * @param callback callback for list item click and firebase recycler adapter
     *                 FirebaseRecyclerAdapter which we can use in Activity/Fragment for
     *                 setting up on RecyclerView
     *                 Also we have to call startListening() and stopListening() method form
     *                 Activity/Fragment to actually get the data
     */
    public void getAllUsersFromDatabase(Query query,
                                        @NonNull final DataCallback.OnAllUsersList callback) {
        FirebaseRecyclerOptions<AllUsers> options =
                new FirebaseRecyclerOptions.Builder<AllUsers>().setQuery(query, AllUsers.class)
                        .build();

        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> adapter
                = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllUsersViewHolder holder,
                                            int position,
                                            @NonNull final AllUsers model) {
                holder.bind(model);

                // Set onclick listener on ViewHolder
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // set call back with user id parameter
                        callback.onListItemClick(getRef(holder.getAdapterPosition()).getKey());
                    }
                });
            }

            @NonNull
            @Override
            public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // get the View
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_list_item, parent, false);
                return new AllUsersViewHolder(AllUsersListItemBinding.bind(view.getRootView()));
            }
        };
        callback.onFirebaseAdapter(adapter);
    }

    /**
     * Call this method to get detail information about current user like name, status, avatar etc..
     * User must implement UserObjectCallback before using this particular method to get the results
     */
    public void getCurrentUserInfo() {
        // Safety to avoid NullPointerException
        if (mUserObjectCallback == null) return;

        FirebaseUtils utils = new FirebaseUtils();
        utils.setFirebaseUsersObjectCallbacks(this);
        utils.getUsersObject(getCurrentUserId(), null);
    }

    /**
     * Call this method to store user avatar and thumbnail to firebase storage
     *
     * @param avatarUri     uri of user avatar
     * @param thumbnailByte user avatar thumbnail in form of byte array
     */
    public void storeAvatar(Uri avatarUri, final byte[] thumbnailByte) {
        // Safety to avoid NullPointerException
        if (mAvatarStoreCallback == null) return;

        SettingsUtils utils = new SettingsUtils();
        utils.setAvatarStorageCallback(this);
        utils.storeAvatarToFirebaseDatabase(avatarUri, thumbnailByte);
    }

    /**
     * Call this method to get current user's friends
     */
    public void getCurrentUserFriendList() {
        // Safety to avoid NullPointerException
        if (mFriendsListCallback == null) return;

        FriendsUtils friendsUtils = new FriendsUtils();
        friendsUtils.setFriendsListCallback(this);
        friendsUtils.getCurrentUserFriends();
    }

    /**
     * Call this method to get current user's friend request
     * received or sent
     */
    public void getCurrentUsersFriendReq() {
        // Safety to avoid NullPointerException
        if (mFriendReqCallback == null) return;

        RequestsUtils requestsUtils = new RequestsUtils();
        requestsUtils.setFriendRequestCallbacks(this);
        requestsUtils.getCurrentUsersFriendRequests();
    }

    public void getChatList() {
        // Safety to avoid NullPointerException
        if (mChatListCallback == null) return;

        ChatUtils chatUtils = new ChatUtils();
        chatUtils.setChatCallbacks(this);
        chatUtils.getChatList();
    }

    /**
     * Implement this {@link FirebaseUtils} callback method for detailed user object
     *
     * @param model  AllUsers object
     * @param userID UserId of a user
     */
    @Override
    public void onFirebaseUsersObject(AllUsers model, String userID, AllUsersViewHolder holder) {
        mUserObjectCallback.onData(model);
    }

    /**
     * Implement this {@link FirebaseUtils} callback method for handling error when retrieving
     * user details from firebase database
     *
     * @param error error message
     */
    @Override
    public void onFirebaseUsersObjectError(String error) {
        mUserObjectCallback.onError(error);
    }

    /**
     * Implement this {@link SettingsUtils} callback method for what to do when we successfully
     * store user avatar and thumbnail to firebase storage
     */
    @Override
    public void onAvatarStoreSuccess() {
        mAvatarStoreCallback.onAvatarStoreSuccess();
    }

    /**
     * Implement this {@link SettingsUtils} callback method for handling error when storing
     * user avatar and thumbnail to firebase storage
     *
     * @param error error message
     */
    @Override
    public void onAvatarStoreError(String error) {
        mAvatarStoreCallback.onAvatarStoreError(error);
    }

    /**
     * Implement this {@link FriendsUtils} callback method for handling error when retrieving detail
     * information about user's friend
     *
     * @param error error message
     */
    @Override
    public void onFriendsListError(String error) {
        mFriendsListCallback.onError(error);
    }

    /**
     * Implement this {@link FriendsUtils} callback method to handle onClick event for user's friend
     * being displayed on RecyclerView
     *
     * @param userId      userId of user on which click action is performed
     * @param displayName name of user
     */
    @Override
    public void onFriendsListItemClick(String userId, String displayName) {
        mFriendsListCallback.onListItemClick(userId, displayName);
    }

    /**
     * Implement this {@link FriendsUtils} callback method for FirebaseRecyclerAdapter for current
     * user's friend
     * Use this FirebaseAdapter in activity's onStart() and onPause() method to
     * startListening()/stopListening() in order to fetch data from firebase
     *
     * @param adapter FirebaseRecyclerAdapter
     */
    @Override
    public void onFriendsFirebaseRecyclerAdapter(FirebaseRecyclerAdapter adapter) {
        mFriendsListCallback.onFriendsAdapter(adapter);
    }

    /**
     * Implement this {@link RequestsUtils} callback method for handling error while getting
     * user's detail information from User's object
     *
     * @param error error message
     */
    @Override
    public void onFriendReqError(String error) {
        mFriendReqCallback.onError(error);
    }

    /**
     * Implement this {@link RequestsUtils} callback method for current user's friend requests
     * Use this FirebaseAdapter in activity's onStart() and onPause() method to
     * startListening()/stopListening() in order to fetch data from firebase
     *
     * @param adapter FirebaseRecyclerAdapter
     */
    @Override
    public void onFriendReqFirebaseAdapter(FirebaseRecyclerAdapter adapter) {
        mFriendReqCallback.onReqAdapter(adapter);
    }

    /**
     * Implement this {@link ChatUtils} callback method for current user's chat list
     * Use this FirebaseAdapter in activity's onStart() and onPause() method to
     * startListening()/stopListening() in order to fetch data from firebase
     *
     * @param adapter FirebaseRecyclerAdapter
     */
    @Override
    public void onChatAdapter(FirebaseRecyclerAdapter adapter) {
        mChatListCallback.onChatAdapter(adapter);
    }

    /**
     * Implement this {@link ChatUtils} callback method for list item click
     *
     * @param userId Id of user on which click is performed
     * @param name   name of user
     */
    @Override
    public void onChatListItemClick(String userId, String name) {
        mChatListCallback.onListItemClick(userId, name);
    }

    /**
     * UserObject callbacks when DataManager is inquired about detail information
     * on particular user
     */
    public interface UserObjectCallback {
        void onData(AllUsers model);

        void onError(String error);
    }

    /**
     * User Avatar storage callback for success and failure
     */
    public interface AvatarStorageCallback {
        void onAvatarStoreSuccess();

        void onAvatarStoreError(String error);
    }

    /**
     * Current user's friends list callback for error, listItemClick and adapter
     */
    public interface FriendsListCallback {
        void onError(String error);

        void onFriendsAdapter(FirebaseRecyclerAdapter adapter);

        void onListItemClick(String userId, String displayName);
    }

    /**
     * Current user's friend request callback for error and firebase adapter
     */
    public interface FriendReqCallback {
        void onError(String error);

        void onReqAdapter(FirebaseRecyclerAdapter adapter);
    }

    /**
     * Current user's chat callback for firebase adapter and list item click
     * Implement this callback to get the list of chats
     */
    public interface ChatListCallback {
        void onChatAdapter(FirebaseRecyclerAdapter adapter);

        void onListItemClick(String userId, String displayName);
    }
}