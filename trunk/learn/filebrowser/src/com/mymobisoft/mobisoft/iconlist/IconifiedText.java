package com.mymobisoft.mobisoft.iconlist;

import android.graphics.drawable.Drawable;

public class IconifiedText {
	private String _text = "";
	private Drawable _icon;
	private boolean _isSelectable = true;

	public IconifiedText(String text, Drawable icon) {
		_text = text;
		_icon = icon;
	}// end IconifiedText

	public boolean isSelectable() {
		return _isSelectable;
	}// end isSelectable()

	public void setSelectable(boolean selectable) {
		_isSelectable = selectable;
	}// end setSelectable()

	public String getText() {
		return _text;
	}// end getText()

	public void setText(String text) {
		_text = text;
	}//end setText

	public Drawable getIcon() {
		return _icon;
	}// end getIcon()

	public void setIcon(Drawable icon) {
		_icon = icon;
	}//end setIcon()

	//@Override
	public int compareTo(IconifiedText other) {
		if (this._text != null) {
			return this._text.compareTo(other.getText());
		} else {
			throw new IllegalArgumentException();
		}// end if
	}// compareTo()
	
}// end class IconFieldText
