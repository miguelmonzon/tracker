package mx.com.efectosoftware.tracker.db1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;

import mx.com.efectosoftware.tracker1.Httppostaux;
import mx.com.efectosoftware.tracker1.URLConexion;


public class ToursDataSource {
	
	private static final String LOGTAG = "LMFM";
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	Httppostaux post = new Httppostaux();
	String URL_connect = new URLConexion().Conexion();
		
	public ToursDataSource(Context context) {
		dbhelper = new ToursDBOpenHelper(context);
	}
	
	public void open() {
//		Log.i(LOGTAG, "Database abierta");
		database = dbhelper.getWritableDatabase();
	}
	
	public void close() {
//		Log.i(LOGTAG, "Database cerrada");
		dbhelper.close();
	}
		
	public void guardoGeolocalizacionInvisible(String timeStamp, String latitud, String longitud, 
			String precision, String proveedor, String velocidad, int bateria, String imeiTelefono, String icono) {
		
		int estatus = 0;
		String senial = "";
		
		ContentValues values = new ContentValues();
//		values.put(ToursDBOpenHelper.COLUMN_ID_GEOLOCALIZACION, id);
		values.put(ToursDBOpenHelper.COLUMN_LATITUD_GEOLOCALIZACION, latitud);
		values.put(ToursDBOpenHelper.COLUMN_LONGITUD_GEOLOCALIZACION, longitud);
		values.put(ToursDBOpenHelper.COLUMN_FECHAHORA_GEOLOCALIZACION, timeStamp);
		values.put(ToursDBOpenHelper.COLUMN_PROVEEDOR_GEOLOCALIZACION, proveedor);
		values.put(ToursDBOpenHelper.COLUMN_VELOCIDAD_GEOLOCALIZACION, velocidad);
		values.put(ToursDBOpenHelper.COLUMN_BATERIA_GEOLOCALIZACION, bateria);
		values.put(ToursDBOpenHelper.COLUMN_ESTATUS_GEOLOCALIZACION, estatus);
		values.put(ToursDBOpenHelper.COLUMN_ICONO_GEOLOCALIZACION, icono);
		values.put(ToursDBOpenHelper.COLUMN_IMEI_GEOLOCALIZACION, imeiTelefono);
		
		database.insert(ToursDBOpenHelper.TABLE_GEOLOCALIZACIONES, null, values);
		Log.i(LOGTAG, "GEOLOCALIZACION GUARDADA");
						
	}

	public void muestraTablaGeolocalizaciones() {
		
		Cursor c = database.query("db_geolocalizaciones", null, null, null, null, null, null);
		
//		Log.i(LOGTAG, "Hay " + c.getCount() + " geolocalizaciones en la base de datos local");
		
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				Log.i(LOGTAG, "id: " + c.getString(c.getColumnIndex("id_geolocalizacion")));
				Log.i(LOGTAG, "latitud: " + c.getString(c.getColumnIndex("latitud_geolocalizacion")));
				Log.i(LOGTAG, "longitud: " + c.getString(c.getColumnIndex("longitud_geolocalizacion")));
				Log.i(LOGTAG, "fecha: " + c.getString(c.getColumnIndex("fechaHora_geolocalizacion")));
				Log.i(LOGTAG, "icono: " + c.getString(c.getColumnIndex("icono_geolocalizacion")));
				Log.i(LOGTAG, "estatus: " + c.getString(c.getColumnIndex("estatus_geolocalizacion")));
				Log.i(LOGTAG, "proveedor: " + c.getString(c.getColumnIndex("proveedor_geolocalizacion")));
				Log.i(LOGTAG, "bateria: " + c.getString(c.getColumnIndex("bateria_geolocalizacion")));
				Log.i(LOGTAG, "velocidad: " + c.getString(c.getColumnIndex("velocidad_geolocalizacion")));
				Log.i(LOGTAG, "imei: " + c.getString(c.getColumnIndex("imei_geolocalizacion")));
			}
		}
	}

	public void enviaGeolocalizacionesInvisiblesAlServidor() {
		
		String id, latitud, longitud, fechaHoraGeo;
		String icono, proveedor, estatus2, velocidad, bateria2, imei, version;
		Integer estatus, bateria;
	
		Cursor c = database.rawQuery("select * from db_geolocalizaciones where estatus_geolocalizacion=0", null);
//		Cursor c = database.query("db_geolocalizaciones", null, null, null, null, null, null);
		
//		Log.i(LOGTAG, "Geolocalizaciones en BD por enviar a servidor: " + c.getCount());
		
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				
				id = c.getString(c.getColumnIndex("id_geolocalizacion"));
				latitud = c.getString(c.getColumnIndex("latitud_geolocalizacion"));
				longitud = c.getString(c.getColumnIndex("longitud_geolocalizacion"));
				fechaHoraGeo = c.getString(c.getColumnIndex("fechaHora_geolocalizacion"));
				icono = c.getString(c.getColumnIndex("icono_geolocalizacion"));
				estatus = c.getInt(c.getColumnIndex("estatus_geolocalizacion"));
				estatus2 = "" + estatus; 
				proveedor = c.getString(c.getColumnIndex("proveedor_geolocalizacion"));
				velocidad = c.getString(c.getColumnIndex("velocidad_geolocalizacion"));
				bateria = c.getInt(c.getColumnIndex("bateria_geolocalizacion"));
				bateria2 = "" + bateria;
				imei = c.getString(c.getColumnIndex("imei_geolocalizacion"));
				
//				Log.i(LOGTAG, "DATOS A ACTUALIZAR EN SERVIDOR");
//				Log.i(LOGTAG, "id: " + c.getString(c.getColumnIndex("id_geolocalizacion")));
//				Log.i(LOGTAG, "latitud: " + c.getString(c.getColumnIndex("latitud_geolocalizacion")));
//				Log.i(LOGTAG, "longitud: " + c.getString(c.getColumnIndex("longitud_geolocalizacion")));
//				Log.i(LOGTAG, "fechaHoraGeo: " + c.getString(c.getColumnIndex("fechaHora_geolocalizacion")));
//				Log.i(LOGTAG, "icono: " + c.getString(c.getColumnIndex("icono_geolocalizacion")));
//				Log.i(LOGTAG, "estatus: " + c.getString(c.getColumnIndex("estatus_geolocalizacion")));
//				Log.i(LOGTAG, "proveedor: " + c.getString(c.getColumnIndex("proveedor_geolocalizacion")));
//				Log.i(LOGTAG, "velocidad: " + c.getString(c.getColumnIndex("velocidad_geolocalizacion")));
//				Log.i(LOGTAG, "bateria: " + c.getString(c.getColumnIndex("bateria_geolocalizacion")));
//				Log.i(LOGTAG, "imei: " + c.getString(c.getColumnIndex("imei_geolocalizacion")));
				
				ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
				postparameters2send.add(new BasicNameValuePair("id", id));
				postparameters2send.add(new BasicNameValuePair("latitud", latitud));
				postparameters2send.add(new BasicNameValuePair("longitud", longitud));
				postparameters2send.add(new BasicNameValuePair("fechaHoraGeo", fechaHoraGeo));
				postparameters2send.add(new BasicNameValuePair("icono", icono));
				postparameters2send.add(new BasicNameValuePair("estatus2", estatus2));
				postparameters2send.add(new BasicNameValuePair("proveedor", proveedor));
				postparameters2send.add(new BasicNameValuePair("velocidad", velocidad));
				postparameters2send.add(new BasicNameValuePair("bateria2", bateria2));
				postparameters2send.add(new BasicNameValuePair("imei", imei));
//				Log.i(LOGTAG, "postparameters2send: " + postparameters2send);
								
				JSONArray data = null;
				try {
					data = post.getserverdata(postparameters2send, URL_connect + "actualizaGeolocalizacionesEnServidor.php");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.i(LOGTAG, "Exception al enviar geos e: " + e);
					e.printStackTrace();
				}				
//				
				
//				Log.i(LOGTAG, "Geolocalizacion guardada con exito en servidor: " + data.length());
				int registro = data.length();
				
				if (registro >= 1) {
//					Log.i(LOGTAG, "Latitud: " + latitud);
					Cursor d = database.rawQuery("update db_geolocalizaciones set estatus_geolocalizacion=1 where latitud_geolocalizacion='" + latitud + "'", null);
//					Log.i(LOGTAG, "Geolocalizaciones actualizadas a estatus 1: " + d.getCount());
				} else {
//					Log.i(LOGTAG, "Registro diferente de 1 revisar que pasa. Registro= " + registro);
				}
			}
		}

	}

	public void eliminaGeolocalizacionesYaEnviadasAlServidor() {
		// TODO Auto-generated method stub
		
		Cursor c2 = database.rawQuery("select * from db_geolocalizaciones where estatus_geolocalizacion=1", null);
//		Log.i(LOGTAG, "Se encontraron " + c2.getCount() + " Geolocalizaciones con Estatus de Geolocalizacion 1");
		
		Cursor c3 = database.rawQuery("delete from db_geolocalizaciones where estatus_geolocalizacion=1", null);
//		Log.i(LOGTAG, "Se borraron " + c3.getCount() + " Geolocalizaciones");
	}

	public int revisaControlDeGeolocalizacion() {

		Cursor c = database.query("db_control", null, null, null, null, null, null);
		int banderaGeolocalizacion = 0;
		
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				banderaGeolocalizacion = c.getInt(c.getColumnIndex("banderaGeolocalizacion"));
			}
			
		}		
		
		return banderaGeolocalizacion;
	}

	public void guardoControlGeolocalizacion(int banderaGeolocalizacion) {

		ContentValues values1 = new ContentValues();
		
		values1.put(ToursDBOpenHelper.COLUMN_CONTROL, banderaGeolocalizacion);
		
		database.update(ToursDBOpenHelper.TABLE_CONTROL_DE_GEOLOCALIZACION, values1, null, null);
	}

	public void enviaVersionAppMovil(String imeiTelefono, String versionAppMovil) {

		ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
		postparameters2send.add(new BasicNameValuePair("imei", imeiTelefono));
		postparameters2send.add(new BasicNameValuePair("version", versionAppMovil));
		Log.i(LOGTAG, "postparameters2send: " + postparameters2send);
						
		JSONArray data = post.getserverdata(postparameters2send, URL_connect + "enviaVersionAlServidor.php");				
		int registro = data.length();
		
		if (registro >= 1) {
			Log.i(LOGTAG, "Versi�n enviada con �xito");
		} else {
			Log.i(LOGTAG, "Versi�n no registrada en servidor");
		}
	}

	public long obtenerDistanciaMinimaDeGeolocalizacion() {
		Cursor c = database.query("db_parametros", null, null, null, null, null, null);
		int distanciaMinimaEnMetros = 150;
		
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				distanciaMinimaEnMetros = c.getInt(c.getColumnIndex("distanciaMinimaGeolocalizacionEnMetros"));
			}
			
		}
		return distanciaMinimaEnMetros;
	}

	public long obtenerTiempoMinimoDeGeolocalizacion() {
		Cursor c = database.query("db_parametros", null, null, null, null, null, null);
		int tiempoMinimoEnMinutos = 5;
		
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				tiempoMinimoEnMinutos = c.getInt(c.getColumnIndex("tiempoMinimoGeolocalizacionEnMinutos"));
			}
		}
		tiempoMinimoEnMinutos = tiempoMinimoEnMinutos * 1000 * 60;
		return tiempoMinimoEnMinutos;
	}

	public void guardoGeolocalizacionInvisible(String imeiTelefono, String icono, String timeStamp) {
		ContentValues values = new ContentValues();
//		values.put(ToursDBOpenHelper.COLUMN_ID_GEOLOCALIZACION, id);
		values.put(ToursDBOpenHelper.COLUMN_IMEI_GEOLOCALIZACION, imeiTelefono);
		values.put(ToursDBOpenHelper.COLUMN_ICONO_GEOLOCALIZACION, icono);
		values.put(ToursDBOpenHelper.COLUMN_FECHAHORA_GEOLOCALIZACION, timeStamp);
		
		database.insert(ToursDBOpenHelper.TABLE_GEOLOCALIZACIONES, null, values);
	}

}
