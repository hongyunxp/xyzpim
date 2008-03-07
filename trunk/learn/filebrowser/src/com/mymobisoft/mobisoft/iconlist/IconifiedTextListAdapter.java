package com.mymobisoft.mobisoft.iconlist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class IconifiedTextListAdapter extends BaseAdapter {
	private Context _context;
	private List<IconifiedText> _items = new ArrayList<IconifiedText>();

	public IconifiedTextListAdapter(Context context) {
		_context = context;
	}// end IconifiedTextListAdapter()

	public void addItem(IconifiedText iconifiedText) {
		_items.add(iconifiedText);
	}// end addItem()

	public void setListItems(List<IconifiedText> list) {
		_items = list;
	}//end setListItems()

	public int getCount() {
		return _items.size();
	}// end getCount()	

	public Object getItem(int location) {
		return _items.get(location);
	}// end getItem()

	public boolean areAllItemsSelectable() {
		return false;
	}// end areAllItemsSelectable()

	public boolean isSelectable(int location) {
		return _items.get(location).isSelectable();
	}// end isSelectable()

	/* 用数级的下标做为唯一代号 */
	public long getItemId(int location) {
		return location;
	}// end getItemId()

	public View getView(int location, View view, ViewGroup viewgroup) {
		IconifiedTextView itv;
		if (view == null) {
			itv = new IconifiedTextView(_context, _items.get(location));
		} else {
			itv = (IconifiedTextView) view;
			itv.setText(_items.get(location).getText());
			itv.setIcon(_items.get(location).getIcon());
		}// end if
		return itv;
	}// end getView()	
}//end class IconifiedTextListAdapter
