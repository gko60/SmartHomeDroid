package at.htl.smarthome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import at.htl.smarthome.entity.Sensor;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartHome";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseManager dbInstance;
    private static Object sLOCK = "";


    // Konstruktor private, da Singleton
    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton
    public static DatabaseManager getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (sLOCK) {
                if (dbInstance == null && context != null) {
                    dbInstance = new DatabaseManager(context.getApplicationContext());
                }
            }
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENSOR_TABLE = "CREATE TABLE sensor ( " +
                "id INTEGER, " +
                "viewtag TEXT, " +
                "unit TEXT )";
        String CREATE_MEASUREMENT_TABLE = "CREATE TABLE measurement ( " +
                "idsensor INTEGER, " +
                "index INTEGER, " +
                "value REAL )";

        // create tables
        db.execSQL(CREATE_SENSOR_TABLE);
        db.execSQL(CREATE_MEASUREMENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS measurement");
        db.execSQL("DROP TABLE IF EXISTS sensor");
        onCreate(db);
    }

    /**
     * Sensor in Datenbank persistieren
     *
     * @param sensor
     */
    public void insertSensor(Sensor sensor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", sensor.getId());
        values.put("viewTag", sensor.getViewTag());
        values.put("unit", sensor.getUnit());
        // insert row
        db.insert("sensor", null, values);
    }
}