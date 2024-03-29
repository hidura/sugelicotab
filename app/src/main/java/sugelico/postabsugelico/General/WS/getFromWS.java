package sugelico.postabsugelico.General.WS;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import sugelico.postabsugelico.General.connection;

public class getFromWS extends AsyncTask<ArrayList<Object>, String, String> {

    @Override
    protected String doInBackground(ArrayList<Object>... sUrl) {

        String ws = (String)sUrl[0].get(0);
        String params = (String)sUrl[0].get(1);

        String fileName="wsResult";

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = new connection().NameSpace()+"/?code="+ws+"&"+params;

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
            String alacartaFldr = "/sdcard/alacarta";

            output = new FileOutputStream("/"+alacartaFldr+"/"+fileName);

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

        return (String)sUrl[0].get(1);
    }

    protected void onPostExecute(String target) {

    }
}
