package ysnow.ysnowsslidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by ysnow on 2015/3/3.
 */
public class SlidingMenu  extends HorizontalScrollView{
      private int mScreenWidth;
      private int mMenuWidth;
      private int mMenuPadding=220;


    private ViewGroup wrapperMenu;
    private ViewGroup wrapperContent;
    private boolean isSetted=false;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获得屏幕的宽度和计算设置的偏移量的像素值,并计算出menu的宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth=metrics.widthPixels;//得到屏幕的宽度(像素)
        mMenuWidth= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMenuPadding,context.getResources().getDisplayMetrics());

    }

    //在onmessure中设置子menu和content的宽度--->并在onlayout中设置初始的scroll位置-->在onTouchEvent中
    //设置滑动


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isSetted) {
            //得到里面的控件
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            wrapperMenu = (ViewGroup) wrapper.getChildAt(0);
            wrapperContent = (ViewGroup) wrapper.getChildAt(1);

            wrapperMenu.getLayoutParams().width = mMenuWidth;
            wrapperContent.getLayoutParams().width = mScreenWidth;
            isSetted=true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            this.scrollTo(mMenuWidth,0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                //隐藏在左边的距离
               int scrollX= getScrollX();
                if (scrollX <= mMenuWidth / 2) {
                    //显示菜单
                    this.smoothScrollTo(0,0);
                } else {
                    //隐藏菜单
                    this.smoothScrollTo(mMenuWidth,0);
                }

return true;
        }
        return super.onTouchEvent(ev);
    }
}
