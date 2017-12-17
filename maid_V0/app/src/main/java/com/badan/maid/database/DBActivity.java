package com.badan.maid.database;


/**
 * @author Michał Moczulski
 * twitter_url: http://twitter.com/#!/moczul
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.badan.maid.R;
import android.widget.*;
import android.speech.*;
import android.content.*;
import java.util.*;
import android.speech.tts.*;
import android.os.*;
import android.media.*;
import android.app.*;
import android.net.*;
import com.badan.maid.*;
import android.view.*;

public class DBActivity extends Activity implements
OnItemClickListener {

	private ListView noteList;
	private Button save, voice, bicara;
	private EditText edit;
	private ArrayAdapter<String> adapter;
	private Cursor notes;
	private DBHelper dbhelper;
	private ArrayList<String> titles;
	private ArrayList<Item> items;
	private int position = 0;
	private TextToSpeech textToSpeech;
	private String hasil_stt;
	private AudioManager myAudioManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db);


		noteList = (ListView) findViewById(R.id.noteList);
		save = (Button) findViewById(R.id.save);
		voice = (Button) findViewById(R.id.voice);
		bicara = (Button) findViewById(R.id.bicara);
		edit = (EditText) findViewById(R.id.isi);

		dbhelper = new DBHelper(getApplicationContext());
		setNotes();
		this.registerForContextMenu(noteList);

		save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String title = edit.getText().toString();
					String content = "a";

					if (title.equals("") || content.equals("")) {
						Toast.makeText(getApplicationContext(), "kosong", Toast.LENGTH_LONG).show();
						return;
					}
					else
					{
						dbhelper = new DBHelper(getApplicationContext());
						dbhelper.addNote(title, content);
						setNotes();
						edit.setText("");
					}
				}

			});
		voice.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{
					Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
									RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
					intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
									"silahkan ngomong ");
					try {
						startActivityForResult(intent, 1);
					} 
					catch (ActivityNotFoundException a) {

					}
				}
			});
		myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

		bicara.setOnLongClickListener(new View.OnLongClickListener()
			{
				public boolean onLongClick(View v)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						textToSpeech.speak(edit.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,null);
					} 
					else {
						textToSpeech.speak(edit.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
					}
					for (int i=0; i<=100; i++){
						myAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
					}
					return true;
				}
			});

		bicara.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v)
				{

					textToSpeech = new TextToSpeech(DBActivity.this, new TextToSpeech.OnInitListener() {
							@Override
							public void onInit(int status) {
								if(status != TextToSpeech.ERROR) {
									textToSpeech.setLanguage(Locale.getDefault());
								}
							}
						});
				}
			});

	}

	public void setNotes() {
		titles = new ArrayList<String>();
		items = new ArrayList<Item>();

		SQLiteDatabase db = dbhelper.getReadableDatabase();
		notes = dbhelper.getNotes2(db);
		db.close();

		if (notes.moveToFirst()) {
			do {
				items.add(new Item(notes.getShort(0), notes.getString(1)));
			} 
			while (notes.moveToNext());
		}


		for (Item i : items) {
			titles.add(i.getTitle());

		}

		adapter = new ArrayAdapter<String>(this,
										   android.R.layout.simple_list_item_1, titles);
		noteList.setAdapter(adapter);
		//noteList.getCount();
		noteList.setOnItemClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		setNotes();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		position = info.position;
		menu.setHeaderTitle(getResources().getString(R.string.hello_world));

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TextView tv = (TextView) noteList.getChildAt(position);

		String title = tv.getText().toString();
		String date = "40;6788";

		switch (item.getItemId()) {
			case R.id.showDate:
				Toast toast= Toast.makeText(getApplicationContext(), "Judul   : "+title+"\n\n"+"dibuat  : "+date, Toast.LENGTH_LONG);  
				toast.setGravity(Gravity.TOP, 0, 100);
				toast.show();
				break;

			case R.id.editNote:
				break;

			case R.id.removeNote:
				dbhelper.removeNote(items.get(position).getId());
				setNotes();
				break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView tv = (TextView) arg1;
		String title = tv.getText().toString();

		String url = "https://www.google.co.id/search?client=ucweb-b-bookmark&source=hp&ei=bd4rWuDrMIvSvwTdop7gCg&q="+title+"&oq="+title+"&gs_l=mobile-gws-hp.12.60063.61826.0.62513.0.0.0.0.0.0.0.0.0.0.0.1c.1.64.mobile-gws-hp.0.0.0.0.lgy7UmHKNrI;";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);

		dbhelper.removeNote(items.get(position).getId());
		setNotes();
	}

	//Untuk menerima inputan speech dan menampilkan text
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
					if (resultCode == RESULT_OK && null != data) {
						ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

						hasil_stt = result.get(0);

						AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
						builder1.setMessage(result.get(0));
						builder1.setTitle("Apakah : ");
						builder1.setCancelable(true);
						builder1.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									edit.setText(hasil_stt);
									String title = edit.getText().toString();
									String content = "a";

									dbhelper = new DBHelper(getApplicationContext());
									dbhelper.addNote(title, content);
									setNotes();

								}
							});

						builder1.setNegativeButton("Ulang", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
									intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
													RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
									intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
									intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
													"silahkan ngomong ");
									try {
										startActivityForResult(intent, 1);
									} 
									catch (ActivityNotFoundException a) {

									}
								}
							});
						AlertDialog alert11 = builder1.create();
						alert11.show();
					}
					break;
				}

        }
    }

}

