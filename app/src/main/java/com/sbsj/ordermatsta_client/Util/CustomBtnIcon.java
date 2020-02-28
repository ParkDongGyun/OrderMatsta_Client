package com.sbsj.ordermatsta_client.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbsj.ordermatsta_client.R;

public class CustomBtnIcon extends LinearLayout implements View.OnClickListener {

    public RelativeLayout relativeLayout;
    ImageView imageView;
    TextView textView;

    OnCustomClickListener onCustomClickListener;

    public CustomBtnIcon(Context context) {
        super(context);
        initView();
    }

    public CustomBtnIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CustomBtnIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_btn_icon, this, false);
        addView(v);
        imageView = findViewById(R.id.iv_custom_icon);
        textView = findViewById(R.id.tv_custom_icon);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.custom_btn_icon);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.custom_btn_icon, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {

        relativeLayout = findViewById(R.id.rl_custom_icon);
        relativeLayout.setOnClickListener(this);
        int image_resID = typedArray.getResourceId(R.styleable.custom_btn_icon_symbol, R.drawable.btn_flash_on);
        imageView.setImageResource(image_resID);
        imageView.setOnClickListener(this);

        String text_string = typedArray.getString(R.styleable.custom_btn_icon_text);
        textView.setText(text_string);
        textView.setOnClickListener(this);

        typedArray.recycle();
    }

    public void setimage(int image_resID) {
        imageView.setImageResource(image_resID);
    }

    public void setText(String text_string) {
        textView.setText(text_string);
    }

    public void setText(int text_resID) {
        textView.setText(text_resID);
    }

    public void setOnCustomClickListener(OnCustomClickListener onCustomClickListener) {
        this.onCustomClickListener = onCustomClickListener;
    }

    @Override
    public void onClick(View view) {
        onCustomClickListener.onClick(view);
    }
}
