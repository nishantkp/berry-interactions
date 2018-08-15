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
 * File Created on 15/08/18 7:03 PM by nishant
 * Last Modified on 23/07/18 10:44 PM
 */

package com.example.nishant.berry.ui.settings;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.nishant.berry.base.BasePresenter;
import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.callbacks.OnTaskCompletion;
import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.ui.model.AllUsers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

/**
 * Presenter that deals with displaying current user's profile as well as
 * updating user's avatar and status
 */
public class SettingsPresenter
        extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    // DataManager
    private DataManager mDataManager;

    SettingsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public SettingsContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(SettingsContract.View view) {
        super.attachView(view);
        retrieveDataFromFirebaseDatabase();
    }

    /**
     * Call this method to retrieve data from Firebase database
     */
    @Override
    public void retrieveDataFromFirebaseDatabase() {
        // Current user's detail
        mDataManager.getCurrentUserInfo(new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId) {
                getView().setUserInfo(model);
                getView().onStatus(model.getStatus());
            }

            @Override
            public void onError(String error) {
                getView().onError(error);
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

        // Store avatar and thumbnail to firebase database
        mDataManager.storeAvatar(avatarUri, finalThumb_byte, new OnTaskCompletion() {
            @Override
            public void onSuccess() {
                getView().cancelProgressDialog();
            }

            @Override
            public void onError(String error) {
                getView().cancelProgressDialog();
                getView().onError(error);
            }
        });
    }
}
