package com.badan.maid;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import java.util.*;
import com.badan.maid.kal.*;
import com.badan.maid.sistem.*;
import java.text.*;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.app.*;

public class LockActivity extends Activity 
{
	private TextView unlock, jam, tangg, wage, senter, persen;
	private ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);
		
		init();
		// mendengarkan peristiwa dipecat selama panggilan	
		StateListener phoneStateListener = new StateListener();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(phoneStateListener,	PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		init();
	}
	
    public void init() {
		ShellExecuter exe = new ShellExecuter();
		String per = "cat /sys/class/power_supply/battery/capacity";
		
		String mper = exe.Executer(per);
		String mjam = new SimpleDateFormat("HH:mm").format(new Date());
		String mtanggal = new SimpleDateFormat("  ,dd MMM yyy").format(new Date());

		makeFullScreen();
        startService(new Intent(this, ServiceKu.class));

        Calendar c = Calendar.getInstance();  
        int mYear = c.get(Calendar.YEAR);  
        int mMonth = c.get(Calendar.MONTH);  
        int mDay = c.get(Calendar.DAY_OF_MONTH);  
		final Kalender kal = new Kalender();  

        String[] jawa = kal.MasehiToJawa(mYear, mMonth, mDay);

		wage = (TextView)findViewById(R.id.wage);
		jam = (TextView)findViewById(R.id.jam);
		tangg = (TextView)findViewById(R.id.tanggal);
		persen = (TextView)findViewById(R.id.persen);
		senter = (TextView)findViewById(R.id.senter);
		img = (ImageView)findViewById(R.id.peng);

		jam.setText(mjam);
		tangg.setText(new StringBuilder().append(jawa[0]+"")+mtanggal);
		wage.setText(new StringBuilder().append(jawa[4]+""));
		wage.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					startActivity(new Intent(LockActivity.this, KalActivity.class));
				}
			});
		senter.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					Senter s = new Senter();
					s.runingKu();
				}
			});
		unlock = (TextView)findViewById(R.id.unlock);
		unlock.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					android.os.Process.killProcess(android.os.Process.myPid());

				}
			});
		img.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					Intent i = new Intent(getApplicationContext(), Pengaturan.class);
					startActivity(i);
				}
			});
		persen.setText(mper+"%");

		

    }
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
								  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
		init();
        return; //Do nothing!
    }
	// Handle events of calls and unlock screen if necessary
	private class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:
					android.os.Process.killProcess(android.os.Process.myPid());
					
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					break;
			}
		}
	};
	
	// Handle button clicks
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		|| (keyCode == KeyEvent.KEYCODE_POWER)
		|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
		    init();
		
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_CAMERA){
			Senter s = new Senter();
			s.runingKu();
			
			return true;
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		}

		return false;

	}
}
