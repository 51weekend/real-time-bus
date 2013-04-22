//
//  FileManager.m
//  JinJiangTravalPlus
//
//  Created by 胡 桂祁 on 11/8/12.
//  Copyright (c) 2012 Leon. All rights reserved.
//

#import "FileManager.h"

@implementation FileManager

+ (NSString *)filePath:(NSString *)fileName {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    return [documentsDirectory stringByAppendingPathComponent:fileName];
}

+ (NSString *)fileCachesPath:(NSString *)fileName
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    return [documentsDirectory stringByAppendingPathComponent:fileName];
}
@end
