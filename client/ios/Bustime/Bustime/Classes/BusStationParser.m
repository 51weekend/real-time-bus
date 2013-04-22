//
//  BusStationParser.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusStationParser.h"
#import "BusStation.h"

@implementation BusStationParser

- (BOOL)parserJSONString:(NSString *)responseData
{
    if ([super parserJSONString:responseData]) {
        NSDictionary *dictionary = [responseData JSONValue];
        
        NSArray *array = [dictionary valueForKey:@"data"];
        NSMutableArray *stationArray = [[NSMutableArray alloc] initWithCapacity:10];
        NSMutableArray *stationTotalArray = [[NSMutableArray alloc] initWithCapacity:10];
        
        for (NSDictionary *dict in array)
        {
            BusStation *busStation = [[BusStation alloc] initWithDictionary:dict];
            BOOL isExist = NO;
            for (BusStation *busStationOnly in stationArray) {
                if ([busStation.standName isEqualToString:busStationOnly.standName]) {
                    isExist = YES;
                    break;
                }
            }
            if (!isExist) {
                [stationArray addObject:busStation];
            }
            [stationTotalArray addObject:busStation];
        }
        
        NSDictionary *data = @{@"data":stationArray, @"stationTotalArray":stationTotalArray};
        
        if(self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:DidParsedData:)]){
            [self.delegate parser:self DidParsedData:data];
        }
    }
    return YES;
}

@end
