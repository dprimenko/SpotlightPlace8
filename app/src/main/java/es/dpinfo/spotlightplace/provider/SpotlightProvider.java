package es.dpinfo.spotlightplace.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import es.dpinfo.spotlightplace.db.DatabaseContract;
import es.dpinfo.spotlightplace.db.DatabaseHelper;

/**
 * Created by dprimenko on 2/03/17.
 */
public class SpotlightProvider extends ContentProvider {

    private static final int PLACE = 1;
    private static final int PLACE_ID = 2;
    private static final int CATEGORY = 3;
    private static final int CATEGORY_ID = 4;

    private SQLiteDatabase sqLiteDatabase;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(SpotlightContractProvider.AUTHORITY, SpotlightContractProvider.Places.CONTENT_PATH, PLACE);
        uriMatcher.addURI(SpotlightContractProvider.AUTHORITY, SpotlightContractProvider.Places.CONTENT_PATH + "/#", PLACE_ID);
        uriMatcher.addURI(SpotlightContractProvider.AUTHORITY, SpotlightContractProvider.Categories.CONTENT_PATH, CATEGORY);
        uriMatcher.addURI(SpotlightContractProvider.AUTHORITY, SpotlightContractProvider.Places.CONTENT_PATH + "/#", CATEGORY_ID);
    }

    @Override
    public boolean onCreate() {

        sqLiteDatabase = DatabaseHelper.getInstance().openDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case CATEGORY:
                sqLiteQueryBuilder.setTables(DatabaseContract.Categories.TABLE_NAME);
                break;
            case PLACE:
                sqLiteQueryBuilder.setTables(DatabaseContract.Places.TABLE_NAME);
                break;
            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("URI Invalid");
        }

        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri newUri = null;
        long regId = -1;

        switch (uriMatcher.match(uri)) {
            case CATEGORY:
                regId = sqLiteDatabase.insert(DatabaseContract.Categories.TABLE_NAME, null, values);
                break;
            case PLACE:
                regId = sqLiteDatabase.insert(DatabaseContract.Places.TABLE_NAME, null, values);
                break;
        }

        newUri = ContentUris.withAppendedId(uri, regId);

        if (regId != -1) {
            getContext().getContentResolver().notifyChange(newUri, null);
        } else {
            throw new SQLException("CRUD: Insert Error");
        }

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Uri newUri = null;
        int regId = -1;

        switch (uriMatcher.match(uri)) {
            case CATEGORY:
                regId = sqLiteDatabase.delete(DatabaseContract.Categories.TABLE_NAME, selection, selectionArgs);
                break;
            case PLACE:
                regId = sqLiteDatabase.delete(DatabaseContract.Places.TABLE_NAME, selection, selectionArgs);
                break;
        }

        newUri = ContentUris.withAppendedId(uri, regId);

        if (regId != -1) {
            getContext().getContentResolver().notifyChange(newUri, null);
        } else {
            throw new SQLException("CRUD: Delete Error");
        }

        return regId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Uri newUri = null;
        int regId = -1;

        switch (uriMatcher.match(uri)) {
            case CATEGORY:
                regId = sqLiteDatabase.update(DatabaseContract.Categories.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PLACE:
                regId = sqLiteDatabase.update(DatabaseContract.Places.TABLE_NAME, values,selection, selectionArgs);
                break;
        }

        newUri = ContentUris.withAppendedId(uri, regId);

        if (regId != -1) {
            getContext().getContentResolver().notifyChange(newUri, null);
        } else {
            throw new SQLException("CRUD: No columns detected on update");
        }

        return regId;
    }
}
