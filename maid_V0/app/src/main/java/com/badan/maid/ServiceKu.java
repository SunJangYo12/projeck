package com.badan.maid;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import com.badan.maid.database.*;

public class ServiceKu extends Service
{
	private BroadcastReceiver re;

	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        key = km.newKeyguardLock("IN");
        key.disableKeyguard();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);

		re = new ReceiverKu();
		registerReceiver(re, filter);

	}

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		unregisterReceiver(re);
	}
	class Threadku extends Thread
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
		}

	}


}
