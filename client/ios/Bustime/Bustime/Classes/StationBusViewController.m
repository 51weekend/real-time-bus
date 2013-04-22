//
//  StationBusViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-6.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "StationBusViewController.h"
#import "BusLineTableViewCell.h"
#import "BusLine.h"
#import "BusStation.h"
#import "BusDetailViewController.h"
#import "FaverateStationBusManager.h"

@interface StationBusViewController ()

@property (nonatomic, strong) NSMutableArray *busLineArray;
@property (nonatomic, strong) NSMutableArray *busLineTotalArray;
@property(nonatomic, strong) BusLineParser *busLineParser;
@property(nonatomic, assign) BOOL isFirst;
@property(nonatomic, strong) FaverateStationBusManager *faverateStationBusManager;
@property(nonatomic, assign) BOOL isFaverate;
@property(nonatomic, strong) UIButton *faverateButton;

@end

@implementation StationBusViewController

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        _busLineArray = [[NSMutableArray alloc] initWithCapacity:10];
        _busLineTotalArray = [[NSMutableArray alloc] initWithCapacity:10];
        _faverateStationBusManager = [[FaverateStationBusManager alloc] init];
        _isFirst = YES;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"站点关联线路查询页面";
    self.navigationItem.title = @"站点查询";
    [self loadDefaultPageView];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self customizeNavBar];
}

- (void)loadDefaultPageView
{
    BusStation *busStation = [self.stationArray objectAtIndex:0];
    self.isFaverate = [self.faverateStationBusManager isBusStationInFaverate:busStation.standName];
    
    if (self.isFaverate)
    {
        self.faverateButton = [self generateNavButton:@"heart_icon_red.png"  action:@selector(faverateButtonClicked:)];
    }
    else
    {
        self.faverateButton = [self generateNavButton:@"heart_icon.png" action:@selector(faverateButtonClicked:)];
    }
    [self addRightBarButton:self.faverateButton];
    if ([self.stationArray count] >0) {
        BusStation *busStation = [self.stationArray objectAtIndex:0];
        [self downloadData:busStation.standCode];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)downloadData:(NSString *) stationCode
{
    if (self.busLineParser != nil) {
        [self.busLineParser cancel];
        self.busLineParser = nil;
    }
    self.busLineParser = [[BusLineParser alloc] init];
    self.busLineParser.serverAddress = [ServerAddressManager serverAddress:@"query_station_line"];
    self.busLineParser.requestString = [NSString stringWithFormat:@"stationCode=%@", stationCode];
    self.busLineParser.delegate = self;
    [self.busLineParser start];
    
    if (self.isFirst) {
        [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeGradient];
    }
}

- (void)faverateButtonClicked:(id) sender
{
    BusStation *busStation = [self.stationArray objectAtIndex:0];
    if (self.isFaverate) {
        [self.faverateStationBusManager deleteBusStationInFaverate:busStation.standName];
        [self.faverateButton setImage:[UIImage imageNamed:@"heart_icon.png"] forState:UIControlStateNormal];
    } else {
        [self.faverateStationBusManager insertIntoFaverateWithStation:busStation];
        if ([self.stationArray count] > 1) {
            [self.faverateStationBusManager insertIntoFaverateWithStation:[self.stationArray objectAtIndex:1]];
        }
        [self.faverateButton setImage:[UIImage imageNamed:@"heart_icon_red.png"] forState:UIControlStateNormal];
    }
    self.isFaverate = !self.isFaverate;
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

#pragma mark - Table view delegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 54;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}

#pragma mark - BaseJSONParserDelegate
- (void)parser:(GDataParser*)parser DidFailedParseWithMsg:(NSString*)msg errCode:(NSInteger)code
{
    NSLog(@"查询站点运行车辆信息发生异常：%@，错误代码：%d", msg, code);
}

- (void)parser:(GDataParser*)parser DidParsedData:(NSDictionary *)data
{
    if (self.isFirst) {
        self.isFirst = NO;
        self.busLineArray = [data valueForKey:@"data"];
        self.busLineTotalArray = [data valueForKey:@"busLineArry"];
        [self.tableView reloadData];
        
        if ([self.stationArray count] > 1) {
            BusStation *busStation = [self.stationArray objectAtIndex:1];
            [self downloadData:busStation.standCode];
        } else {
            [SVProgressHUD dismiss];
        }
        
    } else {
        [self.busLineTotalArray addObjectsFromArray:[data valueForKey:@"busLineArry"]];
        [SVProgressHUD dismiss];
    }
    
}

@end
