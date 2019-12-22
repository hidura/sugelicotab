package sugelico.postabsugelico;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.TableItem;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.WS.openBill;
import sugelico.postabsugelico.General.WS.syncCart;

/**
 * Created by hidura on 10/26/2016.
 */

public class Table_adapter extends RecyclerView.Adapter<Table_adapter.ViewHolder>{
    private FragmentActivity activity;
    private LayoutInflater inflater;

    private HashMap<Integer, TableItem> tables;
    private UserProfile profile;
    private HashMap<Long, CartProds> shoppingcart;
    private cmpDetails companyProfile;
    private View numb_view;
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
        Object[] ArrayKeys=tables.keySet().toArray();
        Arrays.sort(ArrayKeys);
        Integer tblid= (Integer) ArrayKeys[position];
        holder.setIsRecyclable(false);
        final TableItem target_table = tables.get(tblid);

        Button table_name;
        if (target_table.getOrder()==0) {
            table_name=(Button) holder.view.findViewById(R.id.tbl_unoccupied);
        }else {
            table_name=(Button) holder.view.findViewById(R.id.tbl_occupied);
        }
        table_name.setText(target_table.getTblname());
        table_name.setVisibility(View.VISIBLE);
        //((ImageView)holder.view.findViewById(R.id.type_prod_avatar)).setVisibility(View.VISIBLE);
//        if (catalogCat.get)

        table_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Button target=(Button)v;
                HashMap<Integer, Integer> tbl_info=new HashMap<Integer, Integer>();
                tbl_info.put(target_table.getCode(), target_table.getOrder());
                if (target_table.getOrder()>0) {
                    profile.setTablebill(tbl_info);
                    ((TextView) activity.findViewById(R.id.progress_lbl)).
                            setText(activity.getResources().getString(R.string.area_lbl) + target_table.getArea_name() +
                                    " " + activity.getResources().getString(R.string.line_sep) + " " +
                                    activity.getResources().getString(R.string.table_lbl) + target_table.getTblname() +
                                    " " + activity.getResources().getString(R.string.line_sep) + " " +
                                    activity.getResources().getString(R.string.order_no_lbl) + target_table.getOrder());
                    ((ProgressBar) activity.findViewById(R.id.progressbar_main)).setVisibility(View.GONE);
                    ((RelativeLayout) activity.findViewById(R.id.table_area)).setVisibility(View.GONE);
                    ((TextView) activity.findViewById(R.id.progress_lbl)).setVisibility(View.VISIBLE);
                    ((RelativeLayout) activity.findViewById(R.id.product_area)).setVisibility(View.VISIBLE);
                    profile.setBill(target_table.getOrder());
                    profile.setTable_sel(target_table.getCode());
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("profile", profile);
                    params.put("cmprofile",companyProfile);
                    params.put("activity",activity);
                    params.put("order",target_table.getOrder());

                    params.put("shoppingcart",shoppingcart);
                    profile.setBilltype(101);
                    profile.setOrdertype(101);
                    new syncCart().execute(params);
                }else {
                    //Open new order.
                    AlertDialog.Builder dialog_builder = new AlertDialog.Builder(activity);

                    numb_view =activity.getLayoutInflater().inflate(R.layout.number_keyboard, null);
                    ((EditText)numb_view.findViewById(R.id.name_tbl)).setVisibility(View.VISIBLE);
                    //((CheckBox)numb_view.findViewById(R.id.fastfood)).setVisibility(View.VISIBLE);
                    dialog_builder.setTitle(activity.getResources().getString(R.string.people_on_lbl));
                    dialog_builder.setView(numb_view);
                    final AlertDialog dialog = dialog_builder.create();

                    dialog.show();
                    ((ImageButton)numb_view.findViewById(R.id.check)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((View)numb_view.findViewById(R.id.order_det_open)).setVisibility(View.GONE);

                            ((View)numb_view.findViewById(R.id.wait_order)).setVisibility(View.VISIBLE);
                            target.setBackgroundColor(activity.getResources().getColor(R.color.occupied_table));
                            target.setTextColor(activity.getResources().getColor(R.color.white_bk));
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("people_on", ((TextView)numb_view.findViewById(R.id.people_table)).getText());
                            params.put("tbl_name", ((TextView)numb_view.findViewById(R.id.name_tbl)).getText());
                            params.put("profile", profile);

                            params.put("cmprofile", companyProfile);
                            params.put("activity", activity);
                            params.put("main_view", numb_view);
                            params.put("table", target_table);
                            params.put("button", target);
                            params.put("shoppingcart",shoppingcart);
                            profile.setBilltype(101);
                            profile.setOrdertype(101);
                            new openBill().execute(params);
                            dialog.dismiss();
                        }
                    });

                }
            }
        });


    }
    public boolean[] checkedHolder;

    private void createCheckedHolder() {
        checkedHolder = new boolean[getItemCount()];
    }


    @Override
    public int getItemCount() {
        if (tables!=null) {
            return tables.keySet().size();
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

    public Table_adapter(FragmentActivity activity,
                         UserProfile profile,
                         HashMap<Integer, TableItem> tables,
                         cmpDetails companyProfile, HashMap<Long, CartProds> shoppingCart) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.profile = profile;
        this.shoppingcart=shoppingCart;
        this.tables=tables;
        inflater = LayoutInflater.from(activity.getApplicationContext());
        createCheckedHolder();
    }
}
