package sugelico.postabsugelico;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.RuleList;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.WS.PostConnection;
import sugelico.postabsugelico.General.WS.downloadCat;
import sugelico.postabsugelico.General.db.CatalogHelper;
import sugelico.postabsugelico.General.db.CategoriesHelper;
import sugelico.postabsugelico.General.db.ClientHelper;
import sugelico.postabsugelico.General.db.CompanionHelper;
import sugelico.postabsugelico.General.db.Company;
import sugelico.postabsugelico.General.db.CompanyHelper;
import sugelico.postabsugelico.General.db.OptionalsHelper;
import sugelico.postabsugelico.General.db.PriceHelper;
import sugelico.postabsugelico.General.db.PrinterHelper;
import sugelico.postabsugelico.General.db.PrinterTbl;
import sugelico.postabsugelico.General.db.Profile;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.General.db.TermsHelper;
import sugelico.postabsugelico.General.general;

public class Start extends AppCompatActivity {
    public HashMap<Integer, CategoryType> catalog;
    public UserProfile profile;
    private cmpDetails companyProfile;
    private RelativeLayout mLoginFormView;
    private RelativeLayout mProgressView;

    private UserLoginTask mAuthTask = null;
    private SyncEntTask mAuthRootTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        catalog=new HashMap<>();
        mLoginFormView=((RelativeLayout) findViewById(R.id.waiter_login));
        mProgressView = ((RelativeLayout) findViewById(R.id.progress_frm));
        ((EditText)findViewById(R.id.passwd)).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    makeLogin(null);
                    return true;
                }
                return false;
            }
        });
        if (getIntent().hasExtra("company_profile")){
            companyProfile=(cmpDetails)getIntent().getSerializableExtra("company_profile");
            SQLiteDatabase db = getApplicationContext().openOrCreateDatabase(CatalogHelper.DATABASE_NAME, 0, null);
            try {
                String[] projection = {
                        Profile.ProfileCat.usercode
                };

                Cursor cursor = db.query(
                        Profile.ProfileCat.TableName,  // The table to query
                        projection,                               // The columns to return
                        "",                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        ""                                 // The sort order
                );
                if (cursor.getCount() > 0) {
                    //If they are, get them from the database
                    cursor.moveToFirst();

                    ((EditText) findViewById(R.id.passwd)).setText(cursor.getString(0));
                }
            }catch (Exception e){


                db.close();

            }
        }else{
            loadInfo();
        }
        if (getIntent().hasExtra("items")){
            catalog=(HashMap<Integer, CategoryType>)getIntent().getSerializableExtra("items");
        }
        if(companyProfile!=null){
            mLoginFormView.setVisibility(View.VISIBLE);
        }else {
            ((LinearLayout)findViewById(R.id.sync_login)).setVisibility(View.VISIBLE);
        }
        if (getIntent().hasExtra("usercode")) {
            ((EditText)findViewById(R.id.passwd)).setText(getIntent().getStringExtra("usercode"));
            ((CheckBox)findViewById(R.id.savepassword)).setChecked(true);
        }

        }
    public void showLogin(){
        if (companyProfile==null){

        }
    }
    public void makeLogin(View v){
        if(((EditText)findViewById(R.id.passwd)).getText().toString().length()>0) {
            HashMap<String, Object> arguments = new HashMap<>();
            arguments.put("classname", "login.LoginWaiter");
            arguments.put("waiter_code", ((EditText) findViewById(R.id.passwd)).getText().toString());

            mAuthTask = new UserLoginTask(arguments);
            mAuthTask.execute((Void) null);
        }else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.field_empty), Toast.LENGTH_LONG).show();
        }
    }
    public void downloadCatalog(View v){
        try {
            ((RelativeLayout) findViewById(R.id.progress_frm)).setVisibility(View.VISIBLE);
            ((RelativeLayout) findViewById(R.id.waiter_login)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.progress_lbl)).
                    setText(getResources().getString(R.string.downloading_prods));
            SQLiteDatabase db = getApplicationContext().openOrCreateDatabase(CatalogHelper.DATABASE_NAME, 0, null);
            new CatalogHelper(getApplicationContext()).onDelete(db);
            new CategoriesHelper(getApplicationContext()).onDelete(db);
            new TableHelper(getApplicationContext()).onDelete(db);
            new PriceHelper(getApplicationContext()).onDelete(db);
            new TermsHelper(getApplicationContext()).onDelete(db);
            new OptionalsHelper(getApplicationContext()).onDelete(db);
            new CompanionHelper(getApplicationContext()).onDelete(db);
            new ClientHelper(getApplicationContext()).onDelete(db);
            new PrinterHelper(getApplicationContext()).onDelete(db);

            db.close();
            HashMap<String, Object> params = new HashMap<>();
            params.put("activity", Start.this);
            params.put("catalog", catalog);
            params.put("cmprofile", companyProfile);
            params.put("profile", profile);
            new downloadCat().execute(params);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Debe ingresar las credenciales maestras, si ya lo hizo contacte a: help@sugelico.com. Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void makeLoginRoot(View v){

        HashMap<String, Object> arguments=new HashMap<>();
        arguments.put("classname", "Clients.loginRoot");
        arguments.put("username",((EditText)findViewById(R.id.master_username)).getText().toString());
        arguments.put("passwd",((EditText)findViewById(R.id.delreason)).getText().toString());

        arguments.put("token", new FirebaseIDService().getToken());
        arguments.put("brand", Build.BRAND);
        arguments.put("model", Build.MODEL);
        arguments.put("manufacturer", Build.MANUFACTURER);
        arguments.put("serial", Build.SERIAL);

        ((LinearLayout)findViewById(R.id.sync_login)).setVisibility(View.GONE);
        ((RelativeLayout)findViewById(R.id.progress_frm)).setVisibility(View.VISIBLE);
        mAuthRootTask = new SyncEntTask(arguments);
        mAuthRootTask.execute((Void) null);
    }

    public void loadInfo(){
        //This method will open
        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase(CompanyHelper.DATABASE_NAME, 0, null);
        if (new general().checkTable(Company.CompanyCat.TableName, db) && new general().checkTable(PrinterTbl.PrinterCat.TableName, db) ){
            //If the table exist, get the information of the user.


            try{
                String[] projection = {
                        Company.CompanyCat.code,
                        Company.CompanyCat.company_name,
                        Company.CompanyCat.email,
                        Company.CompanyCat.path,
                        Company.CompanyCat.telephone,
                        Company.CompanyCat._address,
                        Company.CompanyCat.companytp,
                        Company.CompanyCat.token,
                        Company.CompanyCat.userkey
                };

                Cursor cursor = db.query(
                        Company.CompanyCat.TableName,  // The table to query
                        projection,                               // The columns to return
                        "",                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        ""                                 // The sort order
                );
                if (cursor.getCount()>0) {
                    try {
                        String[] userprojection = {
                                Profile.ProfileCat.usercode
                        };

                        Cursor usercursor = db.query(
                                Profile.ProfileCat.TableName,  // The table to query
                                userprojection,                               // The columns to return
                                "",                                // The columns for the WHERE clause
                                null,                            // The values for the WHERE clause
                                null,                                     // don't group the rows
                                null,                                     // don't filter by row groups
                                ""                                 // The sort order
                        );
                        if (usercursor.getCount() > 0) {
                            //If they are, get them from the database
                            usercursor.moveToFirst();

                            ((EditText) findViewById(R.id.passwd)).setText(usercursor.getString(0));
                        }
                    }catch (Exception e){

                        String exc = e.getMessage();
                        System.out.println(exc);

                    }
                    //If they are, get them from the database
                    cursor.moveToFirst();
                    companyProfile = new cmpDetails();
                    companyProfile.setCode(cursor.getInt(0));
                    companyProfile.setName(cursor.getString(1));
                    companyProfile.setEmail(cursor.getString(2));
                    companyProfile.setPath(cursor.getString(3));
                    companyProfile.setTelephone(cursor.getString(4));
                    companyProfile.setAddress(cursor.getString(5));
                    companyProfile.setCompanytp(cursor.getInt(6));
                    //companyProfile.setToken(cursor.getString(7));
                    if (!cursor.isNull(8)){
                        if (cursor.getString(8).length()>=1){
                            companyProfile.setUserkey(cursor.getString(8));
                            ((EditText)findViewById(R.id.passwd)).setText(companyProfile.getUserkey());
                            ((CheckBox)findViewById(R.id.savepassword)).setChecked(true);
                        }
                    }
                    companyProfile.setProducts(new HashMap<Integer, Products>());
                    companyProfile.setCategories(new HashMap<Integer, CatalogCat>());
                    cursor.close();

                    String[] printer_proj = {
                            PrinterTbl.PrinterCat.server,
                            PrinterTbl.PrinterCat.username,
                            PrinterTbl.PrinterCat.password,
                            PrinterTbl.PrinterCat.path,
                            PrinterTbl.PrinterCat.categories,
                            PrinterTbl.PrinterCat._type,
                            PrinterTbl.PrinterCat.program,
                            PrinterTbl.PrinterCat.name

                    };
                    Cursor print_cur = db.query(
                            PrinterTbl.PrinterCat.TableName,  // The table to query
                            printer_proj,                               // The columns to return
                            "",                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            ""                                 // The sort order
                    );
                    print_cur.moveToFirst();
                    companyProfile. setPrinters(new ArrayList<printer>());
                    while (!print_cur.isAfterLast()){
                        printer new_printer=new printer();
                        try {
                            companyProfile.getPrinters().add(new_printer);
                            new_printer.setServer(print_cur.getString(0));
                            new_printer.setUsername(print_cur.getString(1));
                            new_printer.setPassword(print_cur.getString(2));
                            new_printer.setPath(print_cur.getString(3));
                            new_printer.setCategories(print_cur.getString(4));
                            new_printer.setType(print_cur.getInt(5));
                            new_printer.setPrinter_name(print_cur.getString(7));
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                        print_cur.moveToNext();
                    }

                    db.close();

                    showLogin();

                }else {
                    cursor.close();
                    db.close();
                    return;
                }
            }catch (Exception e){

                new CompanyHelper(getApplicationContext()).onCreate(db);
                new PrinterHelper(getApplicationContext()).onCreate(db);
                db.close();
                ((LinearLayout)findViewById(R.id.sync_login)).setVisibility(View.VISIBLE);
                ((RelativeLayout)findViewById(R.id.progress_frm)).setVisibility(View.GONE);
                ((RelativeLayout)findViewById(R.id.waiter_login)).setVisibility(View.GONE);
            }


        }else {
            new CompanyHelper(getApplicationContext()).onCreate(db);
            new PrinterHelper(getApplicationContext()).onCreate(db);
            db.close();
            return;
        }
        return;
    }

    public class SyncEntTask extends AsyncTask<Void, Void, String> {
        private String response;
        private final AppCompatActivity activity;
        private HashMap<String, Object> arguments;
        SyncEntTask(HashMap<String, Object> params) {
            activity = Start.this;
            arguments = params;
            ((TextView)findViewById(R.id.progress_lbl)).setText(getResources().getString(R.string.connecting_matrix));
            //showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {


            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String urlPath = "http://my.sugelico.com/";
            try {
                connection =new PostConnection().PostConnection(arguments, null, urlPath);
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
                response= sb.toString();
                return sb.toString();
            } catch (Exception e) {
                System.out.println(e.getMessage());
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

        protected void onPostExecute(String myresponse) {
            mAuthTask = null;

            try {

                JSONObject profile= new JSONObject(myresponse);

                if (profile.has("error")){
                    if (profile.getString("error").equals("0")){
                        Toast.makeText(activity.getApplicationContext(), "Codigo de configuracion invalido", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(activity.getApplicationContext(), profile.getString("error"), Toast.LENGTH_LONG).show();
                    }

                    ((LinearLayout)findViewById(R.id.sync_login)).setVisibility(View.VISIBLE);
                    ((RelativeLayout)findViewById(R.id.progress_frm)).setVisibility(View.GONE);
                }else{
                    SQLiteDatabase db = getApplicationContext().openOrCreateDatabase(CompanyHelper.DATABASE_NAME, 0, null);
                    companyProfile = new cmpDetails();
                    //companyProfile.setToken(arguments.get("token").toString());
                    companyProfile.setCode(profile.getInt("code"));
                    companyProfile.setCompanytp(profile.getInt("client_type"));
                    companyProfile.setName(profile.getString("company_name"));
                    companyProfile.setEmail(profile.getString("email"));
                    companyProfile.setPath(profile.getString("path"));
                    companyProfile.setRNC(profile.getString("rnc"));
                    companyProfile.setAddress(profile.getString("address"));
                    companyProfile.setTelephone(profile.getString("telephone"));
                    companyProfile.setCategories(new HashMap<Integer, CatalogCat>());
                    companyProfile.setProducts(new HashMap<Integer, Products>());
                    if (profile.has("altpath")){
                        //Just for the altpath to the server.
                        companyProfile.setPath(profile.getString("path"));
                    }
                    ContentValues values = new ContentValues();

                    values.put(Company.CompanyCat.code, companyProfile.getCode());
                    values.put(Company.CompanyCat.companytp, companyProfile.getCompanytp());
                    values.put(Company.CompanyCat.company_name, companyProfile.getName());
                    values.put(Company.CompanyCat.email, companyProfile.getEmail());
                    //values.put(Company.CompanyCat.token, companyProfile.getToken());
                    values.put(Company.CompanyCat.path, companyProfile.getPath());
                    values.put(Company.CompanyCat.rnc, companyProfile.getRNC());
                    values.put(Company.CompanyCat._address, companyProfile.getAddress());
                    values.put(Company.CompanyCat.telephone, companyProfile.getTelephone());


                    long newRowId = db.insert(Company.CompanyCat.TableName, null, values);

                    ((TextView)findViewById(R.id.progress_lbl)).setText(getResources().getString(R.string.connecting_matrix));


                    //showProgress(false);
                    mAuthTask = null;
                    HashMap<Long, CartProds> shoppingCart=new HashMap<Long, CartProds>();
                    new general().verifyDB(activity, catalog, shoppingCart, companyProfile);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println(response);
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;


        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, JSONObject> {

        private final AppCompatActivity activity;
        private HashMap<String, Object> arguments;
        private UserProfile userProfile;
        UserLoginTask(HashMap<String, Object> params) {
            activity = Start.this;
            arguments = params;
            ((TextView)findViewById(R.id.progress_lbl)).setText(getResources().getString(R.string.shaking_hand));
            showProgress(true);
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

                    if (!profile.has("error")){

                        userProfile = new UserProfile();
                        if (!profile.isNull("paytypes")) {
                            companyProfile.setPaytypes(new general().toList(profile.getJSONArray("paytypes")));
                        }
                        if (!profile.isNull("rules")) {
                            userProfile.setRules(new RuleList());
                            userProfile.getRules().setRules(profile.getJSONArray("rules"));
                        }

                        if (!profile.isNull("billtypes")) {
                            companyProfile.setBilltypes(new general().toList(profile.getJSONArray("billtypes")));
                        }
                        //Setting default values

                        ClientStruct client =new ClientStruct();
                        client.setName("GENERICO");
                        client.setRnc("");
                        client.setCode(1);
                        client.setNcf_type(2);
                        userProfile.setClient(client);
                        userProfile.setTablebill(new HashMap<Integer, Integer>());
                        userProfile.setActiveorders(new ArrayList<ActiveOrders>());
                        userProfile.setClients(new HashMap<String, ClientStruct>());



                        userProfile.setSession(profile.getString("key"));
                        userProfile.setCode(profile.getInt("id"));
                        userProfile.setName(profile.getString("name"));
                        userProfile.setEmail(profile.getString("email"));
                        userProfile.setUsertype(profile.getInt("type"));
                        userProfile.setAvatar(profile.getString("avatar"));
                        userProfile.setCashbox(0);
                        if (!profile.getString("cashbox").equals("null"))
                            userProfile.setCashbox(profile.getInt("cashbox"));
                        if (!profile.isNull("companies")) {
                            if (profile.getJSONArray("companies").length() > 0) {
                                List<Object> companies = new general().toList(profile.getJSONArray("companies"));

                                userProfile.setCompanies(companies);

                                userProfile.setCompany(Integer.parseInt(((HashMap) companies.get(0)).get("company").toString()));
                            }
                        }

                        if (!profile.isNull("areas")){
                            companyProfile.setAreas(new ArrayList<Integer>());
                            for (int area_cont=0; area_cont<profile.getJSONArray("areas").length(); area_cont++){
                                JSONObject area = profile.getJSONArray("areas").getJSONObject(area_cont);
                                companyProfile.getAreas().add(area.getInt("code"));
                            }
                        }
                        userProfile.setBillCopies(1);
                        if (!profile.isNull("copies")){
                            userProfile.setBillCopies(profile.getInt("copies"));
                        }
                        if (profile.has("orders")) {
                            Iterator<String> keys = profile.getJSONObject("orders").keys();
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
                                            Table.TableCat.name+" DESC"                                 // The sort order
                                    );
                                    cursor.moveToFirst();
                                    JSONArray order_info=profile.getJSONObject("orders").getJSONArray(tblID.toString());

                                    orderInfo.setOrder(order_info.getInt(0));
                                    orderInfo.setName(order_info.getString(1));
                                    ContentValues values = new ContentValues();
                                    values.put(Table.TableCat.name, orderInfo.getName());
                                    values.put(Table.TableCat.order, orderInfo.getOrder());

                                    long newRowId = db.update(Table.TableCat.TableName, values, Table.TableCat.code + "=" + orderInfo.getCode(), null);
                                    if (((CheckBox)activity.findViewById(R.id.savepassword)).isChecked()){
                                        ContentValues passwd_values = new ContentValues();
                                        passwd_values.put(Company.CompanyCat.userkey, ((EditText)activity.findViewById(R.id.passwd)).getText().toString());
                                        db.update(Company.CompanyCat.TableName, passwd_values, "", null);
                                    }else {
                                        ContentValues passwd_values = new ContentValues();
                                        passwd_values.put(Company.CompanyCat.userkey, "");
                                        db.update(Company.CompanyCat.TableName, passwd_values, "", null);
                                    }
                                    orderInfo.setStatus(false);
                                    cursor.close();
                                    db.close();
                                    userProfile.getTablebill().put(orderInfo.getCode(), orderInfo.getOrder());

                                    userProfile.getActiveorders().add(orderInfo);
                                }
                            }
                        }
                    }
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
            mAuthTask = null;
            showProgress(false);

            try {
                if (profile.has("error")) {
                    if (profile.getString("error").equals("0")) {
                        Toast.makeText(activity.getApplicationContext(), getResources().
                                getString(R.string.invalid_waiter_code), Toast.LENGTH_LONG).show();
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

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);

        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public void options(View v){

    }
}
