package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 11/28/2015.
 */
public class language {
    public static abstract class languageCat implements BaseColumns {
        public static final String TableName="language_reg";
        public static final int dbversion =1;

        public static final String KeyID="_id";
        public static final String label="label";
        public static final String language="language";
        public static final String id_entity="id_entity";


    }
}
