package com.app.shadow;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 31/10/18
 * @desc ...
 */
public class ShadowFrameLayout extends FrameLayout {
	private static final int[] COLOR_BACKGROUND_ATTR = new int[]{16842801};
	private static final ShadowViewImpl IMPL;
	
	static {
		if (Build.VERSION.SDK_INT >= 17) {
			IMPL = new ShadowViewApi17Impl();
		}
		else {
			IMPL = new ShadowViewBaseImpl();
		}
		
		IMPL.initStatic();
	}
	
	final Rect mContentPadding;
	final Rect mShadowBounds;
	private final ShadowViewDelegate mCardViewDelegate;
	int mUserSetMinWidth;
	int mUserSetMinHeight;
	private boolean mCompatPadding;
	private boolean mPreventCornerOverlap;
	
	public ShadowFrameLayout(@NonNull Context context) {
		this(context,(AttributeSet)null);
	}
	
	public ShadowFrameLayout(@NonNull Context context,@Nullable AttributeSet attrs) {
		this(context,attrs,R.attr.ShadowLayoutStyle);
	}
	
	public ShadowFrameLayout(
			@NonNull Context context,@Nullable AttributeSet attrs,int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
		this.mContentPadding = new Rect();
		this.mShadowBounds = new Rect();
		this.mCardViewDelegate = new ShadowViewDelegate() {
			private Drawable mCardBackground;
			
			public boolean getUseCompatPadding() {
				return ShadowFrameLayout.this.getUseCompatPadding();
			}
			
			public boolean getPreventCornerOverlap() {
				return ShadowFrameLayout.this.getPreventCornerOverlap();
			}
			
			public void setShadowPadding(int left,int top,int right,int bottom) {
				ShadowFrameLayout.this.mShadowBounds.set(left,top,right,bottom);
				ShadowFrameLayout.super
						.setPadding(left + ShadowFrameLayout.this.mContentPadding.left,
						            top + ShadowFrameLayout.this.mContentPadding.top,
						            right + ShadowFrameLayout.this.mContentPadding.right,
						            bottom + ShadowFrameLayout.this.mContentPadding.bottom);
			}
			
			public void setMinWidthHeightInternal(int width,int height) {
				if (width > ShadowFrameLayout.this.mUserSetMinWidth) {
					ShadowFrameLayout.super.setMinimumWidth(width);
				}
				
				if (height > ShadowFrameLayout.this.mUserSetMinHeight) {
					ShadowFrameLayout.super.setMinimumHeight(height);
				}
				
			}
			
			public Drawable getCardBackground() {
				return this.mCardBackground;
			}
			
			public void setCardBackground(Drawable drawable) {
				this.mCardBackground = drawable;
				ShadowFrameLayout.this.setBackgroundDrawable(drawable);
			}
			
			public View getCardView() {
				return ShadowFrameLayout.this;
			}
		};
		TypedArray a = context
				.obtainStyledAttributes(attrs,R.styleable.ShadowFrameLayout,defStyleAttr,
				                        R.style.ShadowLayoutStyle);
		ColorStateList backgroundColor;
		if (a.hasValue(R.styleable.ShadowFrameLayout_shadowBackgroundColor)) {
			backgroundColor = a
					.getColorStateList(R.styleable.ShadowFrameLayout_shadowBackgroundColor);
		}
		else {
			TypedArray aa = this.getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
			int themeColorBackground = aa.getColor(0,0);
			aa.recycle();
			float[] hsv = new float[3];
			Color.colorToHSV(themeColorBackground,hsv);
			backgroundColor = ColorStateList.valueOf(
					hsv[2] > 0.5F ? this.getResources().getColor(R.color.shadow_light_background)
							: this.getResources().getColor(R.color.shadow_dark_background));
		}
		
		float radius = a.getDimension(R.styleable.ShadowFrameLayout_shadowCornerRadius,0.0F);
		float elevation = a.getDimension(R.styleable.ShadowFrameLayout_shadowElevation,0.0F);
		float maxElevation = a.getDimension(R.styleable.ShadowFrameLayout_shadowMaxElevation,0.0F);
		
		int insetShadow = a.getDimensionPixelSize(R.styleable.ShadowFrameLayout_insetShadow,3);
		int shadowStartColor = a.getColor(R.styleable.ShadowFrameLayout_shadowStartColor,
		                                  getResources().getColor(R.color.shadow_start_color));
		int shadowEndColor = a.getColor(R.styleable.ShadowFrameLayout_shadowEndColor,
		                                getResources().getColor(R.color.shadow_end_color));
		
		this.mCompatPadding = a
				.getBoolean(R.styleable.ShadowFrameLayout_shadowUseCompatPadding,false);
		this.mPreventCornerOverlap = a
				.getBoolean(R.styleable.ShadowFrameLayout_shadowPreventCornerOverlap,true);
		int defaultPadding = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadowContentPadding,0);
		this.mContentPadding.left = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadowContentPaddingLeft,
				                       defaultPadding);
		this.mContentPadding.top = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadowContentPaddingTop,
				                       defaultPadding);
		this.mContentPadding.right = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadowContentPaddingRight,
				                       defaultPadding);
		this.mContentPadding.bottom = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadowContentPaddingBottom,
				                       defaultPadding);
		if (elevation > maxElevation) {
			maxElevation = elevation;
		}
		
		this.mUserSetMinWidth = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadow_android_minWidth,0);
		this.mUserSetMinHeight = a
				.getDimensionPixelSize(R.styleable.ShadowFrameLayout_shadow_android_minHeight,0);
		a.recycle();
		IMPL.initialize(this.mCardViewDelegate,backgroundColor,radius,elevation,maxElevation,
		                shadowStartColor,shadowEndColor,insetShadow);
	}
	
	public void setPadding(int left,int top,int right,int bottom) {
	}
	
	public void setPaddingRelative(int start,int top,int end,int bottom) {
	}
	
	public boolean getUseCompatPadding() {
		return this.mCompatPadding;
	}
	
	public void setUseCompatPadding(boolean useCompatPadding) {
		if (this.mCompatPadding != useCompatPadding) {
			this.mCompatPadding = useCompatPadding;
			IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
		}
		
	}
	
	public void setContentPadding(@Px int left,@Px int top,@Px int right,@Px int bottom) {
		this.mContentPadding.set(left,top,right,bottom);
		IMPL.updatePadding(this.mCardViewDelegate);
	}
	
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode;
		switch (widthMode) {
			case -2147483648:
			case 1073741824:
				heightMode = (int)Math.ceil((double)IMPL.getMinWidth(this.mCardViewDelegate));
				widthMeasureSpec = MeasureSpec
						.makeMeasureSpec(Math.max(heightMode,MeasureSpec.getSize(widthMeasureSpec)),
						                 widthMode);
			case 0:
			default:
				heightMode = MeasureSpec.getMode(heightMeasureSpec);
				switch (heightMode) {
					case -2147483648:
					case 1073741824:
						int minHeight = (int)Math
								.ceil((double)IMPL.getMinHeight(this.mCardViewDelegate));
						heightMeasureSpec = MeasureSpec.makeMeasureSpec(
								Math.max(minHeight,MeasureSpec.getSize(heightMeasureSpec)),
								heightMode);
					case 0:
					default:
						super.onMeasure(widthMeasureSpec,heightMeasureSpec);
				}
		}
	}
	
	public void setMinimumWidth(int minWidth) {
		this.mUserSetMinWidth = minWidth;
		super.setMinimumWidth(minWidth);
	}
	
	public void setMinimumHeight(int minHeight) {
		this.mUserSetMinHeight = minHeight;
		super.setMinimumHeight(minHeight);
	}
	
	public void setCardBackgroundColor(@ColorInt int color) {
		IMPL.setBackgroundColor(this.mCardViewDelegate,ColorStateList.valueOf(color));
	}
	
	@NonNull
	public ColorStateList getCardBackgroundColor() {
		return IMPL.getBackgroundColor(this.mCardViewDelegate);
	}
	
	public void setCardBackgroundColor(@Nullable ColorStateList color) {
		IMPL.setBackgroundColor(this.mCardViewDelegate,color);
	}
	
	@Px
	public int getContentPaddingLeft() {
		return this.mContentPadding.left;
	}
	
	@Px
	public int getContentPaddingRight() {
		return this.mContentPadding.right;
	}
	
	@Px
	public int getContentPaddingTop() {
		return this.mContentPadding.top;
	}
	
	@Px
	public int getContentPaddingBottom() {
		return this.mContentPadding.bottom;
	}
	
	public float getRadius() {
		return IMPL.getRadius(this.mCardViewDelegate);
	}
	
	public void setRadius(float radius) {
		IMPL.setRadius(this.mCardViewDelegate,radius);
	}
	
	public float getCardElevation() {
		return IMPL.getElevation(this.mCardViewDelegate);
	}
	
	public void setCardElevation(float elevation) {
		IMPL.setElevation(this.mCardViewDelegate,elevation);
	}
	
	public float getMaxCardElevation() {
		return IMPL.getMaxElevation(this.mCardViewDelegate);
	}
	
	public void setMaxCardElevation(float maxElevation) {
		IMPL.setMaxElevation(this.mCardViewDelegate,maxElevation);
	}
	
	public boolean getPreventCornerOverlap() {
		return this.mPreventCornerOverlap;
	}
	
	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		if (preventCornerOverlap != this.mPreventCornerOverlap) {
			this.mPreventCornerOverlap = preventCornerOverlap;
			IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
		}
		
	}
}

