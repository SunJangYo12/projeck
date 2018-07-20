package com.otak;


import android.app.*;
import android.content.*;
import android.os.*;
import com.otak.input.*;
import android.widget.*;
import android.media.*;
import android.media.MediaRecorder.AudioSource;

public class ServiceMicHelper extends Service
{
	MicThread mThread;
	SharedPreferences settings;
	public static boolean inte = false;
	
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		settings = getSharedPreferences("Settings", 0);	
		
		mThread = new MicThread(this);
		mThread.recording = true;
		mThread.start();
	}

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		
	/*	Vibrator mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 100, 200, 60, 70 };
		mVibrator.vibrate(pattern, 0);
		*/
		mThread.recording = false;
		mThread.clean();

		Toast.makeText(this, "destroy "+mThread.frequency, Toast.LENGTH_LONG).show();

	}
	
	class MicThread extends Thread
	{
		public boolean recording; 
		public int frequency; 
		private Context context;
		
		AudioRecord recorder;
		int numCrossing,p;
		short audioData[];
		int bufferSize;
		
		public MicThread(Context ctx) {
			context = ctx;
		}

		@Override
		public void run() {
			

			bufferSize=AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_CONFIGURATION_MONO,
													AudioFormat.ENCODING_PCM_16BIT)*3; //get the buffer size to use with this audio record

			recorder = new AudioRecord (AudioSource.MIC,8000,AudioFormat.CHANNEL_CONFIGURATION_MONO,
										AudioFormat.ENCODING_PCM_16BIT,bufferSize); //instantiate the AudioRecorder

			audioData = new short [bufferSize]; 

			while (recording)
			{
				if (recorder.getState()==android.media.AudioRecord.STATE_INITIALIZED) // check to see if the recorder has initialized yet.
					if (recorder.getRecordingState()==android.media.AudioRecord.RECORDSTATE_STOPPED) {
						recorder.startRecording(); 
					}
					else {

						recorder.read(audioData,0,bufferSize);
						
						numCrossing=0;
						for (p=0;p<bufferSize/4;p+=4) 
					    {
							if (audioData[p]>0 && audioData[p+1]<=0) numCrossing++;
							if (audioData[p]<0 && audioData[p+1]>=0) numCrossing++;
							if (audioData[p+1]>0 && audioData[p+2]<=0) numCrossing++;
							if (audioData[p+1]<0 && audioData[p+2]>=0) numCrossing++;
							if (audioData[p+2]>0 && audioData[p+3]<=0) numCrossing++;
							if (audioData[p+2]<0 && audioData[p+3]>=0) numCrossing++;
							if (audioData[p+3]>0 && audioData[p+4]<=0) numCrossing++;
							if (audioData[p+3]<0 && audioData[p+4]>=0) numCrossing++;
						}//for p

						for (p=(bufferSize/4)*4;p<bufferSize-1;p++) {
							if (audioData[p]>0 && audioData[p+1]<=0) numCrossing++;
							if (audioData[p]<0 && audioData[p+1]>=0) numCrossing++;
						}
						
						frequency=(8000/bufferSize)*(numCrossing/2);
						
						if (frequency >= settings.getInt("Spektrum_awal", 0))
						{
							if (inte){
								Intent mIntent = new Intent(context, MainAsisten.class);
								mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(mIntent);
								inte = false;
							}
						}
					}
			} //while recording

		}//run
		public void clean() {
			if (recorder != null && recorder.getState() == AudioRecord.STATE_INITIALIZED)
			{
				try {
					if (recorder.getRecordingState() ==	AudioRecord.RECORDSTATE_RECORDING)
						recorder.stop();
					recorder.release();
				}
				catch (Exception e) {}
			}
		}

	}//recorderThread

}
