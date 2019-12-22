package sugelico.postabsugelico.General.WS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.Start;

/**
 * Created by diegohidalgo on 8/24/17.
 */

public class closeAccount  extends AsyncTask<Void, String, String> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    private ProgressDialog dialog;
    public HashMap<Long, CategoryType> catalog;
    private HashMap<String, JSONArray> printer_info;
    private HashMap<String, Double> paytypePrint;
    private String table_info;
    private ArrayList<Long> orderprints;
    private Integer preorder;
    private LinearLayout wait_layout;
    private HashMap<Integer, CartProds> products_lst;
    private HashMap<Integer, CartProds> products_lst_print;
    private HashMap<String, Object> args;
    private Button btn_confirmation;
    private View v;
    public closeAccount(HashMap<String, Object> params) {

        companyprofile=(cmpDetails)params.get("companyprofile");
        activity = (AppCompatActivity) params.get("activity");
        profile = (UserProfile)params.get("profile");
        context = activity.getApplicationContext();
        products_lst=((HashMap<Integer, CartProds>) params.get("products"));
        products_lst_print=((HashMap<Integer, CartProds>) products_lst.clone());
        args= new HashMap<>();//Getting the WebService parameters.
        args.put("classname", "Bills.closeAccount");
        args.put("waiter", profile.getSession());
        args.put("key", profile.getSession());
        preorder=(Integer) params.get("preorder");
        args.put("ncf_type", "00");
        if (params.containsKey("btn_confirmation"))
            btn_confirmation=((Button)params.get("btn_confirmation"));

        if (params.containsKey("products")) {
            products_lst = ((HashMap<Integer, CartProds>) params.get("products"));
            JSONArray productslst=new general().parseCart2Server(products_lst, profile, companyprofile);
            args.put("products", productslst);
        }
        args.put("client_name_pre", params.get("client_name_pre"));
        args.put("client", params.get("client"));
        if(params.containsKey("rnc"))
            if (params.get("rnc")!=null)
                args.put("rnc", (params.get("rnc").toString()));

        if (profile.getClient().getNCF_type()!=null) {
            args.put("client_type", (profile.getClient().getNCF_type() < 10) ? "0" +
                    profile.getClient().getNCF_type() :
                    profile.getClient().getNCF_type().toString());
        }
        if (params.get("client")==null)
            args.put("client", params.get("client_name_pre").toString().toUpperCase());

        args.put("subtotal", (Double)params.get("subtotal"));
        args.put("tax", (Double)params.get("tax"));

        if (params.containsKey("wait_layout"))
            wait_layout =(LinearLayout)params.get("wait_layout");
        args.put("total", (Double)params.get("total"));
        args.put("discount", (Double)params.get("discount"));
        if (params.containsKey("billtp_extra")){
            args.put("billtp_extra", (Double)params.get("billtp_extra"));
        }
        args.put("paytypelst", params.get("paytypelst"));
        args.put("order_type",profile.getBillType());
        args.put("billtype", profile.getBillType());
        args.put("preorder", preorder);
        if (params.containsKey("company"))
            args.put("company", params.get("company"));
        if (params.containsKey("paytypelstprint"))
            paytypePrint = (HashMap<String, Double>) params.get("paytypelstprint");

        v=(View)params.get("button");

    }


    @Override
    protected String doInBackground(Void ...params) {


        InputStream input = null;

        HttpURLConnection connection = null;

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
            //Eliminating the products of the client.
            Integer prods_size=products_lst.keySet().size();
            Object[] keyslst = products_lst.keySet().toArray();

            for (int cont=0; cont<prods_size; cont++){
                if (products_lst.get( keyslst[cont]).getClient().toLowerCase().equals(args.get("client_name_pre").toString().toLowerCase())) {
                    products_lst.remove( keyslst[cont]);
                }

            }
            return sb.toString();
        } catch (Exception e) {
            String err=e.getMessage();
            return err;
        } finally {

            try {
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }

    }

    protected void onPostExecute(String categories) {
        //Volver a las mesas, no salir.
        //new general().saveTable(activity.getApplicationContext(), profile.getTable_sel(),0);
        String ncf =null;
        String ncf_title=null;
        String ncf_exp=null;

        try {
            JSONObject printer_data=new JSONObject(categories);
            if (printer_data.has("error")){
//                if (btn_confirmation!=null){
//                    btn_confirmation.setEnabled(true);
//                }
                Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.err_ttl)+
                        "\n"+printer_data.getString("error"), Toast.LENGTH_LONG).show();

                return;
            }else{
                if (printer_data.has("ncf")){
                    ncf = printer_data.getString("ncf");
                    ncf_title = printer_data.getString("ncf_title");
                    ncf_exp = printer_data.getString("ncf_exp");
                }

                if (printer_data.getInt("bill_close")==1) {
                    String tblinfo=((TextView) activity.findViewById(R.id.progress_lbl)).getText().toString();
                    String tblname=tblinfo.split("-")[1].split(":")[1].split(Pattern.quote("|"))[0];

                    SQLiteDatabase db = context.openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);

                    db.execSQL("UPDATE " + Table.TableCat.TableName + " SET " + Table.TableCat.order + " =" +
                            0 + ", " + Table.TableCat.name + "='" + tblname + "' WHERE +" + Table.TableCat.code + "=" + profile.getTable_sel());

                    db.close();
                }
                HashMap<String, Object> taskasyncparams =new HashMap<>();
                taskasyncparams.put("activity", activity);
                taskasyncparams.put("profile", profile);
                taskasyncparams.put("companyprofile", companyprofile);
                new LoadOrdersTask(taskasyncparams).execute();
                HashMap<String, Object> param=new HashMap<>();
                param.put("profile", profile);
                param.put("client_name", args.get(("client_name_pre")));
                if (args.containsKey("rnc"))
                    param.put("rnc", args.get(("rnc")));
                param.put("products", products_lst_print);
                param.put("table_info",((TextView) activity.findViewById(R.id.progress_lbl)).
                        getText().toString());
                param.put("companyprofile", companyprofile);
                printer targetprinter=companyprofile.find("1488");
                param.put("target_printer", targetprinter);
                param.put("total", args.get("total"));
                param.put("subtotal", args.get("subtotal"));
                param.put("tax", args.get("tax"));
                if (args.containsKey("billtp_extra")){
                    param.put("billtp_extra", (Double)args.get("billtp_extra"));
                }
                param.put("discount", args.get("discount"));
                param.put("paytype", paytypePrint);
                if (ncf!=null && ncf_title!=null) {
                    param.put("ncf", ncf);
                    param.put("ncf_title", ncf_title);
                    param.put("ncf_exp", ncf_exp);
                }
                if (profile.getBillType()==122){
                    param.put("title","FACTURA A CREDITO");
                }

                new printerTask(param).execute();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}