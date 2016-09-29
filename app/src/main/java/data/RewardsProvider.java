package data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Tiggi on 8/9/2016.
 */
public class RewardsProvider extends ContentProvider {

    private RewardsDbHelper mDbHelper;

    // URI Identifiers
    static final int REWARDS_SINGLE = 0;
    static final int REWARDS_ALL = 100;
    static final int REWARDS_ACTIVE = 101;
    static final int REWARDS_ARCHIVED = 102;
    static final int REWARDS_SINGLE_ARCHIVED = 103;
    private UriMatcher mUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder RewardsQuery =
            new SQLiteQueryBuilder();

    // Queries
    // 1) Return current active chart; For Current Chart Activity
    private static final String mActiveRewards =
            RewardsContract.RewardsTable.COL_ARCHIVED + " != 0";
    // 2) Return all archived charts; For History Activity
    private static final String mArchivedRewards =
            RewardsContract.RewardsTable.COL_ARCHIVED + " = 1";
    // 3) Return single archive chart; For single history view Activity
    private static final String mSingleArchivedRewards =
            RewardsContract.RewardsTable.COL_ARCHIVED + " = 1  AND " +
                    RewardsContract.RewardsTable.COL_USER + " = ? AND " +
                    RewardsContract.RewardsTable.COL_ID + " = ? ";

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RewardsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, null, REWARDS_ALL);
        matcher.addURI(authority, "active", REWARDS_ACTIVE);
        matcher.addURI(authority, "archived", REWARDS_ARCHIVED);
        matcher.addURI(authority, "archived_chart", REWARDS_SINGLE_ARCHIVED);
        matcher.addURI(authority, "single", REWARDS_SINGLE);
        return matcher;
    }

    private int match_uri(Uri uri) {
        String link = uri.toString();
        {
            if(link.contentEquals(RewardsContract.BASE_CONTENT_URI.toString())) {
                return REWARDS_ALL;
            }
            else if(link.contentEquals(RewardsContract.RewardsTable.buildActiveRewardsUri().toString())) {
                return REWARDS_ACTIVE;
            }
            else if(link.contentEquals(RewardsContract.RewardsTable.buildArchivedUri().toString())) {
                return REWARDS_ARCHIVED;
            }
            else if(link.contentEquals(RewardsContract.RewardsTable.buildArchivedChartUri().toString())) {
                return REWARDS_SINGLE_ARCHIVED;
            }
            else if(link.contentEquals(RewardsContract.RewardsTable.buildSingleUri().toString())) {
                return REWARDS_SINGLE;
            }
        }
        return -1;
    }

    // Mandatory built in methods
    @Override
    public boolean onCreate() {
        mDbHelper = new RewardsDbHelper(getContext());
        return false; // Why false???
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case REWARDS_ALL:
                return RewardsContract.RewardsTable.CONTENT_TYPE;
            case REWARDS_ACTIVE:
                return RewardsContract.RewardsTable.CONTENT_TYPE;
            case REWARDS_ARCHIVED:
                return RewardsContract.RewardsTable.CONTENT_TYPE;
            case REWARDS_SINGLE_ARCHIVED:
                return RewardsContract.RewardsTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("No clue what's happening here...: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match = match_uri(uri);
        switch (match) {
            case REWARDS_ALL: retCursor = mDbHelper.getReadableDatabase().query(
                    RewardsContract.REWARDS_PATH,
                    projection,null,null,null,null,sortOrder); break;
            case REWARDS_ACTIVE:
                retCursor = mDbHelper.getReadableDatabase().query(
                        RewardsContract.REWARDS_PATH,
                        projection,mActiveRewards,selectionArgs,null,null,sortOrder); break;
            case REWARDS_ARCHIVED:
                retCursor = mDbHelper.getReadableDatabase().query(
                        RewardsContract.REWARDS_PATH,
                        projection,mArchivedRewards,selectionArgs,null,null,sortOrder); break;
            case REWARDS_SINGLE_ARCHIVED:
                retCursor = mDbHelper.getReadableDatabase().query(
                        RewardsContract.REWARDS_PATH,
                        projection,mSingleArchivedRewards,selectionArgs,null,null,sortOrder); break;
            default: throw new UnsupportedOperationException("Unknown Uri... I guess..." + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = match_uri(uri);

        switch (match) {
            case REWARDS_SINGLE: {
                final long rewards_id = db.insertOrThrow(RewardsContract.RewardsTable.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return RewardsContract.RewardsTable.buildSingleUri();
            } default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    //TODO: below might need to be adjusted for new insert and for updating when done editing charts
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (match_uri(uri)) {
            case REWARDS_ALL:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(RewardsContract.REWARDS_PATH, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            default:
                return super.bulkInsert(uri,values);
        }
    }
}
