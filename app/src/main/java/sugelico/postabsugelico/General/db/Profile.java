package sugelico.postabsugelico.General.db;

import android.provider.BaseColumns;

/**
 * Created by hidura on 11/8/2015.
 */
public class Profile {
    public static abstract class ProfileCat implements BaseColumns {
        public static final String TableName="profile_reg";
        public static final int dbversion =1;
        public static final String KeyID="_id";
        public static final String session="session";
        public static final String person_name="person_name";
        public static final String email="email";
        public static final String usercode="usercode";
        public static final String code="code";
        public static final String avatar="avatar";

    }
}
