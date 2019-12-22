package sugelico.postabsugelico;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import sugelico.postabsugelico.General.Adapters.CompaniesBTN;
import sugelico.postabsugelico.General.Adapters.bill_denominations;
import sugelico.postabsugelico.General.Adapters.denominations;
import sugelico.postabsugelico.General.Adapters.selClientBill;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.SugelicoMobileBill;
import sugelico.postabsugelico.General.Definitions.TableItem;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.paytype_adp;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.WS.ApertureCashBox;
import sugelico.postabsugelico.General.WS.LoadOrdersTask;
import sugelico.postabsugelico.General.WS.PostConnection;
import sugelico.postabsugelico.General.WS.closeAccount;
import sugelico.postabsugelico.General.WS.downloadCat;
import sugelico.postabsugelico.General.WS.getBillsFromCashBox;
import sugelico.postabsugelico.General.WS.getClientBillWS;
import sugelico.postabsugelico.General.WS.getClientWS;
import sugelico.postabsugelico.General.WS.getOpenDelivery;
import sugelico.postabsugelico.General.WS.getProductsConsumed;
import sugelico.postabsugelico.General.WS.newClient;
import sugelico.postabsugelico.General.WS.openBill;
import sugelico.postabsugelico.General.WS.sendProduct;
import sugelico.postabsugelico.General.db.Company;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;
import sugelico.postabsugelico.General.general;


public class Dashboard extends AppCompatActivity
        implements products.OnFragmentInteractionListener, cart_prods.OnFragmentInteractionListener,
        category.OnFragmentInteractionListener,areas.OnFragmentInteractionListener,
        tables.OnFragmentInteractionListener, SugelicoMobile.OnFragmentInteractionListener,
        deliveryorder.OnFragmentInteractionListener, bill_frg.OnFragmentInteractionListener,
        CxcBills.OnFragmentInteractionListener{

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public HashMap<Integer, CategoryType> catalog;
    public UserProfile profile;
    private AppCompatActivity activity;
    public cmpDetails companyprofile;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Long, CartProds> shoppingCart_selected;
    private HashMap<String, Object> billDetails;
    private HashMap<String, Double> paytypes;
    private JSONObject paytypes_send;//The paytypes to be send to the server
    private HashMap<String, String> ncf_type;
    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        //client = new OkHttpClient();
        //start();
        //Definying the NCF
        if (ncf_type==null) {
            ncf_type = new HashMap<>();
        }
        ncf_type.put("Sin valor fiscal","02");
        ncf_type.put("Que generan y/o sustentantan gastos","01");
        ncf_type.put("Regimen Especiales","14");

        profile = (UserProfile) getIntent().getSerializableExtra("profile");
        if (profile.getSugelicomobilebills()==null){
            profile.setSugelicomobilebills(new HashMap<Integer, SugelicoMobileBill>());
        }
        companyprofile = (cmpDetails) getIntent().getSerializableExtra("cmpprofile");
        activity=Dashboard.this;
        if (getIntent().hasExtra("items")) {
            catalog = (HashMap<Integer, CategoryType>) getIntent().getSerializableExtra("items");
        }else {
            catalog = new HashMap<Integer, CategoryType>();

        }
        shoppingCart=new HashMap<Long, CartProds>();

        ((RelativeLayout)findViewById(R.id.progress_frm)).setVisibility(View.VISIBLE);
        if (profile.getRules()!=null) {
            if (!profile.getRules().findRule(6)) {
                ((Button) findViewById(R.id.cxc_btn)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.cxc_btn_charge)).setVisibility(View.GONE);

            }
        }
        if (profile.getCompanies()==null){
            ((Button)findViewById(R.id.close_bill)).setVisibility(View.GONE);

        }else{
            if (profile.getCompanies().size()==0){
                ((Button)findViewById(R.id.close_bill)).setVisibility(View.GONE);
            }
        }
        if (companyprofile.getCompanytp()==1896) {
            ((ConstraintLayout)findViewById(R.id.tablearea)).setVisibility(View.VISIBLE);
            ((ConstraintLayout)findViewById(R.id.fastfood)).setVisibility(View.GONE);
            new general().getTable(Dashboard.this, profile, companyprofile, shoppingCart);

            if (profile.getUserType()==75 || profile.getUserType()==71 || profile.getUserType()==74){
                Button configuration = ((Button)findViewById(R.id.configuration));
                configuration.setVisibility(View.VISIBLE);
                configuration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final android.app.AlertDialog.Builder dialog_builder = new android.app.
                                AlertDialog.Builder(Dashboard.this, android.R.style.
                                Theme_Translucent_NoTitleBar_Fullscreen);
                        final View targ_view=getLayoutInflater().inflate(R.layout.admin_opt,
                                null);

                        dialog_builder.setTitle(getResources().getString(R.string.adm_ttl));
                        dialog_builder.setView(targ_view);
                        final Dialog dialog = dialog_builder.create();
                        dialog.show();

                        ((ImageButton)targ_view.findViewById(R.id.cashbox)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.GONE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.GONE);
                                if (profile.getRules().findRule(14)){
                                    //If this is active means that the user
                                    // have to enter the denomination.
                                    ((ScrollView)targ_view.findViewById(R.id.cashbox_area)).setVisibility(View.VISIBLE);
                                    GridLayoutManager manager = new
                                            GridLayoutManager(activity.getApplicationContext(),
                                            2); // MAX NUMBER OF SPACES
                                    ((RecyclerView)targ_view.findViewById(R.id.cashboxlst)).
                                            setLayoutManager(manager);

                                    ((RecyclerView)targ_view.findViewById(R.id.cashboxlst)).
                                            setAdapter(new bill_denominations(activity,
                                                    profile,companyprofile));
                                    //This part here just apply if the denominations is off.
                                    ((Button)targ_view.findViewById(R.id.cashbox_next)).
                                            setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //This part here just apply if the denominations is off.
                                            HashMap<String, Object> params=new HashMap<String, Object>();
                                            params.put("cashbox",profile.getCashBox());
                                            params.put("mainview",targ_view);
                                            params.put("dialog", dialog);
                                            params.put("activity", Dashboard.this);
                                            params.put("profile", profile);
                                            params.put("companyprofile", companyprofile);
                                            new getProductsConsumed(params).execute();
                                        }
                                    });
                                }else if(profile.getRules().findRule(15) &&
                                        !profile.getRules().findRule(14)){

                                    ((ScrollView)targ_view.findViewById(R.id.cashbox_area)).setVisibility(View.VISIBLE);
                                    //This part here just apply if the denominations is off.
                                    HashMap<String, Object> params=new HashMap<String, Object>();
                                    params.put("classname","Bills.closeInventory");
                                    params.put("cashbox",profile.getCashBox());
                                    params.put("mainview",targ_view);
                                    params.put("close_cashbox", 1);
                                    params.put("dialog", dialog);
                                    new CloseCashBoxTask(params).execute();

                                }else {
                                    HashMap<String, Object> params=new HashMap<String, Object>();
                                    params.put("classname","Bills.closeCashBoxTab");
                                    params.put("cashbox",profile.getCashBox());
                                    params.put("mainview",targ_view);
                                    params.put("close_cashbox", 1);
                                    params.put("dialog", dialog);
                                    new CloseCashBoxTask(params).execute();
                                }

                            }
                        });

                        ((ImageButton)targ_view.findViewById(R.id.apperture_btn)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.VISIBLE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.GONE);
                                ((Button)targ_view.findViewById(R.id.aperture)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap<String, Object> params = new HashMap<String, Object>();
                                        params.put("dialog", dialog);
                                        params.put("activity", Dashboard.this);
                                        params.put("profile", profile);
                                        params.put("companyprofile", companyprofile);
                                        params.put("key", profile.getSession());
                                        params.put("amount_open",Double.parseDouble(((EditText)
                                                targ_view.findViewById(R.id.amount_aperture)).
                                                getText().toString()));
                                        new ApertureCashBox(params).execute();
                                    }
                                });
                            }
                        });
                        ((ImageButton)targ_view.findViewById(R.id.reprint)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.GONE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.VISIBLE);
                                HashMap<String, Object> params= new HashMap<>();
                                params.put("companyprofile", companyprofile);
                                params.put("activity", Dashboard.this);
                                params.put("profile", profile);
                                params.put("dialog", dialog);
                                params.put("mainview", targ_view);

                                new getBillsFromCashBox(params).execute();

                            }
                        });

                        ((ImageButton)targ_view.findViewById(R.id.change_cmp)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.GONE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.VISIBLE);
                                GridLayoutManager manager = new GridLayoutManager(activity.getApplicationContext(),3); // MAX NUMBER OF SPACES
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setLayoutManager(manager);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).
                                        setAdapter(new CompaniesBTN(activity,profile,companyprofile));

                            }
                        });
                    }
                });


            }
        }else{
            ((ConstraintLayout)findViewById(R.id.tablearea)).setVisibility(View.GONE);
            ((ConstraintLayout)findViewById(R.id.fastfood)).setVisibility(View.VISIBLE);

//            ((Button)findViewById(R.id.start_ordering)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HashMap<String, Object> resendparams=new HashMap<>();
//                    resendparams.put("companyprofile",companyprofile);
//                    resendparams.put("userprofile",profile);
//                    resendparams.put("activity",Dashboard.this);
//                    new resendProducts().execute(resendparams);
//                    SQLiteDatabase db = activity.getApplicationContext()
//                            .openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);
//
//                    Cursor cursor = db.query(
//                            Table.TableCat.TableName,  // The table to query
//                            new String[]{Table.TableCat.code,
//                                    Table.TableCat.name,
//                                    Table.TableCat.area,
//                                    Table.TableCat.area_name},                               // The columns to return
//                            "" ,// The columns for the WHERE clause
//                            null,                            // The values for the WHERE clause
//                            null,                                     // don't group the rows
//                            null,                                     // don't filter by row groups
//                            ""                                 // The sort order
//                    );
//                    cursor.moveToFirst();
//                    //AlertDialog.Builder dialog_builder = new AlertDialog.Builder(activity);
//                    View numb_view = activity.getLayoutInflater().inflate(R.layout.number_keyboard, null);
//
//                    HashMap<String, Object> params = new HashMap<String, Object>();
//                    params.put("people_on", 1);
//                    params.put("profile", profile);
//                    params.put("cmprofile", companyprofile);
//                    params.put("activity", activity);
//                    params.put("main_view", numb_view);
//                    profile.setBilltype(101);
//                    profile.setOrdertype(101);
//                    TableItem tableItem = new TableItem();
//                    tableItem.setCode(cursor.getInt(0));
//                    tableItem.setArea_name(cursor.getString(3));
//                    tableItem.setTblname(cursor.getString(1));
//                    tableItem.setArea(cursor.getInt(2));
//                    params.put("table", tableItem);
//                    params.put("shoppingcart", shoppingCart);
//                    cursor.close();
//                    db.close();
//                    new openBill().execute(params);
//                }
//            });
            if (profile.getUserType()==75 || profile.getUserType()==71 || profile.getUserType()==74){
                Button configuration = ((Button)findViewById(R.id.configuration_fastfood));
                configuration.setVisibility(View.VISIBLE);
                configuration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final android.app.AlertDialog.Builder dialog_builder = new android.app.AlertDialog.Builder(Dashboard.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                        final View targ_view=getLayoutInflater().inflate(R.layout.admin_opt, null);

                        dialog_builder.setTitle(getResources().getString(R.string.adm_ttl));
                        dialog_builder.setView(targ_view);
                        final Dialog dialog = dialog_builder.create();
                        dialog.show();
                        ((ImageButton)targ_view.findViewById(R.id.cashbox)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.GONE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.GONE);
                                HashMap<String, Object> params=new HashMap<String, Object>();
                                params.put("classname","Bills.closeCashBoxTab");
                                params.put("cashbox",profile.getCashBox());
                                params.put("close_cashbox", 1);
                                params.put("dialog", dialog);
                                new CloseCashBoxTask(params).execute();
                            }
                        });

                        ((ImageButton)targ_view.findViewById(R.id.apperture_btn)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.VISIBLE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.GONE);
                                ((Button)targ_view.findViewById(R.id.aperture)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap<String, Object> params = new HashMap<String, Object>();
                                        params.put("dialog", dialog);
                                        params.put("activity", Dashboard.this);
                                        params.put("profile", profile);
                                        params.put("companyprofile", companyprofile);
                                        params.put("key", profile.getSession());
                                        params.put("amount_open",Double.parseDouble(((EditText)targ_view.findViewById(R.id.amount_aperture)).getText().toString()));
                                        new ApertureCashBox(params).execute();
                                    }
                                });
                            }
                        });
                        ((ImageButton)targ_view.findViewById(R.id.reprint)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout)targ_view.findViewById(R.id.cashboxapperture)).setVisibility(View.GONE);
                                ((RecyclerView)targ_view.findViewById(R.id.pastorders)).setVisibility(View.VISIBLE);
                                HashMap<String, Object> params= new HashMap<>();
                                params.put("companyprofile", companyprofile);
                                params.put("activity", Dashboard.this);
                                params.put("profile", profile);
                                params.put("dialog", dialog);
                                params.put("mainview", targ_view);

                                new getBillsFromCashBox(params).execute();

                            }
                        });
                    }
                });


            }

        }
        new general().getProducts(Dashboard.this, catalog, shoppingCart, companyprofile,profile);
        if (((Button)findViewById(R.id.send_products))!=null) {
            ((Button) findViewById(R.id.send_products)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    ((LinearLayout)findViewById(R.id.sendProdWait)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.sendwait_lbl)).setText(getResources().
                            getString(R.string.sending2server));
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("profile", profile);
                    params.put("products", shoppingCart);
                    params.put("preorder", profile.getBill());
                    params.put("activity", Dashboard.this);
                    params.put("cmprofile", companyprofile);
                    params.put("catalog", catalog);
                    params.put("table_info", ((TextView) findViewById(R.id.progress_lbl)).getText().toString());

                    ProgressDialog dialog = new ProgressDialog(getApplicationContext()); // this = YourActivity
                    try {
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("Enviando. Por favor espere...");
                        dialog.setIndeterminate(true);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    } catch (Exception e) {

                    }
                    params.put("dialog", dialog);

                    new sendProduct().execute(params);

                }
            });
        }

        //Go Back to the tables
        Button gobacktbls = ((Button)findViewById(R.id.gobacktables));
        if (gobacktbls!=null) {
            gobacktbls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("activity", Dashboard.this);
                    params.put("profile", profile);
                    params.put("companyprofile", companyprofile);
                    new LoadOrdersTask(params).execute();


                }
            });
        }


        ((Button)findViewById(R.id.area_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.delivery_layout)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.sugelicomobile_layout)).setVisibility(View.GONE);
                ((ConstraintLayout) activity.findViewById(R.id.cxcfrag)).setVisibility(View.GONE);
                ((ConstraintLayout)findViewById(R.id.tablearea)).setVisibility(View.VISIBLE);
                if (companyprofile.getCompanytp()==1896) {
                    ((ConstraintLayout) findViewById(R.id.tablearea)).setVisibility(View.VISIBLE);
                    ((ConstraintLayout) findViewById(R.id.fastfood)).setVisibility(View.GONE);
                }else{
                    ((ConstraintLayout) findViewById(R.id.tablearea)).setVisibility(View.GONE);
                    ((ConstraintLayout) findViewById(R.id.fastfood)).setVisibility(View.VISIBLE);
                }
            }

        });

        ((Button)findViewById(R.id.delivery_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecyclerView)findViewById(R.id.orderslst)).setVisibility(View.VISIBLE);
                ((ConstraintLayout)findViewById(R.id.info_order_layout)).setVisibility(View.VISIBLE);
                ((ConstraintLayout)findViewById(R.id.newcl_layout)).setVisibility(View.GONE);
                HashMap<String, Object> params = new HashMap<>();
                params.put("companyprofile", companyprofile);
                params.put("activity", Dashboard.this);
                params.put("profile", profile);
                params.put("mainview", ((LinearLayout)findViewById(R.id.delivery_layout)));
                params.put("shoppingcart",shoppingCart);
                params.put("classname","Bills.getActiveDelivery");

                new getOpenDelivery(params).execute();
                ((LinearLayout)findViewById(R.id.sugelicomobile_layout)).setVisibility(View.GONE);
                ((ConstraintLayout)findViewById(R.id.tablearea)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.delivery_layout)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.addClient)).setVisibility(View.VISIBLE);
                ((ConstraintLayout) activity.findViewById(R.id.cxcfrag)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.find_order)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RecyclerView)findViewById(R.id.orderslst)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)findViewById(R.id.info_order_layout)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)findViewById(R.id.newcl_layout)).setVisibility(View.GONE);
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("companyprofile", companyprofile);
                        params.put("activity", Dashboard.this);
                        params.put("profile", profile);
                        params.put("mainview", ((LinearLayout)findViewById(R.id.delivery_layout)));
                        params.put("shoppingcart",shoppingCart);
                        params.put("infoclient", ((EditText)findViewById(R.id.client_name_search)).getText().toString());
                        new getClientWS(params).execute();
                        }
                });
            }

        });

        ((Button)findViewById(R.id.cxc_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.sugelicomobile_layout)).setVisibility(View.GONE);
                ((ConstraintLayout)findViewById(R.id.tablearea)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.delivery_layout)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.addClient)).setVisibility(View.GONE);
                ((ConstraintLayout) activity.findViewById(R.id.cxcfrag)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.find_order)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RecyclerView)findViewById(R.id.orderslst)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)findViewById(R.id.info_order_layout)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)findViewById(R.id.newcl_layout)).setVisibility(View.GONE);
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("companyprofile", companyprofile);
                        params.put("activity", Dashboard.this);
                        params.put("profile", profile);
                        params.put("mainview", ((LinearLayout)findViewById(R.id.delivery_layout)));
                        params.put("shoppingcart",shoppingCart);
                        params.put("infoclient", ((EditText)findViewById(R.id.client_name_search)).getText().toString());
                        params.put("getDebt", true);
                        profile.setCxcbillpaid(new JSONObject());
                        new getClientWS(params).execute();
                    }
                });
            }

        });

        ((Button)findViewById(R.id.mobile_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.delivery_layout)).setVisibility(View.GONE);
                ((ConstraintLayout)findViewById(R.id.tablearea)).setVisibility(View.GONE);
                ((ConstraintLayout) activity.findViewById(R.id.cxcfrag)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.sugelicomobile_layout)).setVisibility(View.VISIBLE);
            }

        });


        ((Button)findViewById(R.id.addClient)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecyclerView)findViewById(R.id.orderslst)).setVisibility(View.GONE);
                ((ConstraintLayout)findViewById(R.id.info_order_layout)).setVisibility(View.GONE);
                ((ConstraintLayout) activity.findViewById(R.id.cxcfrag)).setVisibility(View.GONE);
                ((ConstraintLayout)findViewById(R.id.newcl_layout)).setVisibility(View.VISIBLE);

            }
        });
        ((Button)findViewById(R.id.save_client)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("companyprofile", companyprofile);
            params.put("activity", Dashboard.this);
            params.put("profile", profile);
            params.put("mainview", ((LinearLayout)findViewById(R.id.delivery_layout)));
            params.put("shoppingcart",shoppingCart);
            params.put("cl_name", ((EditText)findViewById(R.id.client_name)).getText().toString() );
            params.put("telephone", ((EditText)findViewById(R.id.telephone)).getText().toString() );
            params.put("birthdate", ((EditText)findViewById(R.id.birthdate)).getText().toString() );
            params.put("reference", ((EditText)findViewById(R.id.paid_amount)).getText().toString() );
            params.put("_address", ((EditText)findViewById(R.id.client_address)).getText().toString() );
            new newClient(params).execute();
            }
        });

        ((Button)findViewById(R.id.goback_mainprods)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RelativeLayout)findViewById(R.id.table_area)).setVisibility(View.GONE);
                        ((RelativeLayout)findViewById(R.id.product_area)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)findViewById(R.id.payment_conf)).setVisibility(View.GONE);
                    }
                }
        );

    }

    public void onBackPressed() {

    }

    public void sendPreAccount(View v){
        bill_frg frg = (bill_frg) activity.getSupportFragmentManager().
                findFragmentById(R.id.fragment_paymentcnf);
        frg.onTrackSelected(profile, companyprofile, shoppingCart);

        final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        ((GridView)findViewById(R.id.preAcc_div)).setAdapter(new printAcc(Dashboard.this,
                shoppingCart, profile, companyprofile, null));

        ((RelativeLayout)findViewById(R.id.table_area)).setVisibility(View.GONE);
        ((RelativeLayout)findViewById(R.id.product_area)).setVisibility(View.GONE);
        ((ConstraintLayout)findViewById(R.id.payment_conf)).setVisibility(View.VISIBLE);

    }

    public void closeBill(View v) throws JSONException {

        billDetails = new HashMap<>();
        shoppingCart_selected=new HashMap<>();
        bill_frg frg = (bill_frg) activity.getSupportFragmentManager().
                findFragmentById(R.id.fragment_paymentcnf);
        frg.onTrackSelected(profile, companyprofile, shoppingCart_selected);

        final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        paytypes = new HashMap<>();
        paytypes_send = new JSONObject();
        calculateLeft();
        ((GridView)findViewById(R.id.preAcc_div)).setAdapter(null);
        ((GridView) findViewById(R.id.denominations_lst)).setAdapter(null);

        ((GridView)findViewById(R.id.preAcc_div)).setAdapter(new selClientBill(Dashboard.this, shoppingCart, shoppingCart_selected,
                profile, companyprofile, billDetails));
        ((Button)findViewById(R.id.change_client)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder dialog_builder =new android.app.AlertDialog.Builder(Dashboard.this);
                final View client_layout =activity.getLayoutInflater().inflate(R.layout.fragment_deliveryorder, null);
                ((ConstraintLayout)client_layout.findViewById(R.id.info_order_layout)).setVisibility(View.GONE);
                dialog_builder.setView(client_layout);
                profile.setDialog(dialog_builder.create());
                profile.getDialog().show();
                String[]  yearSpinner = new String[] {
                        "Sin valor fiscal",
                        "Que generan y/o sustentantan gastos",
                        "Regimen Especiales",
                        "Gubernamentales"
                };
                Spinner yearSpin = (Spinner) client_layout.findViewById(R.id.client_type);
                ArrayAdapter<String> ncf_adapter = new ArrayAdapter<String>(Dashboard.this,
                        android.R.layout.simple_spinner_item, yearSpinner);
                ncf_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                yearSpin.setAdapter(ncf_adapter);
                ((Button)client_layout.findViewById(R.id.find_order)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RecyclerView)client_layout.findViewById(R.id.orderslst)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)client_layout.findViewById(R.id.info_order_layout)).setVisibility(View.VISIBLE);
                        ((ConstraintLayout)client_layout.findViewById(R.id.newcl_layout)).setVisibility(View.GONE);
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("companyprofile", companyprofile);
                        params.put("activity", Dashboard.this);
                        params.put("profile", profile);
                        params.put("mainview", client_layout);
                        params.put("shoppingcart_selected",shoppingCart_selected);
                        params.put("shoppingcart",shoppingCart);
                        params.put("infoclient", ((EditText)findViewById(R.id.client_name_search)).getText().toString());
                        new getClientBillWS(params).execute();
                    }
                });
                ((Button)client_layout.findViewById(R.id.addClient)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RecyclerView)client_layout.findViewById(R.id.orderslst)).setVisibility(View.GONE);
                        ((ConstraintLayout)client_layout.findViewById(R.id.info_order_layout)).setVisibility(View.GONE);
                        ((ConstraintLayout)client_layout.findViewById(R.id.newcl_layout)).setVisibility(View.VISIBLE);

                    }
                });
                ((Button)client_layout.findViewById(R.id.save_client)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("companyprofile", companyprofile);
                        params.put("activity", Dashboard.this);
                        params.put("profile", profile);
                        params.put("mainview", ((LinearLayout)findViewById(R.id.delivery_layout)));
                        params.put("shoppingcart_selected",shoppingCart_selected);
                        params.put("shoppingcart",shoppingCart);
                        params.put("no_create_bill", false);
                        params.put("cl_name", ((EditText)client_layout.findViewById(R.id.client_name)).getText().toString() );
                        String ncf_name=((Spinner)client_layout.findViewById(R.id.client_type)).getSelectedItem().toString();
                        String ncf_codetp=ncf_type.get(ncf_name);
                        params.put("client_type",ncf_codetp);
                        params.put("rnc",((EditText)client_layout.findViewById(R.id.RNC)).getText().toString() );
                        params.put("telephone", ((EditText)client_layout.findViewById(R.id.telephone)).getText().toString() );
                        params.put("birthdate", ((EditText)client_layout.findViewById(R.id.birthdate)).getText().toString() );
                        params.put("reference", ((EditText)client_layout.findViewById(R.id.paid_amount)).getText().toString() );
                        params.put("_address", ((EditText)client_layout.findViewById(R.id.client_address)).getText().toString() );
                        new newClient(params).execute();
                    }
                });
            }
        });

        ((RelativeLayout)findViewById(R.id.table_area)).setVisibility(View.GONE);
        ((RelativeLayout)findViewById(R.id.product_area)).setVisibility(View.GONE);
        ((ConstraintLayout)findViewById(R.id.payment_conf)).setVisibility(View.VISIBLE);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<paytype_adp> paytype_adps = new ArrayList<>();
        for(int cont=0; cont<companyprofile.getPaytypes().size(); cont++){
            int code=((int)((HashMap)companyprofile.getPaytypes().get(cont)).get("code"));
            String name=((HashMap)companyprofile.getPaytypes().get(cont)).get("tpname").toString();
            paytype_adps.add(new paytype_adp(code,name));
        }
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, paytype_adps);
        // Specify the layout to use when the list of choices appears
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerArrayAdapter);

    }
    public void total_payment(View view){
        for(int cont=0; cont<companyprofile.getPaytypes().size(); cont++){
            int code=((int)((HashMap)companyprofile.getPaytypes().get(cont)).get("code"));
            String name=((HashMap)companyprofile.getPaytypes().get(cont)).get("tpname").toString();
            if (name.equals(( (Spinner) findViewById(R.id.spinner)).getSelectedItem().toString())) {
                try {
                    paytypes_send.put(code+"",Double.parseDouble(((TextView)findViewById(R.id.final_total)).getText().toString()));
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        paytypes.put(( (Spinner) findViewById(R.id.spinner)).getSelectedItem().toString(),
                Double.parseDouble(((TextView)findViewById(R.id.final_total)).getText().toString()));
        GridView denominations_grd = (GridView) findViewById(R.id.denominations_lst);
        denominations_grd.setAdapter(new denominations(Dashboard.this, profile, companyprofile, paytypes, billDetails));
        calculateLeft();
    }
    public void partial_payment(View view){
        android.app.AlertDialog.Builder dialog_builder =new android.app.AlertDialog.Builder(Dashboard.this);
        final View denominations_builder =activity.getLayoutInflater().inflate(R.layout.paytypelst, null);
        dialog_builder.setTitle(activity.getResources().getString(R.string.denomination_ttl)+" "+( (Spinner) findViewById(R.id.spinner)).getSelectedItem().toString());
        dialog_builder.setView(denominations_builder);

        profile.setDialog(dialog_builder.create());
        profile.getDialog().show();
        ((Button) denominations_builder.findViewById(R.id.applyPayment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double paid_amount = Double.parseDouble(((EditText) denominations_builder.findViewById(R.id.paid_amount)).getText().toString());
                paytypes.put(( (Spinner) findViewById(R.id.spinner)).getSelectedItem().toString(),
                        paid_amount);
                for(int cont=0; cont<companyprofile.getPaytypes().size(); cont++){
                    int code=((int)((HashMap)companyprofile.getPaytypes().get(cont)).get("code"));
                    String name=((HashMap)companyprofile.getPaytypes().get(cont)).get("tpname").toString();
                    if (name.equals(( (Spinner) findViewById(R.id.spinner)).getSelectedItem().toString())) {
                        try {
                            paytypes_send.put(code+"",paid_amount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                GridView denominations_grd = (GridView) findViewById(R.id.denominations_lst);
                denominations_grd.setAdapter(new denominations(Dashboard.this, profile, companyprofile, paytypes, billDetails));
                calculateLeft();
                profile.getDialog().dismiss();
                profile.setDialog(null);
            }
        });
    }
    public void calculateLeft(){
        Double paid_total=0.00;
        String[] mKeys=paytypes.keySet().toArray(new String[paytypes.size()]);
        for (int cont=0; cont<paytypes.entrySet().size(); cont++) {
            String mkey=mKeys[cont];
            paid_total+=paytypes.get(mkey);
        }
        ((TextView)findViewById(R.id.total_paid)).setText(paid_total.toString());
        Double final_total=Double.parseDouble(((TextView)findViewById(R.id.final_total)).getText().toString());
        Double devolution=paid_total-final_total;
        ((TextView)findViewById(R.id.devolution_final)).setText(devolution.toString());
    }
    public void confirm_payment(View view){
        ((Button)view).setVisibility(View.GONE);
        ((LinearLayout)findViewById(R.id.wait_lay)).setVisibility(View.VISIBLE);
        Double total_paid=Double.parseDouble(((TextView)findViewById(R.id.total_paid)).getText().toString());
        Double final_total=Double.parseDouble(((TextView)findViewById(R.id.final_total)).getText().toString());
        if (total_paid<final_total){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.war_totalpaid), Toast.LENGTH_LONG).show();

            ((LinearLayout)findViewById(R.id.wait_lay)).setVisibility(View.GONE);
            ((Button)view).setVisibility(View.VISIBLE);
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("preorder", profile.getBill());
        params.put("client",profile.getClient().getCode());
        String subtotal = ((TextView)activity.findViewById(R.id.subtotal_final)).getText().toString();
        Double tax =Double.parseDouble(((TextView)activity.findViewById(R.id.tax_final)).getText().toString());
        Double extra =Double.parseDouble(((TextView)activity.findViewById(R.id.percent_final)).getText().toString());
        String total =((TextView)activity.findViewById(R.id.final_total)).getText().toString();
        params.put("subtotal", Double.parseDouble(subtotal));
        params.put("tax", tax);
        params.put("total", Double.parseDouble(total));
        if (shoppingCart_selected.size()>0) {
            params.put("products", shoppingCart_selected);
        }else {
            params.put("products", shoppingCart);

        }
        params.put("client_name_pre", profile.getClient().getName());
        params.put("rnc", profile.getClient().getRNC());

        params.put("discount", Double.parseDouble(((TextView)activity.findViewById(R.id.final_discount)).getText().toString()));
        params.put("billtp_extra", extra);
        params.put("activity", Dashboard.this);
        params.put("paytypelst",paytypes_send); //By default is in cash
        params.put("paytypelstprint",paytypes); //By default is in cash
        params.put("profile", profile);
        params.put("btn_confirmation", view);
        params.put("wait_layout", ((LinearLayout)findViewById(R.id.wait_lay)));
        if (profile.getCompany()!=null) {
            params.put("company", profile.getCompany());
        }
        params.put("companyprofile", companyprofile);
        new closeAccount(params).execute();




    }

    public void confirm_cxc(View view) {
        ((Button) view).setEnabled(false);
        //Validations
        if (profile.getClient().getCode() == null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.client_not_created_credit), Toast.LENGTH_LONG).show();
            ((Button) view).setEnabled(true);
            return;
        }

        if (profile.getClient().getCode()==1){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.client_not_created_credit), Toast.LENGTH_LONG).show();
            ((Button) view).setEnabled(true);
            return;
        }

        Double total_paid=Double.parseDouble(((TextView)findViewById(R.id.total_paid)).getText().toString());
        Double final_total=Double.parseDouble(((TextView)findViewById(R.id.final_total)).getText().toString());
        HashMap<String, Object> params = new HashMap<>();
        params.put("preorder", profile.getBill());
        params.put("client",profile.getClient().getCode());
        String subtotal = ((TextView)activity.findViewById(R.id.subtotal_final)).getText().toString();
        Double tax =Double.parseDouble(((TextView)activity.findViewById(R.id.tax_final)).getText().toString());
        Double extra =Double.parseDouble(((TextView)activity.findViewById(R.id.percent_final)).getText().toString());
        String total =((TextView)activity.findViewById(R.id.final_total)).getText().toString();
        params.put("subtotal", Double.parseDouble(subtotal));
        params.put("tax", tax);
        params.put("total", Double.parseDouble(total));
        if (shoppingCart_selected.size()>0) {
            params.put("products", shoppingCart_selected);
        }else {
            params.put("products", shoppingCart);
        }
        params.put("client_name_pre", profile.getClient().getName());
        params.put("rnc", profile.getClient().getRNC());

        params.put("discount", Double.parseDouble(((TextView)activity.findViewById(R.id.final_discount)).getText().toString()));
        params.put("billtp_extra", extra);
        params.put("activity", Dashboard.this);
        params.put("paytypelst",paytypes_send); //By default is in cash
        params.put("profile", profile);
        params.put("btn_confirmation", view);
        if (profile.getCompany()!=null) {
            params.put("company", profile.getCompany());
        }
        params.put("companyprofile", companyprofile);
        profile.setBilltype(122);
        new closeAccount(params).execute();
    }
    public void refresh_menu(View v){
        ((TextView)findViewById(R.id.progress_lbl)).
                setText(getResources().getString(R.string.downloading_prods));
        HashMap<String, Object> params=new HashMap<>();
        params.put("activity", Dashboard.this);
        params.put("catalog", catalog);
        params.put("shopping_cart", shoppingCart);
        params.put("cmprofile", companyprofile);
        params.put("profile",profile);
        new downloadCat().execute(params);
    }


    public void goback(View v){

        ((RelativeLayout) findViewById(R.id.recharge_area)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.recharge_area)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.main_win)).setVisibility(View.VISIBLE);
    }
    public void saleCard(View v){
        ((RelativeLayout) findViewById(R.id.main_win)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.recharge_area)).setVisibility(View.VISIBLE);

    }


    public void sacn_qr(View v){
        ((RelativeLayout) findViewById(R.id.main_win)).setVisibility(View.GONE);
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            System.out.println(anfe);
        }
        ((RelativeLayout) findViewById(R.id.recharge_area)).setVisibility(View.VISIBLE);

    }



    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    System.out.println(anfe.getMessage());
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Nothing this do right now.
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void sendPaymentLoan(){

    }


    public void sendMessage(View view) {
//        EditText editText = (EditText)findViewById(R.id.message);
//        mWebSocketClient.send(editText.getText().toString());
//        editText.setText("");
    }


    public class CloseCashBoxTask extends AsyncTask<Void, Void, JSONObject> {

        private final AppCompatActivity activity;
        private HashMap<String, Object> arguments;
        private UserProfile userProfile;
        private JSONArray printer_data=new JSONArray();
        private Dialog dialog;
        CloseCashBoxTask(HashMap<String, Object> params) {
            activity = Dashboard.this;
            //dialog = (Dialog) params.get("dialog");
            params.remove("dialog");
            arguments = params;

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

                try {
                    JSch jsch = new JSch();
                    java.util.Properties config = new java.util.Properties();
                    printer target_printer = companyprofile.find("1488");
                    JSONObject data_resp = new JSONObject(sb.toString());
                    if (data_resp.has("error"))
                        return data_resp;

                    config.put("StrictHostKeyChecking", "no");

                    Session session = jsch.getSession(target_printer.getUsername(), target_printer.getServer(), 22);
                    session.setPassword(target_printer.getPassword());
                    session.setConfig(config);
                    String printer_path=""+target_printer.getPath()+" '"+target_printer.getPrinter_name()+"' ";


                    JSONArray data = new JSONArray();

                    for (int cont=0; cont<data_resp.getJSONArray("dataprint").length(); cont++){
                        JSONObject line_ =new JSONObject();
                        if (data_resp.getJSONArray("dataprint").get(cont).toString().startsWith("-")) {
                            line_.put("type", "B").put("width", "2").put("align", "CENTER").put("height", "2");
                            line_.put("text",data_resp.getJSONArray("dataprint").get(cont).toString().replace("-", " "));
                        }else {
                            line_.put("text",data_resp.getJSONArray("dataprint").get(cont).toString());
                        }
                        data.put(line_);
                    }

                    data.put(new JSONObject().put("text"," "));
                    data.put(new JSONObject().put("text"," "));
                    data.put(new JSONObject().put("text"," "));
                    data.put(new JSONObject().put("text"," "));
                    data.put(new JSONObject().put("text"," "));

                    //data.put(new JSONObject().put("text","Inicial: "+data_resp.getString("initial")).put("type","B").put("width","2").put("height","2"));
                    data.put(new JSONObject().put("text","Sub-Total: "+data_resp.getString("sub_total")).put("type","B").put("width","2").put("height","2"));
                    if (data_resp.has("final_tax"))
                        data.put(new JSONObject().put("text","Impuesto: "+data_resp.getString("final_tax")).put("type","B").put("width","2").put("height","2"));

                    data.put(new JSONObject().put("text","Final: "+data_resp.getString("final_amount")).put("type","B").put("width","2").put("height","2"));

                    data.put(new JSONObject().put("text"," "));
                    data.put(new JSONObject().put("text"," "));

                    if (data_resp.has("status"))
                        data.put(new JSONObject().put("text",""+data_resp.getString("status")).put("type","B").put("width","2").put("height","2"));

                    //data.put(new JSONObject().put("text","Final: "+data_resp.getString("final_amount")).put("type","B").put("width","2").put("height","2"));
                    String args=printer_path+"'"+data+"' ";

                    // X Forwarding
                    // channel.setXForwarding(true);

                    session.connect();
                    Channel channel= session.openChannel("exec");
                    String cmd="echo '"+target_printer.getPassword()+"' | sudo -S python3 "+" "+args+"";
                    ((ChannelExec) channel).setCommand(cmd);

                    channel.setInputStream(null);

                    ((ChannelExec) channel).setErrStream(System.err);

                    InputStream in = channel.getInputStream();

                    channel.connect();
                    String response="";
                    byte[] tmp = new byte[1024];
                    int cont=0;
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0)
                                break;
                            response+=new String(tmp, 0, i);
                            System.out.print(new String(tmp, 0, i));
                        }
                        //channel.close();  // this closes the jsch channel
                        if (channel.isClosed() || cont>=10) {
                            if (in.available() > 0)
                                continue;
                            if (cont==10)
                                break;
                            response+="exit-status: " + channel.getExitStatus();
                            System.out.println("exit-status: " + channel.getExitStatus());
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                            cont+=1;
                        } catch (Exception ee) {
                            response+=ee.getMessage();
                        }
                    }
                    channel.disconnect();
                    session.disconnect();
                    return new JSONObject(){}.put("error","Envio exitoso");
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
            try {
                if (profile.has("error")){
                    Toast.makeText(getApplicationContext(), profile.getString("error"), Toast.LENGTH_LONG).show();
                    SQLiteDatabase db = activity.getApplicationContext()
                            .openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);
                    ContentValues passwd_values = new ContentValues();
                    passwd_values.put(Company.CompanyCat.userkey, "");
                    db.update(Company.CompanyCat.TableName, passwd_values, "", null);
                    db.close();
                }else {
                    dialog.dismiss();
                }
            }
            catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);

        }
    }
    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject(txt);
                    for (int cont=0; cont<object.names().length(); cont++){
                        String key = object.names().getString(0);
                        JSONObject bill = object.getJSONObject(key);
                        if (!profile.getSugelicomobilebills().containsKey(key)){
                            SugelicoMobileBill mobileBill=new SugelicoMobileBill();
                            profile.getSugelicomobilebills().put(Integer.parseInt(key),
                                    mobileBill);
                            mobileBill.set_address(bill.getString("_address"));
                            mobileBill.setBillID(bill.getLong("billid"));
                            mobileBill.setDelivery_fee(bill.getDouble("delivery_fee"));
                            mobileBill.setSubtotal(bill.getDouble("subtotal"));
                            mobileBill.setSugelico_fee(bill.getDouble("sugelico_fee"));
                            mobileBill.set_time(bill.getString("_time"));
                            mobileBill.setTax(bill.getDouble("tax"));
                            mobileBill.setTotal(bill.getDouble("total"));
                            mobileBill.setProducts(new ArrayList<CartProds>());

                            for (int contProd =0; contProd<bill.
                                    getJSONArray("products").length(); contProd++){
                                CartProds product = new CartProds();
                                JSONObject productJson = bill.
                                        getJSONArray("products").getJSONObject(contProd);
                                product.setProduct(productJson.getInt("consbprod_product"));
                                product.setTitle(productJson.getString("consbprod_platename"));
                                product.setAmount(productJson.getDouble("consbprod_amount"));
                                if (!productJson.isNull("consbprod_notes"))
                                    product.setNotes(productJson.getString("consbprod_notes"));
                                if (!productJson.isNull("consbprod_terms"))
                                    product.setTerms(productJson.getString("consbprod_terms"));
                                if (!productJson.isNull("consbprod_portion"))
                                    product.setPortion(productJson.getString("consbprod_portion"));

                                product.setSubtotal(productJson.getDouble("consbprod_subtotal"));
                                product.setTax(productJson.getDouble("consbprod_tax"));
                                product.setPrice(productJson.getDouble("consbprod_total"));
                                product.setClient(bill.getString("person_name"));
                                product.setDiscount(0.00);
                                product.setOrdercode(Long.parseLong("0"));
                                product.setSaveit(false);
                                mobileBill.getProducts().add(product);
                            }
                        }

                    }
                    if (object.names().length()>0){
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void start() {
        Request request = new Request.Builder().url("ws://erpta.sugelico.com:19107/sugelicomobile").build();

        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService();
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel !");
            webSocket.send("What's up ?");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
}
