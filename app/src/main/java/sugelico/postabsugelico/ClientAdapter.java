package sugelico.postabsugelico;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.WS.getClientWS;
import sugelico.postabsugelico.General.WS.getDebtsClient;
import sugelico.postabsugelico.General.WS.openBill;
import sugelico.postabsugelico.General.WS.syncCart;

/**
 * Created by hidura on 1/28/2018.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder>  {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<Object> clients;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private HashMap<Long, CartProds> shoppingCart;
    private boolean debt;
    @Override
    public ClientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderdelivery_adp, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ClientAdapter.ViewHolder vh = new ClientAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ClientAdapter.ViewHolder holder, int position) {
        String client_info="";

        holder.setIsRecyclable(false);
        final HashMap<String, Object> client_data=(HashMap<String, Object>)clients.get(position);
        if (Integer.parseInt(client_data.get("code").toString())>1) {
            client_info = client_data.get("cl_name") + "\n" +
                    client_data.get("telephone") + "\n" +
                    client_data.get("rnc") + "\n"+
                    client_data.get("_address") + "\n";


            holder.order.setText(activity.getResources().getString(R.string.na));
            if (debt){
                holder.action_order.
                        setText(activity.getResources().getString(R.string.openAccount));
                holder.main_view.setBackgroundResource(R.color.colorPrimary);
                holder.action_order.setContentDescription(client_data.get("code").toString());
                holder.action_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("companyprofile", companyProfile);
                        params.put("activity", activity);
                        params.put("profile", profile);
                        params.put("mainview", ((LinearLayout)activity.findViewById(R.id.delivery_layout)));
                        params.put("shoppingcart",shoppingCart);
                        params.put("code", view.getContentDescription());
                        new getDebtsClient(params).execute();
                    }
                });
            }
            if (client_data.get("order").toString().equals("null") && !debt) {
                holder.action_order.
                        setText(activity.getResources().getString(R.string.openAccount));
                holder.main_view.setBackgroundResource(R.color.colorPrimary);
                holder.action_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        profile.setClient(new ClientStruct());
                        profile.getClient().setCode(Integer.parseInt(client_data.get("client").toString()));
                        profile.getClient().setName(client_data.get("cl_name").toString());
                        profile.getClient().setRnc(client_data.get("rnc").toString());
                        profile.getClient().setExtra(client_data.get("telephone") + "\n" +
                                client_data.get("_address") + "\n");
                        profile.getClient().setCredit(Boolean.parseBoolean(client_data.get("credit").toString()));
                        profile.getClient().setCurcredit(Double.parseDouble(client_data.get("current_credit").toString()));
                        profile.getClient().setMax_credit(Double.parseDouble(client_data.get("max_credit").toString()));
                        profile.getClients().put(profile.getClient().getName(), profile.getClient());
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("profile", profile);
                        params.put("client", client_data.get("client"));
                        params.put("cmprofile", companyProfile);
                        params.put("activity", activity);
                        params.put("client_name", client_data.get("cl_name"));
                        params.put("main_view", null);//This is the number view where the client put the amount
                        // of people on the table and the amount of people and the name
                        params.put("shoppingcart", shoppingCart);
                        params.put("billtype", 102);
                        profile.setBilltype(102);
                        profile.setOrdertype(102);
                        new openBill().execute(params);
                    }
                });
            } else if(!debt){

                final String order = client_data.get("order").toString();
                holder.order.setText(order);
                holder.main_view.setBackgroundResource(R.color.colorAccent);
                holder.action_order.
                        setText(activity.getResources().getString(R.string.check_account));
                holder.action_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        profile.setClient(new ClientStruct());

                        profile.getClient().setCode(Integer.parseInt(client_data.get("client").toString()));
                        profile.getClient().setName(client_data.get("cl_name").toString());
                        profile.getClient().setRnc(client_data.get("rnc").toString());
                        profile.getClient().setCredit(Boolean.parseBoolean(client_data.get("credit").toString()));
                        profile.getClient().setCurcredit(Double.parseDouble(client_data.get("current_credit").toString()));
                        profile.getClient().setMax_credit(Double.parseDouble(client_data.get("max_credit").toString()));

                        if (client_data.get("ncf_type")==null) {
                            profile.getClient().setNcf_type(Integer.parseInt(client_data.get("ncf_type").toString()));
                        }
                        profile.getClient().setExtra(client_data.get("telephone") + "\n" +
                                client_data.get("_address") + "\n");
                        profile.getClients().put(profile.getClient().getName(), profile.getClient());
                        profile.setBilltype(102);
                        profile.setOrdertype(102);
                        params.put("profile", profile);
                        params.put("cmprofile",companyProfile);
                        params.put("activity",activity);
                        params.put("shoppingcart",shoppingCart);
                        profile.setBill(Integer.parseInt(client_data.get("order").toString()));
                        params.put("order",client_data.get("order").toString());
                        ((TextView) activity.findViewById(R.id.progress_lbl)).
                                setText(activity.getResources().getString(R.string.deliver_lbl)  +
                                        " " + activity.getResources().getString(R.string.line_sep) + " " +
                                        activity.getResources().getString(R.string.client_lbl) + ":" + client_data.get("cl_name")+
                                        " " + activity.getResources().getString(R.string.line_sep) + " " +
                                        activity.getResources().getString(R.string.order_no_lbl) + order);
                        ((ProgressBar) activity.findViewById(R.id.progressbar_main)).setVisibility(View.GONE);
                        ((RelativeLayout) activity.findViewById(R.id.table_area)).setVisibility(View.GONE);
                        ((TextView) activity.findViewById(R.id.progress_lbl)).setVisibility(View.VISIBLE);
                        ((RelativeLayout) activity.findViewById(R.id.product_area)).setVisibility(View.VISIBLE);
                        new syncCart().execute(params);
                    }
                });
            }

            holder.client_name.setText(client_info);
        }else {
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        private TextView order;
        private TextView client_name;
        private Button action_order;
        private ConstraintLayout main_view;
        public ViewHolder(View v) {
            super(v);

            view = v;
            order=((TextView)view.findViewById(R.id.rnc_info));
            client_name=((TextView)view.findViewById(R.id.client_info));
            action_order=((Button)view.findViewById(R.id.openOrder));
            main_view=((ConstraintLayout)view.findViewById(R.id.mainbk));
        }
    }
    public ClientAdapter(AppCompatActivity activity, UserProfile profile,
                         List<Object> clients,cmpDetails companyProfile,
                         HashMap<Long, CartProds> shoppingCart,boolean debt ) {
        this.activity = activity;
        this.profile = profile;
        this.companyProfile=companyProfile;
        this.clients=clients;
        this.shoppingCart=shoppingCart;
        this.debt=debt;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}
