package com.indatus.smoothseekbar.library;

//  Created by jonstaff on 10/8/13.

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

public class SmoothSeekBar extends SeekBar implements ValueAnimator.AnimatorUpdateListener, View.OnLayoutChangeListener {

	public static final String TAG = SmoothSeekBar.class.getSimpleName();

	private Paint mPaintFill, mPaintBg;

	private Rectangle mRectFill;

	private int mSegments = 100; // default value for progress bars
	private int mStartTime, mEndTime;
	private double mCurrentPercentage = 0;

	private ObjectAnimator mAnimator;
	private static final LinearInterpolator sLinearInterpolator = new LinearInterpolator();

	private int mViewWidth, mHeight;

	private OnSeekBarChangeListener mSeekListener;

	// thumb attributes
	private Drawable mThumb;
	private int mThumbWidth, mThumbHeight;

	//      ____                _                   _
	//     / ___|___  _ __  ___| |_ _ __ _   _  ___| |_ ___  _ __ ___
	//    | |   / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \| '__/ __|
	//    | |__| (_) | | | \__ \ |_| |  | |_| | (__| || (_) | |  \__ \
	//     \____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___/|_|  |___/

	public SmoothSeekBar(Context context) {
		this(context, null);
	}

	public SmoothSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SmoothSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mPaintFill = new Paint();
		mPaintBg = new Paint();

		int fill = Color.CYAN;
		int bg = Color.DKGRAY;

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmoothSeekBar);

		try {
			fill = a.getColor(R.styleable.SmoothSeekBar_progressFillColor, Color.CYAN);
			bg = a.getColor(R.styleable.SmoothSeekBar_progressBackgroundColor, Color.DKGRAY);
            mThumb = a.getDrawable(R.styleable.SmoothSeekBar_thumbDrawable);

            if (mThumb == null) {
                mThumb = new ColorDrawable(a.getColor(R.styleable.SmoothSeekBar_thumbDrawable, 0));
            }
			//            mHeight = a.getDimension(); // TODO: set this up to work from XML
		}
		catch (NullPointerException e) {
			Log.w(TAG, "NullPointerException thrown on line 64.");
			e.printStackTrace();
		}
		finally {
			a.recycle();
		}

		mPaintFill.setColor(fill);
		mPaintBg.setColor(bg);

		mViewWidth = 0;
		mRectFill = new Rectangle(0, 0, 0, 0);

		addOnLayoutChangeListener(this);
	}

	//        _
	//       / \   ___ ___ ___  ___ ___  ___  _ __ ___
	//      / _ \ / __/ __/ _ \/ __/ __|/ _ \| '__/ __|
	//     / ___ \ (_| (_|  __/\__ \__ \ (_) | |  \__ \
	//    /_/   \_\___\___\___||___/___/\___/|_|  |___/

	/**
	 * Sets the end time of the audio playing (total duration).
	 * 
	 * @param time
	 *            the end time in milliseconds
	 */
	public void setEndTime(int time) {
		mEndTime = time;
	}

	/**
	 * Sets the start time of the audio playing. Use this to start playback partially through a clip. By default, this is zero.
	 * 
	 * @param time
	 *            the start time in milleseconds
	 */
	public void setStartTime(int time) {
		mCurrentPercentage = time / mEndTime;
	}

	/**
	 * Sets the OnSeekBarChangeListener for the seekbar. Implement this interface to get the callbacks.
	 * 
	 * @param listener
	 *            OnSeekBarChangeListener
	 */
	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
		super.setOnSeekBarChangeListener(listener);
		mSeekListener = listener;
	}

	/**
	 * Returns the default height of the seekbar fill. This value will be in pixels. If onLayout() has not yet been called, this method will return zero.
	 * 
	 * @return the default height in pixels of the seekbar
	 */
	public int getDefaultHeight() {
		return mHeight;
	}

	/**
	 * Sets the default height of the seekbar fill. This value should be in device specific pixels. If not called, the seekbar will match parent.
	 * 
	 * @param height
	 *            the height in device specific pixels of the seekbar fill
	 */
	public void setDefaultHeight(int height) {
		mHeight = height;
	}

	/**
	 * Returns the current fill color of the seekbar.
	 * 
	 * @return int color
	 */
	public int getFillColor() {
		return mPaintFill.getColor();
	}

	/**
	 * Sets the fill color of the seekbar.
	 * 
	 * @param color
	 *            int color
	 */
	public void setFillColor(int color) {
		mPaintFill.setColor(color);
	}

	/**
	 * Returns the current background color of the seekbar.
	 * 
	 * @return int color
	 */
	public int getBgColor() {
		return mPaintBg.getColor();
	}

	/**
	 * Sets the background color of the seekbar.
	 * 
	 * @param color
	 *            int color
	 */
	public void setBgColor(int color) {
		mPaintBg.setColor(color);
	}

	//      ___                      _     _
	//     / _ \__   _____ _ __ _ __(_) __| | ___  ___
	//    | | | \ \ / / _ \ '__| '__| |/ _` |/ _ \/ __|
	//    | |_| |\ V /  __/ |  | |  | | (_| |  __/\__ \
	//     \___/  \_/ \___|_|  |_|  |_|\__,_|\___||___/

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);

		float complete = (float) progress / getMax();
		int right = (int) (complete * getWidth());

		if (mRectFill != null) {
			mRectFill.set(0, 0, right, getHeight());
			updateToPercentage(complete);
		}
	}

	@Override
	public synchronized void setMax(int max) {
		super.setMax(max);
		mSegments = max;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRect(0, 0, getWidth(), getHeight(), mPaintBg);
		canvas.drawRect(mRectFill, mPaintFill);
	}

	//      ____ _                 __  __      _   _               _
	//     / ___| | __ _ ___ ___  |  \/  | ___| |_| |__   ___   __| |___
	//    | |   | |/ _` / __/ __| | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
	//    | |___| | (_| \__ \__ \ | |  | |  __/ |_| | | | (_) | (_| \__ \
	//     \____|_|\__,_|___/___/ |_|  |_|\___|\__|_| |_|\___/ \__,_|___/

	/**
	 * This method is used for starting the animation of the seekbar. The height of the seekbar is set by default as match parent. Use the setDefaultHeight() to
	 * change this.
	 */
	public void beginAnimating() {
		// this method should start the animation process for the view
		// first, get the value of the start time and the end time
		// second, calculate the total duration of the animation
		// third, animate the expansion of the view from the current position to the max position

		mStartTime = (int) (mCurrentPercentage * mEndTime);

		if (mHeight > 0) {
			mRectFill.setBottom(mHeight); // TODO: fix this to use an attribute dimension
			mAnimator = ObjectAnimator.ofFloat(mRectFill, "right", (float) (mCurrentPercentage * mViewWidth), mViewWidth);
			mAnimator.setDuration(mEndTime - mStartTime);
			mAnimator.setInterpolator(sLinearInterpolator);
			mAnimator.addUpdateListener(this);
			mAnimator.start();
		}
	}

	/**
	 * Call this method to pause the animation, say while seeking or when a pause button is clicked. This does not destroy the animator.
	 */
	public void pauseAnimating() {
		mAnimator.cancel();
		mCurrentPercentage = mRectFill.getRight() / mViewWidth;
	}

	/**
	 * Call this method to end the animation completely. This destroys the animator and sets the seekbar back to progress of 0.
	 */
	public void endAnimation() {
		if (mAnimator != null) {
			mAnimator.end();
		}

		mCurrentPercentage = 0;
	}

	/**
	 * Call this method to update the seekbar to a specified percentage. Values should be between 0 and 1.
	 * 
	 * @param percentage
	 *            double value for the amount the seekbar should be filled
	 */
	public void updateToPercentage(double percentage) {
		mCurrentPercentage = percentage;
		mRectFill.setRight((float) (mCurrentPercentage * mViewWidth));
		invalidate();
	}

	//     ___       _             __                  ___                 _                           _        _   _
	//    |_ _|_ __ | |_ ___ _ __ / _| __ _  ___ ___  |_ _|_ __ ___  _ __ | | ___ _ __ ___   ___ _ __ | |_ __ _| |_(_) ___  _ __  ___
	//     | || '_ \| __/ _ \ '__| |_ / _` |/ __/ _ \  | || '_ ` _ \| '_ \| |/ _ \ '_ ` _ \ / _ \ '_ \| __/ _` | __| |/ _ \| '_ \/ __|
	//     | || | | | ||  __/ |  |  _| (_| | (_|  __/  | || | | | | | |_) | |  __/ | | | | |  __/ | | | || (_| | |_| | (_) | | | \__ \
	//    |___|_| |_|\__\___|_|  |_|  \__,_|\___\___| |___|_| |_| |_| .__/|_|\___|_| |_| |_|\___|_| |_|\__\__,_|\__|_|\___/|_| |_|___/
	//                                                              |_|

	/**
	 * ValueAnimator.AnimatorUpdateListener implementation. This method gets called every time fill rect animates. It sets the current percentage and makes a
	 * callback to the seek listener to notate that the progress has changed.
	 * 
	 * @param valueAnimator
	 *            whichever ValueAnimator is using this interface
	 */
	@Override
	public void onAnimationUpdate(ValueAnimator valueAnimator) {
		invalidate();
		mCurrentPercentage = mRectFill.getRight() / mViewWidth;
		try {
			mSeekListener.onProgressChanged(this, (int) (mCurrentPercentage * mSegments), false);
		}
		catch (NullPointerException e) {
			Log.w(TAG, "Implement the OnSeekBarChangeListener to fix this warning.");
			e.printStackTrace();
		}
	}

	/**
	 * Read the docs for View.OnLayoutChangeListener for more details. This method is used to measure the height of the view, and the values must therefore be
	 * set after onLayout() has been called.
	 */
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		if (mViewWidth == 0) {
			mViewWidth = right;
		}

		if (mHeight == 0) {
			mHeight = bottom;
			beginAnimating();
		}
	}
}
