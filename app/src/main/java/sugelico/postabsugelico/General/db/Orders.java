package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 1/28/2018.
 */

public class Orders {
    public static abstract class OrdersCat implements BaseColumns {
        public static final String TableName="orders_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String order="_order";
        public static final String client="client";
        public static final String status="status";
    }
}
