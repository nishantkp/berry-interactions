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
 * File Created on 08/06/18 7:40 PM by nishant
 * Last Modified on 08/06/18 7:40 PM
 */

package com.example.nishant.berry.application;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * Application class
 */
public class BerryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Adds firebase offline functionality
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Initiate Data manager
        DataManager dataManager = DataManager.getInstance();

        // Picasso with OkHttp3 to download user avatar
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso picasso = builder.build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        // Current Users database reference
        final DatabaseReference usersDatabaseReference = dataManager.getCurrentUsersRef();

        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If this query gets disconnected from firebase it will set it's value to false
                // and update the last_seen value to Firebase server's TimeStamp
                usersDatabaseReference.child(IFirebaseConfig.ONLINE).onDisconnect().setValue(false);
                usersDatabaseReference.child(IFirebaseConfig.LAST_SEEN).onDisconnect().setValue(ServerValue.TIMESTAMP);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /* Do nothing */
            }
        });
    }
}
