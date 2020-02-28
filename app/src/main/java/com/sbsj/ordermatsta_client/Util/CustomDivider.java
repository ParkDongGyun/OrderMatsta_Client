package com.sbsj.ordermatsta_client.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sbsj.ordermatsta_client.R;

public class CustomDivider extends LinearLayout {
	LinearLayout linearLayout;
	int height;

	public CustomDivider(Context context, int height) {
		super(context);
		initView(height);
	}

	public CustomDivider(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(height);
		getAttrs(attrs);
	}

	public CustomDivider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView(height);
		getAttrs(attrs, defStyle);
	}

	private void initView(int height) {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.custom_divider, this, false);
		addView(v);
		linearLayout = (LinearLayout) findViewById(R.id.ll_divider);
		this.height = height;
	}

	private void getAttrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.custom_view);
		setTypeArray(typedArray);
	}

	private void getAttrs(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.custom_view, defStyle, 0);
		setTypeArray(typedArray);
	}

	private void setTypeArray(TypedArray typedArray) {
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
		typedArray.recycle();
	}

	public int getheight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
