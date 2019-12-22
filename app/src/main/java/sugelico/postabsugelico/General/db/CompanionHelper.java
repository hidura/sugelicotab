package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dhidalgo on 7/14/2015.
 */
public class CompanionHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CompanionTbl.CompanionCat.TableName + " (" +
                    CompanionTbl.CompanionCat.KeyID + " INTEGER PRIMARY KEY," +
                    CompanionTbl.CompanionCat.product +  INT_TYPE + COMMA_SEP +
                    CompanionTbl.CompanionCat.companion+ INT_TYPE + COMMA_SEP +
                    CompanionTbl.CompanionCat.companion_name+ TEXT_TYPE + COMMA_SEP +
                    CompanionTbl.CompanionCat.price +TEXT_TYPE + COMMA_SEP +
                    CompanionTbl.CompanionCat.cycle+ INT_TYPE +" )";



    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + CompanionTbl.CompanionCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public CompanionHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + CompanionTbl.CompanionCat.TableName;
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
