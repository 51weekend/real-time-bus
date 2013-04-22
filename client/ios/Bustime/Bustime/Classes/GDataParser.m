//
//  GDataParser.m
//  JinJiang
//
//  Created by Leon on 10/22/12.
//
//

#import "GDataParser.h"
#import "NSDataAES.h"

@interface GDataParser ()

@end

@implementation GDataParser

@synthesize requestString = _requestString;
@synthesize isHTTPGet = _isHTTPGet;
@synthesize delegate = _delegate;
@synthesize serverAddress = _serverAddress;

-(id)init
{
    if(self = [super init])
    {
        _requestData = [[NSMutableData alloc] init];
        self.isHTTPGet = YES;
    }
    return self;
}

-(void)dealloc
{
    self.delegate = nil;

    [self cancel];
    _requestData = nil;
}

- (void)start
{
    [self cancel];
    [_requestData resetBytesInRange:NSMakeRange(0, [_requestData length])];
    [_requestData setLength:0];
    NSString *url = [NSString stringWithFormat:@"%@?%@", self.serverAddress, self.requestString];
    NSLog(@"%@",url);
    NSURL *nsURL = [NSURL URLWithString:[url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSURLRequest *request = [NSURLRequest requestWithURL:nsURL cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval: 5];
    _connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    [_connection start];
}

- (void)cancel
{
    [_connection cancel];
    _connection = nil;
}

- (BOOL)parserJSONString:(NSString *)responseData
{
    if(responseData == nil || [responseData length] == 0)
    {
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidFailedParseWithMsg:errCode:)])
            [self.delegate parser:self DidFailedParseWithMsg:@"response data is nil or empty" errCode:-1];
        return NO;
    }
    NSDictionary *dictionary = [responseData JSONValue];
    if (dictionary == nil) {
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidFailedParseWithMsg:errCode:)])
            [self.delegate parser:self DidFailedParseWithMsg:@"response data is not NSDictionary" errCode:-1];
        return NO;
    }
    int code = [[dictionary valueForKey:@"resultCode"] intValue];
    NSString *resultMsg = [dictionary valueForKey:@"resultMsg"];
    if(code != 0){
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidFailedParseWithMsg:errCode:)])
            [self.delegate parser:self DidFailedParseWithMsg:resultMsg errCode:code];
        return NO;
    }
    
    return YES;
}

#pragma mark NSURLConnectionDelegate

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidFailedParseWithMsg:errCode:)])
    {   [self.delegate parser:self DidFailedParseWithMsg:@"connection error" errCode:-1];   }
    [self cancel];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data;
{
    [_requestData appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    NSString* jsonString = [[NSString alloc] initWithData:_requestData encoding:NSUTF8StringEncoding];
    [self parserJSONString:jsonString];
    [self cancel];
}

@end