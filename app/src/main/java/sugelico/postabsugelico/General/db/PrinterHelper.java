package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 3/12/2017.
 */

public class PrinterHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + PrinterTbl.PrinterCat.TableName + " (" +
                    PrinterTbl.PrinterCat.KeyID + " INTEGER PRIMARY KEY"+COMMA_SEP+
                    PrinterTbl.PrinterCat.brand+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.model+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.username+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat._type+INT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.password+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.path+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.server+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.name+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.program+TEXT_TYPE+COMMA_SEP+
                    PrinterTbl.PrinterCat.categories+TEXT_TYPE
                    +" )";



    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + PrinterTbl.PrinterCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public PrinterHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + PrinterTbl.PrinterCat.TableName;
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
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
