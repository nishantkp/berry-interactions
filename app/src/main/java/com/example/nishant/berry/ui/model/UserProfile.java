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

package com.example.nishant.berry.ui.model;

/**
 * Object to set fields of User profile
 */
public class UserProfile {
    private String displayName;
    private String status;
    private String friendReqButtonText = "send friend request";
    private String avatar;
    private boolean friendReqButtonEnabled = true;
    private int declineFriendReqButtonVisibility = 0;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFriendReqButtonText() {
        return friendReqButtonText;
    }

    public void setFriendReqButtonText(String friendReqButtonText) {
        this.friendReqButtonText = friendReqButtonText;
    }

    public boolean isFriendReqButtonEnabled() {
        return friendReqButtonEnabled;
    }

    public void setFriendReqButtonEnabled(boolean friendReqButtonEnabled) {
        this.friendReqButtonEnabled = friendReqButtonEnabled;
    }

    public int getDeclineFriendReqButtonVisibility() {
        return declineFriendReqButtonVisibility;
    }

    public void setDeclineFriendReqButtonVisibility(int declineFriendReqButtonVisibility) {
        this.declineFriendReqButtonVisibility = declineFriendReqButtonVisibility;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
