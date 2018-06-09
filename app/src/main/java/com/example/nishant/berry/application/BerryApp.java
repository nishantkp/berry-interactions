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

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Application class
 */
public class BerryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Adds firebase offline functionality
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Picasso with OkHttp3 to download user avatar
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso picasso = builder.build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }
}
