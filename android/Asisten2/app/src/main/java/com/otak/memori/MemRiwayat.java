package com.otak.memori;

import android.app.*;
import android.os.*;
import com.otak.R;

import android.widget.*;
import java.util.*;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.AdapterView.*;
import android.content.*;

public class MemRiwayat extends Activity
{
	private DBHelper dbhelper;
	private ListView noteList;
	private Button addNoteBtn;
	private Cursor cursor;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> titles;
	private ArrayList<Item> items;
	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_riwayat);
		noteList = (ListView) findViewById(R.id.riwList);
		
		dbhelper = new DBHelper(getApplicationContext());
		
		titles = new ArrayList<String>();
		items = new ArrayList<Item>();

		db = dbhelper.getReadableDatabase();
	
		cursor = dbhelper.getNotes2(db);
		
		startManagingCursor(cursor);
		db.close();
		
		if (cursor.moveToFirst()) {
			do {
				items.add(new Item(cursor.getShort(0), cursor.getString(1)));
			} while (cursor.moveToNext());
		}


		for (Item i : items) {
			titles.add(i.getTitle());
		}

		adapter = new ArrayAdapter<String>(this,
										   android.R.layout.simple_list_item_1, titles);
		noteList.setAdapter(adapter);
		noteList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
			
				db = dbhelper.getReadableDatabase();
				
				cursor = dbhelper.getNote(db, items.get(arg2).getId());
				startManagingCursor(cursor);
				db.close();
				
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText("label", cursor.getString(0).toString());
				clipboard.setPrimaryClip(clip);
			}
		});
		noteList.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
				{
					dbhelper.removeNote(items.get(pos).getId());
					finish();
					return true;
				}
			}); 
	}
}

class Item {

	private int id;
	private String title;

	public Item(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

}
