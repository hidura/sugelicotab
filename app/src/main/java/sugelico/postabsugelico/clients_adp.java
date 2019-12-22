package sugelico.postabsugelico;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.ActiveOrders;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

/**
 * Created by diegohidalgo on 3/11/17.
 */

public class clients_adp extends BaseAdapter {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private ArrayList<ActiveOrders> tables;
    private UserProfile profile;
    private HashMap<Integer, CartProds> shoppingcart;
    private cmpDetails companyProfile;
    private View main_view;

    public clients_adp(FragmentActivity activity, HashMap<Integer, CartProds> shoppingCart,
                           UserProfile profile,
                           ArrayList<ActiveOrders> tables,
                           cmpDetails companyProfile, View targ_view) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.profile = profile;
        this.shoppingcart=shoppingCart;
        this.tables=tables;
        this.main_view=targ_view;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return tables.size();
    }

    @Override
    public Object getItem(int i) {
        return tables.get(i);
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
        final ActiveOrders tblid= (ActiveOrders) tables.get(position);



        return view;
    }
}
