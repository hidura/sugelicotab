package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 10/4/2016.
 */

public class ShoppingCartHelper  extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ShoppingCart.ShippingCartCat.TableName + " (" +
                    ShoppingCart.ShippingCartCat.KeyID+  INT_TYPE + COMMA_SEP +
                    ShoppingCart.ShippingCartCat.commerce_name+  TEXT_TYPE + COMMA_SEP +
                    ShoppingCart.ShippingCartCat.notes+  TEXT_TYPE + COMMA_SEP +
                    ShoppingCart.ShippingCartCat.item_name+  TEXT_TYPE + COMMA_SEP +
                    ShoppingCart.ShippingCartCat.item_type+  TEXT_TYPE + COMMA_SEP +
                    ShoppingCart.ShippingCartCat.picture+  TEXT_TYPE + COMMA_SEP +
                    ShoppingCart.ShippingCartCat.product+  INT_TYPE+ COMMA_SEP +

                    ShoppingCart.ShippingCartCat.price+  DOUBLE_TYPE+ COMMA_SEP +
                    ShoppingCart.ShippingCartCat.amount+  DOUBLE_TYPE+ COMMA_SEP +
                    ShoppingCart.ShippingCartCat.total+  DOUBLE_TYPE+ COMMA_SEP +
                    ShoppingCart.ShippingCartCat.commerce+  INT_TYPE+ COMMA_SEP +
                    ShoppingCart.ShippingCartCat.code+INT_TYPE+
                    " )";




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ShoppingCart.ShippingCartCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public ShoppingCartHelper(Context context) {
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
