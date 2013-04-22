//
//  PagePushSegue.m
//  Bustime
//
//  Created by 汪君瑞 on 13-3-31.
//  Copyright (c) 2013年 Jerry Wang. All rights reserved.
//

#import "PagePushSegue.h"

@implementation PagePushSegue

- (void)perform
{
    UIViewController *current = [self sourceViewController];
    UIViewController *next = [self destinationViewController];
    
    [UIView
     transitionWithView:current.navigationController.view
     duration:0.8
     options:UIViewAnimationOptionTransitionCurlUp
     animations:^{
         [current.navigationController pushViewController:next animated:NO];
     } completion:NULL];
}

@end
