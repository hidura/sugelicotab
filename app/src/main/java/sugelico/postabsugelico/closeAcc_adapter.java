package sugelico.postabsugelico;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.WS.closeAccount;

/**
 * Created by diegohidalgo on 9/9/17.
 */

public class closeAcc_adapter extends BaseAdapter {

    private FragmentActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private ArrayList<ActiveOrders> tables;
    private UserProfile profile;
    private HashMap<Long, CartProds> shoppingcart;
    private cmpDetails companyProfile;
    private View main_view;

    public closeAcc_adapter(FragmentActivity activity, HashMap<Long, CartProds> shoppingCart,
                    UserProfile profile,
                    cmpDetails companyProfile, View targ_view) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.profile = profile;
        this.shoppingcart=shoppingCart;
        this.main_view=targ_view;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return profile.getClients().size();
    }

    @Override
    public Object getItem(int i) {
        return profile.getClients().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    static class ViewHolder {
        RadioButton chkd_option;
        TextView tblname;
        TextView preorder;
        LinearLayout tblSel;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        tbladapter_base.ViewHolder holder;
        final ClientStruct cl_info = profile.getClients().get(profile.getClients().keySet().toArray()[position]);
        LayoutInflater mInflater = (LayoutInflater)
                activity.getApplicationContext().
                        getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view= mInflater.inflate(R.layout.client_view, null);
        view.findViewById(R.id.cl_name).setVisibility(View.GONE);
        final View btn_view = view;
        ((Button)view.findViewById(R.id.cl_data)).setText(cl_info.getName());
        String idCode=cl_info.getCode()+"";
        ((Button)view.findViewById(R.id.cl_data)).setContentDescription((idCode.equals(null)) ? "0" :cl_info.getCode()+"");
        ((Button)view.findViewById(R.id.cl_data)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer target_printer = companyProfile.find("1488");

                if (target_printer!=null) {
                    HashMap<String, Object> params = new HashMap<>();

                    params.put("preorder", profile.getBill());
                    params.put("client",v.getContentDescription());
                    Double total = 0.00;
                    Double subtotal=0.00;
                    Double tax =0.00;
                    Double discount =0.00;
                    for (int cont=0; cont<shoppingcart.keySet().size(); cont++){

                        Long key = ((Long) shoppingcart.keySet().toArray()[cont]);
                        if (shoppingcart.get(key).getClient().toLowerCase().equals(((Button)v).getText().toString().toLowerCase())) {
                            if (shoppingcart.get(key).getTax()>0) {
                                tax += shoppingcart.get(key).getTax();
                                total += (shoppingcart.get(key).getSubtotal()-shoppingcart.get(key).getDiscount())+(shoppingcart.get(key).getTax());
                            }
                            else {
                                tax += shoppingcart.get(key).getTax();
                                total += (shoppingcart.get(key).getSubtotal()-shoppingcart.get(key).getDiscount())+(shoppingcart.get(key).getTax());
                            }
                            subtotal += shoppingcart.get(key).getSubtotal();
                            discount += shoppingcart.get(key).getDiscount();
                        }

                    }
                    v.setVisibility(View.GONE);
                    ((LinearLayout)btn_view.findViewById(R.id.wait_clientSend)).setVisibility(View.VISIBLE);
                    params.put("subtotal", subtotal);
                    params.put("tax", tax);
                    params.put("total", total);
                    params.put("products", shoppingcart);
                    params.put("client_name_pre", ((Button)v).getText());
                    params.put("discount",discount);
                    params.put("activity", activity);
                    params.put("profile", profile);
                    params.put("companyprofile", companyProfile);
                    params.put("button", v);
                    params.put("wait_layout", ((LinearLayout)btn_view.findViewById(R.id.wait_clientSend)));

                    JSONObject paytypes_send = new JSONObject();
                    try {
                        int paytype=111;
                        int idSelected=((RadioGroup)main_view.findViewById(R.id.paytps)).getCheckedRadioButtonId();
                        if (idSelected<0){
                            Toast.makeText(activity.getApplicationContext(), "Debe elegir un tipo de pago", Toast.LENGTH_LONG).show();
                            return;

                        }else {
                            paytype=Integer.parseInt(((RadioButton)main_view.findViewById(idSelected)).getContentDescription().toString());
                        }
                        paytypes_send.put(paytype+"",total);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    params.put("paytypelst", paytypes_send);
                    new closeAccount(params).execute();
                    //dialog.dismiss();
                }else {
                    Toast.makeText(activity.getApplicationContext(),
                            activity.getResources().getText(R.string.printer_missingpt1)+
                                    " 1488 "+
                                    activity.getResources().getText(R.string.printer_missingpt2),Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }


}