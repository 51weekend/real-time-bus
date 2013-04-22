//
//  BusDetailViewController.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-2.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BannerViewController.h"
#import "BusSingleLineParser.h"

@interface BusDetailViewController : BannerViewController<GDataParserDelegate>

@property(nonatomic, weak) IBOutlet UIView *topTitleView;
@property(nonatomic, strong) UIView *bottomView;
@property(nonatomic, strong) UIScrollView *subScrollView;
@property(nonatomic, strong) NSMutableArray *busLineArray;

@property(nonatomic, weak) IBOutlet UILabel *busNumber;
@property(nonatomic, weak) IBOutlet UILabel *timeLabel;
@property(nonatomic, weak) IBOutlet UILabel *totalStationLabel;
@property(nonatomic, weak) IBOutlet UILabel *startStationLabel;
@property(nonatomic, weak) IBOutlet UILabel *endStationLabel;



@end
