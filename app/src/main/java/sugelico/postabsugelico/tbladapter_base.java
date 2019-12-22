package sugelico.postabsugelico;

import android.app.Activity;
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
 * Created by diegohidalgo on 2/14/17.
 */

public class tbladapter_base extends BaseAdapter{
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private ArrayList<ActiveOrders> tables;
    private UserProfile profile;
    private HashMap<Long, CartProds> shoppingcart;
    private cmpDetails companyProfile;
    private View main_view;

    public tbladapter_base(FragmentActivity activity, HashMap<Long, CartProds> shoppingCart,
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

        ViewHolder holder;
        final ActiveOrders tblid= (ActiveOrders) tables.get(position);
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getApplicationContext().
                            getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view= mInflater.inflate(R.layout.tables_layout, null);
            holder = new ViewHolder();
            view.setTag(holder);
            holder.tblSel = (LinearLayout) view.findViewById(R.id.tblSel);
            holder.tblSel.setVisibility(View.VISIBLE);

            holder.tblname=((TextView) view.findViewById(R.id.area_name));
            holder.preorder=((TextView) view.findViewById(R.id.preorder));
            holder.chkd_option=((RadioButton) view.findViewById(R.id.addprod2tbl));
            holder.chkd_option.setChecked(false);
            holder.tblname.setText(profile.getActiveorders().get(position).getName());
//            holder.chkd_option.setText(tables.get(position).);
            if (tables.get(position).getStatus()){
                holder.chkd_option.setChecked(true);
            }
        }else {
            holder = (ViewHolder) view.getTag();
            holder.chkd_option.setChecked(false);
            if (tables.get(position).getStatus()){
                holder.chkd_option.setChecked(true);
            }
        }
        if(profile.getActiveorders().get(position).getCode() != profile.getBill()) {

            holder.chkd_option.setTag(position);
            holder.tblSel.setVisibility(View.VISIBLE);
            //holder.tblname.setText(target_table.getTblname());
            holder.preorder.setText("Orden:" + profile.getActiveorders().get(position).getOrder());
            holder.chkd_option.setContentDescription(position+"");
            holder.chkd_option.setOnCheckedChangeListener(null);
            holder.chkd_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = (Integer)view.getTag();
                    notifyDataSetChanged();
                    ((TextView)main_view.findViewById(R.id.tbl_trans)).setText(tblid.getOrder()+"");
                }
            });
            if (selectedPosition>=0) {
                holder.chkd_option.setChecked(position == selectedPosition);
            }
            //in some cases, it will prevent unwanted situations


            return view;
        }
        return null;
    }
}
