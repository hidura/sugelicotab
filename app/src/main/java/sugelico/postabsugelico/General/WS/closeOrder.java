package sugelico.postabsugelico.General.WS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.connection;
import sugelico.postabsugelico.General.db.ClientHelper;

/**
 * Created by hidura on 9/21/2015.
 */
public class closeOrder extends AsyncTask<HashMap<String, Object>, String, String> {
    private Spinner clientLst;
    private HashMap<String, Object> params;
    private JSONObject userData;
    private Context context;
    private Integer table;
    private Dialog dialog;
    private ArrayList<CartProds> cart_products;
    private ArrayList<CatalogCat> ProductsCatalog;

    @Override
    protected String doInBackground(HashMap<String, Object>... args) {

        String ws = "createOrder";
        dialog = (Dialog) args[0].get("dialog");
        context = (Context) args[0].get("context");
        table = (Integer)args[0].get("table");
        ProductsCatalog = (ArrayList<CatalogCat>)args[0].get("items");
        cart_products = new ArrayList<CartProds>();

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = new connection().NameSPaceWS()+"/?code="+ws+"&table="+table;

        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
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
            return sb.toString();

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

    protected void onPostExecute(String target) {
        try {
            SQLiteDatabase db = context.openOrCreateDatabase(ClientHelper.DATABASE_NAME,0,null);
            JSONObject response = new JSONObject(target);


//            db.execSQL("UPDATE " + Table.TableCat.TableName + " SET " + Table.TableCat.order + " =" +
//                            response.getString("code") + "," +
//                            Table.TableCat.selected+"=1 "+
//                    " WHERE +" + Table.TableCat.code + "=" + table);
//            db.close();
            if (dialog!=null)
                dialog.dismiss();

            Intent intent=new Intent(context, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("items", ProductsCatalog);
            context.startActivity(intent);


        } catch (JSONException e) {

            e.printStackTrace();
            System.out.println(target);

        }

    }
}

