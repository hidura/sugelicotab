package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import sugelico.postabsugelico.General.connection;
import sugelico.postabsugelico.General.db.Images;
import sugelico.postabsugelico.General.db.ImagesHelper;


/**
 * Created by hidura on 3/1/2016.
 */
public class getImgData extends AsyncTask<Void, String, String> {
    private String imglst;
    private Context context;
    private HashMap connProps;
    public getImgData(HashMap<String, Object> params, Context context){
        this.imglst=(String)params.get("imglst");
        this.connProps=(HashMap<String, Object>)params.get("connpros");

        this.context=context;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection connection=null;
        String urlPath = new connection().NameSPaceWS()+"?code=getImgInfo&imglst="+this.imglst;

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
    protected void onPostExecute(String dataresponse) {
        try {
            JSONArray dataCol = new JSONArray(dataresponse);
            SQLiteDatabase db = this.context.openOrCreateDatabase(ImagesHelper.DATABASE_NAME, 0, null);

            for (int cont = 0; cont<dataCol.length(); cont++) {
                JSONObject piece = (JSONObject) dataCol.get(cont);
                db.execSQL("Update " + Images.ImageCat.TableName + " set " + Images.ImageCat.height +
                        "=" + piece.getString("height") +","+
                        Images.ImageCat.width +
                        "=" + piece.getString("width")
                        + " where " + Images.ImageCat.path + "='" + piece.getString("id")+"'");
            }
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
