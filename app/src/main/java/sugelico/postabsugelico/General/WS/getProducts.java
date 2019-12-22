package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.db.CategoriesHelper;

/**
 * Created by hidura on 11/13/2016.
 */

public class getProducts extends AsyncTask<HashMap<String, Object>, String, String> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    private View view;
    @Override
    protected String doInBackground(HashMap<String, Object>... params) {


        activity = (AppCompatActivity) params[0].get("activity");
        context = activity.getApplicationContext();
        companyprofile = (cmpDetails) params[0].get("cmprofile");
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        if (params[0].containsKey("conparams")){
            connProps=(HashMap)params[0].get("conparams");//The property of the connections.
        }else{
            connProps=null;
        }
        view = ((View)params[0].get("main_view"));
        profile=(UserProfile)params[0].get("profile");
        HashMap<String, Object> args=new HashMap<>();//Getting the WebService parameters.
        args.put("classname", "Bills.getProdsPreorder");
        args.put("key", profile.getSession());
        args.put("bill", params[0].get("bill"));

        try {
            connection = new PostConnection().PostConnection(args, connProps, companyprofile.getPath());
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
            String orderStr=sb.toString();
            SQLiteDatabase db = context.
                    openOrCreateDatabase(CategoriesHelper.DATABASE_NAME, 0, null);
            try {



                JSONArray order = new JSONArray(orderStr);


            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println(orderStr);
            }
            db.close();
            return orderStr;
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

    protected void onPostExecute(String categories) {





    }
}