package sugelico.postabsugelico.General.WS;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.db.BillHelper;
import sugelico.postabsugelico.General.db.ProductsCarts;
import sugelico.postabsugelico.General.db.ProductsHelper;
import sugelico.postabsugelico.General.db.TableHelper;


/**
 * Created by hidura on 7/14/2016.
 */
public class resendProducts extends AsyncTask<HashMap<String, Object>, String, String>{
    String table;
    Context context;
    SQLiteDatabase db;
    HashMap<String, Object> params;
    HashMap connProps;
    AppCompatActivity activity;
    private cmpDetails companyprofile;
    private UserProfile userProfile;
    @Override
    protected String doInBackground(HashMap<String, Object>... paramDict) {
        companyprofile = (cmpDetails)paramDict[0].get("companyprofile");
        userProfile = (UserProfile)paramDict[0].get("userprofile");
        activity = (AppCompatActivity) paramDict[0].get("activity");
        db = activity.getApplicationContext()
                .openOrCreateDatabase(ProductsHelper.DATABASE_NAME, 0, null);
        String[] productCarts_proj = {
                ProductsCarts.ProductsCat.preorder,
                ProductsCarts.ProductsCat.code,
                ProductsCarts.ProductsCat.product,
                ProductsCarts.ProductsCat.amount,
                ProductsCarts.ProductsCat.term,
                ProductsCarts.ProductsCat.item_name,
                ProductsCarts.ProductsCat.compound,
                ProductsCarts.ProductsCat.optional,
                ProductsCarts.ProductsCat.client,
                ProductsCarts.ProductsCat.companion,
                ProductsCarts.ProductsCat.notes,
                ProductsCarts.ProductsCat.discount,
                ProductsCarts.ProductsCat.portion,
                ProductsCarts.ProductsCat.subtotal,
                ProductsCarts.ProductsCat.tax,
                ProductsCarts.ProductsCat.total,
                ProductsCarts.ProductsCat.usercode

        };
        Cursor prodCart_cursor = db.query(
                ProductsCarts.ProductsCat.TableName,  // The table to query
                productCarts_proj,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ProductsCarts.ProductsCat.item_name+" DESC "                                 // The sort order
        );
        prodCart_cursor.moveToFirst();
        JSONArray prodlst = new JSONArray();
        for (int cat_cont = 0; cat_cont < prodCart_cursor.getCount(); cat_cont++) {
            JSONObject prod_send= null;
            try {
                prod_send = new JSONObject().
                        put("preorder", prodCart_cursor.getInt(0)).
                        put("ordercode", prodCart_cursor.getInt(1)).
                        put("product", prodCart_cursor.getInt(2)).
                        put("amount",  prodCart_cursor.getInt(3)).
                        put("terms", prodCart_cursor.getString(4)).
                        put("plate_name", prodCart_cursor.getString(5)).
                        put("client",  prodCart_cursor.getString(6)).
                        put("companion", prodCart_cursor.getString(7)).
                        put("notes", prodCart_cursor.getString(8)).
                        put("portion", prodCart_cursor.getString(9)).
                        put("subtotal", prodCart_cursor.getDouble(10)).
                        put("tax", prodCart_cursor.getDouble(11)).
                        put("total", prodCart_cursor.getDouble(12)).
                        put("usercode", prodCart_cursor.getInt(13));
                prodCart_cursor.moveToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            prodlst.put(prod_send);
        }

        if(prodlst.length()==0){
            return "";
        }
        if (paramDict[0].containsKey("conparams")){
            connProps=(HashMap)paramDict[0].get("conparams");//The property of the connections.
        }else{
            connProps=null;
        }
        params=new HashMap<String, Object>();
        params.put("products", prodlst);
        params.put("key", userProfile.getSession());
        params.put("classname","Bills.addProdudsOffline");
        HttpURLConnection connection=null;
        try {
            connection = new PostConnection().PostConnection(params, connProps, companyprofile.getPath());

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length

            InputStream input = connection.getInputStream();

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;

            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String orderStr=sb.toString();
            JSONArray ordercodeslst = new JSONArray(orderStr);//The system will return the array of order
            if (ordercodeslst.length()>0) {
                if (ordercodeslst.getJSONObject(0).has("error"))
                    return "";
            }
            for (int count =0; count <ordercodeslst.length(); count++) {
                int ordercode = ((JSONObject) ordercodeslst.get(count)).getInt("ordercode");
                int resp=db.delete(ProductsCarts.ProductsCat.TableName, ProductsCarts.ProductsCat.code + "=" + ordercode, null);
                System.out.println(resp);
            }
            db.close();
            return sb.toString();

        } catch (Exception e) {
            return e.toString();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

        protected void onPostExecute(String target) {
            try {
                JSONArray data= new JSONArray(target);
            } catch (JSONException e) {



            }

        }
}
