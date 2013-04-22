//
//  BusSingleLineParser.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusSingleLineParser.h"
#import "BusSingleLine.h"

@implementation BusSingleLineParser

- (BOOL)parserJSONString:(NSString *)responseData
{
    if ([super parserJSONString:responseData]) {
        NSDictionary *dictionary = [responseData JSONValue];
        
        NSArray *array = [dictionary valueForKey:@"data"];
        NSMutableArray *busSingleLineArry = [[NSMutableArray alloc] initWithCapacity:10];
        
        for (NSDictionary *dict in array)
        {
            BusSingleLine *busSingleLine = [[BusSingleLine alloc] initWithDictionary:dict];
            [busSingleLineArry addObject:busSingleLine];
        }
        
        NSDictionary *data = @{@"data":busSingleLineArry};
        
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidParsedData:)]){
            [self.delegate parser:self DidParsedData:data];
        }
    }
    return YES;
}

@end
