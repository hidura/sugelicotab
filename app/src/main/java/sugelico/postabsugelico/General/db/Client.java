package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 8/19/2015.
 */
public class Client {

    public static abstract class ClientCat implements BaseColumns {
        public static final String TableName="client_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String cl_name="cl_name";
        public static final String code="code";
        public static final String last_name="last_name";
        public static final String rnc="rnc";
        public static final String telephone="telephone";
        public static final String table="notable";

    }
}
