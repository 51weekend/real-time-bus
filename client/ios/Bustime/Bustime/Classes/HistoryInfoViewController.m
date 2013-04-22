//
//  HistoryInfoViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "HistoryInfoViewController.h"
#import "PrettyNavigationBar.h"
#import "FaverateBusLineManager.h"
#import "FaverateStationBusManager.h"
#import "BusLineTableViewCell.h"
#import "StationTableViewCell.h"
#import "BusDetailViewController.h"
#import "StationBusViewController.h"

#define kBusLineTag 100
#define kStationTag 101

@interface HistoryInfoViewController ()

@property(nonatomic, strong) FaverateBusLineManager *faverateBusLineManager;
@property (nonatomic, strong) NSMutableArray *busLineArray;
@property (nonatomic, strong) NSMutableArray *busLineTotalArray;

@property(nonatomic, strong) FaverateStationBusManager *faverateStationBusManager;
@property (nonatomic, strong) NSMutableArray *stationArray;
@property (nonatomic, strong) NSMutableArray *stationTotalArray;

@end

@implementation HistoryInfoViewController

- (id)initWithCoder:(NSCoder *)aDecoder{
    self = [super initWithCoder:aDecoder];
    if (self) {
        _faverateBusLineManager = [[FaverateBusLineManager alloc] init];
        _busLineArray = [[NSMutableArray alloc] initWithCapacity:10];
        _busLineTotalArray = [[NSMutableArray alloc] initWithCapacity:10];
        
        _faverateStationBusManager = [[FaverateStationBusManager alloc] init];
        _stationArray = [[NSMutableArray alloc] initWithCapacity:10];
        _stationTotalArray = [[NSMutableArray alloc] initWithCapacity:10];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"我的收藏页面";
	self.navigationItem.title = @"我的收藏";
    [self loadSegmentedButton];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"删除" style:UIBarButtonItemStyleBordered target:self action:@selector(toggleEdit:)];
    
	[self loadCustomBanner];
}

- (void)downloadData
{
    [self.faverateBusLineManager buildLocalFileToArray:self.busLineArray total:self.busLineTotalArray];
    [self.faverateStationBusManager buildLocalFileToArray:self.stationArray total:self.stationTotalArray];
    
    [self.busLineTableView reloadData];
    [self.stationTableView reloadData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self customizeNavBar];
    [self downloadData];
}

- (void)loadSegmentedButton
{
    
    UINavigationBar *navBar = [[UINavigationBar alloc] initWithFrame:CGRectMake(0, 0, 320, 48)];
    // segmented control as the custom title view
	NSArray *segmentTextContent = [NSArray arrayWithObjects:
                                   NSLocalizedString(@"收藏线路", @""),
                                   NSLocalizedString(@"收藏站点", @""),
								   nil];
	UISegmentedControl *segmentedControl = [[UISegmentedControl alloc] initWithItems:segmentTextContent];
	segmentedControl.selectedSegmentIndex = 0;
	segmentedControl.autoresizingMask = UIViewAutoresizingFlexibleWidth;
	segmentedControl.segmentedControlStyle = UISegmentedControlStyleBar;
	segmentedControl.frame = CGRectMake(100, 6, 140, kCustomButtonHeight);
	[segmentedControl addTarget:self action:@selector(segmentAction:) forControlEvents:UIControlEventValueChanged];
        
    [navBar addSubview:segmentedControl];
    UIView *topView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 40)];
    
    [topView addSubview:navBar];
    [self.view addSubview:topView];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)segmentAction:(id)sender
{
	// The segmented control was clicked, handle it here
	UISegmentedControl *segmentedControl = (UISegmentedControl *)sender;
    if (segmentedControl.selectedSegmentIndex == 0) {
        self.busLineTableView.hidden = NO;
        self.stationTableView.hidden = YES;
    } else {
        self.busLineTableView.hidden = YES;
        self.stationTableView.hidden = NO;
    }
}

- (void)toggleEdit:(id)sender {
    [self.busLineTableView setEditing:!self.busLineTableView.editing animated:YES];
    [self.stationTableView setEditing:!self.stationTableView.editing animated:YES];
    
    if (self.busLineTableView.editing)
        [self.navigationItem.rightBarButtonItem setTitle:@"完成"];
    else
        [self.navigationItem.rightBarButtonItem setTitle:@"删除"];
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"faverateToBusDetail"])
    {
        BusDetailViewController *busDetailViewController = (BusDetailViewController *)segue.destinationViewController;
        BusLine *busLine = [self.busLineArray objectAtIndex:[[self.busLineTableView indexPathForSelectedRow] row]];
        
        NSMutableArray *doubleArray = [[NSMutableArray alloc] initWithCapacity:2];
        for (BusLine *tmpBusLine in self.busLineTotalArray) {
            if ([tmpBusLine.lineNumber isEqualToString:busLine.lineNumber]) {
                NSLog(@"lineCode:%@, lineNumber:%@", tmpBusLine.lineCode, tmpBusLine.lineNumber);
                [doubleArray addObject:tmpBusLine];
            }
        }
        busDetailViewController.busLineArray = doubleArray;
    } else if ([segue.identifier isEqualToString:@"faverateToStationBus"]) {
        StationBusViewController *stationBusViewController = (StationBusViewController *)segue.destinationViewController;
        
        BusStation *busStation = [self.stationArray objectAtIndex:[[self.stationTableView indexPathForSelectedRow] row]];
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

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSUInteger row = [indexPath row];
    if (tableView.tag == kBusLineTag) {
        BusLine *busLine = [self.busLineArray objectAtIndex:[indexPath row]];
        [self.busLineArray removeObjectAtIndex:row];
        
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath]
                         withRowAnimation:UITableViewRowAnimationAutomatic];
        
        [self.faverateBusLineManager deleteBusLineInFaverate:busLine.lineNumber];
    } else {
        BusStation *busStation = [self.stationArray objectAtIndex:[indexPath row]];
        [self.stationArray removeObjectAtIndex:row];
        
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath]
                         withRowAnimation:UITableViewRowAnimationAutomatic];
        [self.faverateStationBusManager deleteBusStationInFaverate:busStation.standName];
    }
    
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (tableView.tag == kBusLineTag) {
        return [self.busLineArray count];
    } else {
        return [self.stationArray count];
    }
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (tableView.tag == kBusLineTag) {
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
    } else {
        StationTableViewCell *cell = (StationTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"StationTableViewCell"];
        cell.iconView.clipsToBounds = YES;
        
        BusStation *busStation = [self.stationArray objectAtIndex:[indexPath row]];
        cell.stationLabel.text = busStation.standName;
        cell.areaLabel.text = busStation.area;
        [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
        //cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;
        
        return cell;
    }
    
}

@end
