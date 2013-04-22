//
//  StationSearchViewController.h
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BannerViewController.h"
#import "BusStationParser.h"

@interface StationSearchViewController : BannerViewController<UITableViewDataSource, UITableViewDelegate, GDataParserDelegate, UITextFieldDelegate>

@property(nonatomic, weak) IBOutlet UITableView *tableView;
@property(nonatomic, weak) IBOutlet UITextField *queryField;

- (IBAction)searchButtonTapped:(id)sender;

@end
