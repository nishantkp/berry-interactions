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
 * File Created on 03/06/18 1:09 AM by nishant
 * Last Modified on 03/06/18 1:09 AM
 */

package com.example.nishant.berry.config;

public interface IFirebaseConfig {
    String USERS_OBJECT = "users";
    String FRIEND_REQUEST_OBJECT = "friend_requests";
    String FRIENDS_OBJECT = "friends";
    String NAME = "name";
    String THUMBNAIL = "thumbnail";
    String STATUS = "status";
    String IMAGE = "image";
    String AVATAR_STORAGE_DIR = "profile_images";
    String DEFAULT_VALUE = "default";
    String DEFAULT_STATUS = "Hi there, it's berry!";
    String THUMBNAIL_STORAGE_DIR = "thumb_images";
    String FRIEND_REQUEST_TYPE = "request_type";
    String FRIEND_REQUEST_SENT = "sent";
    String FRIEND_REQUEST_RECEIVED = "received";
    int NOT_FRIEND = 101;
    int REQ_SENT = 102;
    int REQ_RECEIVED = 103;
    int FRIENDS = 104;
}
