'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notifications/{userId}/{notificationId}').onWrite((change, context) => {

  //const notification = event.params.notification;

  console.log('we have a notification to send to : ', context.params.userId);

  // If the notification is deleted from database, we don't want to send it to user
  // So simly return from there
  if(!change.after.exists()){
    return console.log('A notification has been deleted from database : ',context.params.notificationId);
  }

  return admin.database().ref(`/users/${context.params.userId}/token_id`)
                        .once('value').then(function(snapshot) {

    const tokenId = snapshot.val();
    // log the token Id
    console.log('Device token id is : ', tokenId);

    const payload = {
      notification: {
        title: "Friend Request",
        body: "You have received a new Friend Request",
        icon: "default"
      }
    };

    return admin.messaging().sendToDevice(tokenId, payload);
  });
});
