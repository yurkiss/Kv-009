package org.ecomap.android.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.ecomap.android.app.data.EcoMapContract.ProblemsEntry;
import org.ecomap.android.app.data.EcoMapContract.ResourcesEntry;
import org.ecomap.android.app.utils.SharedPreferencesHelper;

/**
 * Manages a local database for weather data.
 */
public class EcoMapDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "ecomap.db";
    private Context mContext;

    public EcoMapDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

        /**
         * We have to do this in order to perform onUpgrade/onDowngrade
         * before EcoMapService fetch data from http://ecomap.org
         * This constructor is called first by EcoMapProvider.onCreate() method.
         */
        getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold problems.
        final String SQL_CREATE_PROBLEMS_TABLE = "CREATE TABLE " + ProblemsEntry.TABLE_NAME + " (" +
                ProblemsEntry._ID + " INTEGER PRIMARY KEY," +
                ProblemsEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_USER_FIRST_NAME + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_USER_LAST_NAME + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_PROBLEM_TYPE_ID + " INTEGER NOT NULL, " +
                ProblemsEntry.COLUMN_SEVERITY + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_NUMBER_OF_VOTES + " INTEGER NOT NULL, " +
                ProblemsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_LATITUDE + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_LONGTITUDE + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_PROPOSAL + " TEXT NOT NULL, " +
                ProblemsEntry.COLUMN_REGION_ID + " INTEGER NOT NULL, " +
                ProblemsEntry.COLUMN_COMMENTS_NUMBER + " INTEGER NOT NULL " +
                " );";


        final String SQL_CREATE_RESOURCES_TABLE = "CREATE TABLE " + ResourcesEntry.TABLE_NAME + " (" +
                ResourcesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ResourcesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ResourcesEntry.COLUMN_CONTENT + " TEXT NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PROBLEMS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RESOURCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProblemsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResourcesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        SharedPreferencesHelper.updateNumRevision(mContext, 0);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProblemsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResourcesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        SharedPreferencesHelper.updateNumRevision(mContext, 0);
    }
}
