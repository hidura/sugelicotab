package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
/**
 * Created by hidura on 2/11/2016.
 */
public class SendUbication extends AsyncTask<HashMap<String,Object>, String, String> {

    Context context;

    HashMap params;
    HashMap connProps;
    boolean close=false;
    @Override
    protected String doInBackground(HashMap<String, Object>... paramDict) {

        params=(HashMap)paramDict[0].get("params");//Getting the WebService parameters.
        context = (Context)paramDict[0].get("Context");//The context, of the activity
        if (paramDict[0].containsKey("close")){
            close = (boolean)paramDict[0].get("close");
        }
        if (paramDict[0].containsKey("conparams")){
            connProps=(HashMap)paramDict[0].get("conparams");//The property of the connections.
        }else{
            connProps=null;
        }
        HttpURLConnection connection=null;
        try {
            connection = new PostConnection().PostConnection(params, connProps,(String)paramDict[0].get("url"));

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
            JSONObject response= new JSONObject(serverResponse);
            if (Integer.parseInt(response.getString("code")) ==0 && close) {
                final Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                context.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}