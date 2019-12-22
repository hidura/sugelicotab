package sugelico.postabsugelico.General.WS;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

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
import sugelico.postabsugelico.General.db.BillHelper;

public class SyncProducts extends AsyncTask<HashMap<String, Object>, String, String> {
    String table;
    Context context;
    SQLiteDatabase db;
    HashMap params;
    HashMap connProps;
    private ArrayList<CatalogCat> ProductsCatalog;
    private ProgressDialog dialog;
    @Override
    protected String doInBackground(HashMap<String, Object>... paramDict) {
        dialog= (ProgressDialog)paramDict[0].get("dialog");
        params=(HashMap)paramDict[0].get("params");//Getting the WebService parameters.
        context = (Context)paramDict[0].get("Context");//The context, of the activity
        ProductsCatalog = (ArrayList<CatalogCat>)paramDict[0].get("items");
        db = context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 1, null);
        if (paramDict[0].containsKey("conparams")){
            connProps=(HashMap)paramDict[0].get("conparams");//The property of the connections.
        }else{
            connProps=null;
        }
        table = (String)paramDict[0].get("table");
        HttpURLConnection connection=null;
        try {
            connection = new PostConnection().PostConnection(params, connProps, (String)paramDict[0].get("url"));

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
            JSONObject response = new JSONObject(target);


            if (response.has("error")){
                Toast.makeText(context, response.getString("error"), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }else if (response.has("msg")){
                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("items", ProductsCatalog);
                dialog.dismiss();
                context.startActivity(intent);
            }



        } catch (JSONException e) {

            e.printStackTrace();
            Toast.makeText(context, "Problemas en el envio al servidor", Toast.LENGTH_LONG).show();

            System.out.println(target);
            dialog.dismiss();

        }

    }
}
