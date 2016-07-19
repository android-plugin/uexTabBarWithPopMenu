package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;


public class BadgeView extends ImageView {

    private boolean mHideOnNull = true;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        int radiusPx = EUExUtil.dipToPixels(3);
        canvas.drawCircle(radiusPx, radiusPx, radiusPx, paint);
    }

    /*
         * Attach the BadgeView to the target view
         *
         * @param target the view to attach the BadgeView
         */
    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        if (target == null) {
            return;
        }

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);

        } else if (target.getParent() instanceof ViewGroup) {
            // use a new Framelayout container for adding badge
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();
            badgeContainer.setLayoutParams(parentLayoutParams);
            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(target);

            int radius = EUExUtil.dipToPixels(3);
            LayoutParams layoutParams = new LayoutParams(radius * 2, radius * 2, Gravity.RIGHT | Gravity.TOP);
            layoutParams.leftMargin = radius;
            badgeContainer.addView(this, layoutParams);
        } else if (target.getParent() == null) {
            Log.e(getClass().getSimpleName(), "ParentView is needed");
        }
    }

    public void remove() {
        ViewGroup parent = (ViewGroup)getParent();

        ViewGroup.LayoutParams params = parent.getLayoutParams();
        //获取外层的
        ViewGroup group = (ViewGroup) getParent().getParent();
        FrameLayout frameLayout = (FrameLayout) this.getParent();
        group.removeView(frameLayout);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            parent.removeView(child);
            ViewGroup.LayoutParams params2 = child.getLayoutParams();
            if (! (child instanceof BadgeView)) {
                group.addView(child, i, params2 );
            }
        }
    }

    /*
     * converts dip to px
     */
    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}