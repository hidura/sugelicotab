package sugelico.postabsugelico.General.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hidura on 10/30/2016.
 */

public class CompanyHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Company.CompanyCat.TableName + " (" +
                    Company.CompanyCat.KeyID + " INTEGER PRIMARY KEY," +
                    Company.CompanyCat.code +  INT_TYPE + COMMA_SEP +
                    Company.CompanyCat.company_name+ TEXT_TYPE+ COMMA_SEP +
                    Company.CompanyCat.path +TEXT_TYPE + COMMA_SEP +
                    Company.CompanyCat.email +TEXT_TYPE + COMMA_SEP +
                    Company.CompanyCat.rnc +TEXT_TYPE + COMMA_SEP +
                    Company.CompanyCat._address +TEXT_TYPE + COMMA_SEP +
                    Company.CompanyCat.telephone + TEXT_TYPE +COMMA_SEP+
                    Company.CompanyCat.token + TEXT_TYPE +COMMA_SEP+
                    Company.CompanyCat.userkey + TEXT_TYPE +COMMA_SEP+
                    Company.CompanyCat.companytp +  INT_TYPE +" )";



    private static final String SQL_DROP_ENTRIES =
            "DROP TABLE IF EXISTS " + Company.CompanyCat.TableName;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alacarta.db";

    public CompanyHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    private static final String SQL_DELETE_ENTRIES =
            "Delete from " + Company.CompanyCat.TableName;
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
