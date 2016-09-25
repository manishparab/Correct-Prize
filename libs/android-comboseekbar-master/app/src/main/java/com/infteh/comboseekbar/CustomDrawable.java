package com.infteh.comboseekbar;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.infteh.comboseekbar.ComboSeekBar.Dot;

/**
 * seekbar background with text on it.
 * 
 * @author sazonov-adm
 * 
 */
public class CustomDrawable extends Drawable {

	private final Drawable myBase;
	private final Paint textUnselected;
	private float mThumbRadius;
	/**
	 * paints.
	 */
	private final Paint unselectLinePaint;
	private List<Dot> mDots;
	private Paint selectLinePaint;
	private Paint circleLinePaint;
	private int mDotRadius;
	private Paint textSelected;
	private int mTextSize;
	private float mTextMargin;
	private int mTextHeight;
	private boolean mIsMultiline;
	private int mSelectedLineHeight;
	private int mUnselectedLineHeight;

	public CustomDrawable(Drawable base, ComboSeekBar slider,
			float thumbRadius, List<Dot> dots, int color, int textSize,
			int selectedLineHeight, int unselectedLineHeight, int dotRadius,
			boolean isMultiline) {
		mIsMultiline = isMultiline;
		myBase = base;
		mDots = dots;
		mTextSize = textSize;
		mSelectedLineHeight = selectedLineHeight;
		mUnselectedLineHeight = unselectedLineHeight;
		mDotRadius = dotRadius;
		textUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textUnselected.setColor(color);
		textUnselected.setAlpha(255);

		textSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textSelected.setTypeface(Typeface.DEFAULT_BOLD);
		textSelected.setColor(color);
		textSelected.setAlpha(255);

		mThumbRadius = thumbRadius;

		unselectLinePaint = new Paint();
		unselectLinePaint.setColor(color);

		unselectLinePaint.setStrokeWidth(mUnselectedLineHeight);

		selectLinePaint = new Paint();
		selectLinePaint.setColor(color);
		selectLinePaint.setStrokeWidth(mSelectedLineHeight);

		circleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circleLinePaint.setColor(color);

		Rect textBounds = new Rect();
		textSelected.setTextSize((int) (mTextSize * 2));
		textSelected.getTextBounds("M", 0, 1, textBounds);

		textUnselected.setTextSize(mTextSize);
		textSelected.setTextSize(mTextSize);

		mTextHeight = textBounds.height();
		// mDotRadius = toPix(DOT_RADIUS);
		mTextMargin = 3;
	}

	@Override
	protected final void onBoundsChange(Rect bounds) {
		myBase.setBounds(bounds);
	}

	@Override
	protected final boolean onStateChange(int[] state) {
		invalidateSelf();
		return false;
	}

	@Override
	public final boolean isStateful() {
		return true;
	}

	@Override
	public final void draw(Canvas canvas) {
		int height = this.getIntrinsicHeight() / 2;
		if (mDots.size() == 0) {
			canvas.drawLine(0, height, getBounds().right, height,
					unselectLinePaint);
			return;
		}
		for (Dot dot : mDots) {
			drawText(canvas, dot, dot.mX, height);
			if (dot.isSelected) {
				canvas.drawLine(mDots.get(0).mX, height, dot.mX, height,
						selectLinePaint);
				canvas.drawLine(dot.mX, height, mDots.get(mDots.size() - 1).mX,
						height, unselectLinePaint);
			}
			canvas.drawCircle(dot.mX, height, mDotRadius, circleLinePaint);
		}
	}

	/**
	 * @param canvas
	 *            canvas.
	 * @param dot
	 *            current dot.
	 * @param x
	 *            x cor.
	 * @param y
	 *            y cor.
	 */
	private void drawText(Canvas canvas, Dot dot, float x, float y) {
		final Rect textBounds = new Rect();
		textSelected.getTextBounds(dot.text, 0, dot.text.length(), textBounds);
		float xres;
		if (dot.id == (mDots.size() - 1)) {
			xres = getBounds().width() - textBounds.width();
		} else if (dot.id == 0) {
			xres = 0;
		} else {
			xres = x - (textBounds.width() / 2);
		}

		float yres;
		// Если многострочный текст
		if (mIsMultiline) {
			// Если четная точка, то сверху
			if ((dot.id % 2) == 0) {
				yres = y - mTextMargin - mDotRadius;
			} else {
				yres = y + mTextHeight;
			}
		} else {
			yres = y - (mDotRadius * 2) + mTextMargin;
		}

		if (dot.isSelected) {
			canvas.drawText(dot.text, xres, yres, textSelected);
		} else {
			canvas.drawText(dot.text, xres, yres, textUnselected);
		}
	}

	@Override
	public final int getIntrinsicHeight() {
		if (mIsMultiline) {
			return (int) (selectLinePaint.getStrokeWidth() + mDotRadius
					+ (mTextHeight) * 2 + mTextMargin);
		} else {
			int thumbHeight = (int) mThumbRadius * 2;
			int dotHeight = (int) mDotRadius * 2;
			return Math.max(thumbHeight, dotHeight);
			// return (int) (mThumbRadius * 2);
			// return (int) (mThumbRadius + mTextMargin + mTextHeight +
			// mDotRadius);
		}
	}

	@Override
	public final int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}