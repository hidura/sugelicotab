package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by dhidalgo on 7/14/2015.
 */
public class OptionalsTbl  {
    public static abstract class OptionalCat implements BaseColumns {
        public static final String TableName="optionals_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String product="product";
        public static final String optional="optional";
        public static final String optional_name="optional_name";

        public static final String price="price";

    }
}
