package mx.com.efectosoftware.tracker1;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundUtils {
	
	private static final String LOGTAG = "LMFM";
	private MediaPlayer mMediaPlayer;
	private soundCommunication mSoundCommunication;
	
	public SoundUtils(Context context, soundCommunication sc) {
		mSoundCommunication = sc;
		mMediaPlayer = MediaPlayer.create(context, R.raw.censorlong);
		
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mSoundCommunication.beepAlreadyFinished();
				Log.i(LOGTAG, "Beep Finished !!");
			}
		});
	}
	
	public void startBeep() {
		mMediaPlayer.start();
	}
	
	public void stopBeep() {
		mMediaPlayer.stop();
	}
	
	public interface soundCommunication {
		void beepAlreadyFinished();
	}

}
