//
//  HistoryInfoViewController.h
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BannerViewController.h"

@interface HistoryInfoViewController : BannerViewController<UITableViewDataSource, UITableViewDelegate>

@property(nonatomic, weak) IBOutlet UITableView *busLineTableView;
@property(nonatomic, weak) IBOutlet UITableView *stationTableView;

@end
