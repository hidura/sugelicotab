package sugelico.postabsugelico.General.WS;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.Area;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.TableItem;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.db.CatalogTbl;
import sugelico.postabsugelico.General.db.Categories;
import sugelico.postabsugelico.General.db.CategoriesHelper;
import sugelico.postabsugelico.General.db.CompanionHelper;
import sugelico.postabsugelico.General.db.CompanionTbl;
import sugelico.postabsugelico.General.db.CompanyHelper;
import sugelico.postabsugelico.General.db.OptionalsTbl;
import sugelico.postabsugelico.General.db.PrinterHelper;
import sugelico.postabsugelico.General.db.PrinterTbl;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.Terms;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.Start;

/**
 * Created by hidura on 9/5/2015.
 */
public class  downloadCat extends AsyncTask<HashMap<String, Object>, String, String> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap<Integer, CategoryType> categoryType;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    private HashMap<Integer, CartProds> shoppingCart;
    private int cont=0;

    @Override
    protected String doInBackground(HashMap<String, Object>... sUrl) {


        activity = (AppCompatActivity) sUrl[0].get("activity");
        context = activity.getApplicationContext();
        categoryType = (HashMap<Integer, CategoryType>) sUrl[0].get("catalog");
        shoppingCart = (HashMap<Integer, CartProds>) sUrl[0].get("shopping_cart");
        companyprofile = (cmpDetails) sUrl[0].get("cmprofile");
        if (sUrl[0].containsKey("set_category")){
            //This means that the system doesnt want to go to the setCategory

        }
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        if (sUrl[0].containsKey("conparams")){
            connProps=(HashMap)sUrl[0].get("conparams");//The property of the connections.
        }else{
            connProps=null;
        }
        profile=(UserProfile)sUrl[0].get("profile");
        HashMap<String, Object> params=new HashMap<>();//Getting the WebService parameters.
        params.put("classname", "Items.getCatalog");
        try {
            connection = new PostConnection().PostConnection(params, connProps, companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;

            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String categories=sb.toString();
            SQLiteDatabase db = context.
                    openOrCreateDatabase(CategoriesHelper.DATABASE_NAME, 0, null);
            try {



                JSONArray fullcatalog = new JSONArray(categories);

                CategoryType category_catalog=null;
                CatalogCat category = null;

                for (cont=0; cont<fullcatalog.length(); cont++){
                    JSONObject product = fullcatalog.getJSONObject(cont);

                    if(!categoryType.containsKey(product.getInt("type_product"))) {
                        category_catalog=new CategoryType();
                        category_catalog.setCat_tpname(product.getString("cat_typename"));
                        String tpavatar="";
                        if (product.has("tp_avatar")) {
                            tpavatar="/resources/site/images/"+product.getString("tp_avatar");
                        }
                        category_catalog.setAvatar(tpavatar);
                        category_catalog.setCode(product.getInt("type_product"));
                        category_catalog.setCategories(new HashMap<Integer, CatalogCat>());
                        categoryType.put(product.getInt("type_product"), category_catalog);

                    }else{
                        category_catalog=categoryType.get(product.getInt("type_product"));
                    }

                    if (!category_catalog.getCategories().containsKey(product.getInt("category"))){
                        category =new CatalogCat();
                        category.setCode(product.getInt("category"));
                        String cat_avatar="";
                        if (product.has("cat_avatar")) {
                            cat_avatar="/resources/site/images/"+product.getString("cat_avatar");
                        }
                        category.setIcon(cat_avatar);
                        category.setTitle(product.getString("cat_name"));
                        category.setPrinter(product.getString("printer"));
                        category.setProducts(new HashMap<Integer, Products>());
                        category_catalog.getCategories().put(product.getInt("category"), category);
                        companyprofile.getCategories().put(product.getInt("category"), category);
                        ContentValues values = new ContentValues();
                        values.put(Categories.ConstantCat.code, category.getCode());
                        values.put(Categories.ConstantCat.printer, category.getPrinter());
                        values.put(Categories.ConstantCat.cat_typename, product.getString("cat_typename"));
                        values.put(Categories.ConstantCat.cat_type, product.getInt("type_product"));
                        values.put(Categories.ConstantCat.tp_avatar, category_catalog.getAvatar());
                        values.put(Categories.ConstantCat.avatar, category.getIcon());

                        values.put(Categories.ConstantCat.namecat, product.getString("cat_name"));
                        long newRowId = db.insert(Categories.ConstantCat.TableName, null, values);


                    }else{
                        category = category_catalog.getCategories().get(product.getInt("category"));

                    }

                    Products product_target = new Products();
                    category.getProducts().put(product.getInt("code"), product_target);
                    companyprofile.getProducts().put(product.getInt("code"), product_target);



                    product_target.setCode(product.getInt("code"));
                    product_target.setTitle(product.getString("item_name"));
                    product_target.setCategory(product.getInt("category"));

                    if (!product.getString("subtotal").equals("null")) {
                        product_target.setSubtotal(Double.parseDouble(product.getString("subtotal")));
                    }else {
                        product_target.setSubtotal(0.00);
                    }

                    if (!product.getString("price").equals("null")) {
                        product_target.setPrice(Double.parseDouble(product.getString("price")));
                    }else {
                        product_target.setPrice(0.00);
                    }
                    if (!product.getString("tax").equals("null")) {
                        product_target.setTax(Double.parseDouble(product.getString("tax")));
                    }else {
                        product_target.setTax(0.00);
                    }

                    String avatar = "";
                    if (product.has("item_avatar"))
                        avatar=product.getString("item_avatar");
                    product_target.setImages(avatar);

                    int recomended=0;
                    if (product.has("recomended")){
                        recomended=1;
                        product_target.setSponsored(product.getBoolean("recomended"));
                    }

                    if (product.has("description")) {
                        product_target.setDescription(product.getString("description"));
                    }

                    ContentValues values = new ContentValues();
                    values.put(CatalogTbl.CatalogCat.code, product_target.getCode());
                    values.put(CatalogTbl.CatalogCat.nameprod, product_target.getTitle());
                    values.put(CatalogTbl.CatalogCat.category, product_target.getCategory());
                    values.put(CatalogTbl.CatalogCat.sponsored, product_target.getSponsored());
                    values.put(CatalogTbl.CatalogCat.desc, product_target.getDescription());
                    values.put(CatalogTbl.CatalogCat.pathimg, product_target.getImages());
                    values.put(CatalogTbl.CatalogCat.subtotal, product_target.getSubtotal());
                    values.put(CatalogTbl.CatalogCat.tax, product_target.getTax());
                    values.put(CatalogTbl.CatalogCat.price, product_target.getPrice());


                    ArrayList<String> termlst=new ArrayList<>();
                    if (product.has("terms")) {
                        if (product.getJSONArray("terms").length()>0){
                            for (int prodCont = 0; prodCont < product.getJSONArray("terms").length(); prodCont++) {
                                JSONObject terms=(JSONObject) product.getJSONArray("terms").get(prodCont);
                                termlst.add(terms.getString("name"));
                                ContentValues compTerm = new ContentValues();
                                compTerm.put(Terms.TermsCat.product, product.getInt("code"));
                                compTerm.put(Terms.TermsCat.code, terms.getInt("term"));
                                compTerm.put(Terms.TermsCat.name, terms.getString("name"));

                                long rowID = db.insert(Terms.TermsCat.TableName, null, compTerm);

                            }
                        }
                    }

                    long newRowId = db.insert(CatalogTbl.CatalogCat.TableName, null, values);








                }





            }catch (JSONException e) {
                String error=e.getMessage();
                System.out.println(cont);
                e.printStackTrace();
                System.out.println(categories);

            }
            connection.disconnect();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activity.findViewById(R.id.progress_lbl)!=null){
                        ((TextView)activity.findViewById(R.id.progress_lbl)).setText(activity.getResources().getString(R.string.loading_additional));
                    }
                }
            });
            /*
            Download additionals
             */
            params=new HashMap<>();//Getting the WebService parameters.
            params.put("classname", "Items.getAdditional");
            new CompanionHelper(context).onCreate(db);
            //Reopening the connection for the tables.
            connection = new PostConnection().PostConnection(params, connProps, companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            br = null;
            sb = new StringBuilder();

            line=null;

            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String additionals=sb.toString();
            try {
                JSONArray addiCatalog = new JSONArray(additionals);
                for (int cont=0; cont<addiCatalog.length(); cont++) {
                    JSONObject addiJSON = addiCatalog.getJSONObject(cont);


                    ContentValues compVal = new ContentValues();
                    compVal.put(CompanionTbl.CompanionCat.product, addiJSON.getInt("product"));
                    compVal.put(CompanionTbl.CompanionCat.companion, addiJSON.getInt("additional"));
                    compVal.put(CompanionTbl.CompanionCat.companion_name, addiJSON.getString("name"));
                    if (!addiJSON.isNull("cycle")) {
                        compVal.put(CompanionTbl.CompanionCat.cycle, addiJSON.getInt("cycle"));
                    }else {
                        compVal.put(CompanionTbl.CompanionCat.cycle, 1);
                    }
                    Double price = addiJSON.getDouble("price");

                    compVal.put(CompanionTbl.CompanionCat.price, price);
                    long rowID = db.insert(CompanionTbl.CompanionCat.TableName, null, compVal);
                }
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println(additionals);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activity.findViewById(R.id.progress_lbl)!=null){
                        ((TextView)activity.findViewById(R.id.progress_lbl)).setText(activity.getResources().getString(R.string.loading_optionals));
                    }
                }
            });
            /*
            Download optionals
             */
            params=new HashMap<>();//Getting the WebService parameters.
            params.put("classname", "Items.getOptional");

            //Reopening the connection for the tables.
            connection = new PostConnection().PostConnection(params, connProps, companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            br = null;
            sb = new StringBuilder();

            line=null;

            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String optional=sb.toString();
            try {
                JSONArray optCatalog = new JSONArray(optional);
                for (int cont=0; cont<optCatalog.length(); cont++) {
                    JSONObject optJSON = optCatalog.getJSONObject(cont);


                    ContentValues optVal = new ContentValues();
                    optVal.put(OptionalsTbl.OptionalCat.product, optJSON.getInt("product"));
                    optVal.put(OptionalsTbl.OptionalCat.optional, optJSON.getInt("optional"));
                    optVal.put(OptionalsTbl.OptionalCat.optional_name, optJSON.getString("name"));
                    Double price = optJSON.getDouble("price");

                    optVal.put(OptionalsTbl.OptionalCat.price, price);
                    long rowID = db.insert(OptionalsTbl.OptionalCat.TableName, null, optVal);
                }
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println(additionals);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activity.findViewById(R.id.progress_lbl)!=null){
                        ((TextView)activity.findViewById(R.id.progress_lbl)).setText(activity.getResources().getString(R.string.loading_tables));
                    }
                }
            });
            /*
            Download the table.
             */
            params=new HashMap<>();//Getting the WebService parameters.
            params.put("classname", "Table.Get");
            params.put("tblname", "");

            //Reopening the connection for the tables.
            connection = new PostConnection().PostConnection(params, connProps, companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            br = null;
            sb = new StringBuilder();

            line=null;

            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String tables=sb.toString();
            try {
                HashMap<Integer, Area> areaLst = new HashMap<Integer, Area> ();
                JSONArray tableCatalog = new JSONArray(tables);
                for (int cont=0; cont<tableCatalog.length(); cont++) {
                    JSONObject areaJSON = tableCatalog.getJSONObject(cont);
                    Area area;
                    HashMap<Integer, TableItem> tblLst=new HashMap<>();
                    System.out.print(areaJSON);
                    if (!areaJSON.isNull("area")) {
                        if (!areaLst.containsKey(areaJSON.getInt("area"))) {
                            area = new Area();
                            areaLst.put(areaJSON.getInt("area"), area);
                            area.setCode(areaJSON.getInt("area"));
                            area.setArea_name(areaJSON.getString("area_name"));
                            area.setTables(tblLst);
                        } else {
                            area = areaLst.get(areaJSON.getInt("area"));
                            tblLst = area.getTables();
                        }
                        TableItem tableItem = new TableItem();
                        tblLst.put(areaJSON.getInt("code"), tableItem);
                        tableItem.setCode(areaJSON.getInt("code"));
                        tableItem.setTblname(areaJSON.getString("tblname"));

                        tableItem.setArea_name(areaJSON.getString("area_name"));
                        tableItem.setArea(areaJSON.getInt("area"));

                        ContentValues values = new ContentValues();
                        values.put(Table.TableCat.code, tableItem.getCode());
                        values.put(Table.TableCat.name, tableItem.getTblname());
                        values.put(Table.TableCat.area, tableItem.getArea());
                        values.put(Table.TableCat.area_name, tableItem.getArea_name());

                        long newRowId = db.insert(Table.TableCat.TableName, null, values);
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println(tables);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activity.findViewById(R.id.progress_lbl)!=null){
                        ((TextView)activity.findViewById(R.id.progress_lbl)).setText(activity.getResources().getString(R.string.loading_printers));
                    }
                }
            });
            params=new HashMap<>();//Getting the WebService parameters.
            params.put("classname", "General.getPrinters");
            params.put("brand", "");

            //Reopening the connection for the tables.
            connection = new PostConnection().PostConnection(params, connProps, companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            br = null;
            sb = new StringBuilder();

            line=null;

            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String printersStr=sb.toString();
            try {
                companyprofile.setPrinters(new ArrayList<printer>());
                JSONArray printerCatalog = new JSONArray(printersStr);
                for (int cont=0; cont<printerCatalog.length(); cont++) {
                    JSONObject printerJSON = printerCatalog.getJSONObject(cont);
                    printer new_printer = new printer();
                    companyprofile.getPrinters().add(new_printer);
                    new_printer.setServer(printerJSON.getString("server"));
                    new_printer.setUsername(printerJSON.getString("username"));
                    new_printer.setPassword(printerJSON.getString("password"));
                    new_printer.setPath(printerJSON.getString("path"));
                    new_printer.setType(printerJSON.getInt("_type"));
                    new_printer.setPrinter_name(printerJSON.getString("name"));
                    ContentValues values = new ContentValues();
                    values.put(PrinterTbl.PrinterCat.code, printerJSON.getInt("code"));
                    values.put(PrinterTbl.PrinterCat.brand, printerJSON.getString("brand"));
                    values.put(PrinterTbl.PrinterCat.model, printerJSON.getString("model"));
                    values.put(PrinterTbl.PrinterCat.categories, printerJSON.getString("category"));
                    values.put(PrinterTbl.PrinterCat.name, printerJSON.getString("name"));
                    values.put(PrinterTbl.PrinterCat.password, printerJSON.getString("password"));
                    values.put(PrinterTbl.PrinterCat.username, printerJSON.getString("username"));
                    values.put(PrinterTbl.PrinterCat.path, printerJSON.getString("path"));
                    values.put(PrinterTbl.PrinterCat._type, printerJSON.getString("_type"));
                    values.put(PrinterTbl.PrinterCat.server, printerJSON.getString("server"));

                    long newRowId = db.insert(PrinterTbl.PrinterCat.TableName, null, values);

                }
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println(tables);
            }
            db.close();
            return categories;
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }

    }

    protected void onPostExecute(String categories) {
        if (categories.contains("Error")){
            SQLiteDatabase db = context.
                    openOrCreateDatabase(CompanyHelper.DATABASE_NAME, 0, null);
            new CompanyHelper(activity.getApplicationContext()).onDelete(db);
            new PrinterHelper(activity.getApplicationContext()).onDelete(db);
            Toast.makeText(activity.getApplicationContext(), "Ha ocurrido un error al cargar los datos, " +
                    "favor intentelo de nuevo", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(activity.getApplicationContext(), Start.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplicationContext().startActivity(intent);
        }

//        new general().setCategories(activity,profile, categoryType, shoppingCart);
        ((TextView)activity.findViewById(R.id.progress_lbl)).
                setText("");


        ((RelativeLayout)activity.findViewById(R.id.progress_frm)).setVisibility(View.GONE);
        if (((RelativeLayout)activity.findViewById(R.id.waiter_login))!=null) {
            ((RelativeLayout) activity.findViewById(R.id.waiter_login)).setVisibility(View.VISIBLE);
        }


        //dialog.dismiss();


    }
}