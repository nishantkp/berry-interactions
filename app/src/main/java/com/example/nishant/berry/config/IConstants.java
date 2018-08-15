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

package com.example.nishant.berry.config;

/**
 * General Constants and Keys
 */
public interface IConstants {
    String KEY_STATUS_INTENT = "status";
    String KEY_USER_ID = "user_id";
    String KEY_USER_DISPLAY_NAME = "display_name";
    String KEY_LAST_MESSAGE = "last_message";
    String KEY_ONLINE_STATUS = "online_status";
    String KEY_THUMBNAIL = "thumbnail";
    int DIFF_ONLINE_STATUS = 2;
    int DIFF_ALL = 0;

    // Constants values to make Views VISIBLE, INVISIBLE and GONE
    int VIEW_GONE = 8;
    int VIEW_VISIBLE = 0;
    int VIEW_INVISIBLE = 4;
}
