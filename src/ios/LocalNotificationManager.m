//
//  LocalNotificationManager.m
//  CFC_Tracker
//
//  Created by Kalyanaraman Shankari on 2/2/15.
//  Copyright (c) 2015 Kalyanaraman Shankari. All rights reserved.
//

#import "LocalNotificationManager.h"
#import "Cordova/edu.berkeley.eecs.emission.cordova.unifiedlogger/DBLogging.h"
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
    NSLog(@"Generating local notification with message %@", notificationMessage);
    notificationCount++;
    NSString* level = @"DEBUG";
    if (showUI) {
        level = @"INFO";
    }
    [[DBLogging database] log:notificationMessage atLevel:level];

    if (showUI) {
        UILocalNotification *localNotif = [[UILocalNotification alloc] init];
        if (localNotif) {
            localNotif.alertBody = notificationMessage;
            localNotif.applicationIconBadgeNumber = notificationCount;
            [[UIApplication sharedApplication] presentLocalNotificationNow:localNotif];
        }
    }
}

@end
