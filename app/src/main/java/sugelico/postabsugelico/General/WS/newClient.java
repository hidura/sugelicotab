package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import sugelico.postabsugelico.ClientAdapter;
import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;

/**
 * Created by hidura on 2/1/2018.
 */

public class newClient extends AsyncTask<Void, String, JSONObject> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    public HashMap<Long, CategoryType> catalog;
    private HashMap<String, Object> args;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Long, CartProds> shoppingCart_selected;
    private boolean create_bill;
    private View mainView;
    public newClient(HashMap<String, Object> params){
        companyprofile=(cmpDetails)params.get("companyprofile");
        activity = (AppCompatActivity) params.get("activity");
        mainView = (View) params.get("mainview");
        profile = (UserProfile)params.get("profile");
        shoppingCart = (HashMap<Long, CartProds>)params.get("shoppingcart");
        context = activity.getApplicationContext();
        create_bill=true;
        String classname="Bills.addClientPreorder";
        if (params.containsKey("no_create_bill")){
            //This means that the client just will be created and no more.
            create_bill=false;
            classname="Clients.create";
        }
        if (params.containsKey("shoppingcart_selected")){
            //This means that the client just will be created and no more.
            shoppingCart_selected=(HashMap)params.get("shoppingcart_selected");
        }
        args= new HashMap<>();//Getting the WebService parameters.
        args.put("classname", classname);
        args.put("cl_name", params.get("cl_name").toString());
        args.put("telephone", params.get("telephone").toString());
        args.put("rnc", (params.containsKey("rnc"))?params.get("rnc").toString():"");//By default goes empty
        args.put("ncf_type", (params.containsKey("client_type"))?params.get("client_type").toString():"02");//By default goes empty
        args.put("key", profile.getSession());
        args.put("cashbox", profile.getCashBox());
        args.put("credit", 0.00);
        //args.put("rnc", params.get("rnc").toString());
        args.put("_address", params.get("_address").toString());
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

        try {
            profile.setClient(new ClientStruct());
            HashMap<Integer, Integer> tbl_info = new HashMap<>();
            profile.setClientbill(tbl_info);
            profile.getClient().setName(args.get("cl_name").toString());
            profile.getClient().setRnc(args.get("rnc").toString());
            profile.getClient().setNcf_type(Integer.parseInt(args.get("ncf_type").toString()));
            profile.getClient().setExtra(args.get("telephone") + "\n" +
                    args.get("_address") + "\n");

            if (create_bill) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("profile", profile);
                params.put("client", objects.get("client_code"));
                tbl_info.put(Integer.parseInt(objects.get("client_code").toString()), objects.getInt("preorder"));
                profile.getClient().setCode(Integer.parseInt(objects.get("client_code").toString()));
                params.put("cmprofile", companyprofile);
                params.put("activity", activity);
                params.put("client_name", args.get("cl_name"));
                params.put("main_view", null);//This is the number view where the client put the amount
                // of people on the table and the amount of people and the name
                params.put("shoppingcart", shoppingCart);
                params.put("billtype", 102);
                ((TextView) activity.findViewById(R.id.progress_lbl)).
                        setText(activity.getResources().getString(R.string.deliver_lbl) +
                                " " + activity.getResources().getString(R.string.line_sep) + " " +
                                activity.getResources().getString(R.string.client_lbl) + ":" + args.get("cl_name") +
                                " " + activity.getResources().getString(R.string.line_sep) + " " +
                                activity.getResources().getString(R.string.order_no_lbl) + tbl_info.get(Integer.parseInt(objects.get("client_code").toString())));
                ((ProgressBar) activity.findViewById(R.id.progressbar_main)).setVisibility(View.GONE);
                ((RelativeLayout) activity.findViewById(R.id.table_area)).setVisibility(View.GONE);
                ((TextView) activity.findViewById(R.id.progress_lbl)).setVisibility(View.VISIBLE);
                ((RelativeLayout) activity.findViewById(R.id.product_area)).setVisibility(View.VISIBLE);
                ActiveOrders orderInfo = new ActiveOrders();
                profile.getActiveorders().add(orderInfo);
                orderInfo.setOrder(objects.getInt("preorder"));
                orderInfo.setName(args.get("cl_name").toString());
                orderInfo.setStatus(true);
                profile.setBill(objects.getInt("preorder"));
            }else {
                ((TextView)activity.findViewById(R.id.client_info)).setText(profile.getClient().getName());
                profile.getClient().setCode(Integer.parseInt(objects.get("code").toString()));

                if (shoppingCart_selected.size()>0) {
                    for (int cont = 0; cont < shoppingCart_selected.keySet().size(); cont++) {
                        Long key = ((Long) shoppingCart_selected.keySet().toArray()[cont]);
                        shoppingCart_selected.get(key).setClient(profile.getClient().getName());
                    }
                }else {
                    for (int cont = 0; cont < shoppingCart.keySet().size(); cont++) {
                        Long key = ((Long) shoppingCart.keySet().toArray()[cont]);
                        shoppingCart.get(key).setClient(profile.getClient().getName());
                    }
                }
                profile.getDialog().dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(objects.toString());
        }





    }
}