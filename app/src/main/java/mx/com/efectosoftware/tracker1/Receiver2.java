package mx.com.efectosoftware.tracker1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver2 extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent serviceIntent = new Intent(context,EnviandoGeolocalizaciones.class);
        if(!EnviandoGeolocalizaciones.isRunning())context.startService(serviceIntent);
	}

}
