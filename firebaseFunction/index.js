'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notifications/{userId}/{notificationId}').onWrite((change, context) => {

    // lod the userId of a user to whome we are sending a notitification
    console.log('we have a notification to send to : ', context.params.userId);

    // If the notification is deleted from database, we don't want to send it to user
    // So simly return from there
    if (!change.after.exists()) {
        return console.log('A notification has been deleted from database : ', context.params.notificationId);
    }


    // Retrive the data from notifications object
    return admin.database().ref(`/notifications/${context.params.userId}/${context.params.notificationId}`)
        .once('value').then(function(snapshot) {

            // Id of a user who sent a friend request
            const fromUserId = snapshot.val().from;
            console.log('You have new notification from : ', fromUserId);

            const deviceToken = admin.database().ref(`/users/${context.params.userId}/token_id`).once('value');
            const userQuerry = admin.database().ref(`/users/${fromUserId}/name`).once('value');

            return Promise.all([deviceToken, userQuerry]).then(function(snapshot) {
                const deviceTokenId = snapshot[0].val();
                const userName = snapshot[1].val();

                // Custom notification payload
                const payload = {
                    notification: {
                        title: "Friend Request",
                        body: `${userName} has sent you friend request!`,
                        icon: "default",
                        click_action: "com.example.nishant.berry.FRIEND_REQUEST_NOTIFICATION"
                    },
                    data: {
                        user_id: fromUserId
                    }
                };

                // Send a notification to user for friend request
                return admin.messaging().sendToDevice(deviceTokenId, payload);
            });
        });
});
