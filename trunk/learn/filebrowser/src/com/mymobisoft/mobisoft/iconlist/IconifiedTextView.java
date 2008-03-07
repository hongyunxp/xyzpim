package com.mymobisoft.mobisoft.iconlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconifiedTextView extends LinearLayout {
	private TextView textView;
	private ImageView imageView;

	public IconifiedTextView(Context context, IconifiedText iconifiedText) {
		super(context);
		this.setOrientation(HORIZONTAL);
		imageView = new ImageView(context);
		imageView.setImageDrawable(iconifiedText.getIcon());
		imageView.setPadding(0, 2, 5, 0);
		// 添加 Icon
		addView(imageView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView = new TextView(context);
		textView.setText(iconifiedText.getText());

		// 在 Icon 之后添加文字
		addView(textView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}// end IconifiedTextView
	
	public void setIcon(Drawable drawable)
	{
		imageView.setImageDrawable(drawable);
	}// end setIcon()
	
	public void setText(String text)
	{
		textView.setText(text);
	}// end setText()
}// end class IconifiedTextView
