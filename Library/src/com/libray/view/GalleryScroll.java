package com.libray.view;

import com.handmark.pulltorefresh.library.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


final public class GalleryScroll extends View {

	private int height = 0, width = 0, x = 0;
	private int count = 0;
	private Bitmap addOne = null, addTwo = null;
	private LayoutParams params = null;
	private Paint mPaint = null;
	private int position = 0;
	private Context context;

	public GalleryScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		addOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.banner_dian1);
		addTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.banner_dian2);
		height = addTwo.getHeight() + 10;
		x = addTwo.getWidth() + 10;
	}

	/**
	 * 设置指示图片
	 * 
	 * @author zhaolin
	 * @Desc: TODO
	 * @param hResId
	 *            高亮
	 * @param nResId
	 *            正常 void
	 */
	public void setPointDrawableResource(int hResId, int nResId) {
		addOne = BitmapFactory.decodeResource(context.getResources(), nResId);
		addTwo = BitmapFactory.decodeResource(context.getResources(), hResId);
	}

	/**
	 * 设置点之间的距离
	 * 
	 * @author zhaolin
	 * @Desc: TODO
	 * @param px
	 *            void
	 */
	public void setPointSpace(int px) {
		height = addTwo.getHeight() + px;
		x = addTwo.getWidth() + px;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mPaint == null) {
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(5);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
		}
		for (int i = 0; i < count; i++) {
			if (i == position) {
				canvas.drawBitmap(addTwo, i * x, 0, mPaint);
			} else {
				canvas.drawBitmap(addOne, i * x, 0, mPaint);
			}
		}
	}

	public void setNumber(int _count) {
		mPaint = null;
		count = _count;
		width = count * x;
		params = getLayoutParams();
		params.width = width;
		params.height = height;
		setLayoutParams(params);
		postInvalidate();
	}

	public void select(int _position) {
		position = _position;
		postInvalidate();
	}
}
