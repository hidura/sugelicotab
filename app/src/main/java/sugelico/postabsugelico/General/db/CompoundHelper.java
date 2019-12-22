package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 10/25/2016.
 */

public class CompoundHelper  extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Compounds.CompoundCat.TableName + " (" +
                    Compounds.CompoundCat.KeyID + " INTEGER PRIMARY KEY," +
                    Compounds.CompoundCat.product +  INT_TYPE + COMMA_SEP +
                    Compounds.CompoundCat.compound+ INT_TYPE + COMMA_SEP +
                    Compounds.CompoundCat.compound_name +TEXT_TYPE + COMMA_SEP +
                    Compounds.CompoundCat.price +TEXT_TYPE + COMMA_SEP +
                    Compounds.CompoundCat.cycle+ INT_TYPE +" )";



    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + Compounds.CompoundCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public CompoundHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + Compounds.CompoundCat.TableName;
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
