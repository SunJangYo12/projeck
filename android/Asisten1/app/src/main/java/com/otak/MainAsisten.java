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
import com.status.*;
import android.graphics.*;


public class MainAsisten extends Activity
{ 
	public String text;
	public SpeechRecognizer micApi;
	
	Intent intent, intentMic, intentTTS;
	public String dataSuara;
	Vibrator mVibrator;
	private static String[] dataSpeech;
	private static String temp;
	SharedPreferences settings;
	
	ServiceTTS sertt;
	TextView txt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startService(new Intent(this, ServiceBoot.class));
		startService(new Intent(this, ServiceStatus.class));
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	    txt = (TextView)findViewById(R.id.main_text);
		
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
	
	public void ngomong(String data, float cepat)
	{
		sertt.cepat = cepat;
		sertt.str = data;
		startService(new Intent(this, ServiceTTS.class));
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

	public void keluaran(String data)
	{
		dataSpeech = data.split(" ");
		temp = "";
		
		for (int a=0; a<dataSpeech.length; a++)
		{
			outHari(a);
			outTanggal(a);
			outStatus(a);
			outJam(a);
			outFile(a);
		}
	
	
	}
	
	private void outHari(int index)
	{
		if(dataSpeech[index].equals("hari")){
			ngomong(getWeton(0)+","+getWeton(4), 0.8f);
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
	}
	private void outTanggal(int index)
	{
		if(dataSpeech[index].equals("tanggal")){
			String tanggal = new SimpleDateFormat("  ,dd,MMM,yyy").format(new Date());
			
			ngomong(getWeton(0)+","+tanggal, 0.8f);
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
	}
	private void outStatus(int index)
	{
		if (dataSpeech[index].equals("status")){
			temp = "status";
			String tempStatus = "";
			for (int b=0; b<dataSpeech.length; b++)
			{
				if (dataSpeech[b].equals("proses")){
					tempStatus = "cpu";
					ngomong(new CpuMon().cpuPakai+", tuan", 0.8f);
				}
				if (dataSpeech[b].equals("aplikasi")){
					tempStatus = "aplikasi";
					ngomong("ini status aplikasi yang berjalan, tuan", 0.8f);
					startActivity(new Intent(this, TaskList.class));
				}
			}
			if (tempStatus.equals("")){
				ngomong("status ponsel ditampilkan", 0.8f);
				startActivity(new Intent(this, ActivityStatus.class));
			}
		}
	}
	
	private void outJam(int index)
	{
		if (dataSpeech[index].equals("jam")){
			String jam = new SimpleDateFormat("HH,mm").format(new Date());
			ngomong(jam,0.8f);
			
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
	}
	private void outFile(int index)
	{
		if(dataSpeech[index].equals("file"))
		{
			temp = "file";
			String tempFile = "";
			String[] format ={"zip","rar","dltemp","mp4","mp3","jpg","wav","mht","txt","pdf","doc"};

			for(int c=0; c<dataSpeech.length; c++)
			{
				if (dataSpeech[c].equals("cari")){
					for (int d=0; d<dataSpeech.length; d++)
					{
						tempFile = "cari";

						if (dataSpeech[d].equals("bernama")){
							Intent inama = new Intent(MainAsisten.this, FileExploler.class);
							txt.setText("Tunggu...");
							txt.setTextColor(Color.RED);
							txt.setTextSize(80);
							
							String ngomongMemori = "";
							for (int e=0; e<dataSpeech.length; e++)
							{
								inama.putExtra("isi",dataSpeech[3]);
							}
							
							try{
								inama.putExtra("memori","ex");
								ngomongMemori = "di External memori";
							}
							catch(Exception h){}
							
							ngomong("file "+dataSpeech[3]+", sedang dicari "+ngomongMemori,0.8f);
							
							inama.putExtra("index","cari nama");
							startActivity(inama);
						}
						else if (dataSpeech[d].equals("format")){
							for (int e=0; e<dataSpeech.length; e++)
							{
								for (int fe=0; fe<format.length; fe++)
								{
									if (dataSpeech[e].equals(format[fe])){
										txt.setText("Tunggu...");
										txt.setTextColor(Color.RED);
										txt.setTextSize(80);
										
										Intent iformat = new Intent(MainAsisten.this, FileExploler.class);
										String ngomongMemori = "";
										try{
											iformat.putExtra("memori","ex");
											ngomongMemori = "di External memori";
										}
										catch(Exception h){}
										ngomong("file "+format[fe]+", sedang dicari "+ngomongMemori, 0.8f);
										
										iformat.putExtra("format",format[fe]);
										iformat.putExtra("index","cari format");
										startActivity(iformat);
									}
								}
							}

						}
					}

				}
			}

			if (tempFile.equals("")){
				startActivity(new Intent(MainAsisten.this, FileExploler.class));
			}
		}// for c
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
		mVoiceRecognition.txt.setText(matches.get(0));
		mVoiceRecognition.keluaran(matches.get(0));
	}

	public void onBeginningOfSpeech() {
		mVoiceRecognition.txt.setText("Good!");
		mVoiceRecognition.txt.setTextColor(Color.WHITE);
		mVoiceRecognition.txt.setTextSize(50);
	}
	public void onBufferReceived(byte[] buffer) {
		//Log.d(TAG, "onBufferReceived");
	}
	public void onEndOfSpeech() {
		//Log.d(TAG, "onEndofSpeech");
		mVoiceRecognition.txt.setText("emm");
		
	}
	public void onError(int error) {
		//Log.d(TAG, "error " + error);		
		mVoiceRecognition.txt.setText("error " + error);
		mVoiceRecognition.keluaran("eror");
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					listenAgain();
				}
			},100);
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


