package sugelico.postabsugelico.General.WS;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.R;

public class reprint_order extends AsyncTask<HashMap<String, Object>, String, String> {
    private Context context;
    private AppCompatActivity activity;
    private HashMap connProps;
    private UserProfile profile;
    private cmpDetails companyprofile;
    private ProgressDialog dialog;
    public HashMap<Long, CategoryType> catalog;
    private HashMap<String, JSONArray> printer_info;
    private String table_info;
    private ArrayList<Long> orderprints;
    private HashMap<String, Object> args;
    private Integer preorder;
    private HashMap<Integer, CartProds> products_lst;
    private Double subtotal = 0.00;
    private Double tax = 0.00;
    private Double total = 0.00;
    private String client;

    @Override
    protected String doInBackground(HashMap<String, Object>... params) {

        printer_info = new HashMap<>();
        activity = (AppCompatActivity) params[0].get("activity");
        context = activity.getApplicationContext();
        companyprofile = (cmpDetails) params[0].get("cmprofile");
        dialog = (ProgressDialog) params[0].get("dialog");
        table_info = params[0].get("table_info").toString();


        if (params[0].containsKey("conparams")) {
            connProps = (HashMap) params[0].get("conparams");//The property of the connections.
        } else {
            connProps = null;
        }
        profile = (UserProfile) params[0].get("profile");
        args = new HashMap<>();//Getting the WebService parameters.
        args.put("classname", "Bills.addProd2Preorder");
        args.put("key", profile.getSession());

        JSONArray prodlst = new JSONArray();
        preorder = Integer.parseInt(params[0].get("preorder").toString());

        products_lst = ((HashMap<Integer, CartProds>) params[0].get("products"));
        Object[] iterator = products_lst.keySet().toArray();
        orderprints = new ArrayList<Long>();

        for (int cont = 0; cont < iterator.length; cont++) {
            Long position = Long.parseLong(iterator[cont].toString());
            CartProds cart_prod = products_lst.get(position);
            if (cart_prod != null) {
                try {
                    JSONArray printer_data = new JSONArray();
                    orderprints.add(cart_prod.getOrderCode());
                    if (!cart_prod.getCompaniontp()) {

                        String printer = companyprofile.getCategories().get(companyprofile.getProducts().
                                get(cart_prod.getProduct()).getCategory()).getPrinter();

                        if (printer_info.containsKey(printer)) {
                            printer_data = (JSONArray) printer_info.get(printer);

                            printer_info.remove(printer);
                        } else {
                            printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                            printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                            printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                            printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                            printer_data.put(new JSONObject().put("text", " COPIA ORDEN YA ENVIADA").put("time", 0));
                            printer_data.put(new JSONObject().put("text", "ORDERN DE COCINA").put("type", "B").
                                    put("align", "CENTER").put("width", "1").put("height", "1").put("time", 0));
                            printer_data.put(new JSONObject().put("text", "MESERO: " + profile.getName()).put("time", 0));
                            printer_data.put(new JSONObject().put("text", table_info).put("type", "B").
                                    put("width", "1").put("height", "1").put("time", 0));
                        }
                        String plate_info = cart_prod.getAmount() + "   " + cart_prod.getTitle() + "";

                        if (cart_prod.getTerms() != null) {
                            plate_info += "\n" + cart_prod.getTerms() + "";
                        }
                        if (cart_prod.getNotes() != null && cart_prod.getNotes().length() >= 2) {
                            plate_info += "\nNotas: " + cart_prod.getNotes() + "";
                        }
                        if (cart_prod.getCompanion() != null) {
                            for (int comp_cont = 0; comp_cont < cart_prod.getCompanion().split("\\|").length; comp_cont++) {
                                orderprints.add(Long.parseLong(cart_prod.getCompanion().split("\\|")[comp_cont]));
                                if (comp_cont == 0) {
                                    if (products_lst.get(Long.parseLong(cart_prod.getCompanion().split("\\|")[comp_cont])) != null) {
                                        plate_info += "\nCon: " + products_lst.get(Long.parseLong(cart_prod.getCompanion().split("\\|")[comp_cont])).getTitle() + "";
                                    }
                                } else {
                                    if (products_lst.get(Long.parseLong(cart_prod.getCompanion().split("\\|")[comp_cont])) != null) {
                                        plate_info += " y " + products_lst.get(Long.parseLong(cart_prod.getCompanion().split("\\|")[comp_cont])).getTitle();
                                    }
                                }
                            }
                        }
                        plate_info += "\n";
                        if (preorder == null)
                            preorder = cart_prod.getOrder();
                        int plate_time = 0;
                        if (activity.getResources().getString(R.string.first_time).equals(cart_prod.getTime_pos())) {
                            plate_time = 1;
                        } else if (activity.getResources().getString(R.string.second_time).equals(cart_prod.getTime_pos())) {
                            plate_time = 2;
                        } else if (activity.getResources().getString(R.string.third_time).equals(cart_prod.getTime_pos())) {
                            plate_time = 3;
                        }
                        client = cart_prod.getClient();
                        printer_data.put(new JSONObject().put("text", plate_info).
                                put("type", "B").put("width", "1").
                                put("height", "1").
                                put("time", plate_time).put("codeorder", cart_prod.getOrderCode()));
                        JSONObject prod_send = new JSONObject().put("preorder", cart_prod.getOrder()).
                                put("ordercode", cart_prod.getOrderCode()).
                                put("product", cart_prod.getProduct()).
                                put("amount", cart_prod.getAmount()).
                                put("terms", cart_prod.getTerms()).
                                put("plate_name", cart_prod.getTitle()).
                                put("usercode", profile.getCode()).
                                put("subtotal", cart_prod.getSubtotal()).
                                put("tax", cart_prod.getTax()).
                                put("total", cart_prod.getPrice()).
                                put("client", cart_prod.getClient()).
                                put("companion", cart_prod.getCompanion()).
                                put("portion", cart_prod.getPortion()).
                                put("notes", cart_prod.getNotes());
                        printer_data.put(prod_send);
                        subtotal += cart_prod.getSubtotal();
                        tax += cart_prod.getTax();
                        total += (cart_prod.getTax() + cart_prod.getSubtotal());
                        prodlst.put(prod_send);
                        printer_data.put(new JSONObject().put("text", " ").put("width", "2").put("height", "1").put("time", 0));
                        printer_info.put(printer, printer_data);

                    } else {
                        if (companyprofile.getProducts().
                                get(cart_prod.getProduct()) != null) {
                            String printer = companyprofile.getCategories().get(companyprofile.getProducts().
                                    get(cart_prod.getProduct()).getCategory()).getPrinter();
                            if (printer_info.containsKey(printer)) {
                                printer_data = (JSONArray) printer_info.get(printer);

                                printer_info.remove(printer);
                            } else {
                                printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                                printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                                printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                                printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                                printer_data.put(new JSONObject().put("text", " ").put("time", 0));
                                printer_data.put(new JSONObject().put("text", "ORDERN DE COCINA").put("type", "B").
                                        put("align", "CENTER").put("width", "1").put("height", "1").put("time", 0));
                                printer_data.put(new JSONObject().put("text", "MESERO: " + profile.getName()).put("time", 0));
                                printer_data.put(new JSONObject().put("text", table_info).put("type", "B").put("time", 0));
                            }
                            JSONObject prod_send = new JSONObject().put("preorder", cart_prod.getOrder()).
                                    put("ordercode", cart_prod.getOrderCode()).
                                    put("product", cart_prod.getProduct()).
                                    put("amount", cart_prod.getAmount()).
                                    put("terms", cart_prod.getTerms()).
                                    put("plate_name", cart_prod.getTitle()).
                                    put("usercode", profile.getCode()).
                                    put("subtotal", cart_prod.getSubtotal()).
                                    put("tax", cart_prod.getTax()).
                                    put("total", cart_prod.getPrice()).
                                    put("client", cart_prod.getClient()).
                                    put("companion", cart_prod.getCompanion()).
                                    put("portion", cart_prod.getPortion()).
                                    put("notes", cart_prod.getNotes());

                            subtotal += cart_prod.getSubtotal();
                            tax += cart_prod.getTax();
                            total += (cart_prod.getTax() + cart_prod.getSubtotal());
                            printer_data.put(prod_send);
                            prodlst.put(prod_send);
                            printer_info.put(printer, printer_data);
                        } else {
                            System.out.println("Producto errado: " + cart_prod.getTitle());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }


        args.put("products", prodlst);
        args.put("preorder", preorder);

        return "";


    }

    protected void onPostExecute(String categories) {
        ((TextView) activity.findViewById(R.id.sendwait_lbl)).setText(activity.getResources().
                getString(R.string.printing));
        for (int cont = 0; cont < printer_info.keySet().toArray().length; cont++) {

            String key = printer_info.keySet().toArray()[cont].toString();
            printer target_printer = companyprofile.find(key);
            if (target_printer != null) {
                JSONArray lines = printer_info.get(key);
                long timeInMillis = System.currentTimeMillis();
                Calendar cal1 = Calendar.getInstance();
                cal1.setTimeInMillis(timeInMillis);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy hh:mm:ss a");
                String dateforrow = dateFormat.format(cal1.getTime());
                try {
                    lines.put(new JSONObject().put("text", dateforrow).put("type", "B").put("time", 10));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                HashMap<String, Object> params_printer = new HashMap<>();
                params_printer.put("activity", activity);
                params_printer.put("lines", lines);
                params_printer.put("cmProfile", companyprofile);
                params_printer.put("userprofile", profile);
                params_printer.put("printer", target_printer);
                params_printer.put("preorder", profile.getBill());

                new new_printertask(params_printer).execute();
            } else {
                Toast.makeText(activity.getApplicationContext(),
                        activity.getResources().getText(R.string.printer_missingpt1) +
                                key +
                                activity.getResources().getText(R.string.printer_missingpt2), Toast.LENGTH_LONG).show();
            }
        }


    }


}