package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.R;

public class sendPaymentsLoan extends AsyncTask<Void, String, JSONObject> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    public HashMap<Long, CategoryType> catalog;
    private HashMap<String, Object> args;
    private Double total_paid;
    private Button btn_target;
    private LinearLayout wait_lay;
    public sendPaymentsLoan(HashMap<String, Object> params){
        companyprofile=(cmpDetails)params.get("companyprofile");
        activity = (AppCompatActivity) params.get("activity");
        profile = (UserProfile)params.get("profile");
        context = activity.getApplicationContext();
        total_paid =(Double)params.get("total_paid");
        String classname="Bills.addPaymentCXC";

        args= new HashMap<>();//Getting the WebService parameters.
        args.put("classname", classname);
        args.put("bills", profile.getCxcbillpaid());
        args.put("key", profile.getSession());
        args.put("cashbox", profile.getCashBox());
        btn_target =((Button)params.get("btn_target"));
        wait_lay=((LinearLayout)params.get("wait_lay"));
    }
    @Override
    protected JSONObject doInBackground(Void... sUrl) {

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
                return new JSONObject(){}.put("error","Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage());
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

            return new JSONObject(sb.toString());

        } catch (Exception e) {
            try {
                return new JSONObject(){}.put("error",e.getMessage());
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
        return null;

    }

    protected void onPostExecute(JSONObject objects) {
        if (objects.has("msg")) {
            try {
                Toast.makeText(activity.getApplicationContext(), objects.getString("msg").toString(),
                        Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            wait_lay.setVisibility(View.GONE);
            btn_target.setVisibility(View.VISIBLE);
            HashMap<String, Object> params = new HashMap<>();

            printer targetprinter = companyprofile.find("1488");
            params.put("target_printer", targetprinter);
            params.put("profile", profile);
            params.put("companyprofile", companyprofile);
            params.put("title", "RECIBO DE PAGO");
            params.put("wait_layout", wait_lay);
            params.put("btn_data", btn_target);
            params.put("activity", activity);
            params.put("total_paid", total_paid);
            new printReceiveTask(params).execute();

        }else {

        }
    }
}