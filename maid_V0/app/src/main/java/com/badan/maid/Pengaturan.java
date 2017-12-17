package com.badan.maid;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;

public class Pengaturan extends Activity
{
	private ReceiverKu rece;
	private Switch swLock, swCat;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pengaturan);

		rece = new ReceiverKu();

		swLock = (Switch)  findViewById(R.id.swLock);
		swLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					rece.aktifLock = isChecked;
					if (isChecked){
						Toast.makeText(getApplicationContext(), "Lock Layar diaktifkan", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(getApplicationContext(), "Matikan", Toast.LENGTH_SHORT).show();
					}
				}       
			});
		swCat = (Switch)  findViewById(R.id.swCat);
		swCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					rece.aktifCat = isChecked;
					if (isChecked){
						Toast.makeText(getApplicationContext(), "catatan diaktifkan", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(getApplicationContext(), "Matikan", Toast.LENGTH_SHORT).show();
					}
				}       
			});
	}
}
