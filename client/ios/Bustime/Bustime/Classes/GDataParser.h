//
//  GDataParser.h
//  JinJiang
//
//  Created by Leon on 10/22/12.
//
//

#import <Foundation/Foundation.h>
#import "SBJson.h"

@class GDataParser;

#pragma mark - GDataParserDelegate

@protocol GDataParserDelegate <NSObject>

@optional

- (void)parser:(GDataParser*)parser DidFailedParseWithMsg:(NSString*)msg errCode:(NSInteger)code;
- (void)parser:(GDataParser*)parser DidParsedData:(NSDictionary *)data;

@end

#pragma mark - GDataParser

@interface GDataParser : NSObject
{
    NSURLConnection*    _connection;
    NSMutableData*      _requestData;
    BOOL _reachAbility;
}

@property (nonatomic) BOOL isHTTPGet;
@property (nonatomic, weak) id<GDataParserDelegate> delegate;
@property (nonatomic, copy) NSString* serverAddress;
@property (nonatomic, copy) NSString* requestString;

- (void)start;
- (void)cancel;
- (BOOL)parserJSONString:(NSString *)responseData;

@end