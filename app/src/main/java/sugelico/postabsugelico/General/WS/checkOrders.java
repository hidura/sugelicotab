package sugelico.postabsugelico.General.WS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

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

import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.connection;
import sugelico.postabsugelico.General.db.Bill;
import sugelico.postabsugelico.General.db.BillHelper;
import sugelico.postabsugelico.General.db.CatalogHelper;
import sugelico.postabsugelico.General.general;

/**
 * Created by dhidalgo on 8/31/2015.
 */
public class checkOrders extends AsyncTask<HashMap<String, Object>, String, String> {
    private Integer notable;
    private Context context;
    private JSONObject orderID;
    private ArrayList<CatalogCat> ProductsCatalog;
    @Override
    protected String doInBackground(HashMap<String, Object>... sUrl) {
        String ws = "checkOrder";
        context = (Context) sUrl[0].get("context");
        String params = (String)sUrl[0].get("params");
        notable = (Integer)sUrl[0].get("table");
        ProductsCatalog = (ArrayList<CatalogCat>)sUrl[0].get("items");


        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = new connection().NameSPaceWS()+"/?code="+ws+"&"+params;
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

    protected void onPostExecute(String jsonStr) {
        try {
            orderID = new JSONObject(jsonStr);
            final int order = orderID.getInt("order");
            SQLiteDatabase db = context.openOrCreateDatabase(CatalogHelper.DATABASE_NAME, 0, null);
            new BillHelper(context).onCreate(db);

            if (order==0){

                if (new general().isNetworkAvailable(context)){
                    context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null).
                            execSQL("Delete from " + Bill.BillCat.TableName);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("context",context);
                    params.put("table", notable);
                    new closeOrder().execute(params);
                }else{
                    Toast.makeText(context, "No hay conexión disponible, favor conectarse a una red", Toast.LENGTH_SHORT).show();

                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Mesa con orden abierta, que hacer?")
                        .setPositiveButton("Cargar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Load the order, before load the order, it has to delete all the current product on bill reg attach to that order.
                                dialog.dismiss();
                                HashMap<String, Object> params = new HashMap<String, Object>();
                                params.put("order",order);
                                params.put("db",context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null));
                                params.put("context",context);
                                params.put("table", notable);
                                params.put("items", ProductsCatalog);
                                new general().saveTable(context, notable, order);
                                if (new general().isNetworkAvailable(context)){
                                    new downloadProdTbl().execute(params);
                                }else{
                                    Toast.makeText(context, "No hay conexión disponible, favor conectarse a una red", Toast.LENGTH_SHORT).show();

                                }


                            }
                        })
                        .setNegativeButton("Cerrar orden", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Call the server to close the order, and open a new one.
                                if (new general().isNetworkAvailable(context)){

                                    context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null).
                                            execSQL("Delete from " + Bill.BillCat.TableName);
                                    HashMap<String, Object> params = new HashMap<String, Object>();
                                    params.put("dialog", dialog);
                                    params.put("context",context);
                                    params.put("table", notable);
                                    params.put("items", ProductsCatalog);
                                    new closeOrder().execute(params);
                                }else{
                                    Toast.makeText(context, "No hay conexión disponible, favor conectarse a una red", Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
                builder.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(jsonStr);
        }

    }
}
