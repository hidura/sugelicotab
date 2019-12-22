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

import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.WS.printerTask;

/**
 * Created by diegohidalgo on 3/20/17.
 */

public class printAcc extends BaseAdapter {

    private FragmentActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private ArrayList<ActiveOrders> tables;
    private UserProfile profile;
    private HashMap<Long, CartProds> shoppingcart;
    private cmpDetails companyProfile;
    private View main_view;

    public printAcc(FragmentActivity activity, HashMap<Long, CartProds> shoppingCart,
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
        return profile.getClients().get(profile.getClients().keySet().toArray()[i]);
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
        ((Button)view.findViewById(R.id.cl_data)).setVisibility(View.GONE);
        ((Button)view.findViewById(R.id.cl_name_stylish)).setVisibility(View.VISIBLE);
        ((Button)view.findViewById(R.id.cl_name_stylish)).setText(cl_info.getName());
        ((Button)view.findViewById(R.id.cl_name_stylish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer target_printer = companyProfile.find("1488");

                if (target_printer!=null) {
                    int paytype=111;
                    if (main_view!=null) {
                        int idSelected = ((RadioGroup) main_view.findViewById(R.id.paytps)).getCheckedRadioButtonId();
                        if (idSelected < 0) {
                            Toast.makeText(activity.getApplicationContext(), "Debe elegir un tipo de pago", Toast.LENGTH_LONG).show();
                            return;

                        } else {
                            paytype = Integer.parseInt(((RadioButton) main_view.findViewById(idSelected)).getContentDescription().toString());
                        }
                    }
                    v.setVisibility(View.GONE);
                    ((LinearLayout)btn_view.findViewById(R.id.wait_clientSend)).setVisibility(View.VISIBLE);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("client_name", ((Button) v).getText().toString());
                    params.put("activity", activity);
                    params.put("profile", profile);
                    params.put("table_info", ((TextView) activity.findViewById(R.id.progress_lbl)).getText().toString());
                    params.put("products", shoppingcart);
                    params.put("companyprofile", companyProfile);
                    params.put("target_printer", target_printer);
                    params.put("wait_layout", ((LinearLayout)btn_view.findViewById(R.id.wait_clientSend)));
                    params.put("btn_data", v);
                    params.put("copies", 0);

                    //params.put("paytype", paytype);
                    params.put("title", "PRE-CUENTA");
                    new printerTask(params).execute();
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
