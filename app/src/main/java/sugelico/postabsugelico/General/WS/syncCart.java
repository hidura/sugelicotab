package sugelico.postabsugelico.General.WS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
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
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.db.CategoriesHelper;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.cart_prods;
import sugelico.postabsugelico.process_prods;
import sugelico.postabsugelico.split_adp;

/**
 * Created by diegohidalgo on 11/15/16.
 */

public class syncCart extends AsyncTask<HashMap<String, Object>, String, String> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    private HashMap<Long, CartProds> shoppingcart;
    private View view;
    private Double total=0.00;
    @Override
    protected String doInBackground(HashMap<String, Object>... params) {


        activity = (AppCompatActivity) params[0].get("activity");
        context = activity.getApplicationContext();
        companyprofile = (cmpDetails) params[0].get("cmprofile");
        view = (View)params[0].get("admin_view");
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        if (params[0].containsKey("conparams")) {
            connProps = (HashMap) params[0].get("conparams");//The property of the connections.
        } else {
            connProps = null;
        }
        shoppingcart=(HashMap<Long, CartProds>) params[0].get("shoppingcart");
        profile = (UserProfile) params[0].get("profile");
        HashMap<String, Object> args = new HashMap<>();//Getting the WebService parameters.
        args.put("classname", "Bills.getProdsPreorder");
        args.put("key", profile.getSession());
        args.put("preorder", params[0].get("order"));

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
            String orderStr = sb.toString();
            SQLiteDatabase db = context.
                    openOrCreateDatabase(CategoriesHelper.DATABASE_NAME, 0, null);
            try {
                if(profile.getClients()==null) {
                    profile.setClients(new HashMap<String, ClientStruct>());
                }
                JSONArray orders = new JSONArray(orderStr);
                for(int cont=0; cont<orders.length(); cont++){
                    JSONObject order_detail = orders.getJSONObject(cont);
                    CartProds product = new CartProds();
                    product.setSaveit(true);
                    product.setAmount(order_detail.getDouble("Cnt"));
                    product.setOrdercode(order_detail.getLong("code"));
                    product.setPrice(order_detail.getDouble("total"));
                    product.setDiscount(order_detail.getDouble("discount"));
                    product.setNotes(order_detail.getString("notes"));
                    product.setSubtotal(order_detail.getDouble("subtotal"));
                    total+=order_detail.getDouble("total");
                    product.setTax(order_detail.getDouble("tax"));
                    product.setProduct(order_detail.getInt("product"));
                    product.setTerms(order_detail.getString("terms"));
                    product.setSaveit(true);
                    product.setPortion(order_detail.getString("portion"));
                    product.setCompaniontp(false);
                    product.setTitle(order_detail.getString("Name"));
                    if (order_detail.has("client_name")){
                        String client=order_detail.getString("client_name");
                        product.setClient(client);
                        if (!profile.getClients().containsKey(client)) {
                            ClientStruct clientData = new ClientStruct();
                            clientData.setName(client);
                            clientData.setRnc("");
                            clientData.setNcf_type(2);
                            profile.getClients().put(client, clientData);
                        }

                    }

                    shoppingcart.put(product.getOrderCode(), product);

                }

            } catch (JSONException e) {
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
        cart_prods frg = (cart_prods) activity.getSupportFragmentManager().
                findFragmentById(R.id.fragment_cart);
        frg.onTrackSelected(shoppingcart, profile, companyprofile);
        ((TextView)activity.findViewById(R.id.total)).setText(total.toString());
        final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().
                beginTransaction();
        ft.detach(frg);
        ft.commit();


    }
}