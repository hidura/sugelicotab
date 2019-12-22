package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 7/11/2015.
 */
public class Images {
    public static abstract class ImageCat implements BaseColumns {
        public static final String TableName="images_reg";
        public static final int dbversion =1;
        public static final String product="product";
        public static final String path="path";
        public static final String width="width";
        public static final String height="height";

    }
}
