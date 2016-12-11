package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tiggi on 8/9/2016.
 */
public class RewardsContract {

    // URI data
    public static final String CONTENT_AUTHORITY = "baptista.tiago.rewardbingo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String REWARDS_PATH = "rewards";

    public static final class RewardsTable implements BaseColumns {

        // Table structure
        public static final String TABLE_NAME = "rewards";
        /** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
        public static final String COL_ID = "rewards_id";
        /** Type: TEXT */
        public static final String COL_USER = "user";
        /** Type: TEXT */
        public static final String COL_DAY = "timestamp";
        /** Type: TEXT */
        public static final String COL_TASK = "task_desc";
        /** Type: INT */
        public static final String COL_TASK_NUMBER = "task_num";
        /** Type: INT */
        public static final String COL_DONE = "is_done";
        /** Type: INT */
        public static final String COL_ARCHIVED = "is_archived";

        // URI Types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + REWARDS_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + REWARDS_PATH;

        // URI to return full content???
        /*public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(REWARDS_PATH).build();*/

        /** Matches: /active/ */
        public static Uri buildActiveRewardsUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("active").build();
        }
        /** Matches: /archived/ */
        public static Uri buildArchivedUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("archived").build();
        }
        /** Matches: /archived_chart/ */
        public static Uri buildArchivedChartUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("archived_chart").build();
        }
        /*Matches: /single/ */
        public static Uri buildSingleUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("single").build();
        }
    }
}
