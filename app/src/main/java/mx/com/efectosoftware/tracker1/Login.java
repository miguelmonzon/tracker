package mx.com.efectosoftware.tracker1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.efectosoftware.tracker.db1.ToursDataSource;

public class Login extends Activity {
	
	private static final String LOGTAG = "LMFM";
	
	GPSTracker mGpsTracker = new GPSTracker();

	TextView version, tvImei, tvMensajeStatusTracker;
	
	Httppostaux post;
	String URL_connect;
	
	ToursDataSource datasource;
	
	String imeiTelefono;
	Button btIngresar, btStatusTracker;
	
	Intent in;
	
	// Variables para obtener los Parametros de la Aplicacion Movil
//	String versionAppMovil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Quita la barra de titulo
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//		Quita la barra de notificaciones
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		
		Log.i(LOGTAG, "onCreate al entrar");
		
//		version = (TextView) findViewById(R.id.textView8);
		tvImei = (TextView) findViewById(R.id.imei);
		tvMensajeStatusTracker = (TextView) findViewById(R.id.tvMensajeStatusTracker);
		btIngresar = (Button) findViewById(R.id.ingresar);
		btStatusTracker = (Button) findViewById(R.id.btStatusTracker);

		in = new Intent(Login.this,GPSTracker.class);
		
//		mGpsTracker = new GPSTracker();
		
		Log.i(LOGTAG, "Primera validacion si GPSTracker.isRunning onCreate");
		if(GPSTracker.isRunning()) {
//			Log.i(LOGTAG, "GPSTracker ya estaba prendido por lo tanto lo deje que siguiera corriendo");
			tvMensajeStatusTracker.setText("El Sistema de Geolocalizacion esta PRENDIDO");
			btStatusTracker.setText("APAGAR");
		} else {
//			Login.this.startService(in);
//			Log.i(LOGTAG, "GPSTracker estaba apagado por lo tanto lo prendi");
			tvMensajeStatusTracker.setText("El Sistema de Geolocalizacion esta APAGADO");
			btStatusTracker.setText("PRENDER");
		}
		
		Intent in2 = new Intent(Login.this,EnviandoGeolocalizaciones.class);
		if(!EnviandoGeolocalizaciones.isRunning())
			Login.this.startService(in2);
		
		datasource = new ToursDataSource(this);
		datasource.open();
//		Log.i(LOGTAG, "Se abre la base de datos");
		
		datasource.eliminaGeolocalizacionesYaEnviadasAlServidor();
//		datasource.muestraTablaGeolocalizaciones();
		
//		version = (TextView) findViewById(R.id.textView8);
//		tvImei = (TextView) findViewById(R.id.imei);
//		tvMensajeStatusTracker = (TextView) findViewById(R.id.tvMensajeStatusTracker);
//		btIngresar = (Button) findViewById(R.id.ingresar);
//		btStatusTracker = (Button) findViewById(R.id.btStatusTracker);
		
		post = new Httppostaux();
		URL_connect = new URLConexion().Conexion();
		
		imeiTelefono = obtenerIMEI();
		tvImei.setText(imeiTelefono);
		
		btStatusTracker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!GPSTracker.isRunning()) {
					Toast.makeText(getApplicationContext(), "Servicio corriendo", Toast.LENGTH_SHORT).show();
					Login.this.startService(in);
					tvMensajeStatusTracker.setText("Sistema de geolocalizacion PRENDIDO");
					btStatusTracker.setText("APAGAR");
				} else {
					Toast.makeText(getApplicationContext(), "Servicio apagado", Toast.LENGTH_SHORT).show();
					Login.this.stopService(in);
					tvMensajeStatusTracker.setText("Sistema de geolocalizacion APAGADO");
					btStatusTracker.setText("PRENDER");
				}
			}
		});
		
		btIngresar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Iniciar Reporte", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(Login.this, MenuPrincipal.class);
				i.putExtra("imeiTelefono", imeiTelefono);
				startActivity(i);
//				finish();
			}
		});
		
		Log.i(LOGTAG, "onCreate al terminar");
		
//		versionAppMovil = (String) version.getText();		
		
	} // Llave de fin de onCreate
	
	private void checaEstatusServicioEnviaGeolocalizaciones() {
		if(EnviandoGeolocalizaciones.isRunning()) {
			Log.i(LOGTAG, "El servicio de envio de geolocalizaciones esta: PRENDIDO");
		} else {
			Log.i(LOGTAG, "El servicio de envio de geolocalizaciones esta: APAGADO");
		}
	}
	
	private void checaEstatusServicioGPSTracker() {
		if(GPSTracker.isRunning()) {
			Log.i(LOGTAG, "El servicio tracker esta: PRENDIDO");
		} else {
			Log.i(LOGTAG, "El servicio tracker esta: APAGADO");
		}
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(LOGTAG, "onPause al entrar");
//		checaEstatusServicioGPSTracker();
//		checaEstatusServicioEnviaGeolocalizaciones();
//		Log.i(LOGTAG, "onPause al terminar");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(LOGTAG, "onResume al entrar");
//		checaEstatusServicioGPSTracker();
//		checaEstatusServicioEnviaGeolocalizaciones();
//		Log.i(LOGTAG, "onResume al terminar");
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(LOGTAG, "onRestart al entrar");
//		checaEstatusServicioGPSTracker();
//		checaEstatusServicioEnviaGeolocalizaciones();
//		Log.i(LOGTAG, "onRestart al terminar");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(LOGTAG, "onStart al entrar");
//		checaEstatusServicioGPSTracker();
//		checaEstatusServicioEnviaGeolocalizaciones();
//		Log.i(LOGTAG, "onStart al terminar");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(LOGTAG, "onStop al entrar");
//		checaEstatusServicioGPSTracker();
//		checaEstatusServicioEnviaGeolocalizaciones();
//		Log.i(LOGTAG, "onStop al terminar");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(LOGTAG, "onDestroy al entrar");
//		checaEstatusServicioGPSTracker();
//		checaEstatusServicioEnviaGeolocalizaciones();
//		Log.i(LOGTAG, "onDestroy al terminar");
	}

	private String obtenerIMEI() {
		TelephonyManager miTelefono;
		miTelefono = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return miTelefono.getDeviceId();
	}

	public boolean haveNetworkConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

}


