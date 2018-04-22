package com.otak;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification;
import android.widget.Toast;
import com.otak.init.ShellExecuter;
import android.os.CountDownTimer;
import android.os.BatteryManager;
import com.tools.Kompas;
import android.widget.RemoteViews;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Gravity;
import com.otak.input.MicHelper;
import android.os.*;
import com.status.*;
import android.app.*;

public class ReceiverBoot extends BroadcastReceiver
{
	Context context;
	public NotificationManager notificationManager;
	private Toast toast;
	Intent pengi, komi, maini;
	PendingIntent pengp, kompp, mainp;
	public static int layar;
	SharedPreferences settings;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.context = context;
		settings = context.getSharedPreferences("Settings", 0);	
	
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
		{
			if (settings.getBoolean("mode hemat", true)){
				Intent mIntent = new Intent(context, MainAsisten.class);
				mIntent.putExtra("layar","off");
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(mIntent);
			}
		}
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			context.startService(new Intent(context, ServiceBoot.class));
			
			ServiceTTS sertts = new ServiceTTS();
			sertts.cepat = 1.0f;
			sertts.str = "proses booting selesai";
			context.startService(new Intent(context, ServiceTTS.class));
			
		}
		if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
		{
			ShellExecuter exe = new ShellExecuter();
			String amp = exe.Executer("cat /sys/class/power_supply/battery/current_now");
			String[] a = amp.split("(?<=\\G.{1})");
			String[] b = amp.split("(?<=\\G.{3})");
			
			if (a[0].equals("-")){
				b = amp.split("(?<=\\G.{4})");
			}
			
			float BatteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
			float voltase     = (float)(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0))/100;
			
			String dataTemp = ""+BatteryTemp+(char)0x00B0+"C";
			String dataVolt = ""+voltase+" V";
			String dataAmp  = ""+b[0]+" mA";

			notifiBoot(context, dataVolt, dataTemp, dataAmp);
			
			if (voltase <= 34.5){
				toastText(context, dataVolt, Color.YELLOW, Gravity.TOP | Gravity.LEFT);
			}
			else {
				if (BatteryTemp >= 36.5){
				    toastText(context, dataTemp, Color.RED, Gravity.TOP | Gravity.RIGHT);
			    }
			}
		
		}
		
	}
	

	public void notifiBoot(Context context, String volt, String temp, String amp)
	{
		MainAsisten asisten = new MainAsisten();
		String ledMic = "OFF";
		if (settings.getBoolean("mode hemat",true)){
			ledMic = "ON";
		}
		
		pengi = new Intent(context, Pengaturan.class);
	    komi = new Intent(context, Kompas.class);
		maini = new Intent(context, MainAsisten.class);
		maini.putExtra("destroy","hancur");

	    pengp = PendingIntent.getActivity(context, 0, pengi, PendingIntent.FLAG_UPDATE_CURRENT);
	    kompp = PendingIntent.getActivity(context, 0, komi, PendingIntent.FLAG_UPDATE_CURRENT);
	    mainp = PendingIntent.getActivity(context, 0, maini, PendingIntent.FLAG_UPDATE_CURRENT);

		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notifi);
		contentView.setImageViewResource(R.id.image, R.drawable.setting);
		contentView.setImageViewResource(R.id.alat, R.drawable.compas);
		contentView.setImageViewResource(R.id.main, R.drawable.icon);
		contentView.setTextViewText(R.id.title, asisten.getWeton(4));
		contentView.setTextViewText(R.id.text, new StringBuilder().append("-Volt    : "+volt+"\t\t\t -Cpu  :  "+new CpuMon().cpuPakai)
									.append("\n-Arus  : "+amp+"        -Mic : "+ledMic)
									.append("\n-Temp : "+temp)
								    );
									
		contentView.setOnClickPendingIntent(R.id.image, pengp);
		contentView.setOnClickPendingIntent(R.id.alat, kompp);
		contentView.setOnClickPendingIntent(R.id.main, mainp);

		int libur = R.drawable.trans;
		if(asisten.getWeton(0).equals("Minggu")){
			libur = R.drawable.transm;
		}
	
		Notification.Builder mBuilder = new Notification.Builder(context)
			.setSmallIcon(libur)
			.setPriority(Notification.PRIORITY_MAX)
			.setContent(contentView);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR;

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		notificationManager.notify(7, notification);
	}

	private void notifi(String data) {
		Notification notification = new Notification.Builder(context)
			.setSmallIcon(R.drawable.trans)
			.setTicker(data)
			.setOngoing(true)
			.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		notificationManager.notify(8, notification);

		CountDownTimer hitungMundur = new CountDownTimer(2000, 100)
		{
			public void onTick(long millisUntilFinished){
			}
			public void onFinish()
			{
				notificationManager.cancel(8);
			}
		}.start();

	}

	public void toastText(Context context, String data, int warna, int letak)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.toast, null);

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


}
