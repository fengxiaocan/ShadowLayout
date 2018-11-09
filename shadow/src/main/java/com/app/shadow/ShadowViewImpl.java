package com.app.shadow;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 31/10/18
 * @desc ...
 */
interface ShadowViewImpl {
	void initialize(
			ShadowViewDelegate cardView,ColorStateList backgroundColor,float radius,float elevation,
			float maxElevation,@ColorInt int shadowStartColor,@ColorInt int shadowEndColor,
			int insetShadow);
	
	void setRadius(ShadowViewDelegate var1,float var2);
	
	float getRadius(ShadowViewDelegate var1);
	
	void setElevation(ShadowViewDelegate var1,float var2);
	
	float getElevation(ShadowViewDelegate var1);
	
	void initStatic();
	
	void setMaxElevation(ShadowViewDelegate var1,float var2);
	
	float getMaxElevation(ShadowViewDelegate var1);
	
	float getMinWidth(ShadowViewDelegate var1);
	
	float getMinHeight(ShadowViewDelegate var1);
	
	void updatePadding(ShadowViewDelegate var1);
	
	void onCompatPaddingChanged(ShadowViewDelegate var1);
	
	void onPreventCornerOverlapChanged(ShadowViewDelegate var1);
	
	void setBackgroundColor(ShadowViewDelegate var1,@Nullable ColorStateList var2);
	
	ColorStateList getBackgroundColor(ShadowViewDelegate var1);
}
