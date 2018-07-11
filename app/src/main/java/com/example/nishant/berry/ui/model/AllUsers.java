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
 * File Created on 04/06/18 8:33 PM by nishant
 * Last Modified on 04/06/18 8:33 PM
 */

package com.example.nishant.berry.ui.model;

import android.view.View;

import com.example.nishant.berry.config.IFirebaseConfig;

import java.util.Objects;

/**
 * Users object for FirebaseRecyclerAdapter
 * This object should contain variable names exactly the same as Firebase Database
 */
public class AllUsers {
    private String id;
    private String name;
    private String image;
    private String status;
    private String thumbnail;
    private String friendRequestType;
    private String declineButtonText;
    private int acceptButtonVisibility;
    private boolean online;
    private boolean messageSeen;
    private int onlineStatus;
    private long last_seen;

    public AllUsers() {
    }

    public AllUsers(String name, String image, String status, String thumbnail, boolean online, long last_seen) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumbnail = thumbnail;
        this.online = online;
        this.last_seen = last_seen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getOnlineStatus() {
        if (online) return View.VISIBLE;
        else return View.INVISIBLE;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public boolean isMessageSeen() {
        return messageSeen;
    }

    public void setMessageSeen(boolean messageSeen) {
        this.messageSeen = messageSeen;
    }

    public String getFriendRequestType() {
        return friendRequestType;
    }

    public void setFriendRequestType(String friendRequestType) {
        this.friendRequestType = friendRequestType;
    }

    public String getDeclineButtonText() {
        switch (friendRequestType) {
            case IFirebaseConfig.FRIEND_REQUEST_RECEIVED:
                return "decline";
            default:
                return "cancel request";
        }
    }

    public void setDeclineButtonText(String declineButtonText) {
        this.declineButtonText = declineButtonText;
    }

    public int getAcceptButtonVisibility() {
        switch (friendRequestType) {
            case IFirebaseConfig.FRIEND_REQUEST_RECEIVED:
                return View.VISIBLE;
            default:
                return View.GONE;
        }
    }

    public void setAcceptButtonVisibility(int acceptButtonVisibility) {
        this.acceptButtonVisibility = acceptButtonVisibility;
    }

    public long getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(long last_seen) {
        this.last_seen = last_seen;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AllUsers) return Objects.equals(((AllUsers) obj).id, this.id);
        return super.equals(obj);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
