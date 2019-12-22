package sugelico.postabsugelico.General.WS;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import org.json.JSONArray;
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

import sugelico.postabsugelico.Area_adapter;
import sugelico.postabsugelico.ClientAdapter;
import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Adapters.ClientDebt;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.connection;
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;

/**
 * Created by dhidalgo on 8/20/2015.
 */
public class getClientWS extends AsyncTask<Void, String, JSONArray> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    public HashMap<Long, CategoryType> catalog;
    private HashMap<String, Object> args;
    private HashMap<Long, CartProds> shoppingCart;
    private boolean debt;
    private View mainView;
    public getClientWS(HashMap<String, Object> params){
        companyprofile=(cmpDetails)params.get("companyprofile");
        activity = (AppCompatActivity) params.get("activity");
        mainView = (View) params.get("mainview");
        profile = (UserProfile)params.get("profile");
        shoppingCart = (HashMap<Long, CartProds>)params.get("shoppingcart");
        context = activity.getApplicationContext();
        args= new HashMap<>();//Getting the WebService parameters.

        args.put("classname", "Clients.getClients");

        debt=false;
        if (params.containsKey("getDebt") ){
            debt=true;
        }
        if (params.containsKey("infoclient"))
            args.put("infoclient", params.get("infoclient").toString());
    }
    @Override
    protected JSONArray doInBackground(Void... sUrl) {

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = companyprofile.getPath();
        JSONArray profile = null;
        try {
            URL url = new URL(urlPath);
            connection =new PostConnection().PostConnection(args, null,companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new JSONArray().put( new JSONObject(){}.put("error","Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage()));
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

            return new JSONArray(sb.toString());

        } catch (Exception e) {
            try {
                return new JSONArray().put( new JSONObject(){}.put("error",e.getMessage()));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
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
        return profile;

    }

    protected void onPostExecute(JSONArray objects) {

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        ((RecyclerView)mainView.findViewById(R.id.orderslst)).setHasFixedSize(true);

        try {
            if (debt){
                ((RecyclerView) mainView.findViewById(R.id.orderslst)).setAdapter(new ClientDebt(activity, profile,
                        new general().toList(objects), companyprofile));
            }else {
                ((RecyclerView) mainView.findViewById(R.id.orderslst)).setAdapter(new ClientAdapter(activity, profile,
                        new general().toList(objects), companyprofile, shoppingCart, debt));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((RecyclerView)mainView.findViewById(R.id.orderslst)).setLayoutManager(layoutManager);




    }
}