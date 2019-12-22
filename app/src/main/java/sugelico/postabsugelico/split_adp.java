package sugelico.postabsugelico;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.WS.PostConnection;
import sugelico.postabsugelico.General.WS.syncCart;

/**
 * Created by diegohidalgo on 3/14/17.
 */

public class split_adp extends BaseAdapter {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private ArrayList<ActiveOrders> tables;
    private UserProfile profile;
    private HashMap<Long, CartProds> shoppingcart;
    private cmpDetails companyProfile;
    private View main_view;

    public split_adp(FragmentActivity activity, HashMap<Long, CartProds> shoppingCart,
                           UserProfile profile,

                           cmpDetails companyProfile, View targ_view) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.profile = profile;
        this.shoppingcart=shoppingCart;
        this.main_view=targ_view;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return profile.getClients().size();
    }

    @Override
    public Object getItem(int i) {
        return profile.getClients().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    static class ViewHolder {
        RadioButton chkd_option;
        TextView tblname;
        TextView preorder;
        LinearLayout tblSel;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        tbladapter_base.ViewHolder holder;
        final ClientStruct cl_info = profile.getClients().get(profile.getClients().keySet().toArray()[position]);
        LayoutInflater mInflater = (LayoutInflater)
                activity.getApplicationContext().
                        getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view= mInflater.inflate(R.layout.client_view, null);
        view.findViewById(R.id.cl_name).setVisibility(View.GONE);
        ((Button)view.findViewById(R.id.cl_data)).setText(cl_info.getName());
        ((Button)view.findViewById(R.id.cl_data)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> params=new HashMap<String, Object>();
                params.put("client_name", ((Button)v).getText().toString().toUpperCase());
                new transfer2Client(params).execute();

            }
        });
        return view;
    }

    public class transfer2Client extends AsyncTask<Void, Void, String> {


        private HashMap<String, Object> arguments;
        private UserProfile userProfile;
        transfer2Client(HashMap<String, Object> params) {

            arguments=new HashMap<>();
            arguments.put("classname","Bills.splitAccount");
            arguments.put("product",((TextView)main_view.findViewById(R.id.ordercodes)).getText().toString());
            arguments.put("client",params.get("client_name").toString().toUpperCase());
        }

        @Override
        protected String doInBackground(Void... params) {


            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String urlPath = companyProfile.getPath();
            JSONObject profile = null;
            try {
                URL url = new URL(urlPath);
                connection =new PostConnection().PostConnection(arguments, null,companyProfile.getPath());
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
                String response=sb.toString();

                return response;

            } catch (Exception e) {
                return e.getMessage();

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

        protected void onPostExecute(String response) {

            try {
                JSONObject msg_response =new JSONObject(response);
                String msg="";
                if (msg_response.has("error")){
                    msg=msg_response.getString("error");
                }else if (msg_response.has("msg")){
                    msg=msg_response.getString("msg");
                    ((TextView)main_view.findViewById(R.id.ordercodes)).setText("");
                }
                Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                final RecyclerView recycler = ((RecyclerView) main_view.findViewById(R.id.plates_lst));
                recycler.setAdapter(new process_prods(activity, profile, shoppingcart, main_view, false));

                //dialog.dismiss();
                HashMap<String, Object> params = new HashMap<String, Object>();
                shoppingcart.clear();//Cleaning the shopping cart in order to be fill again.
                params.put("profile", profile);
                params.put("cmprofile",companyProfile);
                params.put("activity",activity);
                params.put("order",profile.getBill());
                params.put("shoppingcart",shoppingcart);
                params.put("admin_view", main_view);

                new syncCart().execute(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
