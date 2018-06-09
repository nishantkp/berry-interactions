'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notifications/{userId}/{notificationId}').onWrite((change, context) => {

    //const notification = event.params.notification;

    console.log('we have a notification to send to : ', context.params.userId);

    // If the notification is deleted from database, we don't want to send it to user
    // So simly return from there
    if (!change.after.exists()) {
        return console.log('A notification has been deleted from database : ', context.params.notificationId);
    }

    return admin.database().ref(`/users/${context.params.userId}/token_id`)
        .once('value').then(function(snapshot) {

            const tokenId = snapshot.val();
            // log the token Id
            console.log('Device token id is : ', tokenId);

            // Retrive the data from notifications object
            const fromUser = admin.database().ref(`/notifications/${context.params.userId}/${context.params.notificationId}`)
                .once('value').then(function(snapshot) {
                    const fromUserId = snapshot.val().from;
                    console.log('You have new notification from : ', fromUserId);

                    // Get the user name from users database
                    const userQuerry = admin.database().ref(`/users/${fromUserId}/name`)
                        .once('value').then(function(snapshot) {
                            const userName = snapshot.val();

                            // Custom notification payload
                            const payload = {
                                notification: {
                                    title: "Friend Request",
                                    body: `${userName} has sent you friend request!`,
                                    icon: "default"
                                }
                            };

                            // Send a notification to user
                            return admin.messaging().sendToDevice(tokenId, payload);
                        });
                    return;
                });
            return;
        });
});
