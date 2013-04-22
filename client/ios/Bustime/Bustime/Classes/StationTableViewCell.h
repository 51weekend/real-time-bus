//
//  StationTableViewCell.h
//  Bustime
//
//  Created by 汪君瑞 on 13-4-5.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StationTableViewCell : UITableViewCell

@property(nonatomic, weak) IBOutlet UILabel *stationLabel;
@property(nonatomic, weak) IBOutlet UIImageView *iconView;
@property(nonatomic, weak) IBOutlet UILabel *areaLabel;

@end
