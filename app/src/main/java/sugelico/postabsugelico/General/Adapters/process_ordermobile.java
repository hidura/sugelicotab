package sugelico.postabsugelico.General.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.R;

public class process_ordermobile extends RecyclerView.Adapter<pending_ordermobile.ViewHolder>  {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<Object> clients;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Long, CartProds> shoppingCart_selected;

    @Override
    public pending_ordermobile.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mobiletables_adp, parent, false);
        // set the view's size, margins, paddings and layout parameters

        pending_ordermobile.ViewHolder vh = new pending_ordermobile.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(pending_ordermobile.ViewHolder holder, int position) {
        String client_info="";

        holder.setIsRecyclable(false);
        final HashMap<String, Object> client_data=(HashMap<String, Object>)clients.get(position);

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        private TextView order;
        private TextView order_type;
        private TextView client_name;
        private TextView place_hour;
        private Button action_order;
        private LinearLayout main_view;

        public ViewHolder(View v) {
            super(v);

            view = v;
            order=((TextView)view.findViewById(R.id.order_code));
            order_type=((TextView)view.findViewById(R.id.order_type));
            client_name=((TextView)view.findViewById(R.id.client_info));
            place_hour=((TextView)view.findViewById(R.id.place_hour));
            action_order=((Button)view.findViewById(R.id.openOrder));
            main_view=((LinearLayout)view.findViewById(R.id.order_proc));
        }
    }
    public process_ordermobile(AppCompatActivity activity,
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