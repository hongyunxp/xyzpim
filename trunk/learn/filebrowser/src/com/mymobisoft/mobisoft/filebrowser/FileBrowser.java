package com.mymobisoft.mobisoft.filebrowser;

import java.io.File;
import java.util.ArrayList; //import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mymobisoft.mobisoft.iconlist.IconifiedText;
import com.mymobisoft.mobisoft.iconlist.IconifiedTextListAdapter;

public class FileBrowser extends ListActivity {

	// 显示模式
	private enum _DISPLAYMODE {
		_ABSOLUTE, _RELATIVE;
	}// end _displaymode

	private final _DISPLAYMODE _displayMode = _DISPLAYMODE._RELATIVE;
	private List<IconifiedText> _directoryEntries = new ArrayList<IconifiedText>();
	private File _currentDirectory = new File("/"); // 当前目录

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		_listRoot();
	}// end onCreate()

	/**
	 * List 根目录
	 */
	private void _listRoot() {
		_browseTo(new File("/"));
	}// end listRoot()

	/**
	 * 浏览指定的目录并设定 title
	 * 
	 * @param directory
	 *            文件或目录
	 */
	private void _browseTo(final File directory) {
		if (this._displayMode == _DISPLAYMODE._RELATIVE) {
			this.setTitle(getString(R.string.app_name) + "::"
					+ directory.getAbsolutePath());
		}// end if
		// 判断是不是目录
		if (directory.isDirectory()) {
			this._currentDirectory = directory;
			_fill(directory.listFiles());
		} else {
			OnClickListener okButtonListener = new OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int arg1) {
					FileBrowser.this._openFile(directory);
				}// end onClick()
			};
			OnClickListener cancelButtonListener = new OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int arg1) {
					finish();
				}// end onClick()
			};

			AlertDialog.show(this, "询问", R.drawable.icon,"你要打开文件吗？\n" + directory.getName(),
					"确定", okButtonListener, "取消", cancelButtonListener, false,
					null);

		}// end if
	}// end browseTo()

	/**
	 * 回到上一阶目录
	 */
	private void _upOneLevel() {
		if (this._currentDirectory.getParent() != null) {
			this._browseTo(this._currentDirectory.getParentFile());
		}// end if
	}// end upOneLevel()

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
	private void _openFile(File file) {

		Intent intent = new Intent(Intent.VIEW_ACTION, Uri.parse("file://"
				+ file.getAbsolutePath()));
		startActivity(intent);

	}// end openFile()

	/**
	 * 列出目录下的文件
	 * 
	 * @param files
	 */
	private void _fill(File[] files) {
		// fill file list 之前清除畫面
		this._directoryEntries.clear();
		// Add the "." == "current directory"
		this._directoryEntries.add(new IconifiedText(
				getString(R.string.current_dir), getResources().getDrawable(
						R.drawable.folder)));
		if (this._currentDirectory.getParent() != null) {
			this._directoryEntries.add(new IconifiedText(
					getString(R.string.up_one_level), getResources()
							.getDrawable(R.drawable.uponelevel)));
		}// end if
		Drawable currentIcon = null;
		for (File currentFile : files) {
			if (currentFile.isDirectory()) {
				currentIcon = getResources().getDrawable(R.drawable.folder);
			} else {
				String fileName = currentFile.getName();
				if (_checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.imageExt))) {
					currentIcon = getResources().getDrawable(
							R.drawable.pictures);
				} else if (_checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.webFileExt))) {
					currentIcon = getResources().getDrawable(R.drawable.web);
				} else if (_checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.audioExt))) {
					currentIcon = getResources().getDrawable(R.drawable.audio);
				} else if (_checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.packageExt))) {
					currentIcon = getResources().getDrawable(R.drawable.pkg);
				} else {
					currentIcon = getResources().getDrawable(R.drawable.none);
				}// end if
			}// end if
			switch (this._displayMode) {
			case _ABSOLUTE:
				/* on absolute mode, we show the full path */
				this._directoryEntries.add(new IconifiedText(currentFile
						.getPath(), currentIcon));
				break;
			case _RELATIVE:
				int currentPathStringLen = this._currentDirectory
						.getAbsolutePath().length();
				this._directoryEntries.add(new IconifiedText(currentFile
						.getAbsolutePath().substring(currentPathStringLen),
						currentIcon));
				break;
			}// end switch
			// Collections.sort(this._directoryEntries);
			IconifiedTextListAdapter itlAdapter = new IconifiedTextListAdapter(
					this);
			itlAdapter.setListItems(this._directoryEntries);
			this.setListAdapter(itlAdapter);
		}// end for loop
	}// end fill()

	/**
	 * 驗證文件類型是否是列表中的某一個
	 * 
	 * @param ext
	 *            文件後綴名
	 * @param exts
	 *            文件後綴名數組
	 * @return boolean true_當前文件屬於其中的一類, false_不屬於
	 * @author Dennis
	 */
	private boolean _checkEndsWithInStringArray(String ext, String[] exts) {
		for (String aExt : exts) {
			if (ext.endsWith(aExt))
				return true;
		}// end for loop
		return false;
	}// end _checkEndsWithInStringArray()

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		int selectionRowID = (int) this.getSelectedItemId();
		String selectedFileString = this._directoryEntries.get(selectionRowID)
				.getText();

		if (selectedFileString.equals(getString(R.string.current_dir))) {
			// 刷新
			this._browseTo(this._currentDirectory);
		} else if (selectedFileString.equals(getString(R.string.up_one_level))) {
			// 到上一階目錄
			this._upOneLevel();
		} else {
			File clickedFile = null;
			switch (this._displayMode) {
			case _RELATIVE:
				clickedFile = new File(this._currentDirectory.getAbsolutePath()
						+ this._directoryEntries.get(selectionRowID).getText());

				break;
			case _ABSOLUTE:
				clickedFile = new File(this._directoryEntries.get(
						selectionRowID).getText());
				break;
			default:
				break;
			}// end switch
			if (clickedFile != null) {
				this._browseTo(clickedFile);
			}// end if
		}// end if
	}// end onListItemClick()
}// end class FileBrowser
