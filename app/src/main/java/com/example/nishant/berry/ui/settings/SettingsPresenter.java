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
 * File Created on 03/06/18 1:21 AM by nishant
 * Last Modified on 03/06/18 1:21 AM
 */

package com.example.nishant.berry.ui.settings;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.example.nishant.berry.data.DataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class SettingsPresenter
        extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    private DatabaseReference mDatabaseReference;
    private StorageReference mAvatarStorageReference;
    private StorageReference mThumbnailStorageReference;

    SettingsPresenter() {
        // Firebase database reference for current user in Users object
        mDatabaseReference = DataManager.getCurrentUsersRef();

        // Storage reference to store user avatar -> file name will be userId.jpg
        mAvatarStorageReference = DataManager.getAvatarStorageRef();

        // Storage reference to store user avatar thumbnail -> file name will be userId.jpg
        mThumbnailStorageReference = DataManager.getAvatarThumbStorageRef();

        retrieveDataFromFirebaseDatabase();
    }

    @Override
    public SettingsContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SettingsContract.View view) {
        super.attachView(view);
    }

    /**
     * Call this method to retrieve data from Firebase database
     */
    @Override
    public void retrieveDataFromFirebaseDatabase() {
        // Enable offline functionality
        mDatabaseReference.keepSynced(true);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get data from data snapshot
                String name = dataSnapshot.child(IFirebaseConfig.NAME).getValue().toString();
                String status = dataSnapshot.child(IFirebaseConfig.STATUS).getValue().toString();
                String image = dataSnapshot.child(IFirebaseConfig.IMAGE).getValue().toString();

                // Setup callbacks
                getView().setName(name);
                getView().setStatus(status);

                // If image has default value stored in it, pass null in callback, so that we don't
                // need to set image i.e use default
                if (image.equals(IFirebaseConfig.DEFAULT_VALUE)) {
                    getView().setImage(null);
                } else {
                    getView().setImage(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getView().onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Call this method to store avatar to Firebase storage
     * This method sets callback to progressbar and error
     *
     * @param avatarUri  Uri of user avatar
     * @param compressor compressor object to compress avatar to generate thumbnail
     */
    @Override
    public void storeAvatarToFirebaseDatabase(Uri avatarUri, Compressor compressor) {
        getView().showProgressDialog("Uploading avatar...");

        File bitmapFilePath = new File(avatarUri.getPath());

        byte[] thumb_byte = null;
        try {
            // Compress image to create thumbnail
            Bitmap thumbnailBitmap = compressor
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(75)
                    .compressToBitmap(bitmapFilePath);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            thumb_byte = stream.toByteArray();
        } catch (IOException e) {
            getView().onError("Error creating avatar thumbnail");
        }

        final byte[] finalThumb_byte = thumb_byte;

        // Upload original avatar to database storage pointing to "profile_images"
        mAvatarStorageReference.putFile(avatarUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isComplete() && task.isSuccessful()) {

                            // As soon as we are done uploading original avatar to database,
                            // upload thumbnail avatar to database
                            uploadAvatarThumbnail(finalThumb_byte);
                        } else {
                            getView().cancelProgressDialog();
                            getView().onError("Error uploading avatar!");
                        }
                    }
                });
    }

    /**
     * Call this method to upload avatar thumbnail to database storage
     * path: /profile_images/thumb_images
     *
     * @param bytes thumb image in form of byte array
     */
    @Override
    public void uploadAvatarThumbnail(byte[] bytes) {
        UploadTask uploadTask = mThumbnailStorageReference.putBytes(bytes);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Get download url from storage reference to store into out database
                    getDownloadUrlFromStorageRef();
                } else {
                    getView().cancelProgressDialog();
                    getView().onError("Error uploading avatar!");
                }
            }
        });
    }

    /**
     * Call this method to get download url of avatar from StorageReference
     */
    @Override
    public void getDownloadUrlFromStorageRef() {
        // Get the download url of original avatar
        mAvatarStorageReference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String avatarUrl = task.getResult().toString();

                            // Update database with url
                            updateDatabaseWithAvatarUrl(avatarUrl, IFirebaseConfig.IMAGE);
                        } else {
                            getView().onError("Error uploading avatar!");
                        }
                    }
                });

        // Get the download url of avatar thumbnail
        mThumbnailStorageReference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String thumbnailUrl = task.getResult().toString();

                            // Update database with url
                            updateDatabaseWithAvatarUrl(thumbnailUrl, IFirebaseConfig.THUMBNAIL);
                        }
                    }
                });
    }

    /**
     * Call this method to update database for particular user with avatar image url
     * This method sets callback for progressbar and error
     *
     * @param url   download url of avatar or thumbnail
     * @param field database field i.e image or thumbnail
     */
    @Override
    public void updateDatabaseWithAvatarUrl(String url, String field) {
        mDatabaseReference.child(field).setValue(url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            getView().cancelProgressDialog();
                        } else {
                            getView().cancelProgressDialog();
                            getView().onError("Error uploading avatar!");
                        }
                    }
                });
    }
}
