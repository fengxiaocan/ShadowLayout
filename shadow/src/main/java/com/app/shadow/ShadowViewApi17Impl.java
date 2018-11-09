package com.app.shadow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 31/10/18
 * @desc ...
 */
class ShadowViewApi17Impl extends ShadowViewBaseImpl {
	ShadowViewApi17Impl() {
	}
	
	public void initStatic() {
		RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
			public void drawRoundRect(Canvas canvas,RectF bounds,float cornerRadius,Paint paint) {
				canvas.drawRoundRect(bounds,cornerRadius,cornerRadius,paint);
			}
		};
	}
}
