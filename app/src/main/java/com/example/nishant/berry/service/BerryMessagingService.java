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
 * Last Modified on 10/06/18 1:36 PM
 */

package com.example.nishant.berry.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.nishant.berry.R;
import com.example.nishant.berry.config.IConstants;
import com.example.nishant.berry.config.IFirebaseConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

/**
 * Firebase messaging service
 * When app is in background or is closed, and user receives friend request.
 * When user clicks on that notification, {@link com.example.nishant.berry.ui.profile.UserProfileActivity}
 * will open and we can extract data in onCreate method by getIntent().getExtra("key").
 * <p>
 * When app is in foreground, and user received notification, and click on it, onMessageReceived()
 * method will trigger
 */
public class BerryMessagingService extends FirebaseMessagingService {

    private static String CHANNEL_ID = "notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Show friend request notification
        showNotification(remoteMessage);
    }

    /**
     * Call this method to show notification
     * Format of a message received from firebase
     * <p>
     * payload = {
     * notification: {
     * title: "Friend Request",
     * body: `${userName} has sent you friend request!`,
     * icon: "default",
     * click_action: "com.example.nishant.berry.FRIEND_REQUEST_NOTIFICATION"
     * },
     * data:{
     * user_id: fromUserId
     * }
     * }
     *
     * @param remoteMessage Message received from firebase
     */
    private void showNotification(RemoteMessage remoteMessage) {
        // Extract title, body, user id from firebase message
        String notificationTitle = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String notificationBody = remoteMessage.getNotification().getBody();
        String clickAction = remoteMessage.getNotification().getClickAction();
        // UserId of a user who sent friend request
        String fromUserId = remoteMessage.getData().get(IFirebaseConfig.INotification.KEY_FROM_USER_ID);

        // Intent to open Profile activity, when user clicks on notification
        // Add the user Id of a user who sent a friend request to intent extra
        Intent resultIntent = new Intent(clickAction);
        resultIntent.putExtra(IConstants.KEY_USER_ID, fromUserId);

        // Pending intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_person_add_24px)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);

        // Notification Id : current time ensures uniqueness of Id
        // so we can receive different notifications
        int notificationId = (int) System.currentTimeMillis();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}
