package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import sugelico.postabsugelico.General.db.BillHelper;

/**
 * Created by hidura on 1/4/2016.
 */
public class saveProdSys extends AsyncTask<HashMap<String,Object>, String, String> {
    String table;
    Context context;
    SQLiteDatabase db;
    JSONObject params;
    HashMap connProps;
    @Override
    protected String doInBackground(HashMap<String, Object>... paramDict) {

        params=(JSONObject)paramDict[0].get("params");//Getting the WebService parameters.
        context = (Context)paramDict[0].get("Context");//The context, of the activity
        db = context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 1, null);
        if (paramDict[0].containsKey("conparams")){
            connProps=(HashMap)paramDict[0].get("conparams");//The property of the connections.
        }else{
            connProps=null;
        }

        String urlPath="http://test1.conceptoslogicos.com/bprestservices/PedidoSugelico.rsvc?pedido=";
        urlPath+=params.toString();
        System.out.println(urlPath);
        HttpURLConnection connection=null;
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

    protected void onPostExecute(String serverResponse) {
        try {
            JSONObject response = new JSONObject(serverResponse);
            if (response.has("error")){
                String error = response.getString("error");
                System.out.println(error);
            }
            System.out.println(serverResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}