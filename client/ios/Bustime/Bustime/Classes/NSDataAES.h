//
//  NSDataAES.h
//  
//
//  Created by Lipeng on 11-6-25.
//  Copyright 2011年 JinJiang. All rights reserved.
//

#import <Foundation/Foundation.h>

extern NSString* kCryptorKey;

#pragma mark - @interface NSData (AES128)

@interface NSData (AES128)

+ (NSData *)dataFromBase64String:(NSString *)aString;
- (NSString *)base64EncodedString;

@end

#pragma mark - @interface NSString (Encrypt)

@interface NSString (Encrypt)

- (NSString *)AES128EncryptWithKey:(NSString *)key; 
- (NSString *)AES128DecryptWithKey:(NSString *)key;
- (NSString *)stringByURLEncodingStringParameter;

- (NSString *)MD5String; 

@end