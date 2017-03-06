package es.dpinfo.spotlightplace.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dprimenko on 2/03/17.
 */
public class SpotlightContractProvider {

    public static final String AUTHORITY = "es.dpinfo.spotlightplace";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private SpotlightContractProvider() {}

    public static class Places implements BaseColumns {
        public static final String CONTENT_PATH = "place";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);

        public static final String CREATOR = "creator";
        public static final String TITLE = "title";
        public static final String IMG = "img";
        public static final String ADDRESS = "address";
        public static final String DESCRIPTION = "description";
        public static final String CATEGORY = "category";
        public static final String DATETIME_FROM = "datetime_from";
        public static final String DATETIME_TO = "datetime_to";
        public static final String USERS_IN = "users_in";

        public static final String[] PROJECTION = new String[] {BaseColumns._ID, CREATOR, TITLE, IMG, ADDRESS, DESCRIPTION, CATEGORY, DATETIME_FROM, DATETIME_TO, USERS_IN};
    }

    public static class Categories implements BaseColumns {
        public static final String CONTENT_PATH = "category";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);

        public static final String NAME = "name";

        public static final String[] PROJECTION = new String[] {BaseColumns._ID, NAME};


    }
}
