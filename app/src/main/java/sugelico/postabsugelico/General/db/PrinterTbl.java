package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 3/12/2017.
 */

public class PrinterTbl {
    public static abstract class PrinterCat implements BaseColumns{
        public static final String TableName="printer_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String code="code";
        public static final String brand="code";
        public static final String model="model";
        public static final String server="server";
        public static final String name="name";
        public static final String path="path";
        public static final String username="username";
        public static final String password="password";
        public static final String categories="categories";
        public static final String program="program";
        public static final String _type="_type";
    }
}
