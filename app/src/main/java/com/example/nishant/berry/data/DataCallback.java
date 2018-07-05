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
 * File Created on 03/07/18 10:52 PM by nishant
 * Last Modified on 03/07/18 10:52 PM
 */

package com.example.nishant.berry.data;

import com.example.nishant.berry.ui.adapter.AllUsersViewHolder;
import com.example.nishant.berry.ui.model.AllUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public interface DataCallback {
    // Success and failure callbacks
    interface OnTaskCompletion {
        void onSuccess();

        void onError(String error);
    }

    // All users list callback for list-item click and firebase adapter
    interface OnAllUsersList {
        void onListItemClick(String listUserId);

        void onFirebaseAdapter(FirebaseRecyclerAdapter adapter);
    }

    // Users data callback when dealing with user's object from firebase
    interface OnUsersData {
        void onData(AllUsers model, String userId, AllUsersViewHolder holder);

        void onError(String error);
    }

    // Current users info callback for error and AllUsers model containing name, image, thumbnail,
    // online status, last seen etc.
    interface OnCurrentUserInfo {
        void onData(AllUsers model);

        void onError(String error);
    }
}
