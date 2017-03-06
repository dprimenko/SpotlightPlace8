package es.dpinfo.spotlightplace.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import es.dpinfo.spotlightplace.SpotlightApplication;

/**
 * Created by dprimenko on 2/03/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "spotlight.db";

    private volatile static DatabaseHelper databaseHelper;
    private AtomicInteger mOpenCounter;
    private SQLiteDatabase mDatabase;

    private DatabaseHelper() {
        super(SpotlightApplication.getContext(), DB_NAME, null, DB_VERSION);
        mOpenCounter = new AtomicInteger();
    }

    public synchronized static DatabaseHelper getInstance() {

        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }

        return databaseHelper;
    }

    public synchronized SQLiteDatabase openDatabase() {

        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = getWritableDatabase();
        }

        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL(DatabaseContract.Categories.SQL_CREATE_ENTRIES);
            db.execSQL(DatabaseContract.Places.SQL_CREATE_ENTRIES);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("SpotlightDB:", "Error al crear la base de datos");
            Log.e("SpotlightDBLog:", e.getMessage());

        } finally {
            db.endTransaction();
            insertDemoData(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.beginTransaction();
            db.execSQL(DatabaseContract.Categories.DROP_TABLE);
            db.execSQL(DatabaseContract.Places.DROP_TABLE);
            db.setTransactionSuccessful();
            db.endTransaction();
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("SpotlightDB:", "Error al actulizar la base de datos");
            Log.e("SpotlightDBLog:", e.getMessage());

        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            }
        } else {
            db.beginTransaction();
            db.execSQL("PRAGMA foreign_keys = ON");
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void insertDemoData(SQLiteDatabase db) {

        try {
            db.beginTransaction();

            db.execSQL(String.format("INSERT INTO %s (%s) VALUES (\"Public\")",
                    DatabaseContract.Categories.TABLE_NAME,
                    DatabaseContract.Categories.COL_NAME));

            db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (\"5899fe720f68f525b98b7e4c\", " +
                    "\"Test 1\", " +
                    "\"http://xpenology.org/wp-content/themes/qaengine/img/default-thumbnail.jpg\"," +
                    "\"Desc\", " +
                    "\"36.625447500000014,-4.51437109375\", " +
                    "1, " +
                    "\"2017-02-07T17:06:00.000Z\", " +
                    "\"2017-02-07T19:06:00.000Z\", " +
                    "0)",
                    DatabaseContract.Places.TABLE_NAME,
                    DatabaseContract.Places.COL_CREATOR,
                    DatabaseContract.Places.COL_TITLE,
                    DatabaseContract.Places.COL_IMG,
                    DatabaseContract.Places.COL_DESCRIPTION,
                    DatabaseContract.Places.COL_ADDRESS,
                    DatabaseContract.Places.COL_CATEGORY,
                    DatabaseContract.Places.COL_DATETIME_FROM,
                    DatabaseContract.Places.COL_DATETIME_TO,
                    DatabaseContract.Places.COL_USERS_IN));
            db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (\"5899fe720f68f525b98b7e4c\", " +
                            "\"Test 2\", " +
                            "\"http://xpenology.org/wp-content/themes/qaengine/img/default-thumbnail.jpg\"," +
                            "\"Desc\", " +
                            "\"36.625447500000014,-4.51437109375\", " +
                            "1, " +
                            "\"2017-02-07T17:06:00.000Z\", " +
                            "\"2017-02-07T19:06:00.000Z\", " +
                            "0)",
                    DatabaseContract.Places.TABLE_NAME,
                    DatabaseContract.Places.COL_CREATOR,
                    DatabaseContract.Places.COL_TITLE,
                    DatabaseContract.Places.COL_IMG,
                    DatabaseContract.Places.COL_DESCRIPTION,
                    DatabaseContract.Places.COL_ADDRESS,
                    DatabaseContract.Places.COL_CATEGORY,
                    DatabaseContract.Places.COL_DATETIME_FROM,
                    DatabaseContract.Places.COL_DATETIME_TO,
                    DatabaseContract.Places.COL_USERS_IN));
            db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("SpotlightDB: ", "Error al crear los datos demo");
        } finally {
            db.endTransaction();
        }
    }
}
