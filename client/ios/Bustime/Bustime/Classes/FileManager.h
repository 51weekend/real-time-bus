//
//  FileManager.h
//  JinJiangTravalPlus
//
//  Created by 胡 桂祁 on 11/8/12.
//  Copyright (c) 2012 Leon. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface FileManager : NSObject

+ (NSString *)filePath:(NSString *)fileName;
+ (NSString *)fileCachesPath:(NSString *)fileName;

@end
