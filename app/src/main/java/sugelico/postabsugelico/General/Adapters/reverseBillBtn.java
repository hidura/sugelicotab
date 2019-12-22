package sugelico.postabsugelico.General.Adapters;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.WS.PostConnection;
import sugelico.postabsugelico.General.db.Company;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.Start;
import sugelico.postabsugelico.Table_adapter;

public class reverseBillBtn extends RecyclerView.Adapter<Table_adapter.ViewHolder>{
    private FragmentActivity activity;
    private LayoutInflater inflater;

    private JSONArray tables;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private View numb_view;
    private UserProfile userProfile;
    @Override
    public Table_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tables_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        Table_adapter.ViewHolder vh = new Table_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Table_adapter.ViewHolder holder, int position) {
        try {
            JSONObject orderInfo= tables.getJSONObject(position);
            holder.setIsRecyclable(false);

            Button table_name=(Button) holder.view.findViewById(R.id.tbl_unoccupied);
            table_name.setVisibility(View.VISIBLE);
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            String tblname=orderInfo.getString("billpreorder")+"\n "+
                    orderInfo.getString("client_name")+"\n "+
                    formatter.format(orderInfo.getDouble("billtotal"));
            table_name.setText(tblname);
            table_name.setContentDescription(orderInfo.getString("billpreorder"));

            table_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> params=new HashMap<>();
                    params.put("classname", "Bills.reverseBill");
                    params.put("preorder", ((Button)v).getContentDescription());
                    new ReverseBillTask(params).execute();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        if (tables!=null) {
            return tables.length();
        }else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public reverseBillBtn(FragmentActivity activity,
                          UserProfile profile,
                          cmpDetails companyProfile,
                          JSONArray tables) {

        this.activity = activity;
        this.userProfile= profile;
        this.tables=tables;
        this.companyProfile=companyProfile;
        inflater = LayoutInflater.from(activity.getApplicationContext());
    }



    public class ReverseBillTask extends AsyncTask<Void, Void, JSONObject> {

        private HashMap<String, Object> arguments;
        ReverseBillTask(HashMap<String, Object> params) {
            arguments = params;

        }

        @Override
        protected JSONObject doInBackground(Void... params) {

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
                String response=sb.toString();
                try {
                    profile= new JSONObject(response);
                    return profile;


                } catch (JSONException e) {
                    e.printStackTrace();
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
            try {
                if (profile.has("error")) {
                    if (profile.getString("error").equals("0")) {
                        ;
                    } else {
                        Toast.makeText(activity.getApplicationContext(),
                                profile.getString("error"), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Intent intent=new Intent(activity.getApplicationContext(), Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("profile", (Serializable) userProfile);
                    intent.putExtra("cmpprofile", (Serializable) companyProfile);
                    activity.getApplicationContext().startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

}
