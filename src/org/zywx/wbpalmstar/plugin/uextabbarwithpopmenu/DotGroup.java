package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

/**
 * Created by fred on 16/7/14.
 */
public class DotGroup extends LinearLayout {
    private int count;
    private int checkedIndex = 0;
    private int radius = 4;

    private int indicatorColor = Color.GRAY;
    private int indicatorSelectColor = Color.BLACK;

    public DotGroup(Context context) {
        super(context);
    }

    public DotGroup(Context context, int count, String indicatorColor, String indicatorSelectColor) {
        super(context);
        this.count = count;
        try {
            this.indicatorColor = Color.parseColor(indicatorColor);
            this.indicatorSelectColor = Color.parseColor(indicatorSelectColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        render(checkedIndex);
    }

    private void render(int checkedIndex) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(EUExUtil.dipToPixels(radius * 2), EUExUtil.dipToPixels(radius * 2));
        params.leftMargin = EUExUtil.dipToPixels(3);
        if(count > 1) {
            for (int i = 0; i < count; i ++ ) {
                if (i == checkedIndex) {
                    addView(new SolidCircle(getContext(), indicatorSelectColor), params);
                } else {
                    addView(new SolidCircle(getContext(), indicatorColor), params);
                }
            }
        }
    }
    public void setChecked(int position) {
        removeAllViews();
        render(position);
    }



    class SolidCircle extends View {
        private int circleColor;
        public SolidCircle(Context context, int color) {
            super(context);
            this.circleColor = color;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(circleColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            int radiusPx = EUExUtil.dipToPixels(radius);
            canvas.drawCircle(radiusPx, radiusPx, radiusPx, paint);
        }
    }
    class EmptyCircle extends View {

        public EmptyCircle(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            int radiusPx = EUExUtil.dipToPixels(radius);
            canvas.drawCircle(radiusPx, radiusPx, radiusPx, paint);
        }
    }
}
