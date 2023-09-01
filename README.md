# NotificationReader

## Overview

NotificationReader app is an Android application whose primary functionalities are to read out the incoming notifications, store them securely in the local database, and provide a user interface for managing these notifications.

## Features
- Notification Reading: The app listens for incoming notifications and reads out the package name and title of each notification using text-to-speech technology.
- Notification Storage: Notifications are securely stored in a local database on the device for future reference.
- Notification List: The app displays a list of received notifications in the user interface. Each notification entry includes the package name, title, text, and timestamp.
- Notification Details: Users can click on a notification entry to view its details. In this view, users are presented with two buttons:
  - Read-out Notification: Allows users to read out the contents of the notification from the list.
  - Delete Notification: Allows users to delete a notification from the app.
- Secure Storage: The app uses encryption and decryption methods to securely store notification data in the local database.
