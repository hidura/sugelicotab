package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 7/14/2015.
 */
public class PriceTbl {
    public static abstract class PriceCat implements BaseColumns {
        public static final String TableName="price_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String product="product";
        public static final String price="price";
        public static final String comment="comment";


    }
}