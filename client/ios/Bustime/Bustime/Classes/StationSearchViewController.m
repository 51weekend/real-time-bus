//
//  StationSearchViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "StationSearchViewController.h"
#import "StationTableViewCell.h"
#import "BusStation.h"
#import "StationBusViewController.h"
#import "ValidateInputUtil.h"

@interface StationSearchViewController ()

@property (nonatomic, strong) NSMutableArray *stationArray;
@property (nonatomic, strong) NSMutableArray *stationTotalArray;
@property (nonatomic, strong) BusStationParser *busStationParser;

@property (nonatomic, strong) UIControl *touchView;

@end

@implementation StationSearchViewController

- (id)initWithCoder:(NSCoder *)aDecoder{
    self = [super initWithCoder:aDecoder];
    if (self) {
        _stationArray = [[NSMutableArray alloc] initWithCapacity:10];
        _stationTotalArray = [[NSMutableArray alloc] initWithCapacity:10];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationItem.title = @"站点查询";
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

- (void)downloadData
{
    if (self.busStationParser != nil) {
        [self.busStationParser cancel];
        self.busStationParser = nil;
    }
    self.busStationParser = [[BusStationParser alloc] init];
    self.busStationParser.serverAddress = [ServerAddressManager serverAddress:@"query_bus_station"];
    self.busStationParser.requestString = [NSString stringWithFormat:@"stationName=%@",self.queryField.text];
    self.busStationParser.delegate = self;
    [self.busStationParser start];
    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeGradient];
}

- (IBAction)searchButtonTapped:(id)sender
{
    [self.queryField resignFirstResponder];
    [self.touchView removeFromSuperview];
    
    if ([ValidateInputUtil isNotEmpty:self.queryField.text fieldCName:@"查询站点"]) {
        //GA跟踪搜索按钮
        [[GAI sharedInstance].defaultTracker sendEventWithCategory:@"站点查询" withAction:@"用户点击" withLabel:@"查询按钮" withValue:nil];
        [self downloadData];
    }
}

- (void)closeTouchView
{
    [self.queryField resignFirstResponder];
    [self.touchView removeFromSuperview];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.destinationViewController isKindOfClass:[StationBusViewController class]]) {
        StationBusViewController *stationBusViewController = (StationBusViewController *)segue.destinationViewController;
        
        BusStation *busStation = [self.stationArray objectAtIndex:[[self.tableView indexPathForSelectedRow] row]];
        
        NSMutableArray *doubleArray = [[NSMutableArray alloc] initWithCapacity:2];
        for (BusStation *tmpBusStation in self.stationTotalArray) {
            if ([tmpBusStation.standName isEqualToString:busStation.standName]) {
                [doubleArray addObject:tmpBusStation];
            }
        }
        stationBusViewController.stationArray = doubleArray;
    }
    
}

#pragma mark - UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.stationArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    StationTableViewCell *cell = (StationTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"StationTableViewCell"];
    cell.iconView.clipsToBounds = YES;
    
    BusStation *busStation = [self.stationArray objectAtIndex:[indexPath row]];
    cell.stationLabel.text = busStation.standName;
    cell.areaLabel.text = busStation.area;
    [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
    //cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;
    
    return cell;
}

#pragma mark -
#pragma mark BaseJSONParserDelegate
- (void)parser:(GDataParser*)parser DidFailedParseWithMsg:(NSString*)msg errCode:(NSInteger)code
{
    NSLog(@"查询站点信息发生异常：%@，错误代码：%d", msg, code);
}

- (void)parser:(GDataParser*)parser DidParsedData:(NSDictionary *)data
{
    self.stationArray = [data valueForKey:@"data"];
    self.stationTotalArray = [data valueForKey:@"stationTotalArray"];
    [self.tableView reloadData];
    [SVProgressHUD dismiss];
}

#pragma mark - UITextFieldDelegate
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [self.view addSubview:self.touchView];
}

@end
