package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 10/4/2016.
 */

public class ShoppingCart {
    public static abstract class ShippingCartCat implements BaseColumns {
        public static final String TableName="shoppingcart_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String product="product";
        public static final String amount="amount";
        public static final String price="price";
        public static final String code="code";
        public static final String total="total";
        public static final String notes="notes";
        public static final String item_name="item_name";
        public static final String item_type="item_type";
        public static final String picture="picture";
        public static final String commerce="commerce";
        public static final String commerce_name="commerce_name";

    }
}
