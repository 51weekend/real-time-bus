//
//  AppDelegate.h
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GAI.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (nonatomic, strong) UIWindow *window;
@property (nonatomic, strong) id<GAITracker> tracker;

@end
