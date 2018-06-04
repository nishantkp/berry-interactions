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
 * File Created on 02/06/18 9:57 PM by nishant
 * Last Modified on 02/06/18 9:57 PM
 */

package com.example.nishant.berry.ui.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.databinding.ActivitySettingsBinding;
import com.example.nishant.berry.ui.status.StatusActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SettingsActivity
        extends AppCompatActivity
        implements SettingsContract.View {

    private ActivitySettingsBinding mBinding;
    private SettingsPresenter mPresenter;
    private String mStatus;
    private static final int GALLERY_INTENT_REQUEST_CODE = 100;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        mBinding.setActivity(this);

        mPresenter = new SettingsPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void setName(String name) {
        mBinding.settingsDisplayName.setText(name);
    }

    @Override
    public void setStatus(String status) {
        mBinding.settingsStatus.setText(status);
        mStatus = status;
    }

    @Override
    public void setImage(String imageUri) {
        Picasso.get().load(imageUri).into(mBinding.settingsAvatar);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show progress dialog
     */
    @Override
    public void showProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    /**
     * Cancel progress dialog
     */
    @Override
    public void cancelProgressDialog() {
        mProgressDialog.dismiss();
    }

    /**
     * When user press status change button start {@link StatusActivity}
     */
    public void onStatusButtonClick() {
        startActivity(
                new Intent(this, StatusActivity.class)
                        .putExtra(IConstants.KEY_STATUS_INTENT, mStatus)
        );
    }

    /**
     * When user press change avatar button start gallery intent to pick image
     */
    public void onChangeAvatarButtonClick() {

        // Setup intent to select image from device
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "PICK IMAGE"),
                GALLERY_INTENT_REQUEST_CODE);

//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(SettingsActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            // Start image cropper
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        // This will get the result after cropping
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mPresenter.storeAvatarToFirebaseDatabase(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
