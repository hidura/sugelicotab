package sugelico.postabsugelico;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;

/**
 * Created by hidura on 10/26/2016.
 */

public class cart_adapter extends RecyclerView.Adapter<cart_adapter.ViewHolder>  {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private HashMap<Integer, CartProds> shoppingCart;
    private HashMap<Integer, Products> products_lst;
    private UserProfile profile;
    private CartProds product;
    @Override
    public cart_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_adaptador_productos, parent, false);
        // set the view's size, margins, paddings and layout parameters

        cart_adapter.ViewHolder vh = new cart_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(cart_adapter.ViewHolder holder, int position) {
        product = shoppingCart.get((Integer)shoppingCart.keySet().toArray()[position]);
        for (Object code:products_lst.keySet().toArray()){
            Products prod = products_lst.get(code);

            Button cat_name= (Button) holder.view.findViewById(R.id.add_btn);
            cat_name.setText(prod.getTitle());
            cat_name.setVisibility(View.VISIBLE);
            ((ImageView)holder.view.findViewById(R.id.prod_logo)).setVisibility(View.VISIBLE);
            cat_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Add product to cart
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products_lst.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
    public cart_adapter(FragmentActivity activity, UserProfile profile,
                           HashMap<Integer, Products> products_lst,
                           HashMap<Integer, CartProds> shoppingCart) {
        this.activity = activity;
        this.shoppingCart = shoppingCart;
        this.products_lst = products_lst;
        this.profile = profile;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}
