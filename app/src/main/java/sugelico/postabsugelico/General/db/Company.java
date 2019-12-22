package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 10/30/2016.
 */

public class Company {

    public static abstract class CompanyCat implements BaseColumns {
        public static final String TableName="company_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String company_name="company_name";
        public static final String code="code";
        public static final String path="path";
        public static final String email="email";
        public static final String rnc="rnc";
        public static final String _address="_address";
        public static final String telephone="telephone";
        public static final String companytp="companytp";
        public static final String token="token";
        public static final String userkey="userkey";

    }
}
