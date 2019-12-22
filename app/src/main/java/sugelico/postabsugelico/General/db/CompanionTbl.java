package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;



/**
 * Created by dhidalgo on 7/14/2015.
 */
public class CompanionTbl {
    public static abstract class CompanionCat implements BaseColumns {
        public static final String TableName="companion_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String product="product";
        public static final String companion="companion";
        public static final String companion_name="companion_name";
        public static final String price="price";
        public static final String cycle="cycle";

    }
}
