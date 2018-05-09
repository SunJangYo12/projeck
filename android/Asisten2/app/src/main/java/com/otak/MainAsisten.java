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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.*;
import android.util.*;
import android.content.pm.*;
import com.otak.memori.*;
import android.speech.tts.*;
import java.util.*;
import android.database.sqlite.*;
import android.database.*;
import android.net.*;

public class MainAsisten extends Activity implements TextToSpeech.OnInitListener
{ 
	public String text;
	private SpeechRecognizerManager mSpeechManager;
	
	Intent intent, intentMic, intentTTS;
	public String dataSuara;
	Vibrator mVibrator;
	private static String[] dataSpeech;
	private String dataSpeech1;
	private static String temp;
	SharedPreferences settings;
	SharedPreferences.Editor addSettings;
	
	ServiceTTS sertt;
	TextView txt;
	Button btn;
	String ulangi, ngobrol; 
	protected AudioManager mAudioManager;
	
	int judul = 0;
	
	private boolean initialized;
	private String queuedText;
	private TextToSpeech tts;
	private String kata;
	DBHelper dbhelper;
	private int no;
	protected Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		btn = (Button)findViewById(R.id.main_btn);
		btn.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v){
					btn.setClickable(false);

					if(mSpeechManager==null)
					{
						SetSpeechListener();
					}
					else if(!mSpeechManager.ismIsListening())
					{
						mSpeechManager.destroy();
						SetSpeechListener();
					}
					txt.setText("Voice Siap ..");

				}
			});
		
		settings = getSharedPreferences("Settings", 0);	
		addSettings = settings.edit();
		no = settings.getInt("no memori",0);
		
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	    txt = (TextView)findViewById(R.id.main_text);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		try{
			if (getIntent().getStringExtra("layar").equals("off"))
			{
				if(mSpeechManager==null)
				{
					SetSpeechListener();
				}
				else if(!mSpeechManager.ismIsListening())
				{
					mSpeechManager.destroy();
					SetSpeechListener();
				}
			}
			if (getIntent().getStringExtra("layar").equals("on"))
			{
				finish();
			}
		}
		catch(Exception e){}
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		startService(new Intent(getBaseContext(), ServiceTTS.class));
		
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		super.onBackPressed();
		if(mSpeechManager!=null)
		{
			Toast.makeText(this,"destroy", Toast.LENGTH_LONG).show();
			mSpeechManager.destroy();
			mSpeechManager = null;
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		addSettings.putInt("no memori", no);	
		addSettings.commit();
		if(mSpeechManager!=null)
		{
			Toast.makeText(this,"destroy", Toast.LENGTH_LONG).show();
			mSpeechManager.destroy();
			mSpeechManager = null;
		}
		startService(new Intent(getBaseContext(), ServiceTTS.class));
		
	}
	
 

	
	private void SetSpeechListener()
    {
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
				@Override
				public void onResults(ArrayList<String> results)
				{
					SetSpeechListener();
					
					if(results!=null && results.size()>0)
					{
						
						if(results.size()==1)
						{
							mSpeechManager.destroy();
							mSpeechManager = null;
							txt.setText(results.get(0));
							keluaran(results.get(0));
							
						}
						else {
							StringBuilder sb = new StringBuilder();
							if (results.size() > 5) {
								results = (ArrayList<String>) results.subList(0, 5);
							}
							for (String result : results) {
								sb.append(result).append("\n");
							}
							txt.setText(sb.toString());
							
						}
					}
					else
						txt.setText("tidak ada hasil");
				}
			});
    }
	
	public void ngomong(String data, float cepat)
	{
		SharedPreferences.Editor editor = settings.edit();	
		editor.putBoolean("ctrMic", false);	
		editor.commit();
		
		tts.setSpeechRate(cepat);
		speak(data);
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
		no++;
		dbhelper = new DBHelper(this);
		tts = new TextToSpeech(this /* context */, this /* listener */);
		tts.setOnUtteranceProgressListener(mProgressListener);
		
		Toast.makeText(this,""+no,Toast.LENGTH_LONG).show();
	
		SQLiteDatabase dbk = dbhelper.getWritableDatabase();
		dbk.execSQL("INSERT INTO otak (no, memori) VALUES ('"+no+"', '"+data+"');");
	
		dataSpeech = data.split(" ");
		dataSpeech1 = data;
		temp = "";
		
		for (int a=0; a<dataSpeech.length; a++)
		{
			outStatus(a);
			outFile(a);
			outAlat(a);
			outNgobrol(a);
			outPengaturan(a);
			outWaktu(a);
		}
	}
	private void outWaktu(int index)
	{
		if (dataSpeech[index].equals("cuaca"))
		{
			
			for(int a=0; a<dataSpeech.length; a++){
				if (dataSpeech[a].equals("di"))
				{
					SharedPreferences.Editor editor = settings.edit();	
					editor.putString("cuaTempat", dataSpeech1.substring(9, dataSpeech1.length()));	
					editor.commit();
				}
				if (dataSpeech[a].equals("refresh")){
					String negara = settings.getString("cuaNegara","");
					String tempat = settings.getString("cuaTempat","");
					String surl = "https://sunjangyo12.000webhostapp.com/cuaca.php/";
					String url = surl+"?negara="+negara+"&tempat="+tempat;
					
					CallWebPageTask task = new CallWebPageTask();
					task.applicationContext = MainAsisten.this;
					task.execute(new String[] { url });
					
					
					ngomong("baik tuan. cuaca direfresh. silahkan tunggu",0.9f);
				}
				if (dataSpeech[a].equals("hari")){
					for (int b=0; b<dataSpeech.length; b++)
					{
						if (dataSpeech[b].equals("ini")){
							
						}
						if (dataSpeech[b].equals("besok")){

						}
					}
				}
			}	
		}
		if(dataSpeech[index].equals("hari")){
			txt.setText(getWeton(0)+","+getWeton(4));
			txt.setTextColor(Color.WHITE);
			txt.setTextSize(80);

			ngomong(getWeton(0)+","+getWeton(4), 0.8f);
		}
		if(dataSpeech[index].equals("tanggal")){
			String tanggal = new SimpleDateFormat("  ,dd,MMM,yyy").format(new Date());

			txt.setText(getWeton(0)+","+tanggal);
			txt.setTextColor(Color.WHITE);
			txt.setTextSize(30);
			
			ngomong(getWeton(0)+","+tanggal, 0.8f);
		}
		if (dataSpeech[index].equals("jam")){
			String jam = new SimpleDateFormat("HH:mm").format(new Date());
			ngomong(jam,0.8f);
			txt.setText(jam);
		}
	}
	private void outPengaturan(int index)
	{
		settings = getSharedPreferences("Settings", 0);	
		SharedPreferences.Editor editor = settings.edit();	
		
		if (dataSpeech[index].equals("hemat")){
			if(settings.getBoolean("mode hemat",true))
			{
				ngomong("sistem pendengaran di matikan", 0.9f);
				editor.putBoolean("mode hemat", false);	
				editor.commit();
			}
			else{
				editor.putBoolean("mode hemat", true);	
				editor.commit();
				ngomong("melakukan reboot pendengaran", 0.9f);
		
			}
			
		}
		
	}

	private void outNgobrol(int index)
	{
		
		if (dataSpeech[index].equals("halo")){
			ngomong("ya halo selamat malam mas", 0.9f);
		}
		if (dataSpeech[index].equals("airi")){
			ngomong("ya mas", 0.9f);
			startActivity(new Intent(this, ScreenOn.class));
		}
		if (dataSpeech[index].equals("istirahat")){
			ngomong("sistem stenbay", 0.9f);
			if(mSpeechManager!=null)
			{
				Toast.makeText(this,"destroy", Toast.LENGTH_LONG).show();
				mSpeechManager.destroy();
				mSpeechManager = null;
			}
			finish();
		}
		if (dataSpeech[index].equals("buku")){
			
			if (openApp(this, "org.kiwix.kiwixmobile")){
				
				Toast.makeText(this,"sukses",Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this,"buku gagal silahkan download kiwix.apk",Toast.LENGTH_LONG).show();
				
			}
		}
		if (dataSpeech[index].equals("musik")){
			Intent intent = new Intent();  
			intent.setAction(android.content.Intent.ACTION_VIEW);  
			intent.setDataAndType(Uri.parse("sdcard1/audio"), "audio/*");  
			startActivity(intent);
		}
		
		if (dataSpeech[index].equals("riwayat")){
			no++;
			for (int a=0; a<dataSpeech.length; a++)
			{
				if (dataSpeech[a].equals("masukkan")){
					ngomong("menulis riwayat", 0.9f);
					
					String hasil = dataSpeech1.substring(16, dataSpeech1.length());
					
					SQLiteDatabase dbk = dbhelper.getWritableDatabase();
					dbk.execSQL("INSERT INTO otak (no, riwayat) VALUES ('"+no+"', '"+hasil+"');");
					
					Toast.makeText(getApplicationContext(), "Berhasil"+hasil, Toast.LENGTH_LONG).show();
					
				}
				if (dataSpeech[a].equals("tampilkan")){
					startActivity(new Intent(this,MemoriActivity.class));
				}
				if (dataSpeech[a].equals("hapus")){
					ngomong("tahan list list yang akan dihapus", 0.9f);
					
				}
			}
		}
		
	}
	
	private void outAlat(int index)
	{
		if (dataSpeech[index].equals("senter")  ||  dataSpeech[index].equals("berkedip")){
			if (dataSpeech[index].equals("berkedip")){
		    	for (int i=0; i<18; i++)
		    	{
			    	CountDownTimer hitungMundur = new CountDownTimer(200, 100)
			    	{
				    	public void onTick(long millisUntilFinished){
				     	}
				     	public void onFinish()
				    	{
					    	Senter s = new Senter();
					    	s.runingKu();
					    }
			    	}.start();
		    	}
			}
			else{
		    	Senter s = new Senter();
		    	s.runingKu();
			}
		}
		if (dataSpeech[index].equals("kompas")){
			startActivity(new Intent(this, Kompas.class));
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
					ngomong(new CpuMon().cpuPakai+", mas", 0.8f);
				}
				if (dataSpeech[b].equals("aplikasi")){
					tempStatus = "aplikasi";
					ngomong("ini status aplikasi yang berjalan, mas", 0.8f);
					startActivity(new Intent(this, TaskList.class));
				}
				if (dataSpeech[b].equals("baterai")){
					for (int c=0; c<dataSpeech.length; c++)
					{
						if (dataSpeech[c].equals("tegangan")){
							ngomong("tegangan baterai = "+new ReceiverBoot().dataVolt,0.9f);
						}
						if (dataSpeech[c].equals("arus")){
							ngomong("arus baterai= "+new ReceiverBoot().dataAmp,0.9f);
						}
						if (dataSpeech[c].equals("suhu")){
							ngomong("suhu baterai = "+new ReceiverBoot().dataTemp,0.9f);
						}
					}
				}
			}
			if (tempStatus.equals("")){
				startActivity(new Intent(this, ActivityStatus.class));
			}
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
							for (int f=0; f<dataSpeech.length; f++)
							{
								if (dataSpeech[f].equals("external")){
									inama.putExtra("memori","ex");
								}
								if (dataSpeech[f].equals("internal")){
									inama.putExtra("memori","in");
								}
							}
							ngomong("file "+dataSpeech[3]+", sedang dicari "+ngomongMemori,0.9f);
							
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
										
										for (int f=0; f<dataSpeech.length; f++)
										{
											if (dataSpeech[f].equals("external")){
												iformat.putExtra("memori","ex");
											}
											if (dataSpeech[f].equals("internal")){
												iformat.putExtra("memori","in");
											}
										}
										ngomong("file "+format[fe]+", sedang dicari "+ngomongMemori, 1.0f);
										
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
	
	//Method untuk Mengirimkan data keserver
	public String getRequest(String Url){
		String sret;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(Url);
        try{
            HttpResponse response = client.execute(request);
            sret= request(response);
        }
		catch(Exception ex){
			sret= "No Internet !!!";
        }
        return sret;

    }
	
	// Method untuk Menerima data dari server
	public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }
		catch(Exception ex){
            result = "Error";
        }
        return result;
    }
	// Class untuk implementasi class AscyncTask
	private class CallWebPageTask extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog;
		protected Context applicationContext;

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, "Login Process", "Please Wait...", true);
		}

	    @Override
	    protected String doInBackground(String... urls) {
			String response = "";
			response = getRequest(urls[0]);
			return response;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	this.dialog.cancel();
	    	txt.setText(result);
			
			SQLiteDatabase dbk = dbhelper.getWritableDatabase();
			dbk.execSQL("INSERT INTO otak (no, cuaca) VALUES ('"+no+"', '"+result+"');");

			Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
		}
	}
	
	// mtod app external
	public static boolean openApp(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		try {
			Intent i = manager.getLaunchIntentForPackage(packageName);
			if (i == null) {
				return false;
				//throw new ActivityNotFoundException();
			}
			i.addCategory(Intent.CATEGORY_LAUNCHER);
			context.startActivity(i);
			return true;
		} 
		catch (ActivityNotFoundException e) {
			return false;
		}
	}
	
	public void speak(String text) { 

		if (!initialized) {
			queuedText = text;
			return;
		}
		queuedText = null;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
		tts.speak(text, TextToSpeech.QUEUE_ADD, map);
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			initialized = true;
			tts.setLanguage(Locale.getDefault());

			if (queuedText != null) {
				speak(queuedText);
			}
		}
	}
	
	// tts
	private abstract class runnable implements Runnable {
	}
	
	// tts
	private UtteranceProgressListener mProgressListener = new UtteranceProgressListener() {
		@Override
		public void onStart(String utteranceId) {
		} // Do nothing

		@Override
		public void onError(String utteranceId) {
		} // Do nothing.

		@Override
		public void onDone(String utteranceId) {

			new Thread()
			{
				public void run()
				{
					MainAsisten.this.runOnUiThread(new runnable()
						{
							public void run()
							{

								Toast.makeText(getBaseContext(), "TTS Completed", Toast.LENGTH_SHORT).show();
								SharedPreferences.Editor editor = settings.edit();	
								editor.putBoolean("ctrMic", true);	
								editor.commit();
								
								if(mSpeechManager==null)
								{
									SetSpeechListener();
								}
								else if(!mSpeechManager.ismIsListening())
								{
									mSpeechManager.destroy();
									SetSpeechListener();
								}
							}
						});
				}
			}.start();

		}
	}; 
}


class SpeechRecognizerManager {

    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent intent;

    protected boolean mIsListening;
    private boolean mIsStreamSolo; 
	SharedPreferences settings;
	

    public boolean mute=false;
    private final static String TAG="SpeechRecognizerManager";

    private onResultsReady mListener;

	Context context;
    public SpeechRecognizerManager(Context context,onResultsReady listener)
    {
		this.context = context;
		settings = context.getSharedPreferences("Settings", 0);	
		
        try{
            mListener=listener;
        }
        catch(ClassCastException e)
        {
            Log.e(TAG,e.toString());
        }
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
		
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		
        startListening();

    }

    private void listenAgain()
    {
        if(mIsListening) {
            mIsListening = false;
            mSpeechRecognizer.cancel();
			if(settings.getBoolean("ctrMic",true)){
				startListening();
			}
        
        }
    }


    private void startListening()
    {
        if(!mIsListening)
        {
            mIsListening = true; 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // turn off beep sound
                
            }
            mSpeechRecognizer.startListening(intent);
        }
    }

    public void destroy()
    {
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer=null;
        }

    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {}

        @Override
        public synchronized void onError(int error)
        {

            if(error==SpeechRecognizer.ERROR_RECOGNIZER_BUSY)
            {
                if(mListener!=null) {
                    ArrayList<String> errorList=new ArrayList<String>(1);
                    errorList.add("ERROR RECOGNIZER BUSY");
                    if(mListener!=null)
						mListener.onResults(errorList);
                }
                return;
            }

            if(error==SpeechRecognizer.ERROR_NO_MATCH)
            {
                if(mListener!=null)
                    mListener.onResults(null);
            }

            if(error==SpeechRecognizer.ERROR_NETWORK)
            {
                ArrayList<String> errorList=new ArrayList<String>(1);
                errorList.add("STOPPED LISTENING");
                if(mListener!=null)
                    mListener.onResults(errorList);
            }
            Log.d(TAG, "error = " + error);
           	new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						listenAgain();
					}
				},100);

        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {}

        @Override
        public void onResults(Bundle results)
        {
            if(results!=null && mListener!=null)
				mListener.onResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            listenAgain();

        }

        @Override
        public void onRmsChanged(float rmsdB) {}

    }

    public boolean ismIsListening() {
        return mIsListening;
    }


    public interface onResultsReady
    {
        public void onResults(ArrayList<String> results);
    }

}
