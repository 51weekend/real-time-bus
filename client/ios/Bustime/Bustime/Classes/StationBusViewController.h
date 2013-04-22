//
//  StationBusViewController.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-6.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BannerViewController.h"
#import "BusLineParser.h"

@interface StationBusViewController : BannerViewController<UITableViewDataSource, UITableViewDelegate, GDataParserDelegate>

@property(nonatomic, strong) NSMutableArray *stationArray;
@property(nonatomic, weak) IBOutlet UITableView *tableView;

@end
