//
//  BusLineViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusLineViewController.h"
#import "BusLineTableViewCell.h"
#import "BusLine.h"
#import "BusDetailViewController.h"
#import "REMenu.h"
#import "ValidateInputUtil.h"

@interface BusLineViewController ()

@property (strong, nonatomic) REMenu *menu;
@property (nonatomic, strong) NSMutableArray *busLineArray;
@property (nonatomic, strong) NSMutableArray *busLineTotalArray;

@property (nonatomic, strong) BusLineParser *busLineParser;
@property (nonatomic, strong) UIControl *touchView;

@end

@implementation BusLineViewController

- (id)initWithCoder:(NSCoder *)aDecoder{
    self = [super initWithCoder:aDecoder];
    if (self) {
        _busLineArray = [[NSMutableArray alloc] initWithCapacity:10];
        _busLineTotalArray = [[NSMutableArray alloc] initWithCapacity:10];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"线路查询页面";
    self.navigationItem.title = @"线路查询";
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"苏州" style:UIBarButtonItemStyleBordered target:self action:@selector(showMenu)];
    [self loadCustomBanner];
    
    self.touchView = [[UIControl alloc] initWithFrame:self.tableView.frame];
    [self.touchView addTarget:self action:@selector(closeTouchView) forControlEvents:UIControlEventTouchUpInside];
    self.touchView.backgroundColor = [UIColor blackColor];
    self.touchView.alpha = 0.8;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self customizeNavBar];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)searchButtonTapped:(id)sender
{
    [self.queryField resignFirstResponder];
    [self.touchView removeFromSuperview];
    
    if ([ValidateInputUtil isNotEmpty:self.queryField.text fieldCName:@"查询线路"]) {
        //GA跟踪搜索按钮
        [[GAI sharedInstance].defaultTracker sendEventWithCategory:@"线路查询" withAction:@"用户点击" withLabel:@"查询按钮" withValue:nil];
        [self downloadData];
    }
}

- (void)closeTouchView
{
    [self.queryField resignFirstResponder];
    [self.touchView removeFromSuperview];
}

- (void)downloadData
{
    if (self.busLineParser != nil) {
        [self.busLineParser cancel];
        self.busLineParser = nil;
    }
    self.busLineParser = [[BusLineParser alloc] init];
    self.busLineParser.serverAddress = [ServerAddressManager serverAddress:@"query_bus_line"];
    self.busLineParser.requestString = [NSString stringWithFormat:@"lineNumber=%@",self.queryField.text];
    self.busLineParser.delegate = self;
    [self.busLineParser start];
    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeGradient];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.destinationViewController isKindOfClass:[BusDetailViewController class]]) {
        BusDetailViewController *busDetailViewController = (BusDetailViewController *)segue.destinationViewController;
        
        BusLine *busLine = [self.busLineArray objectAtIndex:[[self.tableView indexPathForSelectedRow] row]];
        
        NSMutableArray *doubleArray = [[NSMutableArray alloc] initWithCapacity:2];
        for (BusLine *tmpBusLine in self.busLineTotalArray) {
            if ([tmpBusLine.lineNumber isEqualToString:busLine.lineNumber]) {
                [doubleArray addObject:tmpBusLine];
            }
        }
        busDetailViewController.busLineArray = doubleArray;
    }
    
}

#pragma mark - UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.busLineArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    BusLineTableViewCell *cell = (BusLineTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"BusLineTableViewCell"];
    cell.iconView.clipsToBounds = YES;
    
    BusLine *busLine = [self.busLineArray objectAtIndex:[indexPath row]];
    
    NSString *regexString = @"^[0-9]*$";

    BOOL matched = [busLine.lineNumber isMatchedByRegex:regexString];
    if (matched) {
        cell.nameLabel.text =[NSString stringWithFormat:@"%@路", busLine.lineNumber];
    } else {
        cell.nameLabel.text = busLine.lineNumber;
    }
    
    cell.stationLabel.text = [NSString stringWithFormat:@"%@ - %@", busLine.startStation, busLine.endStation];
    [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
    //cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;
    
    return cell;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}

#pragma mark -
#pragma mark BaseJSONParserDelegate
- (void)parser:(GDataParser*)parser DidFailedParseWithMsg:(NSString*)msg errCode:(NSInteger)code
{
    NSLog(@"查询线路信息发生异常：%@，错误代码：%d", msg, code);
}

- (void)parser:(GDataParser*)parser DidParsedData:(NSDictionary *)data
{
    self.busLineArray = [data valueForKey:@"data"];
    self.busLineTotalArray = [data valueForKey:@"busLineArry"];
    [self.tableView reloadData];
    [SVProgressHUD dismiss];
}

#pragma mark - UITextFieldDelegate
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [self.view addSubview:self.touchView];
}

- (void)showMenu
{
//    if (_menu.isOpen)
//        return [_menu close];
    REMenuItem *homeItem = [[REMenuItem alloc] initWithTitle:@"Home"
                                                    subtitle:@"Return to Home Screen"
                                                       image:[UIImage imageNamed:@"Icon_Home"]
                                            highlightedImage:nil
                                                      action:^(REMenuItem *item) {
                                                          NSLog(@"Item: %@", item);
                                                      }];
    
    REMenuItem *exploreItem = [[REMenuItem alloc] initWithTitle:@"Explore"
                                                       subtitle:@"Explore 47 additional options"
                                                          image:[UIImage imageNamed:@"Icon_Explore"]
                                               highlightedImage:nil
                                                         action:^(REMenuItem *item) {
                                                             NSLog(@"Item: %@", item);
                                                         }];
    
    REMenuItem *activityItem = [[REMenuItem alloc] initWithTitle:@"Activity"
                                                        subtitle:@"Perform 3 additional activities"
                                                           image:[UIImage imageNamed:@"Icon_Activity"]
                                                highlightedImage:nil
                                                          action:^(REMenuItem *item) {
                                                              NSLog(@"Item: %@", item);
                                                          }];
    
    REMenuItem *profileItem = [[REMenuItem alloc] initWithTitle:@"Profile"
                                                          image:[UIImage imageNamed:@"Icon_Profile"]
                                               highlightedImage:nil
                                                         action:^(REMenuItem *item) {
                                                             NSLog(@"Item: %@", item);
                                                         }];
    
    homeItem.tag = 0;
    exploreItem.tag = 1;
    activityItem.tag = 2;
    profileItem.tag = 3;
    
    _menu = [[REMenu alloc] initWithItems:@[homeItem, exploreItem, activityItem, profileItem]];
    _menu.cornerRadius = 4;
    _menu.shadowColor = [UIColor blackColor];
    _menu.shadowOffset = CGSizeMake(0, 1);
    _menu.shadowOpacity = 1;
    _menu.imageOffset = CGSizeMake(5, -1);
    
    //[_menu showFromNavigationController:self.navigationController];
}

@end
