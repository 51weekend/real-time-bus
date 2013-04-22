//
//  BusLineParser.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-1.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusLineParser.h"
#import "BusLine.h"

@implementation BusLineParser

- (BOOL)parserJSONString:(NSString *)responseData
{
    if ([super parserJSONString:responseData]) {
        NSDictionary *dictionary = [responseData JSONValue];
        
        NSArray *array = [dictionary valueForKey:@"data"];
        NSMutableArray *busLineArry = [[NSMutableArray alloc] initWithCapacity:10];
        NSMutableArray *busLineOnlyArry = [[NSMutableArray alloc] initWithCapacity:10];
    
        for (NSDictionary *dict in array)
        {
            BusLine *busLine = [[BusLine alloc] initWithDictionary:dict];
            BOOL isExist = NO;
            for (BusLine *busLineOnly in busLineOnlyArry) {
                if ([busLine.lineNumber isEqualToString:busLineOnly.lineNumber]) {
                    isExist = YES;
                    break;
                }
            }
            if (!isExist) {
                [busLineOnlyArry addObject:busLine];
            }
            [busLineArry addObject:busLine];
        }
             
        NSDictionary *data = @{@"data":busLineOnlyArry, @"busLineArry":busLineArry};
        
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidParsedData:)]){
            [self.delegate parser:self DidParsedData:data];
        }
    }
    return YES;
}

@end
