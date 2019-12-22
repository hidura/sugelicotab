package sugelico.postabsugelico.General.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sugelico.postabsugelico.Dashboard;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.WS.getClientWS;
import sugelico.postabsugelico.General.WS.getDebtsClient;
import sugelico.postabsugelico.R;

public class ClientDebt extends RecyclerView.Adapter<ClientDebt.ViewHolder>  {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<Object> clients;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Long, CartProds> shoppingCart_selected;

    @Override
    public ClientDebt.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderdelivery_adp, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ClientDebt.ViewHolder vh = new ClientDebt.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ClientDebt.ViewHolder holder, int position) {
        String client_info="";

        holder.setIsRecyclable(false);
        final HashMap<String, Object> client_data=(HashMap<String, Object>)clients.get(position);
        if (Integer.parseInt(client_data.get("code").toString())>1) {
            client_info = client_data.get("cl_name") + "\n" +
                    client_data.get("telephone") + "\n" +
                    client_data.get("rnc") + "\n"+
                    client_data.get("_address") + "\n";

            holder.action_order.
                    setText(activity.getResources().getString(R.string.view_debts));
            holder.main_view.setBackgroundResource(R.color.colorPrimary);
            holder.action_order.setContentDescription(client_data.get("code").toString());
            holder.action_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RecyclerView) activity.findViewById(R.id.orderslst)).setVisibility(View.GONE);
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("companyprofile", companyProfile);
                    params.put("activity", activity);
                    params.put("profile", profile);
                    params.put("mainview", ((LinearLayout)activity.findViewById(R.id.delivery_layout)));
                    params.put("code", view.getContentDescription());
                    params.put("getDebt", true);
                    new getDebtsClient(params).execute();
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
    public ClientDebt(AppCompatActivity activity,
                      UserProfile profile,
                      List<Object> clients,
                      cmpDetails companyProfile) {
        this.activity = activity;
        this.profile = profile;
        this.companyProfile=companyProfile;
        this.clients=clients;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}