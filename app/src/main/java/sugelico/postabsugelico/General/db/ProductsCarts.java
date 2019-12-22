package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

public class ProductsCarts {
    public static abstract class ProductsCat implements BaseColumns {
        public static final String TableName="products_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String product="product";
        public static final String preorder="preorder";
        public static final String amount="amount";
        public static final String subtotal="subtotal";
        public static final String tax="tax";
        public static final String discount="discount";
        public static final String code="code";
        public static final String total="total";
        public static final String notes="notes";
        public static final String item_name="item_name";
        public static final String companion="companion";
        public static final String compound="compound";
        public static final String optional="optional";
        public static final String term="term";
        public static final String portion="portion";
        public static final String saveit="saveit";
        public static final String client="client";
        public static final String usercode="usercode";

    }
}
