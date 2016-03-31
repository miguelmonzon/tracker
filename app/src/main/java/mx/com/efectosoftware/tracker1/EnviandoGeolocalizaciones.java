package mx.com.efectosoftware.tracker1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import mx.com.efectosoftware.tracker.db1.ToursDataSource;


public class EnviandoGeolocalizaciones extends Service {
	
	private static final String LOGTAG = "LMFM";
	private static EnviandoGeolocalizaciones instance  = null; 
	
	ToursDataSource datasource;
	
	CountDownTimer cronometro2;
	
	public static boolean isRunning() { 
	      return instance != null; 
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		instance=this;
		datasource = new ToursDataSource(this);
		datasource.open();
		
		cronometro2 = new CountDownTimer(86400000,30000) {  // Cronometro a 24 horas con intervalos de 5 minutos
//		cronometro2 = new CountDownTimer(86400000,300000) {  // Cronometro a 24 horas con intervalos de 5 minutos
//		cronometro2 = new CountDownTimer(20000,1000) {  // Cronometro a 2 segundos
//		cronometro2 = new CountDownTimer(240000,1000) {  // Cronometro a 240 segundos
					
					
			@Override
			public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
				
//				if (millisUntilFinished <= 86160000 ) {
				if (millisUntilFinished <= 86400000) {
					if (haveNetworkConnection()) {
//						Log.i(LOGTAG, "Ya es hora de enviar a Servidor: " + millisUntilFinished/1000 + " segundos");
//						Toast.makeText(getApplicationContext(), "Hora de enviar geolocalizaciones al Servidor", Toast.LENGTH_LONG).show();
						datasource.enviaGeolocalizacionesInvisiblesAlServidor();
					} else {
//						Toast.makeText(getApplicationContext(), "No hay internet. No se puede enviar al servidor", Toast.LENGTH_LONG).show();
						Log.i(LOGTAG, "No hay internet. No se puede enviar al servidor");
					}
				} else {
					Log.i(LOGTAG, "AUN NO, AGUANTA: " + millisUntilFinished/1000 + " segundos");
				}
			}
					
			@Override
			public void onFinish() {
			// TODO Auto-generated method stub
				Log.i(LOGTAG, "Reiniciamos el cronometro");
				cronometro2.start();
			}
		};
		cronometro2.start();				
			
	}
	
	

	private boolean haveNetworkConnection() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
//		cronometro2.start();
	}




	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
