package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 7/25/2015.
 */
public class Bill {



    public static abstract class BillCat implements BaseColumns {
        public static final String TableName="bill_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String prodname="prodname";
        public static final String code="code";
        public static final String ordercode="ordercode";
        public static final String amount="amount";
        public static final String price="price";
        public static final String table="notable";
        public static final String guarnicion="guarnicion";
        public static final String notes="notes";
        public static final String portion="portion";
        public static final String owner="owner";
        public static final String order="_order";
        public static final String comment="comment";
        public static final String term="term";
        public static final String compound="compound";
        public static final String saveit="sendit";//It means that if a product is save it on the server.

    }
}
