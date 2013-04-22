//
//  SingleLineTimeParser.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "SingleLineTimeParser.h"

@implementation SingleLineTimeParser

- (BOOL)parserJSONString:(NSString *)responseData
{
    if ([super parserJSONString:responseData]) {
        NSDictionary *dictionary = [responseData JSONValue];
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidParsedData:)]){
            [self.delegate parser:self DidParsedData:dictionary];
        }
    }
    return YES;
}

@end
