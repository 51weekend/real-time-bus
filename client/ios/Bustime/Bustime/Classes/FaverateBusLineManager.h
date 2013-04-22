//
//  FaverateHotelManager.h
//  JinJiangTravelPlus
//
//  Created by jerry on 13-3-8.
//  Copyright (c) 2013年 JinJiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BusLine.h"

@interface FaverateBusLineManager : NSObject

- (BOOL)insertIntoFaverateWithBusLine:(BusLine *) busLine;
- (BOOL)isBusLineInFaverate:(NSString *) lineNumber;
- (BOOL)deleteBusLineInFaverate:(NSString *) lineNumber;

- (void)buildLocalFileToArray:(NSMutableArray *) busLineArray total:(NSMutableArray *) busLineTotalArray;

@end