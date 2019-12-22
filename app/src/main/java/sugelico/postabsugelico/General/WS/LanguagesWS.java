package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.connection;


/**
 * Created by hidura on 11/29/2015.
 */
public class LanguagesWS extends AsyncTask<HashMap<String, Object>, String, String> {
    private int ordercode;
    private Context context;
    private JSONObject orderID;
    private String language;

    @Override
    protected String doInBackground(HashMap<String, Object>... sUrl) {
        String ws = "getTranslation";
        context = (Context) sUrl[0].get("context");
        language = (String)sUrl[0].get("language");


        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = new connection().NameSPaceWS()+"/?code="+ws+"&language="+language+"&company="+new cmpDetails().getCode();

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
            String alacartaFldr = Environment.getExternalStorageDirectory().getPath()+"/alacarta";
            if (!new File(alacartaFldr).exists()){
                new File(alacartaFldr).mkdir();

            }
            String filePath =language+".json";

            String fullPath = alacartaFldr+"/"+filePath;
            output = new FileOutputStream(fullPath);

            byte data[] = new byte[4096];
            long total = 0;
            int count;

            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                /*if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));*/
                output.write(data, 0, count);
            }
            return "";

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
        Intent intent =new Intent(context, Dashboard.class);
        //intent =new Intent(context, Catalog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);

    }
}
