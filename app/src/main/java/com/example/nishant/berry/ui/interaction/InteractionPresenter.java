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

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.ui.utils.GetTimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InteractionPresenter
        extends BasePresenter<InteractionContract.View>
        implements InteractionContract.Presenter {

    private String mUserId;
    private String mDisplayName;
    private DatabaseReference mUsersRootRef;
    private String mLastSeen;
    private String mAvatarThumbUrl;
    private String mOnlineStatus;

    InteractionPresenter(Intent receivedIntent) {
        // Extract the userId and user displayName from intent
        mUserId = receivedIntent.hasExtra(IConstants.KEY_USER_ID) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_ID) : null;
        mDisplayName = receivedIntent.hasExtra(IConstants.KEY_USER_DISPLAY_NAME) ?
                receivedIntent.getStringExtra(IConstants.KEY_USER_DISPLAY_NAME) : null;
        if (mUserId == null) return;

        // Root reference to Users database
        mUsersRootRef = FirebaseDatabase.getInstance().getReference().child(IFirebaseConfig.USERS_OBJECT);
        extractBasicInfoDatabase();
    }

    @Override
    public InteractionContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(InteractionContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to extract the basic information about user from database
     * like display name, thumb avatar url, user online status etc
     */
    @Override
    public void extractBasicInfoDatabase() {
        mUsersRootRef.child(mUserId).addValueEventListener(new ValueEventListener() {
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
                // Set call back for setting up action bar
                getView().setActionBar(mDisplayName, mAvatarThumbUrl, mOnlineStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getView().setActionBar("Berry", "default", "Hey It's Berry");
            }
        });
    }
}
