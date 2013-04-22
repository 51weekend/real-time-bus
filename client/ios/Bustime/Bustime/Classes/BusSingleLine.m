//
//  BusSingleLine.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusSingleLine.h"
#import "ValidateInputUtil.h"

@implementation BusSingleLine

- (id) initWithDictionary : (NSDictionary *) dict
{
    if (dict == nil) {
        self = [super init];
        return self;
    }
    
    if (self = [super init]) {
        self.standCode = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"standCode"]];
        self.standName = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"standName"]];
        self.time = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"time"]];
    }
    return self;
}

@end
