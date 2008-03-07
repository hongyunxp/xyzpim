package cn.edu.nju.software.fsexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FSExplorer extends ListActivity {
	private List<String> items = null;

	private File cpath = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.directory_list);
		cpath=new File("/");
		fill(cpath);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int selectionRowID = (int) this.getSelectedItemId();
		if (selectionRowID == 0) {
			if(!"/".equals(cpath.getPath()))
				fill(cpath.getParentFile());
		} else {
			File file = new File(items.get(selectionRowID));
			if (file.isDirectory())
				fill(file);
			else
				AlertDialog.show(this, getText(R.string.not_directory_title),
						R.drawable.icon,
						getText(R.string.not_directory_message),
						getText(R.string.cancel), false);
		}
	}

	/*private void fillWithRoot() {
		fill(new File("/").listFiles());
	}*/

	private void fill(File file) {
		cpath=file;
		TextView cptahView=(TextView)findViewById(R.id.cpath);
		cptahView.setText(file.getPath());
		items = new ArrayList<String>();
		items.add(getString(R.string.to_top));
		File[] files=file.listFiles();
		for (File f : files)
			items.add(f.getPath());
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.file_row, items);
		setListAdapter(fileList);
	}
}