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

package com.example.nishant.berry.ui.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.nishant.berry.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * ImageLoader class
 */
public class ImageLoad {

    /**
     * Use this method to download image and display it into ImageView
     *
     * @param url  url of image
     * @param view ImageView in which we want to display image
     */
    private static void load(final String url, final ImageView view) {
        // Download and load image into ImageView
        Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.user_default_avatar)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        /* These means, we have image stored locally */
                    }

                    @Override
                    public void onError(Exception e) {
                        /* This means, we don't have image stored locally so download from database */
                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.user_default_avatar)
                                .into(view);
                    }
                });
    }

    /**
     * Binding adapter for updating avatar in ImageView
     *
     * @param view        ImageView in which we want to load avatar
     * @param downloadUrl Download url of avatar
     */
    @BindingAdapter({"app:loadImageFromUrl"})
    public static void setImageResource(ImageView view, String downloadUrl) {
        load(downloadUrl, view);
    }
}
