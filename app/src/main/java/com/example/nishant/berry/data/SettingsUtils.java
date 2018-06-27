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
 * File Created on 26/06/18 6:44 PM by nishant
 * Last Modified on 26/06/18 6:44 PM
 */

package com.example.nishant.berry.data;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

class SettingsUtils {
    private static SettingsUtils sSettingUtils;
    private AvatarStorageCallback mAvatarStorageCallback;

    // Singleton
    static SettingsUtils getInstance() {
        if (sSettingUtils == null) {
            sSettingUtils = new SettingsUtils();
        }
        return sSettingUtils;
    }

    /**
     * Use this method to set storage callbacks
     * Must set it before calling storeAvatarToFirebaseDatabase() method to store avatar to firebase
     * storage
     *
     * @param callback Must be initiated by call which implements this callback
     *                 i.e {@link DataManager} class
     */
    void setAvatarStorageCallback(AvatarStorageCallback callback) {
        mAvatarStorageCallback = callback;
    }

    /**
     * Call this method to store user's avatar and thumbnail to FirebaseStorage
     *
     * @param avatarUri     Uri of user avatar
     * @param thumbnailByte thumb image inform of byte array
     */
    void storeAvatarToFirebaseDatabase(Uri avatarUri,
                                       final byte[] thumbnailByte) {
        //Safety to avoid NullPointerException
        if (mAvatarStorageCallback == null) return;

        DataManager.getAvatarStorageRef().putFile(avatarUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            uploadAvatarThumbnail(thumbnailByte);
                        } else {
                            mAvatarStorageCallback.onAvatarStoreError("Error uploading avatar!");
                        }
                    }
                });
    }

    /**
     * Call this method to upload avatar thumbnail to database storage
     * path: /profile_images/thumb_images
     *
     * @param thumbnailBytes thumb image in form of byte array
     */
    private void uploadAvatarThumbnail(byte[] thumbnailBytes) {
        UploadTask uploadTask = DataManager.getAvatarThumbStorageRef().putBytes(thumbnailBytes);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Get download url from storage reference to store into out database
                    getDownloadUrlFromStorageRef();
                } else {
                    mAvatarStorageCallback.onAvatarStoreError("Error updating avatar!");
                }
            }
        });
    }

    /**
     * Call this method to get download url of avatar from StorageReference
     * And update the Users object with avatar url and thumbnail url
     */
    private void getDownloadUrlFromStorageRef() {
        // Get the download url of original avatar
        DataManager.getAvatarStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String avatarUrl = task.getResult().toString();
                            // Update user's object
                            updateDatabaseWithAvatarUrl(avatarUrl, IFirebaseConfig.IMAGE);
                        } else {
                            mAvatarStorageCallback.onAvatarStoreError("Error updating avatar!");
                        }
                    }
                });

        // Get the download url of avatar thumbnail
        DataManager.getAvatarThumbStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String thumbnailUrl = task.getResult().toString();
                            // Update user's object
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
    private void updateDatabaseWithAvatarUrl(String url, String field) {
        DataManager.getCurrentUsersRef().child(field).setValue(url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mAvatarStorageCallback.onAvatarStoreSuccess();
                        } else {
                            mAvatarStorageCallback.onAvatarStoreError("Error uploading avatar!");
                        }
                    }
                });
    }

    /**
     * Callbacks for when user uploads avatar and thumbnail to firebase storage
     */
    interface AvatarStorageCallback {
        void onAvatarStoreSuccess();

        void onAvatarStoreError(String error);
    }
}
