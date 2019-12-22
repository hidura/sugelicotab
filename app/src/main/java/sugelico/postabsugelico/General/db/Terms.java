package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by diegohidalgo on 12/7/16.
 */

public class Terms implements BaseColumns {

    public static abstract class TermsCat   {
        public static final String TableName="terms_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String code="code";
        public static final String name="name";
        public static final String product="product";
    }

}