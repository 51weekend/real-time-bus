//
//  BannerViewController.h
//  ChoiceCourse
//
//  Created by jerry on 13-3-28.
//
//

#import <UIKit/UIKit.h>
#import "SampleConstants.h"
#import "GADBannerViewDelegate.h"
#import "GADBannerView.h"
#import "GADRequest.h"
#import "GAI.h"

@interface BannerViewController : GAITrackedViewController<GADBannerViewDelegate>

@property(nonatomic, strong) GADBannerView *adBanner;

- (void)loadCustomBanner;
- (GADRequest *)createRequest;
- (UIButton *)generateNavButton:(NSString *) imageName action:(SEL) actionName;

- (void)addLeftBarButton:(NSString *) imageName;
- (void)backHome: (id) sender;
- (void)addRightBarButton:(UIButton *) button;

- (void)customizeNavBar;

@end
