//
//  ValidateInputUtil.h
//  JinJiangTravelPlus
//
//  Created by jerry on 12-12-21.
//  Copyright (c) 2012年 JinJiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ValidateInputUtil : NSObject

+ (BOOL)isNotEmpty:(NSString *)text fieldCName:(NSString *)cName;
+ (BOOL)isEffectivePhone:(NSString *)text;
+ (BOOL)isEffectiveEmail:(NSString *)text;
+ (BOOL)isEffectivePassword:(NSString *)text;
+ (BOOL)isPureInt:(NSString *) text;
+ (BOOL)isIdentifyNumber :(NSString *) text;

+ (NSString *)valueOfObjectToString:(id) obj;

@end
