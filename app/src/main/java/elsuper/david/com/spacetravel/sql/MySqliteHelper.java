package elsuper.david.com.spacetravel.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Andrés David García Gómez.
 */
public class MySqliteHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "space_travel_favorites_db";
    private final static int DATABASE_VERSION = 1;
    public final static String COLUMN_ID = BaseColumns._ID;

    /***********************Tabla Apod***********************/
    public final static String TABLENAME_APOD = "apod_table";
    public final static String COLUMN_APOD_COPYRIGHT = "copyright";
    public final static String COLUMN_APOD_DATE = "date";
    public final static String COLUMN_APOD_EXPLANATION = "explanation";
    public final static String COLUMN_APOD_HDURL = "hdurl";
    public final static String COLUMN_APOD_MEDIA_TYPE = "media_type";
    public final static String COLUMN_APOD_SERVICE_VERSION = "service_version";
    public final static String COLUMN_APOD_TITLE = "title";
    public final static String COLUMN_APOD_URL = "url";

    private final static String CREATE_TABLE_APOD = "create table " + TABLENAME_APOD +
            "(" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_APOD_COPYRIGHT + " text not null, " +
            COLUMN_APOD_DATE + " text not null, " +
            COLUMN_APOD_EXPLANATION + " text not null, " +
            COLUMN_APOD_HDURL + " text not null, " +
            COLUMN_APOD_MEDIA_TYPE + " text not null, " +
            COLUMN_APOD_SERVICE_VERSION + " text not null, " +
            COLUMN_APOD_TITLE + " text not null, " +
            COLUMN_APOD_URL + " text not null)";

    /***********************Tabla photo***********************/
    public final static String TABLENAME_PHOTO = "photo_table";
    public final static String COLUMN_PHOTO_ID = "photo_id";
    public final static String COLUMN_PHOTO_SOL = "sol";
    public final static String COLUMN_PHOTO_IMG_SRC = "img_src";
    public final static String COLUMN_PHOTO_EARTH_DATE = "earth_date";

    private final static String CREATE_TABLE_PHOTO = "create table " + TABLENAME_PHOTO +
            "(" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_PHOTO_ID + " integer not null, " +
            COLUMN_PHOTO_SOL + " integer not null, " +
            COLUMN_PHOTO_IMG_SRC + " text not null, " +
            COLUMN_PHOTO_EARTH_DATE + " text not null)";

    /***********************Tabla camera***********************/
    public final static String TABLENAME_CAMERA = "camera_table";
    public final static String COLUMN_CAMERA_PHOTO_ID = "photo_id";
    public final static String COLUMN_CAMERA_ID = "camera_id";
    public final static String COLUMN_CAMERA_NAME = "name";
    public final static String COLUMN_CAMERA_ROVER_ID = "rover_id";
    public final static String COLUMN_CAMERA_FULL_NAME = "full_name";

    private final static String CREATE_TABLE_CAMERA = "create table " + TABLENAME_CAMERA +
            "(" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_CAMERA_PHOTO_ID + " integer not null, " + //Para identificar a qué foto pertenece
            COLUMN_CAMERA_ID + " integer not null, " +
            COLUMN_CAMERA_NAME + " text not null, " +
            COLUMN_CAMERA_ROVER_ID + " integer not null, " +
            COLUMN_CAMERA_FULL_NAME + " text not null)";

    /***********************Tabla rover***********************/
    public final static String TABLENAME_ROVER = "rover_table";
    public final static String COLUMN_ROVER_PHOTO_ID = "photo_id";
    public final static String COLUMN_ROVER_ID = "rover_id";
    public final static String COLUMN_ROVER_NAME = "name";
    public final static String COLUMN_ROVER_LANDING_DATE = "landing_date";
    public final static String COLUMN_ROVER_MAX_SOL = "max_sol";
    public final static String COLUMN_ROVER_MAX_DATE = "max_date";
    public final static String COLUMN_ROVER_TOTAL_PHOTOS = "total_photos";

    private final static String CREATE_TABLE_ROVER = "create table " + TABLENAME_ROVER +
            "(" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_ROVER_PHOTO_ID + " integer not null, " + //Para identificar a qué foto pertenece
            COLUMN_ROVER_ID + " integer not null, " +
            COLUMN_ROVER_NAME + " text not null, " +
            COLUMN_ROVER_LANDING_DATE + " text not null, " +
            COLUMN_ROVER_MAX_SOL + " integer not null, " +
            COLUMN_ROVER_MAX_DATE + " text not null, " +
            COLUMN_ROVER_TOTAL_PHOTOS + " integer not null)";

    /***********************Tabla camerasecondary***********************/
    public final static String TABLENAME_CAMERASECONDARY = "camerasecondary_table";
    public final static String COLUMN_CAMERASECONDARY_PHOTO_ID = "photo_id";
    public final static String COLUMN_CAMERASECONDARY_NAME = "name";
    public final static String COLUMN_CAMERASECONDARY_FULL_NAME = "full_name";

    private final static String CREATE_TABLE_CAMERASECONDARY = "create table " + TABLENAME_CAMERASECONDARY +
            "(" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_CAMERASECONDARY_PHOTO_ID + " integer not null, " + //Para identificar a qué foto pertenece
            COLUMN_CAMERASECONDARY_NAME + " text not null, " +
            COLUMN_CAMERASECONDARY_FULL_NAME + " text not null)";

    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_APOD);
        db.execSQL(CREATE_TABLE_PHOTO);
        db.execSQL(CREATE_TABLE_CAMERA);
        db.execSQL(CREATE_TABLE_ROVER);
        db.execSQL(CREATE_TABLE_CAMERASECONDARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
