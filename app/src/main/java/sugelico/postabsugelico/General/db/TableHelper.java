package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 9/19/2015.
 */
public class TableHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Table.TableCat.TableName + " (" +
                    Table.TableCat.KeyID + " INTEGER PRIMARY KEY," +
                    Table.TableCat.code +  INT_TYPE +COMMA_SEP+
                    Table.TableCat.name +  TEXT_TYPE +COMMA_SEP+
                    Table.TableCat.order +  INT_TYPE +COMMA_SEP+
                    Table.TableCat.area_name +  TEXT_TYPE +COMMA_SEP+
                    Table.TableCat.area +  INT_TYPE +
                    " )";




    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + Table.TableCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + CatalogTbl.CatalogCat.TableName;
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }


    public TableHelper(Context context) {
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