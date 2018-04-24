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

public class MainAsisten extends Activity
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
	private String surl = "https://sunjangyo12.000webhostapp.com/cuaca.php/";
	protected AudioManager mAudioManager;
	private DBHelper dbhelper;
	int judul = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		settings = getSharedPreferences("Settings", 0);	
		addSettings = settings.edit();
		
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

		}
		catch(Exception e){
			
		}
		
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
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
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
		if(mSpeechManager!=null)
		{
			Toast.makeText(this,"destroy", Toast.LENGTH_LONG).show();
			mSpeechManager.destroy();
			mSpeechManager = null;
		}
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
						
						Toast.makeText(MainAsisten.this,"hsj",Toast.LENGTH_LONG).show();
						
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
			// antri
			for(int a=0; a<dataSpeech.length; a++){
				if (dataSpeech[a].equals("di"))
				{
					CallWebPageTask task = new CallWebPageTask();
					task.applicationContext = MainAsisten.this;
					String url = surl+"?negara="+"indonesia"+"&tempat="+"";
					task.execute(new String[] { url });
					
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
				ngomong("melakukan reboot pendengaran", 0.9f);
				editor.putBoolean("mode hemat", true);	
				editor.commit();
			}
			
		}
		if (dataSpeech[index].equals("suara")){
			if(settings.getBoolean("suara",true)){
				editor.putBoolean("suara", false);	
				editor.commit();
				Toast.makeText(this,"on",Toast.LENGTH_LONG).show();
				mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
				mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
				mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
				mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
	    	}
			else{
				editor.putBoolean("suara", true);	
				editor.commit();
				Toast.makeText(this,"zzzzzzz",Toast.LENGTH_LONG).show();

				mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
				mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
				mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
				mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
				
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
			finish();
		}
		if (dataSpeech[index].equals("buku")){
			if (openApp(this, "org.kiwix.kiwixmobile")){
				startActivity(new Intent(this,MemRiwayat.class));
			
				Toast.makeText(this,"sukses",Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this,"buku gagal",Toast.LENGTH_LONG).show();
				
			}
		}
		if (dataSpeech[index].equals("riwayat")){
			dbhelper = new DBHelper(getApplicationContext());
		
			for (int a=0; a<dataSpeech.length; a++)
			{
				if (dataSpeech[a].equals("masukkan")){
					ngomong("menulis riwayat", 0.9f);
					
					String hasil = dataSpeech1.substring(16, dataSpeech1.length());
					dbhelper.addNote(hasil, "");
				}
				if (dataSpeech[a].equals("tampilkan")){
					startActivity(new Intent(this,MemRiwayat.class));
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
			}
			if (tempStatus.equals("")){
				ngomong("status ponsel ditampilkan", 1.0f);
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
	
}


class SpeechRecognizerManager {

    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;

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
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
										 RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
										 context.getPackageName());
        startListening();

    }

    private void listenAgain()
    {
        if(mIsListening) {
            mIsListening = false;
            mSpeechRecognizer.cancel();
            startListening();
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
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }
    }

    public void destroy()
    {
        mIsListening=false;
        if (!mIsStreamSolo) {
                    mIsStreamSolo = true;
        }
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
