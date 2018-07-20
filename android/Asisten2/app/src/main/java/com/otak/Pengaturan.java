package com.otak;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.speech.tts.*;
import java.util.*;
import com.tools.*;
import com.status.*;

public class Pengaturan extends Activity
{
	Button btnTelinga, aSave, btnMulut, btnDir, btnCuaca;
	Switch swMicControl, swSuaraControl;
	EditText edCuaca, edCuaca1;
	public TextToSpeech textToSpeech;
	EditText ed;
	RadioButton radioB, radioB2;
	private String title, isi;
	private int index;
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_pengaturan);
        
		startService(new Intent(this, ServiceBoot.class));
		startService(new Intent(this, ServiceStatus.class));
		
		btnTelinga = (Button)findViewById(R.id.telinga);
		btnTelinga.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					telinga(1);
				}
			});
		settings = getSharedPreferences("Settings", 0);	
		
		swMicControl = (Switch)findViewById(R.id.run_service);
		swMicControl.setChecked(settings.getBoolean("mode hemat",false));
		swMicControl.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					SharedPreferences.Editor editor = settings.edit();	
					
					if (swMicControl.isChecked()) {
						editor.putBoolean("mode hemat", true);	
						editor.commit();
						
					}
					else{
						editor.putBoolean("Mode hemat", false);	
						editor.commit();
					}
					Toast.makeText(getBaseContext(), "disimpan "+settings.getBoolean("Mic Control", true), Toast.LENGTH_LONG).show();

				}
			});
		swSuaraControl = (Switch)findViewById(R.id.mute);
		swSuaraControl.setChecked(settings.getBoolean("suara",false));
		swSuaraControl.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					SharedPreferences.Editor editor = settings.edit();	
					
					if (swMicControl.isChecked()) {
						editor.putBoolean("suara", true);	
						editor.commit();
						
					}
					else{
						editor.putBoolean("suara", false);	
						editor.commit();
					}
					Toast.makeText(getBaseContext(), "disimpan "+settings.getBoolean("Mic Control", true), Toast.LENGTH_LONG).show();

				}
			});
		btnMulut = (Button)findViewById(R.id.mulut);
		btnMulut.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					ServiceTTS sertts = new ServiceTTS();
					sertts.cepat = 1.0f;
					sertts.str = "selamat datang";
					startService(new Intent(Pengaturan.this, ServiceTTS.class));
					
				}
			});
		
		btnDir = (Button)findViewById(R.id.dir);
		btnDir.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					//telinga(3);
					startActivity(new Intent(Pengaturan.this, FileExploler.class));

				}
			});
		edCuaca = (EditText)findViewById(R.id.cuaEdit);
	   	edCuaca1 = (EditText)findViewById(R.id.cuaEdit1);
		
		edCuaca.setText(settings.getString("cuaTempat",""));
		edCuaca1.setText(settings.getString("cuaNegara",""));
		if (settings.getString("cuaTempat","").equals("") && settings.getString("cuaNegara","").equals("")){
			Toast.makeText(this,"silahkan isikan tempat dan negara cuaca",Toast.LENGTH_LONG).show();
		}
		btnCuaca = (Button)findViewById(R.id.btnCua);
		btnCuaca.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					SharedPreferences.Editor editor = settings.edit();	
					editor.putString("cuaNegara", edCuaca1.getText().toString());	
					editor.putString("cuaTempat", edCuaca.getText().toString());	
					Toast.makeText(getApplicationContext(),"Negara : "+settings.getString("cuaNegara","")+"\nTempat : "+settings.getString("cuaTempat",""),Toast.LENGTH_LONG).show();
					editor.commit();
					
				}
			});
	}

	public void telinga(int data)
	{
		index = data;
		if (index == 1) {
			title = "Masukan Spektrum Terkecil\nisi : "+settings.getInt("Spektrum_awal",0);
			isi = "Spektrum_awal";
		}
		else if (index == 2) {
			title = "Sekarang Spektrum Terbesar\nisi : "+settings.getInt("Spektrum_akhir",0);
			isi = "Spektrum_akhir";
		}
		else if (index == 3)	{
			title = "Atur directory";
			isi = "Atur Dir";
		}
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle(title);
		builder1.setCancelable(true);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.alert_telinga, null);

		ed = (EditText) layout.findViewById(R.id.aEdit);
		aSave = (Button) layout.findViewById(R.id.aButton);
		aSave.setOnClickListener(new View.OnClickListener()
			{
				SharedPreferences settings = getSharedPreferences("Settings", 0);	
				SharedPreferences.Editor editor = settings.edit();	

				public void onClick(View v){
					if (index == 1){
						int ku = 0;
						try{
							ku = Integer.parseInt(ed.getText().toString());
						}
						catch(Exception e){}

						editor.putInt(isi, ku);	
						editor.commit();
						Toast.makeText(getBaseContext(), "rendah : "+ku, Toast.LENGTH_SHORT).show();
						telinga(2);
					}
					else if (index == 2){
						int ku = 0;
						try{
							ku = Integer.parseInt(ed.getText().toString());
						}
						catch(Exception e){}

						editor.putInt(isi, ku);	
						editor.commit();
						Toast.makeText(getBaseContext(), "tinggi : "+ku, Toast.LENGTH_SHORT).show();
					}
					else if (index == 3){
						editor.putString(isi, ed.getText().toString());	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan dir : "+settings.getString(isi,""), Toast.LENGTH_LONG).show();
					}
				}
			});
	    radioB = (RadioButton) layout.findViewById(R.id.rg1);
		radioB.setText(settings.getString("Atur dir",""));
        radioB.setOnClickListener(new View.OnClickListener()
			{
				SharedPreferences settings = getSharedPreferences("Settings", 0);	
				SharedPreferences.Editor editor = settings.edit();	

				public void onClick(View v){
					if (index == 1){
						editor.putInt(isi, 500);	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan awal : 500", Toast.LENGTH_LONG).show();
						telinga(2);
					}
					else if (index == 2){
						editor.putInt(isi, 500);	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan akhir : 500", Toast.LENGTH_LONG).show();
					}
					else if (index == 3){
						editor.putString(isi, "removable/sdcard1/");	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan dir : removable/sdcard1/", Toast.LENGTH_LONG).show();
					}
				}
			});
		radioB2 = (RadioButton) layout.findViewById(R.id.rg2);
		radioB2.setText(settings.getString("Atur dir",""));
        radioB2.setOnClickListener(new View.OnClickListener()
			{
				SharedPreferences settings = getSharedPreferences("Settings", 0);	
				SharedPreferences.Editor editor = settings.edit();	

				public void onClick(View v){
					if (index == 1){
						editor.putInt(isi, 800);	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan awal : 800", Toast.LENGTH_LONG).show();
						telinga(2);
					}
					else if (index == 2){
						editor.putInt(isi, 800);	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan akhir : 800", Toast.LENGTH_LONG).show();
					}
					else if (index == 3){
						editor.putString(isi, "emulated/0/");	
						editor.commit();
						Toast.makeText(getBaseContext(), "disimpan dir : emulated/0/", Toast.LENGTH_LONG).show();
					}
				}
			});

		builder1.setView(layout);

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

}
