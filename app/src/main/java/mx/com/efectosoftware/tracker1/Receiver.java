package mx.com.efectosoftware.tracker1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent serviceIntent = new Intent(context,GPSTracker.class);
        if(!GPSTracker.isRunning())context.startService(serviceIntent);
		
	}

}
