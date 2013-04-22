//
//  BusSingleLine.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BusSingleLine : NSObject

@property(nonatomic, copy) NSString *standCode;
@property(nonatomic, copy) NSString *standName;
@property(nonatomic, copy) NSString *time;

- (id) initWithDictionary : (NSDictionary *) dict;

@end
