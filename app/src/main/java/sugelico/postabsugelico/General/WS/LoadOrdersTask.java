package sugelico.postabsugelico.General.WS;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.db.Company;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.R;

/**
 * Created by hidura on 2/9/2018.
 */

public class LoadOrdersTask extends AsyncTask<Void, Void, JSONObject> {

    private final AppCompatActivity activity;
    private HashMap<String, Object> arguments;
    private UserProfile userProfile;
    private JSONArray printer_data=new JSONArray();
    public cmpDetails companyprofile;
    private Dialog dialog;
    public LoadOrdersTask(HashMap<String, Object> params) {
        activity = (AppCompatActivity)params.get("activity");
        userProfile=(UserProfile)params.get("profile");
        companyprofile=(cmpDetails)params.get("companyprofile");
        params.remove("activity");
        params.remove("profile");
        params.remove("companyprofile");
        arguments = params;
        arguments.put("classname","login.getOrdersByUser");
        System.gc();
        //((TextView)findViewById(R.id.progress_lbl)).setText(getResources().getString(R.string.shaking_hand));
        //showProgress(true);
    }

    @Override
    protected JSONObject doInBackground(Void... params) {


        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String urlPath = companyprofile.getPath();
        JSONObject profile = null;
        try {
            URL url = new URL(urlPath);
            connection =new PostConnection().PostConnection(arguments, null,companyprofile.getPath());
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
            userProfile.setTablebill(new HashMap<Integer, Integer>());
            userProfile.setActiveorders(new ArrayList<ActiveOrders>());
            ClientStruct client =new ClientStruct();
            client.setName("GENERICO");
            client.setRnc("");
            client.setCode(1);
            client.setNcf_type(2);
            userProfile.setClient(client);
            userProfile.setClients(new HashMap<String, ClientStruct>());
            try {
                profile= new JSONObject(sb.toString());
                Iterator<String> keys = profile.keys();
                while (keys.hasNext()) {
                    Integer tblID = Integer.parseInt(keys.next());
                    if ((tblID) > 0) {
                        ActiveOrders orderInfo=new ActiveOrders();
                        orderInfo.setCode(tblID);
                        SQLiteDatabase db = activity.getApplicationContext()
                                .openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);

                        Cursor cursor = db.query(
                                Table.TableCat.TableName,  // The table to query
                                new String[]{
                                        Table.TableCat.name},                               // The columns to return
                                Table.TableCat.code+"="+tblID ,// The columns for the WHERE clause
                                null,                            // The values for the WHERE clause
                                null,                                     // don't group the rows
                                null,                                     // don't filter by row groups
                                ""                                 // The sort order
                        );
                        cursor.moveToFirst();
                        JSONArray order_info=profile.getJSONArray(tblID.toString());

                        orderInfo.setOrder(order_info.getInt(0));
                        orderInfo.setName(order_info.getString(1));
                        ContentValues values = new ContentValues();
                        values.put(Table.TableCat.name, orderInfo.getName());
                        values.put(Table.TableCat.order, orderInfo.getOrder());

                        long newRowId = db.update(Table.TableCat.TableName, values, Table.TableCat.code + "=" + orderInfo.getCode(), null);

                        ContentValues passwd_values = new ContentValues();
                        passwd_values.put(Company.CompanyCat.userkey, "");
                        db.update(Company.CompanyCat.TableName, passwd_values, "", null);

                        orderInfo.setStatus(false);
                        cursor.close();
                        db.close();
                        userProfile.getTablebill().put(orderInfo.getCode(), orderInfo.getOrder());
                        userProfile.getActiveorders().add(orderInfo);
                    }
                }
                return new JSONObject(){}.put("msg","Envio exitoso");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            return profile;

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
        return profile;
    }

    protected void onPostExecute(final JSONObject profile) {


        if (!profile.has("error")) {
            System.gc();
            Intent intent = new Intent(activity.getApplicationContext(), Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            userProfile.setDialog(null);
            userProfile.setCxcbillpaid(null);
            intent.putExtra("profile", (Serializable) userProfile);
            intent.putExtra("cmpprofile", (Serializable) companyprofile);
            activity.getApplicationContext().startActivity(intent);
        } else {
            try {
                Toast.makeText(activity.getApplicationContext(), profile.getString("error"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onCancelled() {
        //mAuthTask = null;
        //showProgress(false);

    }
}