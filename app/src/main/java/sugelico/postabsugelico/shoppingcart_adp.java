package sugelico.postabsugelico;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

/**
 * Created by hidura on 11/8/2016.
 */

public class shoppingcart_adp extends RecyclerView.Adapter<shoppingcart_adp.ViewHolder>  {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private HashMap<Long, CartProds> shoppingCart;
    private cmpDetails cmprofile;
    private UserProfile profile;
    @Override
    public shoppingcart_adp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_prods, parent, false);
        // set the view's size, margins, paddings and layout parameters

        shoppingcart_adp.ViewHolder vh = new shoppingcart_adp.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(shoppingcart_adp.ViewHolder holder, int position) {
        final CartProds product = shoppingCart.get(shoppingCart.keySet().toArray()[position]);
        ((TextView)holder.view.findViewById(R.id.prod_name)).setText(product.getTitle());
        if (product!=null) {
            if (!product.getSaveit()) {
                ((FloatingActionButton) holder.view.findViewById(R.id.delbtn)).setVisibility(View.VISIBLE);
                ((FloatingActionButton) holder.view.findViewById(R.id.delbtn)).setContentDescription((product.getOrderCode().toString()));
                ((FloatingActionButton) holder.view.findViewById(R.id.delbtn)).
                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//Delete of the product
                                Long prodID = Long.parseLong(v.getContentDescription().toString());
                                CartProds prod_del = shoppingCart.get(prodID);
                                Double cur_total = Double.parseDouble(((TextView) activity.findViewById(R.id.total)).getText().toString()) - prod_del.getPrice();

                                ((TextView) activity.findViewById(R.id.total)).setText(cur_total.toString());


                                shoppingCart.remove(prod_del.getOrderCode());

                                cart_prods frg = (cart_prods) activity.getSupportFragmentManager().
                                        findFragmentById(R.id.fragment_cart);
                                frg.onTrackSelected(shoppingCart, profile, cmprofile);

                                final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.commit();

                            }
                        });
            } else {
                ((FloatingActionButton) holder.view.findViewById(R.id.delbtn)).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return shoppingCart.keySet().size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
    public shoppingcart_adp(FragmentActivity activity,
                           HashMap<Long, CartProds> shoppingCart,
                            UserProfile profile, cmpDetails cmprofile) {
        this.activity = activity;
        this.shoppingCart = shoppingCart;
        this.cmprofile=cmprofile;
        this.profile=profile;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}
