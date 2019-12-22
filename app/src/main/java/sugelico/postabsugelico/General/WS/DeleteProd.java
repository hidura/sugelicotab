package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import sugelico.postabsugelico.General.connection;
import sugelico.postabsugelico.General.db.Bill;
import sugelico.postabsugelico.General.db.BillHelper;


/**
 * Created by hidura on 9/13/2015.
 */
public class DeleteProd extends AsyncTask<HashMap<String, Object>, String, String> {
    private int ordercode;
    private Context context;
    private JSONObject orderID;

    @Override
    protected String doInBackground(HashMap<String, Object>... sUrl) {
        String ws = "delPlate";
        context = (Context) sUrl[0].get("context");
        String params = (String)sUrl[0].get("params");
        ordercode = (int)sUrl[0].get("ordercode");

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
        if (jsonStr.equals("0")) {
            SQLiteDatabase db = context.
                    openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null);
            db.execSQL("DELETE FROM " + Bill.BillCat.TableName + " where " + Bill.BillCat.ordercode + "=" + ordercode);
            db.close();
//            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_LONG).show();
//            Intent intent=new Intent(context, Catalog.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
        }
        else{
            System.out.println(jsonStr);
        }
    }
}
