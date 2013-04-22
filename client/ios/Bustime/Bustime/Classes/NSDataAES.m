//
//  NSDataAES.m
//  
//
//  Created by Lipeng on 11-6-25.
//  Copyright 2011年 JinJiang. All rights reserved.
//  reference: http://tech.plogie.com/?p=198

#import "NSDataAES.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>

NSString *kCryptorKey = @"!k@U3p~1%*";

static const char encodingTable[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

#pragma mark - NSData (AES128)

@implementation NSData (AES128)

+ (NSData *)dataFromBase64String:(NSString *)string
{
    if (string == nil || [string length] == 0)
        return [NSData data];

    static char *decodingTable = NULL;
    if (decodingTable == NULL)
    {
        decodingTable = malloc(256);
        if (decodingTable == NULL)
        {   return nil; }
        memset(decodingTable, CHAR_MAX, 256);
        for (unsigned int i = 0; i < 64; i++)
        {   decodingTable[(short)encodingTable[i]] = i; }
    }
    
    const char *characters = [string cStringUsingEncoding:NSASCIIStringEncoding];
    if (characters == NULL)     //  Not an ASCII string!
    {   return nil; }
    char *bytes = malloc((([string length] + 3) / 4) * 3);
    if (bytes == NULL)
    {   return nil; }
    unsigned int length = 0;
    
    unsigned int i = 0;
    while (YES)
    {
        char buffer[4];
        short bufferLength;
        for (bufferLength = 0; bufferLength < 4; i++)
        {
            if (characters[i] == '\0')
            {   break;  }
            if (isspace(characters[i]) || characters[i] == '=')
            {   continue;   }
            buffer[bufferLength] = decodingTable[(short)characters[i]];
            if (buffer[bufferLength++] == CHAR_MAX)      //  Illegal character!
            {
                free(bytes);
                return nil;
            }
        }

        if (bufferLength == 0)
        {   break;  }
        if (bufferLength == 1)      //  At least two characters are needed to produce one byte!
        {
            free(bytes);
            return nil;
        }
        
        //  Decode the characters in the buffer to bytes.
        bytes[length++] = (buffer[0] << 2) | (buffer[1] >> 4);
        if (bufferLength > 2)
        {   bytes[length++] = (buffer[1] << 4) | (buffer[2] >> 2);  }
        if (bufferLength > 3)
        {   bytes[length++] = (buffer[2] << 6) | buffer[3]; }
    }
    
    realloc(bytes, length);
    return [NSData dataWithBytesNoCopy:bytes length:length];
}

- (NSString *)base64EncodedString
{
    if ([self length] == 0)
    {   return @""; }
    
    char *characters = malloc((([self length] + 2) / 3) * 4);
    if (characters == NULL)
    {   return nil; }
    unsigned int length = 0;
    unsigned int i = 0;

    while (i < [self length])
    {
        char buffer[3] = {0,0,0};
        short bufferLength = 0;
        while (bufferLength < 3 && i < [self length])
        {   buffer[bufferLength++] = ((char *)[self bytes])[i++];   }
        
        //  Encode the bytes in the buffer to four characters, including padding "=" characters if necessary.
        characters[length++] = encodingTable[(buffer[0] & 0xFC) >> 2];
        characters[length++] = encodingTable[((buffer[0] & 0x03) << 4) | ((buffer[1] & 0xF0) >> 4)];
        if (bufferLength > 1)
        {   characters[length++] = encodingTable[((buffer[1] & 0x0F) << 2) | ((buffer[2] & 0xC0) >> 6)];    }
        else
        {   characters[length++] = '='; }
        if (bufferLength > 2)
        {   characters[length++] = encodingTable[buffer[2] & 0x3F]; }
        else
        {   characters[length++] = '='; }
    }

    return [[NSString alloc] initWithBytesNoCopy:characters length:length encoding:NSASCIIStringEncoding freeWhenDone:YES];
}

@end

#pragma mark - NSString (Encrypt)

@implementation NSString (Encrypt)

- (NSString *)AES128EncryptWithKey:(NSString *)key
{
    // 'key' should be 32 bytes for AES256, will be null-padded otherwise
    char keyPtr[kCCKeySizeAES128+1]; // room for terminator (unused)
    bzero(keyPtr, sizeof(keyPtr)); // fill with zeroes (for padding)

    // fetch key data
    [key getCString:keyPtr maxLength:sizeof(keyPtr) encoding:NSUTF8StringEncoding];

    // pad key with n. Private rule, agree with server, add by lipeng
    const unsigned int keylen = strlen(keyPtr);
    for (unsigned int i = keylen; i < kCCKeySizeAES128; ++i)
    {   keyPtr[i] = (kCCKeySizeAES128 - keylen);    }

    NSData *dataText = [self dataUsingEncoding:NSUTF8StringEncoding];    
    NSUInteger dataLength = [dataText length];

    //See the doc: For block ciphers, the output size will always be less than or 
    //equal to the input size plus the size of one block.
    //That's why we need to add the size of one block here
    size_t bufferSize = dataLength + kCCBlockSizeAES128;
    void *buffer = malloc(bufferSize);

    size_t numBytesEncrypted = 0;
    CCCryptorStatus cryptStatus = CCCrypt(kCCEncrypt, kCCAlgorithmAES128, kCCOptionPKCS7Padding | kCCOptionECBMode,
                                          keyPtr, kCCKeySizeAES128,
                                          NULL /* initialization vector (optional) */,
                                          [dataText bytes], dataLength, /* input */
                                          buffer, bufferSize, /* output */
                                          &numBytesEncrypted);
    if (cryptStatus == kCCSuccess)
    {
        //the returned NSData takes ownership of the buffer and will free it on deallocation
        NSData *result = [NSData dataWithBytesNoCopy:buffer length:numBytesEncrypted];
        NSLog(@"%s in hex.. %@", __FUNCTION__, [result description]);
        NSString *strResult = [result base64EncodedString];
        return strResult;
    }
    free(buffer);
    return @"";
}

- (NSString *)AES128DecryptWithKey:(NSString *)key
{
	char keyPtr[kCCKeySizeAES128+1];
	bzero(keyPtr, sizeof(keyPtr));

	[key getCString:keyPtr maxLength:sizeof(keyPtr) encoding:NSUTF8StringEncoding];

    // pad key with n  agree with server, add by lipeng
    const unsigned int keylen = strlen(keyPtr);
    for (unsigned int i = keylen; i < kCCKeySizeAES128; ++i)
    {   keyPtr[i] = (kCCKeySizeAES128 - keylen);    }

    NSData *dataText = [NSData dataFromBase64String:self];    
    unsigned int dataLength = [dataText length];

	size_t bufferSize = dataLength + kCCBlockSizeAES128;
	void *buffer = malloc(bufferSize);

	size_t numBytesDecrypted = 0;
	CCCryptorStatus cryptStatus = CCCrypt(kCCDecrypt, kCCAlgorithmAES128,
										  kCCOptionPKCS7Padding | kCCOptionECBMode,
										  keyPtr, kCCBlockSizeAES128,
										  NULL, [dataText bytes], dataLength,
										  buffer, bufferSize, &numBytesDecrypted);
	if (cryptStatus == kCCSuccess)
    {
        NSData *result = [NSData dataWithBytesNoCopy:buffer length:numBytesDecrypted];
        //NSLog(@"%s in hex.. %@", __FUNCTION__, [result description]);
        //NSString *strResult = [result base64EncodedString];
        NSString* strResult = [[NSString alloc] initWithData:result encoding:NSUTF8StringEncoding];
        return strResult;
    }
    free(buffer);
    return nil;
}

- (NSString*)stringByURLEncodingStringParameter
{
    // NSURL's stringByAddingPercentEscapesUsingEncoding: does not escape
    // some characters that should be escaped in URL parameters, like / and ?; 
    // we'll use CFURL to force the encoding of those
    //
    // We'll explicitly leave spaces unescaped now, and replace them with +'s
    //
    // Reference: [url]http://www.ietf.org/rfc/rfc3986.txt[/url]

    NSString *resultStr = self;

    CFStringRef originalString = (__bridge CFStringRef) self;
    CFStringRef leaveUnescaped = CFSTR(" ");
    CFStringRef forceEscaped = CFSTR("!*'();:@&=+$,/?%#[]");

    CFStringRef escapedStr;
    escapedStr = CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                         originalString,
                                                         leaveUnescaped, 
                                                         forceEscaped,
                                                         kCFStringEncodingUTF8);
    if (escapedStr)
    {
        NSMutableString *mutableStr = [NSMutableString stringWithString:(__bridge NSString *)escapedStr];
        CFRelease(escapedStr);

        // replace spaces with plusses
        [mutableStr replaceOccurrencesOfString:@" "
                                    withString:@"%20"
                                       options:0
                                         range:NSMakeRange(0, [mutableStr length])];
        resultStr = mutableStr;
    }
    return resultStr;
}

- (NSString *)MD5String
{
    const char *cStr = [self UTF8String];
    unsigned char result[16];
    CC_MD5(cStr, strlen(cStr), result);
    return [NSString stringWithFormat:
            @"%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",
            result[0], result[1], result[2], result[3], 
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];
}

@end
