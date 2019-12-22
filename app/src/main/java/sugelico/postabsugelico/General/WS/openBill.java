package sugelico.postabsugelico.General.WS;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.TableItem;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.db.Orders;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.R;

/**
 * Created by hidura on 11/13/2016.
 */

public class openBill extends AsyncTask<HashMap<String, Object>, String, JSONObject> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    private View view;
    private TableItem tableItem;
    //private AlertDialog dialog;
    private View main_view;
    private Button btn_view;
    private int billtype;
    private int client;
    private String client_name;
    @Override
    protected JSONObject doInBackground(HashMap<String, Object>... params) {

        JSONObject order=new JSONObject();
        activity = (AppCompatActivity) params[0].get("activity");
        context = activity.getApplicationContext();
        main_view = (View) params[0].get("main_view");
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
        //dialog=(AlertDialog)params[0].get("dialog");
        tableItem=(TableItem) params[0].get("table");
        btn_view=(Button) params[0].get("button");
        HashMap<String, Object> args=new HashMap<>();//Getting the WebService parameters.
        args.put("classname", "Bills.openPreorder");
        args.put("key", profile.getSession());
        if (params[0].containsKey("tbl_name"))
            args.put("tbl_name", params[0].get("tbl_name").toString());
        billtype=101;
        if (params[0].containsKey("billtype"))
            billtype=Integer.parseInt(params[0].get("billtype").toString());

        args.put("billtype", billtype);

        if (params[0].containsKey("client")) {
            client = Integer.parseInt(params[0].get("client").toString());
            args.put("client", client);
        }
        if (params[0].containsKey("client_name")) {
            client_name = params[0].get("client_name").toString();
            args.put("client_name", client_name);

        }

        args.put("cashbox", profile.getCashBox());
        if (tableItem!=null) {
            args.put("table", tableItem.getCode());
        }
        args.put("people_on", 1);
//        if (params[0].containsKey("people_on")) {
//            if (params[0].get("people_on").toString().length() > 0)
//                args.put("people_on", Integer.parseInt(params[0].get("people_on").toString()));
//        }


        try {
            connection = new PostConnection().PostConnection(args, connProps, companyprofile.getPath());
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new JSONObject().put("error","Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage()) ;
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

            try {
                order = new JSONObject(orderStr);
                if (order.has("preorder")) {
                    SQLiteDatabase db = context.
                            openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);
                    if (billtype==101) {

                        ContentValues values = new ContentValues();
                        String table_name_new= tableItem.getTblname();
                        if (params[0].containsKey("tbl_name"))
                            table_name_new+= "|" + params[0].get("tbl_name").toString();

                        tableItem.setTblname(table_name_new);
                        values.put(Table.TableCat.name, table_name_new);
                        values.put(Table.TableCat.order, order.getInt("preorder"));

                        long newRowId = db.update(Table.TableCat.TableName, values, Table.TableCat.code + "=" + tableItem.getCode(), null);
                    }else {
                        ContentValues values = new ContentValues();

                        values.put(Orders.OrdersCat.order, order.getInt("preorder"));
                        values.put(Orders.OrdersCat.client, client);
                        long newRowId = db.insert(Orders.OrdersCat.TableName, null, values);
                    }


                }
                return order;
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println(orderStr);
            }
            return null;
        } catch (Exception e) {
            try {
                return new JSONObject().put("error",e.toString()) ;
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
        return order;
    }

    protected void onPostExecute(JSONObject order) {

        try {
            if (order ==null){

                Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.problem_loadingtbl) ,Toast.LENGTH_LONG).show();
                ((View)main_view.findViewById(R.id.wait_order)).setVisibility(View.GONE);
                ((View)main_view.findViewById(R.id.order_det_open)).setVisibility(View.VISIBLE);
                return;
            }
            if (order.has("preorder")) {

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("profile", profile);
                params.put("cmprofile",companyprofile);
                params.put("activity",activity);
                if (billtype==101) {
                    HashMap<Integer, Integer> tbl_info = new HashMap<>();
                    tbl_info.put(tableItem.getCode(), order.getInt("preorder"));
                    tableItem.setOrder(order.getInt("preorder"));
                    profile.setTablebill(tbl_info);
                    profile.setTable_sel(tableItem.getCode());

                    params.put("order",tableItem.getOrder());
                        ((TextView) activity.findViewById(R.id.progress_lbl)).
                            setText(activity.getResources().getString(R.string.area_lbl) + tableItem.getArea_name() +
                                    " " + activity.getResources().getString(R.string.line_sep) + " " +
                                    activity.getResources().getString(R.string.table_lbl) + tableItem.getTblname() +
                                    " " + activity.getResources().getString(R.string.line_sep) + " " +
                                    activity.getResources().getString(R.string.order_no_lbl) + tableItem.getOrder());

                }else {
                    HashMap<Integer, Integer> tbl_info = new HashMap<>();
                    tbl_info.put(client, order.getInt("preorder"));
                    profile.setClientbill(tbl_info);

                    params.put("order",tbl_info.get(client));
                    ((TextView) activity.findViewById(R.id.progress_lbl)).
                            setText(activity.getResources().getString(R.string.deliver_lbl)  +
                                    " " + activity.getResources().getString(R.string.line_sep) + " " +
                                    activity.getResources().getString(R.string.client_lbl) + ":" + client_name+
                                    " " + activity.getResources().getString(R.string.line_sep) + " " +
                                    activity.getResources().getString(R.string.order_no_lbl) + tbl_info.get(client));
                }

                ((ProgressBar) activity.findViewById(R.id.progressbar_main)).setVisibility(View.GONE);
                ((RelativeLayout) activity.findViewById(R.id.table_area)).setVisibility(View.GONE);
                ((TextView) activity.findViewById(R.id.progress_lbl)).setVisibility(View.VISIBLE);
                ((RelativeLayout) activity.findViewById(R.id.product_area)).setVisibility(View.VISIBLE);
                profile.setBill(order.getInt("preorder"));
                if (btn_view!=null) {
                    btn_view.setText(tableItem.getTblname());
                }
                new syncCart().execute(params);
            }else {
                Toast.makeText(activity.getApplicationContext(), order.getString("error") ,Toast.LENGTH_LONG).show();
            }


        }catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.problem_loadingtbl) ,Toast.LENGTH_LONG).show();
            ((View)main_view.findViewById(R.id.wait_order)).setVisibility(View.GONE);
            ((View)main_view.findViewById(R.id.order_det_open)).setVisibility(View.VISIBLE);

        }


    }
}