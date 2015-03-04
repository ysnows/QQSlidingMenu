package ysnow.ysnowsslidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

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
    private boolean isOpen;
    private boolean isclosed;

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //使用自定义属性的时候回调用这个
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //在自定义组件中使用自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);//获得属性数组
       mMenuWidth= (int) array.getDimension(R.styleable.SlidingMenu_menuSpadding, 420);

        //获得屏幕的宽度和计算设置的偏移量的像素值,并计算出menu的宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth=metrics.widthPixels;//得到屏幕的宽度(像素)
//      mMenuWidth= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMenuPadding,context.getResources().getDisplayMetrics());

        array.recycle();
    }

//在代码中new的时候
    public SlidingMenu(Context context) {
        this(context, null);
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

    public void openMenu() {
        if (isOpen) return;
        this.smoothScrollTo(0,0);
        isOpen=true;
    }

    public void closeMenu() {
        if (!isOpen)return;
        this.smoothScrollTo(mMenuWidth,0);
        isOpen=false;
    }

    /**
     * 打开和关闭菜单
     */
    public void toggleMenu() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale=l*1.0f/mMenuWidth;
        ViewHelper.setTranslationX(wrapperMenu,mMenuWidth*scale);


    }
}
