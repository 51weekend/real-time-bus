//
//  BusLine.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-1.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusLine.h"
#import "ValidateInputUtil.h"

@implementation BusLine

+ (BusLine *)unarchived:(NSData *) data
{
    BusLine *tempLine = (BusLine *)[NSKeyedUnarchiver unarchiveObjectWithData:data];
    tempLine.stationArray = [[NSMutableArray alloc] initWithCapacity:10];
    return tempLine;
}

- (id) initWithDictionary : (NSDictionary *) dict
{
    if (dict == nil) {
        self = [super init];
        _stationArray = [[NSMutableArray alloc] initWithCapacity:10];
        return self;
    }
    
    if (self = [super init]) {
        
        self.lineNumber = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"lineNumber"]];
        self.lineCode = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"lineGuid"]];
        self.totalStation = [[dict objectForKey:@"totalStation"] intValue];
        self.startStation = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"startStation"]];
        self.endStation = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"endStation"]];
        self.runTime = [ValidateInputUtil valueOfObjectToString:[dict objectForKey:@"runTime"]];
        _stationArray = [[NSMutableArray alloc] initWithCapacity:10];
    }
    return self;
}

- (NSData *)archived
{
    return [NSKeyedArchiver archivedDataWithRootObject:self];
}

#pragma mark - NSCoding

- (id)initWithCoder:(NSCoder *)aDecoder
{
    if (self = [super init])
    {
        self.lineNumber = [aDecoder decodeObjectForKey:@"lineNumber"];
        self.lineCode = [aDecoder decodeObjectForKey:@"lineCode"];
        self.totalStation = [aDecoder decodeIntegerForKey:@"totalStation"];
        self.startStation = [aDecoder decodeObjectForKey:@"startStation"];
        self.endStation = [aDecoder decodeObjectForKey:@"endStation"];
        self.runTime = [aDecoder decodeObjectForKey:@"runTime"];
    }
    
    return self;
}

-(void)encodeWithCoder:(NSCoder *)aCoder
{
    [aCoder encodeObject:self.lineNumber forKey:@"lineNumber"];
    [aCoder encodeObject:self.lineCode forKey:@"lineCode"];
    [aCoder encodeInteger:self.totalStation forKey:@"totalStation"];
    [aCoder encodeObject:self.startStation forKey:@"startStation"];
    [aCoder encodeObject:self.endStation forKey:@"endStation"];
    [aCoder encodeObject:self.runTime forKey:@"runTime"];
}

#pragma mark - NSCopying

- (id)copyWithZone:(NSZone *)zone
{
    BusLine *busLine = [[self class] allocWithZone:zone];
    self.lineNumber = [self.lineNumber copyWithZone:zone];
    self.lineCode = [self.lineCode copyWithZone:zone];
    self.totalStation = self.totalStation;
    self.startStation = [self.startStation copyWithZone:zone];
    self.endStation = [self.endStation copyWithZone:zone];
    self.runTime = [self.runTime copyWithZone:zone];
    return busLine;
}

@end
