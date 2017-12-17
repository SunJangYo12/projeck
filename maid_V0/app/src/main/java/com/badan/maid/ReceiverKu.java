package com.badan.maid;

import android.content.*;
import android.widget.*;
import com.badan.maid.database.*;
import android.database.sqlite.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.app.*;
import com.badan.maid.sistem.*;
import java.util.*;
import com.badan.maid.kal.*;
import android.net.*;
import java.text.*;

public class ReceiverKu extends BroadcastReceiver
{
	public static String dataTemp;
	public static String dataVolt;
	public static String dataPersen;
	public static boolean aktifLock = true;
	public static boolean aktifCat = true;
	private Toast toast;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		float BatteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
		float voltase     = (float)(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0))/100;

		dataTemp = ""+BatteryTemp+(char)0x00B0+"C";
		dataVolt = ""+voltase+" V";

		ShellExecuter exe = new ShellExecuter();
		String aru = "cat /sys/class/power_supply/battery/current_now";
		String per = "cat /sys/class/power_supply/battery/capacity";

		String dataPer = exe.Executer(per);
		String dataAmp = exe.Executer(aru);

		

		notifi(context, dataVolt, dataTemp, dataAmp);

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			Intent mIntent = new Intent(context, ServiceKu.class);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(mIntent);
		}
		if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
			if (BatteryTemp >= 36.5){
				toastText(context, ""+BatteryTemp+(char)0x00B0+"C", Color.YELLOW, Gravity.TOP | Gravity.RIGHT);
			}
			if (voltase <= 34.5){
				toastText(context, ""+voltase+" V", Color.RED, Gravity.TOP | Gravity.LEFT);
			}
		}
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
			if (aktifLock){
				Intent mIntent = new Intent(context, LockActivity.class);
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				context.startActivity(mIntent);
			}
		}

		if (internetCek(context) && aktifCat)
		{
			DBHelper dbhelper = new DBHelper(context);
		   	SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor notes = dbhelper.getNotes(db);
			db.close();

			try{
				if (notes.getString(0).toString().equals("")){
				}
				else{
					Intent cati = new Intent(context, DBActivity.class);
					PendingIntent catp = PendingIntent.getActivity(context, 0, cati, PendingIntent.FLAG_UPDATE_CURRENT);

					Notification.Builder mBuilder = new Notification.Builder(context)
						.setSmallIcon(R.drawable.catatan)
						.setContentIntent(catp)
						.setContentText("buka catatan yang ada menggunakan browser")
						.setContentTitle("Ada catatan");

					Notification notification = mBuilder.build();
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.defaults |= Notification.DEFAULT_SOUND;
					notification.defaults |= Notification.DEFAULT_VIBRATE;

					NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
					notificationManager.notify(1, notification);
				}
			}
			catch (Exception e){
	    	}
		}
	}

	public void toastText(Context context, String data, int warna, int letak)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.toast_text, null);

    	TextView text = (TextView) layout.findViewById(R.id.toast);
		text.setText(data);
		text.setTextColor(Color.BLUE);
		text.setTextSize(13);
		text.setGravity(Gravity.CENTER);

		toast = new Toast(context.getApplicationContext());
		toast.setGravity(letak, 0, 0);
		toast.setView(text);
		toast.setView(layout);

		View toastView = toast.getView();
		toastView.setBackgroundColor(warna);

    	CountDownTimer hitungMundur = new CountDownTimer(3000, 100)
		{
			public void onTick(long millisUntilFinished)
			{
				toast.show();
			}
			public void onFinish()
			{
				toast.cancel();
			}
		}.start();
	}

	public void notifi(Context context, String volt, String temp, String amp)
	{
	    Calendar c = Calendar.getInstance();  
        int mYear = c.get(Calendar.YEAR);  
        int mMonth = c.get(Calendar.MONTH);  
        int mDay = c.get(Calendar.DAY_OF_MONTH);  
		int libur;

		final Kalender kal = new Kalender();  

        String[] jawa = kal.MasehiToJawa(mYear, mMonth, mDay);
		
		Intent kali = new Intent(context, KalActivity.class);
		Intent cati = new Intent(context, DBActivity.class);
		Intent pengi = new Intent(context, Pengaturan.class);
		Intent komi = new Intent(context, Kompas.class);

		PendingIntent kalp = PendingIntent.getActivity(context, 0, kali, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent catp = PendingIntent.getActivity(context, 0, cati, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pengp = PendingIntent.getActivity(context, 0, pengi, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent kompp = PendingIntent.getActivity(context, 0, komi, PendingIntent.FLAG_UPDATE_CURRENT);

		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notifi);
		contentView.setImageViewResource(R.id.image, R.drawable.setting);
		contentView.setImageViewResource(R.id.alat, R.drawable.compas);
		contentView.setTextViewText(R.id.title, new StringBuilder().append(jawa[4]+""));
		contentView.setTextViewText(R.id.text, "-volt    : "+volt+"\n"+"-arus  : "+amp+" mA\n"+"-temp : "+temp);

		contentView.setOnClickPendingIntent(R.id.title, kalp);
		contentView.setOnClickPendingIntent(R.id.imcat, catp);
		contentView.setOnClickPendingIntent(R.id.image, pengp);
		contentView.setOnClickPendingIntent(R.id.alat, kompp);

		Notification.Builder mBuilder = new Notification.Builder(context)
			.setSmallIcon(R.drawable.trans)
			.setPriority(Notification.PRIORITY_MAX)
			.setContent(contentView);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR;

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}

	private boolean internetCek(Context context) {
		// get Connectivity Manager object to check connection
		ConnectivityManager connec = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

		// Check for network connections
		if ( connec.getNetworkInfo(0).getState() == 
			android.net.NetworkInfo.State.CONNECTED ||
			connec.getNetworkInfo(0).getState() == 
			android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == 
			android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;
		}
		else if (
            connec.getNetworkInfo(0).getState() == 
            android.net.NetworkInfo.State.DISCONNECTED ||
            connec.getNetworkInfo(1).getState() == 
            android.net.NetworkInfo.State.DISCONNECTED  ) {

			return false;
		}
		return false;
	}

}
