//
//  BusDetailViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-2.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "BusDetailViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "BusLine.h"
#import "FaverateBusLineManager.h"

@interface BusDetailViewController ()

@property(nonatomic, strong) BusSingleLineParser *busSingleLineParser;
@property(nonatomic, strong) BusLine *currentBusLine;
@property(nonatomic, assign) BOOL isFirst;
@property(nonatomic, strong) NSMutableArray *busSingleStationArry;
@property(nonatomic, strong) FaverateBusLineManager *faverateBusLineManager;

@property(nonatomic, assign) BOOL isFaverate;
@property(nonatomic, strong) UIButton *faverateButton;

@end

@implementation BusDetailViewController

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        _isFirst = YES;
        _busSingleStationArry = [[NSMutableArray alloc] initWithCapacity:20];
        _faverateBusLineManager = [[FaverateBusLineManager alloc] init];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"运行车俩详情页面";
    [self loadDefaultPageView];
    
    self.subScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    self.subScrollView.backgroundColor = [UIColor clearColor];
    
    [self loadSegmentedButton];
    [self loadTopTitleView];
    self.subScrollView.scrollEnabled = YES;
    self.subScrollView.showsHorizontalScrollIndicator = NO;
    self.subScrollView.showsVerticalScrollIndicator = NO;
    [self.view addSubview:self.subScrollView];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self customizeNavBar];
}

- (void)downloadDataForBusStation
{
    if (self.isFirst) {
        [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeGradient];
    }
    
    if (self.busSingleLineParser != nil) {
        [self.busSingleLineParser cancel];
        self.busSingleLineParser = nil;
    }
    self.busSingleLineParser = [[BusSingleLineParser alloc] init];
    self.busSingleLineParser.serverAddress = [ServerAddressManager serverAddress:@"query_bus_single_line"];
    self.busSingleLineParser.delegate = self;
    self.busSingleLineParser.requestString = [NSString stringWithFormat:@"lineCode=%@",self.currentBusLine.lineCode];
    [self.busSingleLineParser start];
}


- (void)loadDefaultPageView
{
    self.currentBusLine = self.busLineArray[0];
    self.isFaverate = [self.faverateBusLineManager isBusLineInFaverate:self.currentBusLine.lineNumber];
    
    if (self.isFaverate)
    {
        self.faverateButton = [self generateNavButton:@"heart_icon_red.png"  action:@selector(faverateButtonClicked:)];
    }
    else
    {
        self.faverateButton = [self generateNavButton:@"heart_icon.png" action:@selector(faverateButtonClicked:)];
    }
    [self addRightBarButton:self.faverateButton];
    
    [self loadBusBaseInfo:self.currentBusLine];
    [self downloadDataForBusStation];
}

- (void)loadBusBaseInfo:(BusLine *) busLine
{
    NSString *regexString = @"^[0-9]*$";
    BOOL matched = [self.currentBusLine.lineNumber isMatchedByRegex:regexString];
    if (matched) {
        self.busNumber.text =[NSString stringWithFormat:@"%@路", self.currentBusLine.lineNumber];
    } else {
        self.busNumber.text = self.currentBusLine.lineNumber;
    }
    self.timeLabel.text = [NSString stringWithFormat:@" 首末班时间：%@", busLine.runTime];
    self.totalStationLabel.text = [NSString stringWithFormat:@"全程%d站", busLine.totalStation];
    self.startStationLabel.text = busLine.startStation;
    self.endStationLabel.text = busLine.endStation;
}


- (void)loadSegmentedButton
{
    // segmented control as the custom title view
	NSArray *segmentTextContent = [NSArray arrayWithObjects: @"上行", @"下行", nil];
	UISegmentedControl *segmentedControl = [[UISegmentedControl alloc] initWithItems:segmentTextContent];
	segmentedControl.selectedSegmentIndex = 0;
	segmentedControl.autoresizingMask = UIViewAutoresizingFlexibleWidth;
	segmentedControl.segmentedControlStyle = UISegmentedControlStyleBar;
	segmentedControl.frame = CGRectMake(0, 0, 100, kCustomButtonHeight);
	[segmentedControl addTarget:self action:@selector(segmentAction:) forControlEvents:UIControlEventValueChanged];
    
	self.navigationItem.titleView = segmentedControl;
    //self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:segmentedControl];
}

- (void)loadTopTitleView
{
    self.topTitleView.backgroundColor = [UIColor clearColor];
    UIView *topView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 100)];
    topView.backgroundColor = [UIColor clearColor];
    
    CALayer *subLayer = [CALayer layer];
    subLayer.backgroundColor = [UIColor whiteColor].CGColor;
    subLayer.shadowOffset = CGSizeMake(0, 1);
    subLayer.shadowRadius = 5.0;
    subLayer.shadowColor = [UIColor blackColor].CGColor;
    subLayer.shadowOpacity = 0.5;
    subLayer.frame = CGRectMake(10, 5, 300, 95);
    subLayer.cornerRadius = 10;
    subLayer.borderWidth = 0;
    [topView.layer addSublayer:subLayer];
    [topView addSubview:self.topTitleView];
    [self.subScrollView addSubview:topView];
}

- (void)loadBottomView:(int) index
{
    [self.bottomView removeFromSuperview];
    self.bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, 100, 320, 800)];
    self.bottomView.backgroundColor = [UIColor clearColor];
    
    UIView *graphcisView = [self graphcisStationViews:index];
    self.bottomView.frame = CGRectMake(0, 100, 320, graphcisView.frame.size.height);
    CALayer *subLayer = [CALayer layer];
    subLayer.backgroundColor = [UIColor whiteColor].CGColor;
    subLayer.shadowOffset = CGSizeMake(0, 1);
    subLayer.shadowRadius = 5.0;
    subLayer.shadowColor = [UIColor blackColor].CGColor;
    subLayer.shadowOpacity = 0.5;
    subLayer.frame = CGRectMake(10, 10, 300, self.bottomView.frame.size.height-20);
    subLayer.cornerRadius = 10;
    subLayer.borderWidth = 0;
    [self.bottomView.layer addSublayer:subLayer];
    
    [self.bottomView addSubview:graphcisView];
    [self.subScrollView addSubview:self.bottomView];
    [self.subScrollView setContentSize:CGSizeMake(320, 110 + self.bottomView.frame.size.height + 80)];
    [self.subScrollView setContentOffset:CGPointMake(0.0, 0.0) animated:YES];
}


- (UIView *)graphcisStationViews:(int) index
{
    BusLine *bline = self.busLineArray[index];
    
    
    CGFloat totalHeight = 0;
    UIView *graphcisView = [[UIView alloc] initWithFrame:CGRectMake(20, 20, 300, 800)];
    graphcisView.backgroundColor = [UIColor clearColor];
    
    for (int i=0; i< [bline.stationArray count]; i++) {
        BusSingleLine *singStation = [bline.stationArray objectAtIndex:i];
        
        if (singStation.time != nil && ![singStation.time isEqualToString:@""]) {
            
            UIView *startView = [[UIView alloc] initWithFrame:CGRectMake(17, i*79, 10, 51)];
            startView.backgroundColor = RGBCOLOR(100, 182, 57);
            
            UIImageView *imageStartView = [[UIImageView alloc] initWithFrame:CGRectMake(15, i*79, 13, 51)];
            imageStartView.image = [UIImage imageNamed:@"start_icon.png"];
            if (i==0) {
                [graphcisView addSubview:imageStartView];
            } else {
                [graphcisView addSubview:startView];
            }
            
            UIImageView *imageStationView = [[UIImageView alloc] initWithFrame:CGRectMake(6, 51+(i*79), 30, 30)];
            imageStationView.image = [UIImage imageNamed:@"run_current_station.png"];
            [graphcisView addSubview:imageStationView];
            
            UIImageView *imageTopView = [[UIImageView alloc] initWithFrame:CGRectMake(49, 21+(i*79), 221, 39)];
            imageTopView.image = [UIImage imageNamed:@"current_top.png"];
            [graphcisView addSubview:imageTopView];
            
            UIImageView *imageBottomView = [[UIImageView alloc] initWithFrame:CGRectMake(49, 60+(i*79), 221, 35)];
            imageBottomView.image = [UIImage imageNamed:@"current_bottom.png"];
            [graphcisView addSubview:imageBottomView];
            
            UILabel *stationLabel = [[UILabel alloc] initWithFrame:CGRectMake(60, 32+(i*79), 197, 21)];
            stationLabel.backgroundColor = [UIColor clearColor];
            stationLabel.font = [UIFont boldSystemFontOfSize:17];
            stationLabel.textColor = [UIColor whiteColor];
            stationLabel.text = singStation.standName;
            [graphcisView addSubview:stationLabel];
            
            UILabel *stationTimeLabel = [[UILabel alloc] initWithFrame:CGRectMake(60, 66+(i*79), 197, 18)];
            stationTimeLabel.backgroundColor = [UIColor clearColor];
            stationTimeLabel.font = [UIFont systemFontOfSize:14];
            stationTimeLabel.text = [NSString stringWithFormat:@"进站时间  %@",singStation.time];
            [graphcisView addSubview:stationTimeLabel];
            
            totalHeight = 51+(i*79) + 80;
            
        } else {
            UIView *startView = [[UIView alloc] initWithFrame:CGRectMake(17, i*79, 10, 51)];
            startView.backgroundColor = RGBCOLOR(100, 182, 57);
            
            UIImageView *imageStartView = [[UIImageView alloc] initWithFrame:CGRectMake(15, i*79, 13, 51)];
            imageStartView.image = [UIImage imageNamed:@"start_icon.png"];
            if (i==0) {
                [graphcisView addSubview:imageStartView];
            } else {
                [graphcisView addSubview:startView];
            }
            
            UIImageView *imageStationView = [[UIImageView alloc] initWithFrame:CGRectMake(7, 51+(i*79), 30, 30)];
            imageStationView.image = [UIImage imageNamed:@"run_station.png"];
            [graphcisView addSubview:imageStationView];
            
            UILabel *stationLabel = [[UILabel alloc] initWithFrame:CGRectMake(49, 51+(i*79)+6, 221, 18)];
            stationLabel.backgroundColor = [UIColor clearColor];
            stationLabel.font = [UIFont boldSystemFontOfSize:14];
            stationLabel.text = singStation.standName;
            [graphcisView addSubview:stationLabel];
            totalHeight = 51+(i*79) + 80;
        }
    }
    graphcisView.frame = CGRectMake(20, 20, 300, totalHeight);
    
    return graphcisView;
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
    if ([self.busLineArray count] > 1) {
        BusLine *busLine = self.busLineArray[segmentedControl.selectedSegmentIndex];
        [self loadBusBaseInfo:busLine];
        [self loadBottomView:segmentedControl.selectedSegmentIndex];
    } else {
        BusLine *busLine = self.busLineArray[0];
        [self loadBusBaseInfo:busLine];
        [self loadBottomView:0];
    }
}

- (void)faverateButtonClicked:(id) sender
{
    BusLine *busLine = self.busLineArray[0];
    if (self.isFaverate) {
        [self.faverateBusLineManager deleteBusLineInFaverate:busLine.lineNumber];
        [self.faverateButton setImage:[UIImage imageNamed:@"heart_icon.png"] forState:UIControlStateNormal];
    } else {
        [self.faverateBusLineManager insertIntoFaverateWithBusLine:busLine];
        if ([self.busLineArray count] > 1) {
            [self.faverateBusLineManager insertIntoFaverateWithBusLine:self.busLineArray[1]];
        }
        [self.faverateButton setImage:[UIImage imageNamed:@"heart_icon_red.png"] forState:UIControlStateNormal];
    }
    self.isFaverate = !self.isFaverate;
}

- (void) mergeDataFromArry
{    
    if (self.isFirst) {
        BusLine *bline = self.busLineArray[0];
        [bline.stationArray removeAllObjects];
        [bline.stationArray addObjectsFromArray:self.busSingleStationArry];
        self.isFirst = NO;
        [self loadBottomView:0];
        [SVProgressHUD dismiss];
 
        if ([self.busLineArray count] > 1) {
            self.currentBusLine = self.busLineArray[1];
            [self downloadDataForBusStation];
        }
        
    } else {
        BusLine *bline = self.busLineArray[1];
        [bline.stationArray removeAllObjects];
        [bline.stationArray addObjectsFromArray:self.busSingleStationArry];
    }
}

#pragma mark - BaseJSONParserDelegate
- (void)parser:(GDataParser*)parser DidFailedParseWithMsg:(NSString*)msg errCode:(NSInteger)code
{
    NSLog(@"查询运行车辆发生异常：%@，错误代码：%d", msg, code);
}

- (void)parser:(GDataParser*)parser DidParsedData:(NSDictionary *)data
{
    if ([parser isKindOfClass:[BusSingleLineParser class]]) {
        self.busSingleStationArry = [data valueForKey:@"data"];
        [self mergeDataFromArry];
    } 
}

@end
