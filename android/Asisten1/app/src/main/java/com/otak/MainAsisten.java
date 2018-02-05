package com.otak;

import android.app.*;
import android.widget.*;
import android.content.*;
import android.os.*;
import com.otak.input.MicApi;
import com.otak.init.KalenderKu;
import java.util.*;
import com.otak.input.*;
import android.speech.*;
import android.speech.tts.*;
import java.text.*;
import android.media.*;
import com.otak.init.*;
import com.tools.*;
import android.view.*;


public class MainAsisten extends Activity
{ 
	public String text;
	public SpeechRecognizer micApi;
	public static String suara;
	Intent intent, intentMic, intentTTS;
	public String dataSuara;
	Vibrator mVibrator;
	private MicApi mSpeechManager;
	SharedPreferences settings;
	MicHelper micHelper;
	ServiceTTS sertt;
	TextView mText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startService(new Intent(this, ServiceBoot.class));
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		TextView txt = (TextView)findViewById(R.id.main_text);
		Button btn = (Button)findViewById(R.id.main_btn);
		
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v){
				Toast.makeText(getBaseContext(), "speech",Toast.LENGTH_LONG).show();
				Intent intentV = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intentV.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				micApi.startListening(intentV);
				
			}
		});
		micApi = SpeechRecognizer.createSpeechRecognizer(this);
		micApi.setRecognitionListener(new VoiceHelper(this));
        intentTTS = new Intent(this, ServiceTTS.class);
	    sertt = new ServiceTTS();
		
		startService(new Intent(this, ServiceBoot.class));
	
		try{
			if (getIntent().getStringExtra("destroy").equals("hancur"))
			{
				Toast.makeText(getBaseContext(), "ADA APA TUAN",Toast.LENGTH_LONG).show();
				Intent intentV = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intentV.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				micApi.startListening(intentV);
			}
		}
		catch(Exception e){
			mVibrator.vibrate(400);
			
			stopService(new Intent(this, ServiceMicHelper.class));
		
			CountDownTimer hitungMundur = new CountDownTimer(100, 100)
			{
				public void onTick(long millisUntilFinished){
				}
				public void onFinish()
				{
					Intent intentV = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					intentV.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					micApi.startListening(intentV);
				}
			}.start();
			
		}
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}
	

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		
		if (new ReceiverBoot().layar == 1){
			new ServiceMicHelper().inte = true;
			startService(new Intent(this, ServiceMicHelper.class));
		}
	}

	public String getWeton(int index){
		final KalenderKu kal = new KalenderKu();  
		Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);  
        int mMonth = c.get(Calendar.MONTH);  
        int mDay = c.get(Calendar.DAY_OF_MONTH);  

        String[] jawa = kal.MasehiToJawa(mYear, mMonth, mDay);// (mDay, mMonth, mYear);  

		return jawa[index];
	}

	public void keluaran(String dataText)
	{
		String[] dtts = dataText.split(" ");
		
		String temp = "";
		
		for (int a=0; a<dtts.length; a++)
		{
			
			if(dtts[a].equals("hari")){
				sertt.cepat = 0.8f;
				sertt.str = getWeton(0)+","+getWeton(4);
				startService(new Intent(this, ServiceTTS.class));
				temp = "hari";
				CountDownTimer hitungMundur = new CountDownTimer(3000, 100)
				{
					public void onTick(long millisUntilFinished){
					}
					public void onFinish()
					{
						finish();
					}
				}.start();
			}

			if(dtts[a].equals("tanggal")){
				String tanggal = new SimpleDateFormat("  ,dd,MMM,yyy").format(new Date());
				sertt.cepat = 0.8f;
				sertt.str = getWeton(0)+","+tanggal;
				startService(new Intent(this, ServiceTTS.class));

				temp = "tanggal";
				CountDownTimer hitungMundur = new CountDownTimer(3000, 100)
				{
					public void onTick(long millisUntilFinished){
					}
					public void onFinish()
					{
						finish();
					}
				}.start();
			}
			if(dtts[a].equals("jam")){
				String jam = new SimpleDateFormat("HH,mm").format(new Date());
				sertt.cepat = 0.8f;
				sertt.str = jam;
				startService(new Intent(this, ServiceTTS.class));

				temp = "jam";
				CountDownTimer hitungMundur = new CountDownTimer(3000, 100)
				{
					public void onTick(long millisUntilFinished){
					}
					public void onFinish()
					{
						finish();
					}
				}.start();
			}
			if(dtts[a].equals("file"))
			{
				temp = "file";
				String tempFile = "";
				String[] format ={"zip","rar","dltemp","mp4","mp3","jpg","wav","mht","txt","pdf","doc"};
				
				for(int c=0; c<dtts.length; c++)
				{
					if (dtts[c].equals("cari")){
						for (int d=0; d<dtts.length; d++)
						{
							tempFile = "cari";
							
							if (dtts[d].equals("bernama")){
								for (int e=0; e<dtts.length; e++)
								{
									sertt.cepat = 0.8f;
									sertt.str = "file ,"+dtts[3]+", sedang di cari, tuan";
									startService(new Intent(this, ServiceTTS.class));
									
									Intent hIntent = new Intent(MainAsisten.this, FileExploler.class);
									try
									{
										if (dtts[4].equals("external")){
											hIntent.putExtra("folder","external");
										}
									}
									catch(Exception l){}
									hIntent.putExtra("isi",dtts[3]);
									hIntent.putExtra("index","cari nama");
									startActivity(hIntent);
								}
							}
							else if (dtts[d].equals("format")){
								for (int e=0; e<dtts.length; e++)
								{
									for (int fe=0; fe<format.length; fe++)
									{
										if (dtts[e].equals(format[fe])){
											sertt.cepat = 0.8f;
											sertt.str = "file, "+format[fe]+", sedang di cari, tuan";
											startService(new Intent(this, ServiceTTS.class));
											
											Intent hIntent = new Intent(MainAsisten.this, FileExploler.class);
											try
											{
												if (dtts[4].equals("external")){
													hIntent.putExtra("folder","external");
												}
											}
											catch(Exception l){}
											hIntent.putExtra("format",format[fe]);
											hIntent.putExtra("index","cari format");
											startActivity(hIntent);
										}
									}
								}
								
							}
						}
								
					}
				}
				for(int c=0; c<dtts.length; c++)
				{
					if (dtts[c].equals("buka")){
						for (int d=0; d<dtts.length; d++)
						{
							
						}
					}
				}
				if (tempFile.equals("")){
					startActivity(new Intent(MainAsisten.this, FileExploler.class));
				}
			}// for c
		} // if file
		if (temp.equals("")) {
			sertt.cepat = 1.0f;
			sertt.str = "saya nggak dengar, tuan";
			startService(new Intent(this, ServiceTTS.class));

			temp = "";
			CountDownTimer hitungMundur = new CountDownTimer(3000, 100)
			{
				public void onTick(long millisUntilFinished){
				}
				public void onFinish()
				{
					finish();
				}
			}.start();
		}
	}
}


class VoiceHelper implements RecognitionListener
{
	MainAsisten mVoiceRecognition;

	public VoiceHelper(MainAsisten instance) {
		mVoiceRecognition = instance;
	}
	public void onResults(Bundle data) {
		ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		mVoiceRecognition.text = matches.get(0);
		mVoiceRecognition.keluaran(matches.get(0));
	}

	public void onBeginningOfSpeech() {
		mVoiceRecognition.text = "Good!";
	}
	public void onBufferReceived(byte[] buffer) {
		//Log.d(TAG, "onBufferReceived");
	}
	public void onEndOfSpeech() {
		//Log.d(TAG, "onEndofSpeech");
		mVoiceRecognition.text = "emm";
		
	}
	public void onError(int error) {
		//Log.d(TAG, "error " + error);		
		mVoiceRecognition.text = "error " + error;
		mVoiceRecognition.keluaran("eror");
	}
	public void onEvent(int eventType, Bundle params) {
		//Log.d(TAG, "onEvent " + eventType);
	}
	public void onPartialResults(Bundle partialResults) {
		//Log.d(TAG, "onPartialResults");
	}
	public void onReadyForSpeech(Bundle params) {
		//Log.d(TAG, "onReadyForSpeech");
	}		
	public void onRmsChanged(float rmsdB) {
		//Log.d(TAG, "onRmsChanged");
	}
}


