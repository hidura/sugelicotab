package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 11/8/2015.
 */
public class ProfileHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Profile.ProfileCat.TableName + " (" +
                    Profile.ProfileCat.KeyID+  INT_TYPE + COMMA_SEP +
                    Profile.ProfileCat.session+  TEXT_TYPE + COMMA_SEP +
                    Profile.ProfileCat.person_name+  TEXT_TYPE + COMMA_SEP +
                    Profile.ProfileCat.avatar+  TEXT_TYPE+ COMMA_SEP +
                    Profile.ProfileCat.email+  TEXT_TYPE + COMMA_SEP +
                    Profile.ProfileCat.usercode+  INT_TYPE + COMMA_SEP +
                    Profile.ProfileCat.code+INT_TYPE+
                    " )";




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Profile.ProfileCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public ProfileHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
