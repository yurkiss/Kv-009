package org.ecomap.android.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class EcoMapContract {

    public static final String HTTP_ECOMAP_BASE_URL = "http://176.36.11.25:8000";

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "org.ecomap.android.app.data";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_PROBLEMS = "problems";
    public static final String PATH_RESOURCES = "resources";

    /* Inner class that defines the table contents of the problems table */
    public static final class ProblemsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROBLEMS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROBLEMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROBLEMS;

        // Table name
        public static final String TABLE_NAME = "problems";

        //status of a problem, String
        public static final String COLUMN_STATUS = "status";
        //title, String
        public static final String COLUMN_TITLE = "title";
        //first name of user who added problem, String
        public static final String COLUMN_USER_FIRST_NAME = "first_name";
        //last name of user who added problem, String
        public static final String COLUMN_USER_LAST_NAME = "last_name";
        //type if a problem (1-6), int
        public static final String COLUMN_PROBLEM_TYPE_ID = "problem_type_id";
        //severuty - can be init by administrator, int
        public static final String COLUMN_SEVERITY = "severity";
        //number of "likes, int
        public static final String COLUMN_NUMBER_OF_VOTES = "number_of_votes";
        //date of adding problem, String
        public static final String COLUMN_DATE = "date";
        //description of a problem, String
        public static final String COLUMN_CONTENT = "content";
        //latitude coordinate, String
        public static final String COLUMN_LATITUDE = "latitude";
        //longtitude coordinate, String
        public static final String COLUMN_LONGTITUDE = "longtitude";
        //proposal of decision, String
        public static final String COLUMN_PROPOSAL = "proposal";
        //ID of region, int
        public static final String COLUMN_REGION_ID = "region_id";
        //number of comments for each problem, int
        public static final String COLUMN_COMMENTS_NUMBER = "comments_number";


        public static Uri buildProblemsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ResourcesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESOURCES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESOURCES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESOURCES;

        //TABLE NAME
        public static final String TABLE_NAME = "resources";

        //title of resource, String
        public static final String COLUMN_TITLE = "title";
        //description, String
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildResourcesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
