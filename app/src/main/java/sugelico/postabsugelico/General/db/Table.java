package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 9/19/2015.
 */
public class Table {

    public static abstract class TableCat implements BaseColumns {
        public static final String TableName="table_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String code="code";
        public static final String name="name";
        public static final String area="area";
        public static final String area_name="area_name";
        public static final String order="_order";//It means that if a product is save it on the server.

    }
}
