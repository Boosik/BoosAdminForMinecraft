package cz.boosik.boosadminforminecraft.app.components;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Custom view pager allowing to disable swipe paging on target pages
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class CustomViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    /**
     * Default constructor
     *
     * @param context Context of view pager
     */
    public CustomViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor with attributes
     *
     * @param context Context of view pager
     * @param attrs   Target attributes
     */
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    /**
     * Sets the pagingEnabled
     *
     * @param b The pagingEnabled
     */
    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
