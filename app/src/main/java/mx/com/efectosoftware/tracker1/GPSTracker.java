package mx.com.efectosoftware.tracker1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mx.com.efectosoftware.tracker.db1.ToursDataSource;


public class GPSTracker extends Service implements LocationListener {
	
	private static final String LOGTAG = "LMFM";
	private static GPSTracker instance  = null; 
	
	// The minimum distance to change Updates in meters
	private static long MIN_DISTANCE_CHANGE_FOR_UPDATES; // 0 meters

	// The minimum time between updates in milliseconds
	private static long MIN_TIME_BW_UPDATES; // 1 minute
	
	private LocationManager mLocManagerGPS;
	private LocationManager mLocManagerNetwork;
	private LocationListener mLocListenerGPS;
	private LocationListener mLocListenerNetwork;
	
//	private SoundUtils mSoundUtils;
	
	String latitud, longitud, timeStamp, precision, proveedor, velocidad;
	
	ToursDataSource datasource;
	
	public static boolean isRunning() { 
	      return instance != null; 
	}
	
	@Override
	public void onCreate() {
		instance=this;
		datasource = new ToursDataSource(this);
		datasource.open();
		MIN_DISTANCE_CHANGE_FOR_UPDATES = datasource.obtenerDistanciaMinimaDeGeolocalizacion();
		MIN_TIME_BW_UPDATES = datasource.obtenerTiempoMinimoDeGeolocalizacion();
		
//		mSoundUtils = new SoundUtils(this, new SoundUtils.soundCommunication() {
//			
//			@Override
//			public void beepAlreadyFinished() {
//				// TODO Auto-generated method stub
//				Log.i(LOGTAG, "Espeo a que el beep termine");
//			}
//		});
		
	}
	
	@Override
	public void onDestroy() {
		Log.i(LOGTAG, "Servicio GPSTracker destruido");
//		locManager.removeUpdates(locListener);
		removeGpsUpdates();
		instance = null;
	}
	
	public void removeGpsUpdates() {
		Log.i(LOGTAG, "remove updates");
		Log.i(LOGTAG, "mLocListenerGPS: " + mLocListenerGPS);
		Log.i(LOGTAG, "mLocManageGPS: " + mLocManagerGPS);
		
		if (mLocListenerGPS != null) {
			mLocManagerGPS.removeUpdates(mLocListenerGPS);
		}
		
		Log.i(LOGTAG, "mLocListenerNetwork: " + mLocListenerNetwork);
		Log.i(LOGTAG, "mLocManagerNetwork: " + mLocManagerNetwork);
		
		if (mLocListenerNetwork != null) {
			mLocManagerNetwork.removeUpdates(mLocListenerNetwork);
		}
		
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.i(LOGTAG, "Servicio GPSTracker iniciado");
		comenzarLocalizacionPorGPS();
		int banderaControlGeolocalizacion = datasource.revisaControlDeGeolocalizacion();
//		Toast.makeText(getApplicationContext(), "bandera: " + banderaControlGeolocalizacion, Toast.LENGTH_SHORT).show();
		if (banderaControlGeolocalizacion == 0) { 
			comenzarLocalizacionPorRedDeDatos();
		} else {
			int banderaGeolocalizacion = 0;
			datasource.guardoControlGeolocalizacion(banderaGeolocalizacion);
		}
	}
	
	public void iniciarConLaGeolocalizacion() {
		Log.i(LOGTAG, "INICIO A GEOLOCALIZAR");
	}
	

	private void comenzarLocalizacionPorRedDeDatos() {
		
		//Obtenemos una referencia al LocationManager
		mLocManagerNetwork = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		Log.i(LOGTAG, "mLocManagerNetwork: " + mLocManagerNetwork);
		
		//Nos registramos para recibir actualizaciones de la posicion
		mLocListenerNetwork = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
//				Log.i(LOGTAG, "Cambio en estatus RED DE DATOS");
//				Toast.makeText(getApplicationContext(), "Cambio en estatus RED DE DATOS", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(getApplicationContext(), "Red de Datos Encendida", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(getApplicationContext(), "Red de Datos Apagada por favor revise", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLocationChanged(Location location) {
				guardarPosicion(location);
			}
		};
//		Log.i(LOGTAG, "mLocListenerNetwork: " + mLocListenerNetwork);
		mLocManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocListenerNetwork);
	}

	private void comenzarLocalizacionPorGPS() {
		
		//Obtenemos una referencia al LocationManager
		mLocManagerGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		Log.i(LOGTAG, "mLocManagerGPS: " + mLocManagerGPS);
		
		//Nos registramos para recibir actualizaciones de la posicion
		mLocListenerGPS = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
//				Log.i(LOGTAG, "Cambio en estatus GPS: " + provider + " status: " + status);
//				Toast.makeText(getApplicationContext(), "Cambio en estatus GPS: " + provider + " status: " + status, Toast.LENGTH_SHORT).show();
//				if (status != 1) {
//					Toast.makeText(getApplicationContext(), "GPS NA, Iniciar geo por datos", Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(getApplicationContext(), "Se geolocalizo por GPS, no lanzar geo por datos", Toast.LENGTH_SHORT).show();
//				}
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(getApplicationContext(), "GPS Encendido", Toast.LENGTH_SHORT).show();
				timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
//				String numeroTelefonico = obtenerLineaTelefonica();
				String imeiTelefono = obtenerIMEI();
				String icono = "icons/gpsEncendido.png";
				datasource.guardoGeolocalizacionInvisible(imeiTelefono, icono, timeStamp);
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(getApplicationContext(), "GPS Apagado por favor revise", Toast.LENGTH_SHORT).show();
				timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
//				String numeroTelefonico = obtenerLineaTelefonica();
				String imeiTelefono = obtenerIMEI();
				String icono = "icons/gpsApagado.png";
				datasource.guardoGeolocalizacionInvisible(imeiTelefono, icono, timeStamp);
			}
			
			@Override
			public void onLocationChanged(Location location) {
				guardarPosicion(location);
			}
		};
//		Log.i(LOGTAG, "mLocListenerGPS: " + mLocListenerGPS);
		mLocManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocListenerGPS);
	}
	
	public void guardarPosicion(Location location) {

		if (location != null) {
			
			latitud = String.valueOf(location.getLatitude());
			longitud = String.valueOf(location.getLongitude());
			precision = String.valueOf(location.getAccuracy());
			proveedor = String.valueOf(location.getProvider());
			velocidad = String.valueOf(location.getSpeed());
			
			int bateria = obtieneEstatusBateria();
			
			timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
			
//			Log.i(LOGTAG, "Fecha Hora: " + timeStamp);
////			Log.i(LOGTAG, "Contador: " + contadorGeolocalizacion);
//			Log.i(LOGTAG, "Latitud: " + latitud);
//			Log.i(LOGTAG, "Longitud: " + longitud);
//			Log.i(LOGTAG, "Precision: " + precision);
//			Log.i(LOGTAG, "Velocidad: " + velocidad);
//			Log.i(LOGTAG, "Proveedor: " + proveedor);
			
//			String numeroTelefonico = obtenerLineaTelefonica();
			String imeiTelefono = obtenerIMEI();
			String icono;
			int banderaGeolocalizacion;
			
			if(proveedor.equals("network")) {
				icono = "icons/geoPorDatos.png";
				banderaGeolocalizacion = 0;
				datasource.guardoControlGeolocalizacion(banderaGeolocalizacion);
			} else {
				icono = "";
				banderaGeolocalizacion = 1;
				datasource.guardoControlGeolocalizacion(banderaGeolocalizacion);
			}
			
//			mSoundUtils.startBeep();
			datasource.guardoGeolocalizacionInvisible(timeStamp, latitud, longitud, precision, proveedor, velocidad, bateria, imeiTelefono, icono);
//			Toast.makeText(getApplicationContext(), "Geolocalizacion Guardada", Toast.LENGTH_SHORT).show();
//			Toast.makeText(getApplicationContext(), "E.S.: " + proveedor + " , " + precision, Toast.LENGTH_SHORT).show();
		} else {

		}
	}

	private String obtenerIMEI() {
		TelephonyManager miTelefono;
		miTelefono = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return miTelefono.getDeviceId();
	}

	private int obtieneEstatusBateria() {
		// TODO Auto-generated method stub
		
		try 
        { 
           IntentFilter batIntentFilter = 
              new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
            Intent battery = 
               this.registerReceiver(null, batIntentFilter); 
            int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1); 
            return nivelBateria; 
        } 
        catch (Exception e) 
        {            
           Toast.makeText(getApplicationContext(), 
                    "Error al obtener estado de la bateria",
                    Toast.LENGTH_SHORT).show(); 
           return 0; 
        }       
	}

//	private String obtenerLineaTelefonica() {
//		// TODO Auto-generated method stub
//		TelephonyManager miTelefono;
//		miTelefono = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		return miTelefono.getLine1Number();
//	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
