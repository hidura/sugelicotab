package sugelico.postabsugelico;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
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

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.Area;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.TableItem;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.WS.PostConnection;
import sugelico.postabsugelico.General.WS.new_printertask;
import sugelico.postabsugelico.General.WS.reprint_order;
import sugelico.postabsugelico.General.WS.syncCart;
import sugelico.postabsugelico.General.db.Table;
import sugelico.postabsugelico.General.db.TableHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cart_prods.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cart_prods#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cart_prods extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private HashMap<Long, CartProds> shoppingCart;
    private UserProfile profile;
    private cmpDetails cmprofile;
    private TableItem tableItem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private View targ_view;
    private OnFragmentInteractionListener mListener;
    private Dialog dialog;
    public cart_prods() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cart_prods.
     */
    // TODO: Rename and change types and number of parameters
    public static cart_prods newInstance(String param1, String param2) {
        cart_prods fragment = new cart_prods();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_cart_prods, container, false);

        ((Button)view.findViewById(R.id.various_process)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final android.app.AlertDialog.Builder dialog_builder = new android.app.AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                        targ_view=getActivity().getLayoutInflater().inflate(R.layout.admin_opt, null);
                        ((LinearLayout)targ_view.findViewById(R.id.regular_opts)).setVisibility(View.VISIBLE);
                        ((LinearLayout)targ_view.findViewById(R.id.trans_div)).setVisibility(View.VISIBLE);
                        dialog_builder.setTitle(getResources().getString(R.string.adm_ttl));
                        dialog_builder.setView(targ_view);
                        ((View)targ_view.findViewById(R.id.admin_opts)).setVisibility(View.GONE);
                        dialog = dialog_builder.create();
                        final RecyclerView recycler = ((RecyclerView) targ_view.findViewById(R.id.plates_lst));
                        recycler.setVisibility(View.VISIBLE);
                        recycler.setAdapter(new process_prods(getActivity(), profile, shoppingCart, targ_view, false));
                        new TblLoad().execute();
                        GridLayoutManager manager = new GridLayoutManager(getContext(), 3); // MAX NUMBER OF SPACES
                        recycler.setLayoutManager(manager);
                        //((ProgressBar)getActivity().findViewById(R.id.progressBar2)).setVisibility(View.GONE);
                        recycler.setVisibility(View.VISIBLE);
                        if (profile==null){
                            dialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    getResources().getString(R.string.prof_notloading),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (profile.getUserType()==73){
                            if(!profile.getRules().findRule(14))
                                ((LinearLayout)targ_view.findViewById(R.id.del_div)).setVisibility(View.GONE);
                            if(!profile.getRules().findRule(15))
                                ((ImageButton)targ_view.findViewById(R.id.transfer)).setVisibility(View.GONE);
                            if(!profile.getRules().findRule(16))
                                ((ImageButton)targ_view.findViewById(R.id.discount)).setVisibility(View.GONE);

                            ((LinearLayout)targ_view.findViewById(R.id.bill_div)).setVisibility(View.GONE);
                        }
                        //Print Bill of the order
                        ((ImageButton)targ_view.findViewById(R.id.getBill)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((RelativeLayout)targ_view.findViewById(R.id.print_preAcc)).setVisibility(View.VISIBLE);
                                ((RelativeLayout)targ_view.findViewById(R.id.splitAcc_div)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.discform_div)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.delform_div)).setVisibility(View.GONE);
                                ((LinearLayout)targ_view.findViewById(R.id.tables_lay)).setVisibility(View.GONE);
                                recycler.setAdapter(new process_prods(getActivity(), profile, shoppingCart, targ_view, false));

                                ((LinearLayout)targ_view.findViewById(R.id.paytp_div)).setVisibility(View.VISIBLE);
                                ((GridView)targ_view.findViewById(R.id.preAcc_div)).setAdapter(new closeAcc_adapter(getActivity(), shoppingCart, profile, cmprofile, targ_view));
                            }
                        });

                        //Transfer products to table.
                        ((ImageButton)targ_view.findViewById(R.id.transfer)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((LinearLayout)targ_view.findViewById(R.id.tables_lay)).setVisibility(View.VISIBLE);
                                ((RelativeLayout)targ_view.findViewById(R.id.print_preAcc)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.splitAcc_div)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.delform_div)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.discform_div)).setVisibility(View.GONE);
                                recycler.setAdapter(new process_prods(getActivity(), profile, shoppingCart, targ_view, false));
                                ((Button)targ_view.findViewById(R.id.transfer_prods)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new TransferProdsTask().execute();
                                    }
                                });
                            }
                        });

                        //Make a discount to a product.
                        ((ImageButton)targ_view.findViewById(R.id.discount)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((RelativeLayout)targ_view.findViewById(R.id.discform_div)).setVisibility(View.VISIBLE);
                                ((Button)targ_view.findViewById(R.id.make_discount)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new DiscountProdsTask().execute();
                                    }
                                });

                                ((LinearLayout)targ_view.findViewById(R.id.tables_lay)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.delform_div)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.print_preAcc)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.splitAcc_div)).setVisibility(View.GONE);
                                recycler.setAdapter(new process_prods(getActivity(), profile, shoppingCart, targ_view, true));
                            }
                        });

                        //Split the account.
                        ((ImageButton)targ_view.findViewById(R.id.split)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((RelativeLayout)targ_view.findViewById(R.id.splitAcc_div)).setVisibility(View.VISIBLE);
                                ((RelativeLayout)targ_view.findViewById(R.id.print_preAcc)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.delform_div)).setVisibility(View.GONE);
                                ((LinearLayout)targ_view.findViewById(R.id.tables_lay)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.discform_div)).setVisibility(View.GONE);
                                recycler.setAdapter(new process_prods(getActivity(), profile, shoppingCart, targ_view, false));

                                ((Button)targ_view.findViewById(R.id.split_send)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ClientStruct nClient = new ClientStruct();
                                        nClient.setName(((EditText)targ_view.findViewById(R.id.split_name)).getText().toString().toUpperCase());
                                        profile.getClients().put(nClient.getName() ,nClient);
                                        ((GridView)targ_view.findViewById(R.id.cl_lst)).setAdapter(
                                                new split_adp(getActivity(), shoppingCart, profile, cmprofile, targ_view));
                                        ((EditText)targ_view.findViewById(R.id.split_name)).setText("");
                                    }
                                });

                                ((GridView)targ_view.findViewById(R.id.cl_lst)).setAdapter(new split_adp(getActivity(), shoppingCart, profile, cmprofile, targ_view));
                            }
                        });

                        //Print the pre-account.
                        ((ImageButton)targ_view.findViewById(R.id.reprint_preorder)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap<String, Object> params = new HashMap<String, Object>();
                                params.put("profile", profile);
                                params.put("products", shoppingCart);
                                params.put("preorder", profile.getBill());
                                params.put("activity", getActivity());
                                params.put("cmprofile", cmprofile);
                                params.put("table_info", ((TextView) getActivity().findViewById(R.id.progress_lbl)).getText().toString());


                                new reprint_order().execute(params);
                            }
                        });

                        //Delete product.
                        ((ImageButton)targ_view.findViewById(R.id.delProd)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((RelativeLayout)targ_view.findViewById(R.id.delform_div)).setVisibility(View.VISIBLE);
                                ((Button)targ_view.findViewById(R.id.deleteprod)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new DelProdsTask().execute();
                                    }
                                });

                                ((LinearLayout)targ_view.findViewById(R.id.tables_lay)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.print_preAcc)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.discform_div)).setVisibility(View.GONE);
                                ((RelativeLayout)targ_view.findViewById(R.id.splitAcc_div)).setVisibility(View.GONE);
                                recycler.setAdapter(new process_prods(getActivity(), profile, shoppingCart, targ_view, true));
                            }
                        });
                        dialog.show();
                    }
                }
        );


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onTrackSelected(HashMap<Long, CartProds> shoppingCart,
                                UserProfile profile, cmpDetails cmprofile) {

        this.shoppingCart=shoppingCart;
        this.profile=profile;
        this.cmprofile=cmprofile;
        if (shoppingCart!=null) {
            RecyclerView recycler = ((RecyclerView) getActivity().findViewById(R.id.cat_prods_data));
            recycler.setAdapter(new shoppingcart_adp(getActivity(), shoppingCart, profile, cmprofile));

            GridLayoutManager manager = new GridLayoutManager(getContext(), 1); // MAX NUMBER OF SPACES
            recycler.setLayoutManager(manager);
            //((ProgressBar)getActivity().findViewById(R.id.progressBar2)).setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }

    }



    public class TblLoad extends AsyncTask<Void, Void, HashMap<Integer, TableItem>> {


        TblLoad() {

        }

        @Override
        protected HashMap<Integer, TableItem> doInBackground(Void... params) {HashMap<Integer, Area> arealst=new HashMap<>();
            HashMap<Integer, TableItem> tableslst= new HashMap<>();



            try {
                SQLiteDatabase db = getActivity().getApplicationContext()
                        .openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);

                Cursor cursor = db.query(
                        Table.TableCat.TableName,  // The table to query
                        new String[]{Table.TableCat.code,
                                Table.TableCat.name,
                                Table.TableCat.area,
                                Table.TableCat.area_name},                               // The columns to return
                        "" ,// The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        ""                                 // The sort order
                );
                cursor.moveToFirst();

                for(int cont=0; cont<cursor.getCount(); cont++){

                    TableItem tblitem=new TableItem();
                    tblitem.setCode(cursor.getInt(0));
                    tblitem.setTblname(cursor.getString(1));
                    tblitem.setArea_name(cursor.getString(3));

                    tableslst.put(tblitem.getCode(), tblitem);
                    cursor.moveToNext();
                }

                cursor.close();
                db.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                SQLiteDatabase db = getActivity().getApplicationContext().openOrCreateDatabase(TableHelper.DATABASE_NAME, 0, null);
                new TableHelper(getActivity().getApplicationContext()).onCreate(db);
                db.close();
            }
            return tableslst;
        }

        protected void onPostExecute(final HashMap<Integer, TableItem> tables) {

            GridView recycler = ((GridView) targ_view.findViewById(R.id.tables));
            ArrayList<ActiveOrders> activTables=profile.getActiveorders();
            recycler.setAdapter(new tbladapter_base(getActivity(), shoppingCart, profile, activTables, cmprofile, targ_view));
            recycler.setChoiceMode(GridView .CHOICE_MODE_SINGLE);
            recycler.setNumColumns(2);
            //((ProgressBar)getActivity().findViewById(R.id.progressBar2)).setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onCancelled() {

        }
    }

    public class DiscountProdsTask extends AsyncTask<Void, Void, String> {

        private final FragmentActivity activity;
        private HashMap<String, Object> arguments;
        private UserProfile userProfile;
        DiscountProdsTask() {
            activity = getActivity();
            arguments=new HashMap<>();
            arguments.put("classname","Bills.applyDiscount2Prod");
            arguments.put("product",((TextView)targ_view.findViewById(R.id.ordercodes)).getText().toString());
            arguments.put("discount",((EditText)targ_view.findViewById(R.id.reference)).getText().toString());
        }

        @Override
        protected String doInBackground(Void... params) {


            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String urlPath = cmprofile.getPath();
            JSONObject profile = null;
            try {
                URL url = new URL(urlPath);
                connection =new PostConnection().PostConnection(arguments, null,cmprofile.getPath());
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
                }
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                HashMap<String, Object> params = new HashMap<String, Object>();
                shoppingCart.clear();//Cleaning the shopping cart in order to be fill again.
                params.put("profile", profile);
                params.put("cmprofile",cmprofile);
                params.put("activity",activity);
                params.put("order",profile.getBill());
                params.put("shoppingcart",shoppingCart);

                new syncCart().execute(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public class DelProdsTask extends AsyncTask<Void, Void, String> {

        private final FragmentActivity activity;
        private HashMap<String, Object> arguments;
        private UserProfile userProfile;
        private JSONArray printer_data;
        DelProdsTask() {
            activity = getActivity();
            arguments=new HashMap<>();
            arguments.put("classname","Bills.delProductPreorder");
            arguments.put("key", profile.getSession());
            arguments.put("preorder", profile.getBill());
            arguments.put("amount", 1);//By default i dont know why ia asking for that.
            arguments.put("preorder_product", ((TextView)targ_view.findViewById(R.id.ordercodes)).getText().toString());
            arguments.put("products",((TextView)targ_view.findViewById(R.id.ordercodes)).getText().toString());
            arguments.put("reason",((TextView)targ_view.findViewById(R.id.delreason)).getText().toString());
            printer_data=new JSONArray();
        }

        @Override
        protected String doInBackground(Void... params) {


            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String urlPath = cmprofile.getPath();
            JSONObject profile = null;
            try {
                URL url = new URL(urlPath);
                connection =new PostConnection().PostConnection(arguments, null,cmprofile.getPath());
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
                CartProds product = shoppingCart.get(Long.parseLong(((TextView)targ_view.findViewById(R.id.ordercodes)).getText().toString()));
                printer_data.put(new JSONObject().put("text",cmprofile.getName()).put("type","B").
                        put("width","2").put("align","CENTER").put("height","2"));
                printer_data.put(new JSONObject().put("text",cmprofile.getRNC()).put("align","CENTER"));
                printer_data.put(new JSONObject().put("text",cmprofile.getTelephone()).put("align","CENTER"));
                printer_data.put(new JSONObject().put("text",cmprofile.getAddress()).put("align","CENTER"));
                printer_data.put(new JSONObject().
                        put("text","Producto BORRADO: "+product.getTitle()).
                        put("align", "LEFT").
                        put("width", "1").
                        put("height", "1"));
                printer_data.put(new JSONObject().
                        put("text","CANTIDAD: "+product.getAmount()).
                        put("align", "LEFT").
                        put("width", "1").
                        put("height", "1"));
                printer_data.put(new JSONObject().
                        put("text","Razon: "+
                                ((TextView)targ_view.findViewById(R.id.delreason)).getText()).
                        put("align", "LEFT").
                        put("width", "1").
                        put("height", "1"));
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
                }

                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text","____________________________________________"));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text","COLOCADO POR: "+msg_response.getString("user_added")));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text","____________________________________________"));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text","BORRADO POR: "+profile.getName()));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text"," "));

                HashMap<String, Object> print_params=new HashMap<>();
                print_params.put("activity", activity);
                print_params.put("lines", printer_data);
                print_params.put("cmProfile", cmprofile);


                new printTicket(print_params).execute();
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                ((TextView)targ_view.findViewById(R.id.ordercodes)).setText("");
                dialog.dismiss();
                HashMap<String, Object> params = new HashMap<String, Object>();
                shoppingCart.clear();//Cleaning the shopping cart in order to be fill again.
                params.put("profile", profile);
                params.put("cmprofile",cmprofile);
                params.put("activity",activity);
                params.put("order",profile.getBill());
                params.put("shoppingcart",shoppingCart);

                new syncCart().execute(params);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), activity.getResources().getString(R.string.del_problem), Toast.LENGTH_LONG).show();
            }

        }


    }


    public class TransferProdsTask extends AsyncTask<Void, Void, String> {

        private final FragmentActivity activity;
        private HashMap<String, Object> arguments;
        private UserProfile userProfile;
        TransferProdsTask() {
            activity = getActivity();
            arguments=new HashMap<>();
            arguments.put("classname","Bills.trans_product");
            arguments.put("product",((TextView)targ_view.findViewById(R.id.ordercodes)).getText().toString());
            arguments.put("preorder",((TextView)targ_view.findViewById(R.id.tbl_trans)).getText().toString());
        }

        @Override
        protected String doInBackground(Void... params) {


            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String urlPath = cmprofile.getPath();
            JSONObject profile = null;
            try {
                URL url = new URL(urlPath);
                connection =new PostConnection().PostConnection(arguments, null,cmprofile.getPath());
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
                }
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                HashMap<String, Object> params = new HashMap<String, Object>();
                shoppingCart.clear();//Cleaning the shopping cart in order to be fill again.
                params.put("profile", profile);
                params.put("cmprofile",cmprofile);
                params.put("activity",activity);
                params.put("order",profile.getBill());
                params.put("shoppingcart",shoppingCart);

                new syncCart().execute(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * Created by diegohidalgo on 3/21/17.
     */

    public class printTicket extends AsyncTask<Void, HashMap<String, Object>, String> {
        HashMap<String, Object> args=new HashMap<>();
        private AppCompatActivity activity;
        private cmpDetails cmprofile;
        private JSONArray printerStr;
        private printer target_printer;
        private Integer preorder;
        private Integer status;
        public printTicket(HashMap<String, Object> param) {
            args=param;
            activity=(AppCompatActivity)param.get("activity");
            printerStr=(JSONArray)param.get("lines");


            cmprofile=(cmpDetails)param.get("cmProfile");
            target_printer= cmprofile.find("1488");

            preorder=(Integer)param.get("preorder");
            status=0;

        }

        @Override
        protected String doInBackground(Void... params) {


            JSch jsch = new JSch();
            try {
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");

                Session session = jsch.getSession(target_printer.getUsername(), target_printer.getServer(), 22);
                session.setPassword(target_printer.getPassword());
                session.setConfig(config);
                String printer_path=""+target_printer.getPath()+" '"+target_printer.getPrinter_name()+"' '0'  ";



                String args=printer_path+"'"+printerStr.toString()+"'";
                System.out.println(args);
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
                        status=channel.getExitStatus();
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        cont+=1;
                    } catch (Exception ee) {
                        response+=ee.getMessage();
                        status=1;
                    }
                }
                channel.disconnect();
                session.disconnect();
                return response;

            } catch (JSchException e) {
                e.printStackTrace();
                status=1;
                System.out.println(e.getMessage());
                return e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                status=1;
                System.out.println(e.getMessage());
                return e.getMessage();
            }


        }

        protected void onPostExecute(String response) {

            if (status==0) {
                Toast.makeText(activity.getApplicationContext(),
                        activity.getResources().getString(R.string.send_success),
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(activity.getApplicationContext(),
                        activity.getResources().getString(R.string.problem_with_comm),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
