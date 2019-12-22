package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductsHelper  extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ProductsCarts.ProductsCat.TableName + " (" +
                    ProductsCarts.ProductsCat.KeyID+  INT_TYPE + COMMA_SEP +
                    ProductsCarts.ProductsCat.product+  INT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.usercode+  INT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.preorder+  INT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.amount+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.item_name+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.client+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.companion+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.optional+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.compound+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.discount+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.notes+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.portion+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.saveit+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.subtotal+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.tax+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.total+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.term+  TEXT_TYPE+ COMMA_SEP +
                    ProductsCarts.ProductsCat.code+INT_TYPE+
                    " )";




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductsCarts.ProductsCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public ProductsHelper(Context context) {
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
