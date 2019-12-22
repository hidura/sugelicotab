package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 7/2/2015.
 */
public class CategoriesHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Categories.ConstantCat.TableName + " (" +
                    Categories.ConstantCat.KeyID + " INTEGER PRIMARY KEY," +
                    Categories.ConstantCat.code +  INT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.namecat + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.titlecat + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.cat_typename + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.cat_type + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.avatar + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.tp_avatar + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.printer + TEXT_TYPE + COMMA_SEP +
                    Categories.ConstantCat.additional + INT_TYPE +
                    " )";

    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + Categories.ConstantCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + Categories.ConstantCat.TableName;
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
    public static final String DATABASE_NAME = "Alacarta.db";

    public CategoriesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

