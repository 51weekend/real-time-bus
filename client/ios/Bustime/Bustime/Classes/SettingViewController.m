//
//  SettingViewController.m
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "SettingViewController.h"
#import "PrettyKit.h"

@interface SettingViewController ()

@end

@implementation SettingViewController

- (id)initWithCoder:(NSCoder *)aDecoder{
    self = [super initWithCoder:aDecoder];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"系统设置页面";
	self.navigationItem.title = @"设置";
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view datasource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 4;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if (section == 0) {
        return @"主题设置";
    }
    if (section == 1) {
        return @"数据管理";
    }
    if (section == 2) {
        return @"软件信息";
    }
    if (section == 3) {
        return @"联系我们";
    }
    
    return @"";
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if (section == 0) {
        return 1;
    }
    if (section == 1) {
        return 1;
    }
    if (section == 2) {
        return 3;
    }
    if (section == 3) {
        return 1;
    }

    
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    static NSString *SegmentedCellIdentifier = @"SegmentedCell";
    PrettySegmentedControlTableViewCell *segmentedCell;
    
    PrettyTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[PrettyTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.tableViewBackgroundColor = tableView.backgroundColor;
    }
    //下降阴影
    cell.dropsShadow = NO;
    //圆弧
    cell.cornerRadius = 10;
    //选中行背景色
    cell.selectionGradientStartColor = RGBCOLOR(231, 231, 231);
    cell.selectionGradientEndColor = RGBCOLOR(231, 231, 231);
    
    switch (indexPath.section) {
        case 0:
        {
            switch (indexPath.row) {
                case 0:
                {
                    segmentedCell = [tableView dequeueReusableCellWithIdentifier:SegmentedCellIdentifier];
                    if (segmentedCell == nil) {
                        segmentedCell = [[PrettySegmentedControlTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:SegmentedCellIdentifier];
                    }
                    //下降阴影
                    segmentedCell.dropsShadow = NO;
                    //圆弧
                    segmentedCell.cornerRadius = 10;
                    
                    [segmentedCell prepareForTableView:tableView indexPath:indexPath];
                    segmentedCell.titles = [NSArray arrayWithObjects:@"红色主题", @"蓝色主题", nil];
                    segmentedCell.tableViewBackgroundColor = tableView.backgroundColor;
                    //segmentedCell.selectedIndex = 1;
                    NSString *styleColor = [[NSUserDefaults standardUserDefaults] stringForKey:kNavigationBarDefaultColor];
                    if ([styleColor isEqualToString:kNavigationBarRedColor]) {
                        segmentedCell.selectedIndex = 0;
                    } else {
                        segmentedCell.selectedIndex = 1;
                    }
                    
                    segmentedCell.actionBlock = ^(NSIndexPath *indexPath, int selectedIndex) {
                        
                        //GA跟踪搜索按钮
                        [[GAI sharedInstance].defaultTracker sendEventWithCategory:@"系统主题设置" withAction:@"用户点击" withLabel:@"主题设置按钮" withValue:nil];
                        
                        if (selectedIndex == 0) {
                            [[NSUserDefaults standardUserDefaults] setObject:kNavigationBarRedColor forKey:kNavigationBarDefaultColor];
                        } else {
                            [[NSUserDefaults standardUserDefaults] setObject:kNavigationBarblueColor forKey:kNavigationBarDefaultColor];
                        }
                        [[NSUserDefaults standardUserDefaults] synchronize];
                        [self customizeNavBar];
                    };
                    return segmentedCell;
                }
                default:
                    break;
            }
            break;
        }
        case 1:
        {
            [cell prepareForTableView:tableView indexPath:indexPath];
            cell.textLabel.text = @"当前城市";
            UILabel *cityLabel = [[UILabel alloc] initWithFrame:CGRectMake(140, 10, 60, 20)];
            cityLabel.textColor = [UIColor blueColor];
            cityLabel.font = [UIFont systemFontOfSize:18];
            cityLabel.text = @"苏州";
            cityLabel.backgroundColor = [UIColor clearColor];
            [cell.contentView addSubview:cityLabel];
            [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
            return cell;
        }
        case 2:
        {
            switch (indexPath.row) {
                case 0:
                {
                    [cell prepareForTableView:tableView indexPath:indexPath];
                    cell.textLabel.text = @"实时公交查询系统1.0版(苏州版)";
                    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
                    return cell;
                }
                case 1:
                {
                    [cell prepareForTableView:tableView indexPath:indexPath];
                    cell.textLabel.text = @"给我好评";
                    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(270, 15, 7, 10)];
                    imageView.image = [UIImage imageNamed:@"line-next.png"];
                    [cell.contentView addSubview:imageView];
                    [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
                    return cell;
                }
                case 2:
                {
                    [cell prepareForTableView:tableView indexPath:indexPath];
                    cell.textLabel.text = @"分享我们";
                    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(270, 15, 7, 10)];
                    imageView.image = [UIImage imageNamed:@"line-next.png"];
                    [cell.contentView addSubview:imageView];
                    [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
                    return cell;
                }
                default:
                    break;
            }
            break;
        }
        
        case 3:
        {
            [cell prepareForTableView:tableView indexPath:indexPath];
            cell.textLabel.text = @"联系我们";
            UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(270, 15, 7, 10)];
            imageView.image = [UIImage imageNamed:@"line-next.png"];
            [cell.contentView addSubview:imageView];
            [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
            return cell;
        }
        default:
            break;
    }
    
    return cell;
}


#pragma mark - Table view delegate

-(CGFloat) tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return tableView.rowHeight + [PrettyTableViewCell tableView:tableView neededHeightForIndexPath:indexPath];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 2) {
        PrettyTableViewCell *cell = (PrettyTableViewCell *)[tableView cellForRowAtIndexPath:indexPath];
        [cell setSelected:NO animated:YES];
    }
    
    if (indexPath.section == 3) {
        PrettyTableViewCell *cell = (PrettyTableViewCell *)[tableView cellForRowAtIndexPath:indexPath];
        [cell setSelected:NO animated:YES];
        [self performSegueWithIdentifier:@"aboutUs" sender:nil];
    }
}

@end
