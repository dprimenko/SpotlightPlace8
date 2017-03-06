package es.dpinfo.spotlightplace.db;

import android.provider.BaseColumns;

/**
 * Created by dprimenko on 1/03/17.
 */
public class DatabaseContract {

    public DatabaseContract() {
    }

    public static class Places implements BaseColumns {
        public static final String TABLE_NAME = "place";

        public static final String COL_CREATOR = "creator";
        public static final String COL_TITLE = "title";
        public static final String COL_IMG = "img";
        public static final String COL_ADDRESS = "address";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_CATEGORY = "category";
        public static final String COL_DATETIME_FROM = "datetime_from";
        public static final String COL_DATETIME_TO = "datetime_to";
        public static final String COL_USERS_IN = "users_in";
        public static final String REFERENCE_CATEGORY = String.format("REFERENCES %s (%s) ON UPDATE CASCADE ON DELETE RESTRICT", Categories.TABLE_NAME, BaseColumns._ID);

        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s INTEGER NOT NULL %s," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s INTEGER NOT NULL" +
                ")",
                TABLE_NAME,
                BaseColumns._ID,
                COL_CREATOR,
                COL_TITLE,
                COL_IMG,
                COL_ADDRESS,
                COL_DESCRIPTION,
                COL_CATEGORY,
                REFERENCE_CATEGORY,
                COL_DATETIME_FROM,
                COL_DATETIME_TO,
                COL_USERS_IN);

        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

    public static class Categories implements BaseColumns {
        public static final String TABLE_NAME = "category";

        public static final String COL_NAME = "title";

        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL" +
                        ")",
                TABLE_NAME,
                BaseColumns._ID,
                COL_NAME);

        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }
}
