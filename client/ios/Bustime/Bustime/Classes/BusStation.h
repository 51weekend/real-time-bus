//
//  BusStation.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BusStation : NSObject<NSCoding, NSCopying>

@property(nonatomic, copy) NSString *standCode;
@property(nonatomic, copy) NSString *standName;
@property(nonatomic, copy) NSString *trend;
@property(nonatomic, copy) NSString *area;
@property(nonatomic, copy) NSString *road;
@property(nonatomic, copy) NSString *bus;

+ (BusStation *)unarchived:(NSData *) data;

- (id) initWithDictionary : (NSDictionary *) dict;

- (NSData *)archived;

@end
