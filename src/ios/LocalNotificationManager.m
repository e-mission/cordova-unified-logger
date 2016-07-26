//
//  LocalNotificationManager.m
//  CFC_Tracker
//
//  Created by Kalyanaraman Shankari on 2/2/15.
//  Copyright (c) 2015 Kalyanaraman Shankari. All rights reserved.
//

#import "LocalNotificationManager.h"
#import "ConfigManager.h"
#import "DBLogging.h"
#import <UIKit/UIKit.h>

@implementation LocalNotificationManager

static int notificationCount = 0;

+(void)clearNotifications {
    notificationCount = 0;
    [[DBLogging database] clear];
}

+(void)addNotification:(NSString *)notificationMessage {
    [self addNotification:notificationMessage showUI:false];
}

+(void)addNotification:(NSString *)notificationMessage showUI:(BOOL)showUI {
    NSLog(@"%@", notificationMessage);
    NSString* level = @"DEBUG";
    if (showUI) {
        level = @"INFO";
    }
    [[DBLogging database] log:notificationMessage atLevel:level];

    // TODO: This adds a dependency between the notification manager and the config manager
    // This is not a good thing, but it works for now
    // Filed https://github.com/e-mission/e-mission-data-collection/issues/113 to track longer term issue
    if (showUI && [ConfigManager instance].simulate_user_interaction) {
        [LocalNotificationManager showNotification:notificationMessage];
    }
}

+(void)showNotification:(NSString *)notificationMessage {
    notificationCount++;
        UILocalNotification *localNotif = [[UILocalNotification alloc] init];
        if (localNotif) {
            localNotif.alertBody = notificationMessage;
            localNotif.applicationIconBadgeNumber = notificationCount;
            [[UIApplication sharedApplication] presentLocalNotificationNow:localNotif];
        }
    }

@end
