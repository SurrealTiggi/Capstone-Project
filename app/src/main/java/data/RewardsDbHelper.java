package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.RewardsContract.RewardsTable;

/**
 * Created by Tiggi on 8/9/2016.
 */
public class RewardsDbHelper extends SQLiteOpenHelper {

    private static final String TAG = RewardsDbHelper.class.getSimpleName();
    private static Context mContext;

    // SQLite Variables
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Rewards.db";

    //private static String DATABASE_PATH;

    public RewardsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        /*DATABASE_PATH = Environment.getDataDirectory() +
                "/data/" +
                mContext.getApplicationContext().getPackageName() +
                "/databases/" + DATABASE_NAME;*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_REWARDS_TABLE = "CREATE TABLE " + RewardsTable.TABLE_NAME + " (" +
                RewardsTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RewardsTable.COL_USER + " TEXT," +
                RewardsTable.COL_DAY + " TEXT," +
                RewardsTable.COL_TASK + " TEXT," +
                RewardsTable.COL_TASK_NUMBER + " INT," +
                RewardsTable.COL_DONE + " INT," +
                RewardsTable.COL_ARCHIVED + " INT)";

        db.execSQL(SQL_CREATE_REWARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RewardsTable.TABLE_NAME);
        onCreate(db);
    }
}
