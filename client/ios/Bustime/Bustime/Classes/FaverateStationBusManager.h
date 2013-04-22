//
//  FaverateStationBusManager.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-9.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BusStation.h"

@interface FaverateStationBusManager : NSObject

- (BOOL)insertIntoFaverateWithStation:(BusStation *) busStation;
- (BOOL)isBusStationInFaverate:(NSString *) standName;
- (BOOL)deleteBusStationInFaverate:(NSString *) standName;

- (void)buildLocalFileToArray:(NSMutableArray *) stationArray total:(NSMutableArray *) stationTotalArray;

@end
