//
//  AboutViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-4-16.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "AboutViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface AboutViewController ()

@end

@implementation AboutViewController

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"联系我们页面";
	self.navigationItem.title = @"联系我们";
    [self loadInfoView];
	// Do any additional setup after loading the view.
}

- (void)loadInfoView
{    
    UIView *graphcisView = [[UIView alloc] initWithFrame:CGRectMake(0, 29, 320, 200)];
    graphcisView.backgroundColor = [UIColor clearColor];
    
    CALayer *subLayer = [CALayer layer];
    subLayer.backgroundColor = [UIColor whiteColor].CGColor;
    subLayer.shadowOffset = CGSizeMake(0, 1);
    subLayer.shadowRadius = 5.0;
    subLayer.shadowColor = [UIColor blackColor].CGColor;
    subLayer.shadowOpacity = 0.5;
    subLayer.frame = CGRectMake(10, 5, 300, 190);
    subLayer.cornerRadius = 10;
    subLayer.borderWidth = 0;
    [graphcisView.layer addSublayer:subLayer];
    [graphcisView addSubview:self.infoView];
    [self.view addSubview:graphcisView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
