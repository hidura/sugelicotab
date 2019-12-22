package sugelico.postabsugelico.General;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sugelico.postabsugelico.General.Definitions.Area;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.GPSLocation;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.db.CompanionHelper;
import sugelico.postabsugelico.General.db.Company;
import sugelico.postabsugelico.General.db.CompanyHelper;
import sugelico.postabsugelico.General.db.Orders;
import sugelico.postabsugelico.General.db.OrdersHelper;
import sugelico.postabsugelico.General.db.PrinterHelper;
import sugelico.postabsugelico.General.db.PrinterTbl;
import sugelico.postabsugelico.General.db.ProductsCarts;
import sugelico.postabsugelico.General.db.ProductsHelper;
import sugelico.postabsugelico.General.db.Profile;
import sugelico.postabsugelico.General.db.ProfileHelper;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.WS.downloadCat;
import sugelico.postabsugelico.General.db.Bill;
import sugelico.postabsugelico.General.db.BillHelper;
import sugelico.postabsugelico.General.db.Categories;
import sugelico.postabsugelico.General.db.CategoriesHelper;
import sugelico.postabsugelico.General.db.Client;
import sugelico.postabsugelico.General.db.ClientHelper;
import sugelico.postabsugelico.General.db.CompanionTbl;
import sugelico.postabsugelico.General.db.OptionalsHelper;
import sugelico.postabsugelico.General.db.ShoppingCart;
import sugelico.postabsugelico.General.Definitions.TableItem;
import sugelico.postabsugelico.General.db.CatalogHelper;
import sugelico.postabsugelico.General.db.CatalogTbl;
import sugelico.postabsugelico.General.db.Images;
import sugelico.postabsugelico.General.db.ImagesHelper;
import sugelico.postabsugelico.General.db.OptionalsTbl;
import sugelico.postabsugelico.General.db.PriceHelper;
import sugelico.postabsugelico.General.db.PriceTbl;
import sugelico.postabsugelico.General.db.ShoppingCartHelper;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.General.db.Terms;
import sugelico.postabsugelico.General.db.TermsHelper;
import sugelico.postabsugelico.General.db.language;
import sugelico.postabsugelico.General.db.languageHelper;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.areas;
import sugelico.postabsugelico.category;
import sugelico.postabsugelico.products;
import sugelico.postabsugelico.tables;

/**
 * Created by hidura on 7/1/2015.
 */
public class general {

    public void verifyDB(AppCompatActivity activity,
                         HashMap<Integer, CategoryType> catalog,
                         HashMap<Long, CartProds> shoppingCart,
                         cmpDetails companyProfile) {
        SQLiteDatabase db = activity.getApplicationContext().openOrCreateDatabase(ProfileHelper.DATABASE_NAME, 0, null);
        ((TextView)activity.findViewById(R.id.progress_lbl)).
                setText(activity.getResources().getString(R.string.verifying_db));
        if (!new general().checkTable(ShoppingCart.ShippingCartCat.TableName, db)) {
            new ShoppingCartHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Bill.BillCat.TableName, db)) {
            new BillHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Client.ClientCat.TableName, db)) {
            new ClientHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(CatalogTbl.CatalogCat.TableName, db)) {
            new CatalogHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Categories.ConstantCat.TableName, db)) {
            new CategoriesHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Images.ImageCat.TableName, db)) {
            new ImagesHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(language.languageCat.TableName, db)) {
            new languageHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(OptionalsTbl.OptionalCat.TableName, db)) {
            new OptionalsHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(PriceTbl.PriceCat.TableName, db)) {
            new PriceHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Table.TableCat.TableName, db)) {
            new TableHelper(activity.getApplicationContext()).onCreate(db);
        }

        if (!new general().checkTable(Terms.TermsCat.TableName, db)) {
            new TermsHelper(activity.getApplicationContext()).onCreate(db);
        }

        if (!new general().checkTable(PrinterTbl.PrinterCat.TableName, db)) {
            new PrinterHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(CompanionTbl.CompanionCat.TableName, db)) {
            new CompanionHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Company.CompanyCat.TableName, db)) {
            new CompanyHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Orders.OrdersCat.TableName, db)) {
            new OrdersHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(Profile.ProfileCat.TableName, db)) {
            new ProfileHelper(activity.getApplicationContext()).onCreate(db);
        }
        if (!new general().checkTable(ProductsCarts.ProductsCat.TableName, db)) {
            new ProductsHelper(activity.getApplicationContext()).onCreate(db);
        }


        getProducts(activity,catalog,shoppingCart, companyProfile, null);
    }

    public void setCategories(AppCompatActivity activity,
                              UserProfile profile,
                              HashMap<Integer, CategoryType> products,
                              HashMap<Long, CartProds> shoppingCart,
                              cmpDetails companyProfile) {
        category frg = null;
        //((RelativeLayout)activity.findViewById(R.id.progress_frm)).setVisibility(View.INVISIBLE);
        frg = (category) activity.getSupportFragmentManager().findFragmentById(R.id.frag_category);
        if (frg!=null) {
            frg.onTrackSelected(profile, products, shoppingCart, companyProfile);
            final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }

    public boolean checkTable(String TableName, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }


        return false;
    }

    public boolean checkCartCode(Integer code, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from '" + ShoppingCart.ShippingCartCat.TableName +
                "' where '" + ShoppingCart.ShippingCartCat.code + "' = " + code + "", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }


        return false;
    }

    public HttpURLConnection setProperties(HttpURLConnection connection, HashMap connProp) throws ProtocolException {
        //String ctype="application/x-www-form-urlencoded";

        String ctype = "false";
        if (connProp != null) {
            /**
             * this is because i can preffer send everything on
             */
            if (connProp.get("method") == null) {
                connection.setRequestMethod((String) connProp.get("method"));
            } else {
                connection.setRequestMethod("POST");
            }

            if (connProp.get("Content-Type") == null) {
                connection.setRequestProperty("Content-Type", ctype);
            } else {
                connection.setRequestProperty("Content-Type", (String) connProp.get("Content-Type"));
            }

            if (connProp.get("charset") == null) {
                connection.setRequestProperty("charset", "utf-8");
            } else {
                connection.setRequestProperty("charset", (String) connProp.get("charset"));
            }
        } else {
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Type", ctype);
            connection.setRequestMethod("POST");
        }
        return connection;
    }

    private GPSLocation TODO;

    /**
     * This function return if there's network aviable
     *
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Context context;

    public void getProducts(AppCompatActivity activity,
                            HashMap<Integer, CategoryType> catalog,
                            HashMap<Long, CartProds> shoppingCart,
                            cmpDetails companyProfile, UserProfile profile) {

        SQLiteDatabase db = activity.getApplicationContext().openOrCreateDatabase(CatalogHelper.DATABASE_NAME, 0, null);

        String[] category_proj = {
                Categories.ConstantCat.cat_type,
                Categories.ConstantCat.cat_typename,
                Categories.ConstantCat.code,
                Categories.ConstantCat.namecat,
                Categories.ConstantCat.avatar,
                Categories.ConstantCat.printer,
                Categories.ConstantCat.KeyID

        };
        Cursor cat_cursor = db.query(
                Categories.ConstantCat.TableName,  // The table to query
                category_proj,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                Categories.ConstantCat.namecat+" ASC "                                 // The sort order
        );
        cat_cursor.moveToFirst();
        if (catalog == null)
            catalog = new HashMap<Integer, CategoryType>();
        if (cat_cursor.getCount()>0){
            String status_prog=activity.getResources()
                    .getString(R.string.getting_prods);

            ((TextView)activity.findViewById(R.id.progress_lbl)).setText(status_prog);
            for (int cat_cont = 0; cat_cont < cat_cursor.getCount(); cat_cont++) {
                CategoryType category_catalog = null;
                if (!catalog.containsKey(cat_cursor.getInt(0))) {
                    category_catalog = new CategoryType();
                    category_catalog.setCat_tpname(cat_cursor.getString(1));
                    category_catalog.setCode(cat_cursor.getInt(0));
                    category_catalog.setCategories(new HashMap<Integer, CatalogCat>());
                    catalog.put(cat_cursor.getInt(0), category_catalog);
                } else {
                    category_catalog = catalog.get(cat_cursor.getInt(0));
                }

                CatalogCat category = null;
                if (!category_catalog.getCategories().containsKey(cat_cursor.getInt(2))) {
                    category = new CatalogCat();
                    category.setCode(cat_cursor.getInt(2));
                    category.setTitle(cat_cursor.getString(3));
                    category.setIcon(cat_cursor.getString(4));
                    category.setPrinter(cat_cursor.getString(5));
                    category.setProducts(new HashMap<Integer, Products>());
                    companyProfile.getCategories().put(cat_cursor.getInt(2), category);
                    category_catalog.getCategories().put(cat_cursor.getInt(2), category);
                }
                    String[] projection = {
                            CatalogTbl.CatalogCat.code,
                            CatalogTbl.CatalogCat.nameprod,
                            CatalogTbl.CatalogCat.desc,
                            CatalogTbl.CatalogCat.compounds,
                            CatalogTbl.CatalogCat.sponsored,
                            CatalogTbl.CatalogCat.terms,
                            CatalogTbl.CatalogCat.category,
                            CatalogTbl.CatalogCat.price,
                            CatalogTbl.CatalogCat.subtotal,
                            CatalogTbl.CatalogCat.tax,
                            CatalogTbl.CatalogCat.pathimg
                    };
                    String whrCol = CatalogTbl.CatalogCat.category + "=" + cat_cursor.getInt(2);

                    Cursor cursor = db.query(
                            CatalogTbl.CatalogCat.TableName,  // The table to query
                            projection,                               // The columns to return
                            whrCol,                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            CatalogTbl.CatalogCat.category+" ASC"                                 // The sort order
                    );
                    cursor.moveToFirst();

                for (int cont = 0; cont < cursor.getCount(); cont++) {
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().put(cursor.getInt(0), new Products());

                    companyProfile.getProducts().put(cursor.getInt(0),
                            category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)));


                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                            setCode(cursor.getInt(0));
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).setCycle(1);

                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                            setTitle(cursor.getString(1));
                    if (!cursor.isNull(2))
                        category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                                setDescription(cursor.getString(2));
                    if (!cursor.isNull(4)) {
                        if (cursor.getInt(4) == 1) {
                            category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                                    setSponsored(true);
                        }
                    }
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                            setCategory(cursor.getInt(6));

                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                            setPrice(Double.parseDouble(cursor.getString(7)));

                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                            setSubtotal(cursor.getDouble(8));
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).
                            setTax(cursor.getDouble(9));

                    companyProfile.getProducts().get(category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).getCode()).setCat_name(category_catalog.getCategories().get(cat_cursor.getInt(2)).getTitle());
                    companyProfile.getProducts().put(category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).getCode(), category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)));

                    String[] termProject = {
                            Terms.TermsCat.name
                    };


                    Cursor termCur = db.query(
                            Terms.TermsCat.TableName,  // The table to query
                            termProject,                               // The columns to return
                            Terms.TermsCat.product+"="+category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).getCode(),                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            ""                                 // The sort order
                    );
                    termCur.moveToFirst();
                    ArrayList<String> termlst = new ArrayList<>();
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).setTerms(termlst);
                    for (int tcont = 0; tcont < termCur.getCount(); tcont++) {
                        termlst.add(termCur.getString(0));
                        termCur.moveToNext();
                    }
                    termCur.close();



                    String[] compProject = {
                            CompanionTbl.CompanionCat.companion,
                            CompanionTbl.CompanionCat.companion_name,
                            CompanionTbl.CompanionCat.companion_name,
                            CompanionTbl.CompanionCat.cycle

                    };
                    Cursor CompCur = db.query(
                            CompanionTbl.CompanionCat.TableName,  // The table to query
                            compProject,                               // The columns to return
                            CompanionTbl.CompanionCat.product+"="+category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).getCode(),                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            ""                                 // The sort order
                    );
                    CompCur.moveToFirst();
                    ArrayList<Products> complst = new ArrayList<>();
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).setCompanion(complst);
                    for (int tcont = 0; tcont < CompCur.getCount(); tcont++) {
                        Products companion=new Products();
                        companion.setCode(CompCur.getInt(0));
                        companion.setTitle(CompCur.getString(1));
                        companion.setCycle(CompCur.getInt(2));
                        complst.add(companion);
                        CompCur.moveToNext();
                    }
                    CompCur.close();

                    String[] optProject = {
                            OptionalsTbl.OptionalCat.optional,
                            OptionalsTbl.OptionalCat.optional_name,
                            OptionalsTbl.OptionalCat.price

                    };
                    Cursor OptCur = db.query(
                            OptionalsTbl.OptionalCat.TableName,  // The table to query
                            optProject,                               // The columns to return
                            OptionalsTbl.OptionalCat.product+"="+category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).getCode(),                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            ""                                 // The sort order
                    );
                    OptCur.moveToFirst();
                    ArrayList<Products> optlst = new ArrayList<>();
                    category_catalog.getCategories().get(cat_cursor.getInt(2)).getProducts().get(cursor.getInt(0)).setOptional(complst);
                    for (int tcont = 0; tcont < OptCur.getCount(); tcont++) {
                        Products companion=new Products();
                        companion.setCode(OptCur.getInt(0));
                        companion.setTitle(OptCur.getString(1));
                        companion.setPrice(OptCur.getDouble(2));
                        companion.setCycle(1);//By default always is 1
                        optlst.add(companion);
                        OptCur.moveToNext();
                    }
                    OptCur.close();

                    cursor.moveToNext();
                }
                cursor.close();
                cat_cursor.moveToNext();

            }
            cat_cursor.close();
            db.close();

            setCategories(activity,profile,catalog, shoppingCart, companyProfile);
            products frg = null;
//            frg = (products) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_prodlst);
//            frg.onTrackSelected(profile, companyProfile.getCategories(),shoppingCart, null, companyProfile);
//            final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.commit();
        }else {
            ((TextView)activity.findViewById(R.id.progress_lbl)).
                    setText(activity.getResources().getString(R.string.downloading_prods));
            HashMap<String, Object> params=new HashMap<>();
            params.put("activity", activity);
            params.put("catalog", catalog);
            params.put("shopping_cart", shoppingCart);
            params.put("cmprofile", companyProfile);
            params.put("profile",profile);
            new downloadCat().execute(params);
        }

    }

    public ArrayList<String> getImages(Context context, Integer prodCode) {

        SQLiteDatabase db = context.openOrCreateDatabase(ImagesHelper.DATABASE_NAME, 0, null);
        ArrayList<String> imgList = new ArrayList<String>();
        String[] projection = {
                Images.ImageCat.path
        };

        String whrCol = Images.ImageCat.product + "=" + Integer.toString(prodCode);
        Cursor cursor = db.query(
                Images.ImageCat.TableName,  // The table to query
                projection,                               // The columns to return
                whrCol,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ""                                 // The sort order
        );

        cursor.moveToFirst();
        for (int cont = 0; cont < cursor.getCount(); cont++) {
            imgList.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return imgList;
    }
    public Double numberFixed(Double number, Integer fixed){
        if (fixed == null)
            fixed=2;
        try {
            BigDecimal numberBigDecimal = new BigDecimal(number);
            numberBigDecimal  = numberBigDecimal .setScale(fixed, BigDecimal.ROUND_HALF_UP);
            return numberBigDecimal.doubleValue();
        }catch (Exception e){
            return 0.00;
        }

    }

    public ArrayList<HashMap<String, Double>> getPrices(Context context, Integer prodCode) {

        SQLiteDatabase db = context.openOrCreateDatabase(PriceHelper.DATABASE_NAME, 0, null);
        ArrayList<HashMap<String, Double>> priceLst = new ArrayList<HashMap<String, Double>>();
        String[] projection = {
                PriceTbl.PriceCat.comment,
                PriceTbl.PriceCat.price
        };

        String whrCol = PriceTbl.PriceCat.product + "=" + Integer.toString(prodCode);
        Cursor cursor = db.query(
                PriceTbl.PriceCat.TableName,  // The table to query
                projection,                               // The columns to return
                whrCol,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ""                                 // The sort order
        );

        cursor.moveToFirst();

        for (int cont = 0; cont < cursor.getCount(); cont++) {
            HashMap<String, Double> price = new HashMap<>();
            price.put(cursor.getString(0),cursor.getDouble(1));
            priceLst.add(price);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return priceLst;
    }

    /**
     *
     * @param context
     * @param table
     * @return
     *
     * The next area works with the table and the orders
     * saving the orders and the table.
     */
    public long saveTable(Context context, Integer table, Integer order) {
        /*
        This method ask for the context, the
        table, and the order and inserted all
        of that on the table, and return the id.
         */
        SQLiteDatabase db = context.openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);
        db.execSQL("UPDATE " + Table.TableCat.TableName + " SET " + Table.TableCat.order + " =" +
                order + " WHERE +" + Table.TableCat.code + "=" + table);
        db.close();
        return 1;
    }

    public HashMap<Integer, TableItem> getTable(AppCompatActivity activity,
                         UserProfile profile, cmpDetails companyProfile,
                         HashMap<Long, CartProds> shoppingCart) {
        /**
         * This method return the information
         * of the table, and the product.
         *
         */
        HashMap<Integer, Area> arealst=new HashMap<>();
        HashMap<Integer, TableItem> tableslst= new HashMap<>();
        HashMap<Integer, TableItem> maintblst= new HashMap<>();



        try {
            SQLiteDatabase db = activity.getApplicationContext()
                    .openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);

            Cursor cursor = db.query(
                    Table.TableCat.TableName,  // The table to query
                    new String[]{Table.TableCat.code,
                            Table.TableCat.name,
                            Table.TableCat.area,
                            Table.TableCat.area_name},                               // The columns to return
                    "" ,// The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    Table.TableCat.name+" ASC"                                 // The sort order
            );
            cursor.moveToFirst();

            for(int cont=0; cont<cursor.getCount(); cont++){
                if (!arealst.containsKey(cursor.getInt(2))){
                    tableslst=new HashMap<>();
                    arealst.put(cursor.getInt(2),new Area());
                    arealst.get(cursor.getInt(2)).setArea_name(cursor.getString(3));
                    arealst.get(cursor.getInt(2)).setCode(cursor.getInt(2));
                    arealst.get(cursor.getInt(2)).setTables(tableslst);
                }else {
                    tableslst = arealst.get(cursor.getInt(2)).getTables();
                }
                TableItem tblitem=new TableItem();
                tblitem.setCode(cursor.getInt(0));
                tblitem.setTblname(cursor.getString(1));
                if (profile.getTablebill().get(cursor.getInt(0))!=null) {
                    tblitem.setOrder(profile.getTablebill().get(cursor.getInt(0)));
                }
                tblitem.setArea(cursor.getInt(2));
                tblitem.setArea_name(cursor.getString(3));
                tableslst.put(tblitem.getCode(), tblitem);
                maintblst.put(tblitem.getCode(), tblitem);
                cursor.moveToNext();
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            SQLiteDatabase db = activity.getApplicationContext().openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);
            new TableHelper(activity.getApplicationContext()).onCreate(db);
            db.close();
        }

        areas frg = null;
        //((RelativeLayout)activity.findViewById(R.id.progress_frm)).setVisibility(View.INVISIBLE);
        frg = (areas) activity.getSupportFragmentManager().findFragmentById(R.id.frag_areas);
        frg.onTrackSelected(profile, arealst, companyProfile,shoppingCart);
        final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();

        tables frgtbls = (tables) activity.getSupportFragmentManager().
                findFragmentById(R.id.fragment_tbl);
        frgtbls.onTrackSelected(profile, maintblst, companyProfile, shoppingCart);

        final android.support.v4.app.FragmentTransaction ft_tbl = activity.getSupportFragmentManager().beginTransaction();
        ft_tbl.detach(frgtbls);
        ft_tbl.attach(frgtbls);
        ft_tbl.commit();

        return tableslst;
    }


    public int getID(SQLiteDatabase db, String tablename) {

        Cursor cursor = db.query(
                tablename,  // The table to query
                new String[]{"_id"},                               // The columns to return
                "",                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ""                                 // The sort order
        );

        return (cursor.getCount() + 1);
    }

    public boolean Exist(SQLiteDatabase db, String tablename, String colwhere) {
        boolean status = false;
        Cursor cursor = db.query(
                tablename,  // The table to query
                new String[]{"Count(*)"},                               // The columns to return
                colwhere,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ""                                 // The sort order
        );
        cursor.moveToFirst();
        int exist = cursor.getInt(0);
        if (exist > 0) {
            status = true;
        }
        cursor.close();

        return status;
    }


    public void forceOrientation(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float scaleFactor = metrics.density;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth > 720) {
            //Device is a 10" tablet
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (smallestWidth > 600) {
            //Device is a 7" tablet

            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (smallestWidth > 400) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (smallestWidth > 300) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public int getDay() {
        /*
        This function returns the
        id of the day.
         */
        HashMap<String, Integer> dayLst = new HashMap<>();
        dayLst.put("Monday", 1);
        dayLst.put("Tuesday", 2);
        dayLst.put("Wednesday", 3);
        dayLst.put("Thursday", 4);
        dayLst.put("Friday", 5);
        dayLst.put("Saturday", 6);
        dayLst.put("Sunday", 7);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        return dayLst.get(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            else if(value == null) {
                value = null;
            }
            map.put(key, value);
        }
        return map;
    }



    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public HashMap<String, Object> getBillType(cmpDetails companyProfile, int billtype){
        for (Object billtp :
                companyProfile.getBilltypes()) {
            if ( Integer.parseInt(((HashMap<String, Object>)billtp).get("code").toString())==billtype){
                return ((HashMap<String, Object>)billtp);
            }
        }
        return null;
    }

    public JSONArray parseCart2Server(HashMap<Integer, CartProds> products_lst, UserProfile profile, cmpDetails companyprofile){
        JSONArray prodlst=new JSONArray();
        Object[] iterator = products_lst.keySet().toArray();

        for (int cont = 0; cont < iterator.length; cont++) {
            Long position = Long.parseLong(iterator[cont].toString());
            CartProds cart_prod = products_lst.get(position);
            if (cart_prod != null) {
                boolean state = cart_prod.getSaveit();
                try {
                    if (!cart_prod.getCompaniontp()) {



                        JSONObject prod_send=new JSONObject().put("preorder", cart_prod.getOrder()).
                                put("ordercode", cart_prod.getOrderCode()).
                                put("product", cart_prod.getProduct()).
                                put("amount", cart_prod.getAmount()).
                                put("terms", cart_prod.getTerms()).
                                put("plate_name", cart_prod.getTitle()).
                                put("usercode", profile.getCode()).
                                put("subtotal", cart_prod.getSubtotal()).
                                put("tax", cart_prod.getTax()).
                                put("total", cart_prod.getPrice()).
                                put("client", cart_prod.getClient()).
                                put("companion", cart_prod.getCompanion()).
                                put("portion", cart_prod.getPortion()).
                                put("notes", cart_prod.getNotes());

                        prodlst.put(prod_send);


                    } else{
                        if (companyprofile.getProducts().
                                get(cart_prod.getProduct())!=null) {

                            JSONObject prod_send=new JSONObject().put("preorder", cart_prod.getOrder()).
                                    put("ordercode", cart_prod.getOrderCode()).
                                    put("product", cart_prod.getProduct()).
                                    put("amount", cart_prod.getAmount()).
                                    put("terms", cart_prod.getTerms()).
                                    put("plate_name", cart_prod.getTitle()).
                                    put("usercode", profile.getCode()).
                                    put("subtotal", cart_prod.getSubtotal()).
                                    put("tax", cart_prod.getTax()).
                                    put("total", cart_prod.getPrice()).
                                    put("client", cart_prod.getClient()).
                                    put("companion", cart_prod.getCompanion()).
                                    put("portion", cart_prod.getPortion()).
                                    put("notes", cart_prod.getNotes());

                            prodlst.put(prod_send);
                        }
                        else {
                            System.out.println("Producto errado: "+cart_prod.getTitle());
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }


        return prodlst;
    }

    public boolean checkNumber(String data){
        try{
            Double numberCheck = Double.parseDouble(data);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public HashMap<String, ArrayList<Products>> getOrderByCat(HashMap<Integer, Products> mainlst, ArrayList<Products> products){
        //This method receive the list of return it order by category.
        HashMap<String, ArrayList<Products>> listOrder=new HashMap<>();
        for (Products prod:products){
            if (!listOrder.containsKey(mainlst.get(prod.getCode()).getCat_name())) {
                listOrder.put(mainlst.get(prod.getCode()).getCat_name(), new ArrayList<Products>());
            }
            listOrder.get(mainlst.get(prod.getCode()).getCat_name()).add(prod);
        }

        return listOrder;
    }

    public ArrayList<HashMap<String, String>> cursor2Map(Cursor cursor) {

        ArrayList<HashMap<String, String>> resultSet=new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            HashMap<String, String> rowObject = new HashMap<String, String>();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d(ContentValues.TAG, e.getMessage());
                    }
                }
            }
            resultSet.add(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

}



