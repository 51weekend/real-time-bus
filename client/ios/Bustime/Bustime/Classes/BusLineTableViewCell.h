//
//  BusLineTableViewCell.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-1.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BusLineTableViewCell : UITableViewCell

@property(nonatomic, weak) IBOutlet UILabel *nameLabel;
@property(nonatomic, weak) IBOutlet UIImageView *iconView;
@property(nonatomic, weak) IBOutlet UILabel *stationLabel;

@end
