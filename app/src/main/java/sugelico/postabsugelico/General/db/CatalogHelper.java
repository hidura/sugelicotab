package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 7/4/2015.
 */
public class CatalogHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CatalogTbl.CatalogCat.TableName + " (" +
                    CatalogTbl.CatalogCat.KeyID + " INTEGER PRIMARY KEY," +
                    CatalogTbl.CatalogCat.code +  INT_TYPE + COMMA_SEP +
                    CatalogTbl.CatalogCat.category+INT_TYPE + COMMA_SEP+
                    CatalogTbl.CatalogCat.nameprod+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.price+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.subtotal+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.tax+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.desc+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.terms+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.sponsored+INT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.pathimg+TEXT_TYPE+COMMA_SEP+
                    CatalogTbl.CatalogCat.compounds+TEXT_TYPE+
                    " )";

    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + CatalogTbl.CatalogCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";



    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + CatalogTbl.CatalogCat.TableName;
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
    public CatalogHelper(Context context) {
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
