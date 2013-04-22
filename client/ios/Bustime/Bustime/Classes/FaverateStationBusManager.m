//
//  FaverateStationBusManager.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-9.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "FaverateStationBusManager.h"
#import "FileManager.h"

@interface FaverateStationBusManager ()

- (NSMutableArray *) readFaverateBusStationFromLocalFile;

@end


@implementation FaverateStationBusManager

- (BOOL)insertIntoFaverateWithStation:(BusStation *) busStation
{
    NSString *path = [FileManager fileCachesPath:@"FaverateBusStation.plist"];
    
    NSMutableArray *stationArray = [self readFaverateBusStationFromLocalFile];
    if (stationArray == nil) {
        stationArray = [[NSMutableArray alloc] initWithCapacity:20];
    }
    [stationArray addObject:[busStation archived]];
    return [stationArray writeToFile: path atomically:YES];
}

- (BOOL)isBusStationInFaverate:(NSString *) standName
{
    NSMutableArray *stationArray = [self readFaverateBusStationFromLocalFile];
    if (stationArray) {
        for (NSData *data in stationArray) {
            BusStation *busStation = [BusStation unarchived:data];
            if ([standName caseInsensitiveCompare:busStation.standName] == NSOrderedSame) {
                return YES;
            }
        }
    }
    return NO;
}

- (BOOL)deleteBusStationInFaverate:(NSString *) standName
{
    NSMutableArray *stationArray = [self readFaverateBusStationFromLocalFile];
    if (stationArray) {
        NSInteger count = 0;
        for (NSInteger i=0; i < [stationArray count]; i++) {
            NSData *data = [stationArray objectAtIndex:i];
            BusStation *busStation = [BusStation unarchived:data];
            if ([standName caseInsensitiveCompare:busStation.standName] == NSOrderedSame) {
                count += 1;
            }
        }
        
        for (NSInteger m=0; m < count; m++) {
            for (NSInteger i=0; i < [stationArray count]; i++) {
                NSData *data = [stationArray objectAtIndex:i];
                BusStation *busStation = [BusStation unarchived:data];
                if ([standName caseInsensitiveCompare:busStation.standName] == NSOrderedSame) {
                    [stationArray removeObjectAtIndex:i];
                    break;
                }
            }
        }
    }
    
    NSString *path = [FileManager fileCachesPath:@"FaverateBusStation.plist"];
    return [stationArray writeToFile: path atomically:YES];
}

- (NSMutableArray *) readFaverateBusStationFromLocalFile
{
    NSString *path = [FileManager fileCachesPath:@"FaverateBusStation.plist"];
    return [[NSMutableArray alloc] initWithContentsOfFile:path];
}

- (void)buildLocalFileToArray:(NSMutableArray *) stationArray total:(NSMutableArray *) stationTotalArray
{
    NSMutableArray *busStationArray = [self readFaverateBusStationFromLocalFile];
    [stationArray removeAllObjects];
    [stationTotalArray removeAllObjects];
    
    if (busStationArray) {
        for (NSInteger i=0; i < [busStationArray count]; i++) {
            NSData *data = [busStationArray objectAtIndex:i];
            BusStation *busStation = [BusStation unarchived:data];
            
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
    }
}

@end
