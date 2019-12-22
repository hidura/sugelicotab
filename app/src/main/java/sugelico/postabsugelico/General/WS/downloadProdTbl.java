package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.connection;

/**
 * Created by dhidalgo on 8/10/2015.
 */

public class downloadProdTbl extends AsyncTask<HashMap<String, Object>, String, String> {
    int table;
    int order;
    Context context;
    SQLiteDatabase db;
    private ArrayList<CatalogCat> ProductsCatalog;
    private ArrayList<CartProds> cart_products;
    @Override
    protected String doInBackground(HashMap<String, Object>... sUrl) {
        table=(int)sUrl[0].get("table");
        order =(int)sUrl[0].get("order");
        String ws = "getPlates";
        db= (SQLiteDatabase)sUrl[0].get("db");
        String params = "order="+order;
        context = (Context) sUrl[0].get("context");
        ProductsCatalog = (ArrayList<CatalogCat>)sUrl[0].get("items");
        cart_products = new ArrayList<CartProds>();


        String fileName="wsResult";

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = new connection().NameSPaceWS()+"?code="+ws+"&"+params;

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

            if (connection != null)
                connection.disconnect();
        }

    }

    protected void onPostExecute(String jsonStr) {
        JSONArray configArray = null;
        JSONArray clientArray = null;
        if (jsonStr != null || jsonStr.equals("null") != false){
            return;
        }


    }
}