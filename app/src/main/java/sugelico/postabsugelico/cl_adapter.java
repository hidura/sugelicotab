package sugelico.postabsugelico;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;

/**
 * Created by diegohidalgo on 12/26/16.
 */

public class cl_adapter extends RecyclerView.Adapter<cl_adapter.ViewHolder>  {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Integer, Products> products_lst;
    private UserProfile profile;
    private CartProds product_target;
    @Override
    public cl_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        cl_adapter.ViewHolder vh = new cl_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(cl_adapter.ViewHolder holder, int position) {
        final String clName=profile.getClients().get(profile.getClients().keySet().toArray()[position]).getName();
        final ClientStruct client = profile.getClients().get(profile.getClients().keySet().toArray()[position]);
        ((Button)holder.view.findViewById(R.id.cl_data)).setText(client.getName());
        ((Button)holder.view.findViewById(R.id.cl_data)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product_target.setClient(clName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profile.getClients().size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
    public cl_adapter(FragmentActivity activity, UserProfile profile,
                      HashMap<Integer, Products> products_lst,
                      HashMap<Long, CartProds> shoppingCart, CartProds product_target) {
        this.activity = activity;
        this.shoppingCart = shoppingCart;
        this.products_lst = products_lst;
        this.profile = profile;
        this.product_target=product_target;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}