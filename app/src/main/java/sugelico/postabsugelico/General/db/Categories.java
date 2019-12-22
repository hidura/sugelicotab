package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 7/2/2015.
 */
public class Categories {

    public static abstract class ConstantCat implements BaseColumns{
        public static final String TableName="categories_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String code="code";
        public static final String namecat="name";
        public static final String titlecat="title";
        public static final String avatar="avatar";
        public static final String tp_avatar="tp_avatar";
        public static final String additional="additional";
        public static final String cat_type="cat_type";
        public static final String printer="printer";
        public static final String cat_typename="cat_typename";

    }

}
