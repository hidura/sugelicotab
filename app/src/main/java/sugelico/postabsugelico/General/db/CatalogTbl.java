package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 7/4/2015.
 */
public class CatalogTbl {

    public static abstract class CatalogCat implements BaseColumns {
        public static final String TableName="catalog_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String category="category";
        public static final String code="code";
        public static final String nameprod="name";
        public static final String price="price";
        public static final String subtotal="subtotal";
        public static final String tax="tax";
        public static final String pathimg="path";
        public static final String desc="desc";
        public static final String sponsored="sponsored";
        public static final String terms="terms";
        public static final String compounds="compounds";

    }
}
