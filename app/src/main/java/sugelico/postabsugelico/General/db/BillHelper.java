package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 7/25/2015.
 */
public class BillHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Bill.BillCat.TableName + " (" +
                    Bill.BillCat.KeyID + " INTEGER PRIMARY KEY," +
                    Bill.BillCat.code +  INT_TYPE + COMMA_SEP +
                    Bill.BillCat.prodname +TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.amount +TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.guarnicion+TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.notes+TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.portion+TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.price+TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.table+TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.owner+INT_TYPE+COMMA_SEP+
                    Bill.BillCat.term+INT_TYPE+COMMA_SEP+
                    Bill.BillCat.ordercode+INT_TYPE+COMMA_SEP+
                    Bill.BillCat.comment+TEXT_TYPE+COMMA_SEP+
                    Bill.BillCat.order+INT_TYPE+" DEFAULT 0"+COMMA_SEP+
                    Bill.BillCat.saveit+INT_TYPE+" DEFAULT 0"+COMMA_SEP+
                    Bill.BillCat.compound+TEXT_TYPE+
                    " )";




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Bill.BillCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public BillHelper(Context context) {
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

