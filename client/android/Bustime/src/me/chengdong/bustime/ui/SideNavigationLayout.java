/***
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package me.chengdong.bustime.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * A custom FrameLayout that copies the side navigation UI design pattern found
 * in the YouTube app. It expects two child views to be added to it. The first
 * one will be the navigation view and the second one will be the content view.
 */
public class SideNavigationLayout extends FrameLayout {

    private static final int SCROLL_DURATION = 500;

    private Scroller mScroller;

    private int mOffsetX;

    private boolean mShowingNavigation;

    public SideNavigationLayout(Context context) {
        super(context);
        init();
    }

    public SideNavigationLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SideNavigationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    public void showNavigationView() {
        mScroller.startScroll((int) mOffsetX, 0, (int) (getNavigationViewWidth() - mOffsetX), SCROLL_DURATION);
        requestLayout();
        invalidate();
    }

    public void showContentView() {
        mScroller.startScroll((int) mOffsetX, 0, (int) -mOffsetX, 0, SCROLL_DURATION);
        requestLayout();
        invalidate();
    }

    public boolean isShowingNavigationView() {
        return mShowingNavigation;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.isFinished()) {
            return;
        }

        mScroller.computeScrollOffset();
        mOffsetX = mScroller.getCurrX();
        requestLayout();
        invalidate();

        if (mScroller.isFinished()) {
            mShowingNavigation = mOffsetX != 0;
            getChildAt(0).setVisibility(mShowingNavigation ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getChildCount() > 1) {
            View contentView = getChildAt(1);
            int childLeft = contentView.getLeft() + mOffsetX;
            int childTop = contentView.getTop();
            int childRight = contentView.getRight() + mOffsetX;
            int childBottom = contentView.getBottom();
            contentView.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    private int getNavigationViewWidth() {
        if (getChildCount() > 0) {
            return getChildAt(0).getWidth();
        }
        return 0;
    }
}
