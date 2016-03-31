package mx.com.efectosoftware.tracker.db1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToursDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "LMFM";

	private static final String DATABASE_NAME = "prestamos.db";
	
	private static final int DATABASE_VERSION = 1;	
	
	public static final String TABLE_GEOLOCALIZACIONES = "db_geolocalizaciones";
	public static final String TABLE_PARAMETROS = "db_parametros";
	public static final String TABLE_VERSIONES = "db_versiones";
	public static final String TABLE_CONTROL_DE_GEOLOCALIZACION = "db_control";
	
	public static final String COLUMN_ID_GEOLOCALIZACION = "id_geolocalizacion";
	public static final String COLUMN_LATITUD_GEOLOCALIZACION = "latitud_geolocalizacion";
	public static final String COLUMN_LONGITUD_GEOLOCALIZACION = "longitud_geolocalizacion";
	public static final String COLUMN_FECHAHORA_GEOLOCALIZACION = "fechaHora_geolocalizacion";
	public static final String COLUMN_ICONO_GEOLOCALIZACION = "icono_geolocalizacion";
	public static final String COLUMN_ESTATUS_GEOLOCALIZACION = "estatus_geolocalizacion";
	public static final String COLUMN_PROVEEDOR_GEOLOCALIZACION = "proveedor_geolocalizacion";
	public static final String COLUMN_BATERIA_GEOLOCALIZACION = "bateria_geolocalizacion";
	public static final String COLUMN_VELOCIDAD_GEOLOCALIZACION = "velocidad_geolocalizacion";
	public static final String COLUMN_IMEI_GEOLOCALIZACION = "imei_geolocalizacion";
	
	public static final String COLUMN_ID_PARAMETRO = "idParametroAppMovil";
	public static final String COLUMN_TIEMPO_MINIMO = "tiempoMinimoGeolocalizacionEnMinutos";
	public static final String COLUMN_DISTANCIA_MINIMA = "distanciaMinimaGeolocalizacionEnMetros";
	public static final String COLUMN_DIA_SINCRONIZACION = "diaSincronizacionDeParametros";
	
	public static final String COLUMN_CONTROL = "banderaGeolocalizacion";
	
	private static final String TABLE_CREATE_GEOLOCALIZACIONES = 
			"CREATE TABLE " + TABLE_GEOLOCALIZACIONES + " (" +
			COLUMN_ID_GEOLOCALIZACION + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_LATITUD_GEOLOCALIZACION + " TEXT, " +
			COLUMN_LONGITUD_GEOLOCALIZACION + " TEXT, " +
			COLUMN_FECHAHORA_GEOLOCALIZACION + " TEXT, " +
			COLUMN_ICONO_GEOLOCALIZACION + " TEXT, " +
			COLUMN_ESTATUS_GEOLOCALIZACION + " INTEGER, " +
			COLUMN_PROVEEDOR_GEOLOCALIZACION + " TEXT, " +
			COLUMN_BATERIA_GEOLOCALIZACION + " INTEGER, " +
			COLUMN_VELOCIDAD_GEOLOCALIZACION + " TEXT, " +
			COLUMN_IMEI_GEOLOCALIZACION + " TEXT " +
			")";
	
	private static final String TABLE_CREATE_PARAMETROS = 
			"CREATE TABLE " + TABLE_PARAMETROS + " (" +
			COLUMN_ID_PARAMETRO + " INTEGER, " +
			COLUMN_TIEMPO_MINIMO + " INTEGER, " +
			COLUMN_DISTANCIA_MINIMA + " INTEGER " +
			")";
	
	private static final String TABLE_CREATE_CONTROL = 
			"CREATE TABLE " + TABLE_CONTROL_DE_GEOLOCALIZACION + " (" +
			COLUMN_CONTROL + " INTEGER " +
			")";

			
	public ToursDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("pragma foreign_keys = on");
		
		db.execSQL(TABLE_CREATE_GEOLOCALIZACIONES);
//		Log.i(LOGTAG, "La tabla " + TABLE_GEOLOCALIZACIONES + " ha sido creada");
		
		db.execSQL(TABLE_CREATE_PARAMETROS);
//		Log.i(LOGTAG, "La tabla " + TABLE_PARAMETROS + " ha sido creada");
		
		db.execSQL("insert into db_parametros values (1, 9, 600);");
//		Log.i(LOGTAG, "Se insertaron los parametros por default");
		
		db.execSQL(TABLE_CREATE_CONTROL);
//		Log.i(LOGTAG, "La tabla " + TABLE_CONTROL_DE_GEOLOCALIZACION + " ha sido creada");
		
		db.execSQL("insert into db_control values (0);");
//		Log.i(LOGTAG, "Se inserto por default a cero el control de geolocalizacion");
		
//		db.execSQL("insert into db_geolocalizaciones values (1, '19.440313568338752', '-99.21001601032913', '525532723245', '2014-06-12 15:11:06', '30', 'icons/pins.png', 0, 'gps', 60, '20%', '4 m/s');");
//		Log.i(LOGTAG, "Se inserto una geolocalizacion");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GEOLOCALIZACIONES);
		onCreate(db);
	}

}
