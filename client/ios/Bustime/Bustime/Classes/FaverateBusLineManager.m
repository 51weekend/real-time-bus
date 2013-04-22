//
//  FaverateHotelManager.m
//  JinJiangTravelPlus
//
//  Created by jerry on 13-3-8.
//  Copyright (c) 2013年 JinJiang. All rights reserved.
//

#import "FaverateBusLineManager.h"
#import "FileManager.h"

@interface FaverateBusLineManager ()

- (NSMutableArray *) readFaverateBusListFromLocalFile;

@end

@implementation FaverateBusLineManager

- (BOOL)insertIntoFaverateWithBusLine:(BusLine *) busLine
{
    NSString *path = [FileManager fileCachesPath:@"FaverateBusList.plist"];
    
    NSMutableArray *busArray = [self readFaverateBusListFromLocalFile];
    if (busArray == nil) {
        busArray = [[NSMutableArray alloc] initWithCapacity:20];
    }
    [busArray addObject:[busLine archived]];
    
    return [busArray writeToFile: path atomically:YES];
    
}


- (BOOL)isBusLineInFaverate:(NSString *) lineNumber
{
    NSMutableArray *busArray = [self readFaverateBusListFromLocalFile];
    if (busArray) {
        for (NSData *data in busArray) {
            BusLine *busLine = [BusLine unarchived:data];
            if ([lineNumber caseInsensitiveCompare:busLine.lineNumber] == NSOrderedSame) {
                return YES;
            }
        }
    }
    return NO;
}

- (BOOL)deleteBusLineInFaverate:(NSString *) lineNumber
{
    NSMutableArray *busArray = [self readFaverateBusListFromLocalFile];
    
    if (busArray) {
        NSInteger count = 0;
        for (NSInteger i=0; i < [busArray count]; i++) {
            NSData *data = [busArray objectAtIndex:i];
            BusLine *busLine = [BusLine unarchived:data];
            if ([lineNumber caseInsensitiveCompare:busLine.lineNumber] == NSOrderedSame) {
                count += 1;
            }
        }
        
        for (NSInteger m=0; m < count; m++) {
            for (NSInteger i=0; i < [busArray count]; i++) {
                NSData *data = [busArray objectAtIndex:i];
                BusLine *busLine = [BusLine unarchived:data];
                if ([lineNumber caseInsensitiveCompare:busLine.lineNumber] == NSOrderedSame) {
                    [busArray removeObjectAtIndex:i];
                    break;
                }
            }
        }
    }
    
    NSString *path = [FileManager fileCachesPath:@"FaverateBusList.plist"];
    return [busArray writeToFile: path atomically:YES];
}

- (NSMutableArray *) readFaverateBusListFromLocalFile
{
    NSString *path = [FileManager fileCachesPath:@"FaverateBusList.plist"];
    return [[NSMutableArray alloc] initWithContentsOfFile:path];
}

- (void)buildLocalFileToArray:(NSMutableArray *) busLineArray total:(NSMutableArray *) busLineTotalArray
{
    NSMutableArray *busArray = [self readFaverateBusListFromLocalFile];
    [busLineArray removeAllObjects];
    [busLineTotalArray removeAllObjects];
    if (busArray) {
        for (NSInteger i=0; i < [busArray count]; i++) {
            NSData *data = [busArray objectAtIndex:i];
            BusLine *busLine = [BusLine unarchived:data];
            
            BOOL isExist = NO;
            for (BusLine *busLineOnly in busLineArray) {
                if ([busLine.lineNumber isEqualToString:busLineOnly.lineNumber]) {
                    isExist = YES;
                    break;
                }
            }
            if (!isExist) {
                [busLineArray addObject:busLine];
            }
            [busLineTotalArray addObject:busLine];
        }
    }
}

@end
