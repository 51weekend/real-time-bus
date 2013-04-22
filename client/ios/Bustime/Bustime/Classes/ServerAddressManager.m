//
//  ServerAddressManager.m
//  ChoiceCourse
//
//  Created by 汪君瑞 on 12-10-30.
//  Copyright (c) 2012年 jerry. All rights reserved.
//

#import "ServerAddressManager.h"

@implementation ServerAddressManager

+ (NSString *)serverAddress:(NSString *)interfaceType
{
    NSString* path = [[NSBundle mainBundle] pathForResource:@"server" ofType:@"plist"];
    NSDictionary* dict = [NSDictionary dictionaryWithContentsOfFile:path];
    return [dict objectForKey:interfaceType];
}

@end
