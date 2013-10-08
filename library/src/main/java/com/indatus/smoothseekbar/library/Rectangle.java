package com.indatus.smoothseekbar.library;

//  Created by jonstaff on 10/8/13.

import android.graphics.Rect;
import android.graphics.RectF;

public class Rectangle extends RectF {
	private static final String TAG = Rectangle.class.getSimpleName();

	private float mLeft, mTop, mRight, mBottom;

	//     ____                _                   _
	//    / ___|___  _ __  ___| |_ _ __ _   _  ___| |_ ___  _ __ ___
	//   | |   / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \| '__/ __|
	//   | |__| (_) | | | \__ \ |_| |  | |_| | (__| || (_) | |  \__ \
	//    \____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___/|_|  |___/

	public Rectangle() {

	}

	public Rectangle(float left, float top, float right, float bottom) {
		super(left, top, right, bottom);

		mLeft = left;
		mTop = top;
		mRight = right;
		mBottom = bottom;
	}

	public Rectangle(RectF r) {
		super(r);

		mLeft = r.left;
		mTop = r.top;
		mRight = r.right;
		mBottom = r.bottom;
	}

	public Rectangle(Rect r) {
		super(r);

		mLeft = r.left;
		mTop = r.top;
		mRight = r.right;
		mBottom = r.bottom;
	}

	//        _
	//       / \   ___ ___ ___  ___ ___  ___  _ __ ___
	//      / _ \ / __/ __/ _ \/ __/ __|/ _ \| '__/ __|
	//     / ___ \ (_| (_|  __/\__ \__ \ (_) | |  \__ \
	//    /_/   \_\___\___\___||___/___/\___/|_|  |___/

	public double getLeft() {
		return mLeft;
	}

	public void setLeft(float left) {
		mLeft = left;
		set(mLeft, mTop, mRight, mBottom);
	}

	public double getTop() {
		return mTop;
	}

	public void setTop(float top) {
		mTop = top;
		set(mLeft, mTop, mRight, mBottom);
	}

	public double getRight() {
		return mRight;
	}

	public void setRight(float right) {
		mRight = right;
		set(mLeft, mTop, mRight, mBottom);
	}

	public double getBottom() {
		return mBottom;
	}

	public void setBottom(float bottom) {
		mBottom = bottom;
		set(mLeft, mTop, mRight, mBottom);
	}
}
