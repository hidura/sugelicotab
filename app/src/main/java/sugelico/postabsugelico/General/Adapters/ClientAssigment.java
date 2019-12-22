package sugelico.postabsugelico.General.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.R;

/**
 * Created by hidura on 3/22/2018.
 */

public class ClientAssigment extends RecyclerView.Adapter<ClientAssigment.ViewHolder>  {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<Object> clients;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Long, CartProds> shoppingCart_selected;

    @Override
    public ClientAssigment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderdelivery_adp, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ClientAssigment.ViewHolder vh = new ClientAssigment.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ClientAssigment.ViewHolder holder, int position) {
        String client_info="";

        holder.setIsRecyclable(false);
        final HashMap<String, Object> client_data=(HashMap<String, Object>)clients.get(position);
        if (Integer.parseInt(client_data.get("code").toString())>1) {
            client_info = client_data.get("cl_name") + "\n" +
                    client_data.get("telephone") + "\n" +
                    client_data.get("rnc") + "\n"+
                    client_data.get("_address") + "\n";

            holder.action_order.
                    setText(activity.getResources().getString(R.string.openAccount));
            holder.main_view.setBackgroundResource(R.color.colorPrimary);
            holder.action_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    profile.setClient(new ClientStruct());
                    profile.getClient().setCode(Integer.parseInt(client_data.get("code").toString()));
                    profile.getClient().setName(client_data.get("cl_name").toString());
                    profile.getClient().setNcf_type(Integer.parseInt(client_data.get("ncf_type").toString()));
                    profile.getClient().setRnc(client_data.get("rnc").toString());
                    profile.getClient().setCredit(Boolean.parseBoolean(client_data.get("credit").toString()));
                    profile.getClient().setCurcredit(Double.parseDouble(client_data.get("code").toString()));
                    profile.getClient().setMax_credit(Double.parseDouble(client_data.get("code").toString()));

                    ((TextView)activity.findViewById(R.id.client_info)).setText(client_data.get("cl_name").toString());
                    if (shoppingCart_selected.size()>0) {
                        for (int cont = 0; cont < shoppingCart_selected.keySet().size(); cont++) {
                            Long key = ((Long) shoppingCart_selected.keySet().toArray()[cont]);
                            shoppingCart_selected.get(key).setClient(client_data.get("cl_name").toString());
                        }
                    }else {
                        for (int cont = 0; cont < shoppingCart.keySet().size(); cont++) {
                            Long key = ((Long) shoppingCart.keySet().toArray()[cont]);
                            shoppingCart.get(key).setClient(client_data.get("cl_name").toString());
                        }
                    }
                    profile.getDialog().dismiss();
                }
            });
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
    public ClientAssigment(AppCompatActivity activity,
                           UserProfile profile,
                           List<Object> clients,
                           cmpDetails companyProfile,
                           HashMap<Long, CartProds> shoppingCart,
                           HashMap<Long, CartProds> shoppingCart_selected) {
        this.activity = activity;
        this.profile = profile;
        this.companyProfile=companyProfile;
        this.clients=clients;
        this.shoppingCart=shoppingCart;
        this.shoppingCart_selected=shoppingCart_selected;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}