package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 10/25/2016.
 */

public class Compounds {
    public static abstract class CompoundCat implements BaseColumns {
        public static final String TableName="compound_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String product="product";
        public static final String compound="compound";
        public static final String compound_name="compound_name";
        public static final String price="price";
        public static final String cycle="cycle";

    }
}
