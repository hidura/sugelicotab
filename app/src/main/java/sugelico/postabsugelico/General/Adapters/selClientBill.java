package sugelico.postabsugelico.General.Adapters;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.process_prods;
import sugelico.postabsugelico.tbladapter_base;

/**
 * Created by hidura on 2/11/2018.
 */

public class selClientBill extends BaseAdapter {

    private FragmentActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private ArrayList<ActiveOrders> tables;
    private UserProfile profile;
    private HashMap<Long, CartProds> shoppingcart;
    private HashMap<Long, CartProds> shoppingcart_selected;
    private cmpDetails companyProfile;
    private View main_view;
    private HashMap<String, Object> billDetails;
    public selClientBill(FragmentActivity activity, HashMap<Long, CartProds> shoppingCart,
                         HashMap<Long, CartProds> shoppingCart_selected,
                         UserProfile profile,
                         cmpDetails companyProfile, HashMap<String, Object> billDetails) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.billDetails=billDetails;
        this.profile = profile;
        this.shoppingcart=shoppingCart;
        this.shoppingcart_selected=shoppingCart_selected;
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
                ((TextView)activity.findViewById(R.id.client_info)).setText(((Button)v).getText());
                profile.setClient(cl_info);
                ((TextView)activity.findViewById(R.id.subtotal_final)).setText("0.00");
                ((TextView)activity.findViewById(R.id.percent_final)).setText("0.00");
                ((TextView)activity.findViewById(R.id.tax_final)).setText("0.00");
                ((TextView)activity.findViewById(R.id.final_total)).setText("0.00");
                Double percent=0.00;

                HashMap<String, Object> billTps=new general().getBillType(companyProfile, profile.getBillType());
                if (((HashMap<String, Object>)billTps.get("billdetails")).containsKey("percent_extra")){
                    percent=Double.parseDouble(((HashMap<String, Object>)billTps.get("billdetails")).get("percent_extra").toString());
                }
                ((ConstraintLayout)activity.findViewById(R.id.preorder)).setVisibility(View.GONE);
                ((ConstraintLayout)activity.findViewById(R.id.payment_data)).setVisibility(View.VISIBLE);
                final RecyclerView recycler = ((RecyclerView) activity.findViewById(R.id.products));
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                recycler.setLayoutManager(layoutManager);
                for (int cont=0; cont<shoppingcart.size(); cont++) {
                    CartProds product = shoppingcart.get((Long)
                            shoppingcart.keySet().toArray()[cont]);
                    if (product.getClient().toUpperCase().equals(cl_info.getName().toUpperCase()))
                        shoppingcart_selected.put(product.getOrderCode(), product);
                }
                recycler.setAdapter(new shoppingcart_payment(activity, profile, shoppingcart,
                        shoppingcart_selected, main_view, false, percent, cl_info.getName()));

            }
        });
        return view;
    }


}
