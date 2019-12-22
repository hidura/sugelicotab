package sugelico.postabsugelico.General.Adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.cart_adapter;

/**
 * Created by hidura on 2/11/2018.
 */

public class shoppingcart_payment extends RecyclerView.Adapter<cart_adapter.ViewHolder>  {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Long, CartProds> shoppingCart_selected;
    private UserProfile profile;
    private CartProds product;
    private int selectedPosition=-1;
    private View dialog;
    private boolean single_choise;
    private Double percent;
    private String client_name;
    @Override
    public cart_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shoppingcartprods, parent, false);
        // set the view's size, margins, paddings and layout parameters

        cart_adapter.ViewHolder vh = new cart_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(cart_adapter.ViewHolder holder, final int position) {
        product = shoppingCart_selected.get((Long) shoppingCart_selected.keySet().toArray()[position]);
        holder.setIsRecyclable(false);



        if (product.getClient().toUpperCase().equals(profile.getClient().getName().toUpperCase())){
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setTag(position);
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setContentDescription(product.getOrderCode().toString());
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setChecked(checkedHolder[position]);
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    checkedHolder[position] = isChecked;
                    CartProds product = shoppingCart.get((Long) shoppingCart.keySet().toArray()[position]);
                    if (!isChecked) {
                        shoppingCart_selected.remove(product.getOrderCode());
                    } else {
                        shoppingCart_selected.put(product.getOrderCode(), product);
                    }
                    if (shoppingCart_selected.size() > 0) {
                        calculateItems(shoppingCart_selected);
                    } else {
                        calculateItems(shoppingCart);
                    }
                }
            });
            ((TextView) holder.view.findViewById(R.id.name)).setText(product.getTitle());
            final Long orderCode = product.getOrderCode();
//            if (product.getNotes() != null) {
//                if (product.getNotes().length() > 40) {
//                    ((TextView) holder.view.findViewById(R.id.client_name)).setText(product.getNotes().substring(0, 40));
//                } else {
//                    ((TextView) holder.view.findViewById(R.id.client_name)).setText(product.getNotes());
//                }
//            }
            ((TextView) holder.view.findViewById(R.id.client_name)).setText(product.getClient());
            ((TextView) holder.view.findViewById(R.id.amount)).setText(product.getAmount().toString());
            ((TextView) holder.view.findViewById(R.id.price)).setText(product.getPrice().toString());

            holder.view.setVisibility(View.VISIBLE);
        }else{
            holder.view.setVisibility(View.GONE);
        }

    }
    public boolean[] checkedHolder;

    private void createCheckedHolder() {
        checkedHolder = new boolean[getItemCount()];
    }

    @Override
    public int getItemCount() {
        if (shoppingCart_selected!=null) {
            return shoppingCart_selected.keySet().size();
        }else {
            return 0;
        }
    }

    private void calculateItems(HashMap<Long, CartProds> _shoppingCart_){
        Double subtotal_final=0.00;
        Double tax_final=0.00;
        Double discount_final=0.00;
        Double total_final=0.00;
        Double total_percent=0.00;
        ((TextView)activity.findViewById(R.id.subtotal_final)).setText(subtotal_final.toString());
        ((TextView)activity.findViewById(R.id.percent_final)).setText(total_percent.toString());
        ((TextView)activity.findViewById(R.id.tax_final)).setText(tax_final.toString());
        ((TextView)activity.findViewById(R.id.final_discount)).setText(discount_final.toString());
        ((TextView)activity.findViewById(R.id.final_total)).setText(total_final.toString());
        boolean prodqualify=false;
        for (int cont = 0; cont < _shoppingCart_.keySet().size(); cont++) {

            Long key = ((Long) _shoppingCart_.keySet().toArray()[cont]);
            CartProds prod=(CartProds) _shoppingCart_.get(key);
            if (client_name==null){
                prodqualify=true;
            }else{
                prodqualify=false;
                if (prod.getClient().toUpperCase().equals(client_name.toUpperCase()))
                    prodqualify=true;
            }
            if (prodqualify) {
                if (new general().checkNumber(((TextView) activity.findViewById(R.id.subtotal_final)).getText().toString())) {
                    subtotal_final = Double.parseDouble(((TextView) activity.findViewById(R.id.subtotal_final)).getText().toString()) + prod.getSubtotal();
                } else {
                    subtotal_final = prod.getSubtotal();
                }
                ((TextView) activity.findViewById(R.id.subtotal_final)).setText(new general().numberFixed(subtotal_final,2).toString());
                Double percent_prod = prod.getSubtotal() * (percent / 100);
                if (new general().checkNumber(((TextView) activity.findViewById(R.id.percent_final)).getText().toString())) {
                    total_percent = Double.parseDouble(((TextView) activity.findViewById(R.id.percent_final)).getText().toString()) + percent_prod;
                } else {
                    percent_prod = prod.getSubtotal() * (percent / 100);
                    total_percent = percent_prod;
                }
                ((TextView) activity.findViewById(R.id.percent_final)).setText(new general().numberFixed(total_percent,2).toString());

                if (new general().checkNumber(((TextView) activity.findViewById(R.id.tax_final)).getText().toString())) {
                    tax_final = Double.parseDouble(((TextView) activity.findViewById(R.id.tax_final)).getText().toString()) + prod.getTax();
                } else {
                    tax_final = prod.getTax();
                }
                ((TextView) activity.findViewById(R.id.tax_final)).setText(new general().numberFixed(tax_final,2).toString());

                if (new general().checkNumber(((TextView) activity.findViewById(R.id.final_discount)).getText().toString())) {
                    discount_final = Double.parseDouble(((TextView) activity.findViewById(R.id.final_discount)).getText().toString()) + prod.getDiscount();
                } else {
                    discount_final = prod.getTax();
                }
                ((TextView) activity.findViewById(R.id.final_discount)).setText(new general().numberFixed(discount_final,2).toString());
                if (new general().checkNumber(((TextView) activity.findViewById(R.id.final_total)).getText().toString())) {
                    total_final = Double.parseDouble(((TextView) activity.findViewById(R.id.final_total)).getText().toString()) + (prod.getPrice() + percent_prod);
                } else {
                    total_final = (prod.getPrice() + percent_prod);
                }
                ((TextView) activity.findViewById(R.id.final_total)).setText(new general().numberFixed(total_final,2).toString());
            }
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
    public shoppingcart_payment(FragmentActivity activity, UserProfile profile,
                                HashMap<Long, CartProds> shoppingCart,
                                HashMap<Long, CartProds> shoppingCart_selected,
                                View dialog_view, boolean single_choise,
                                Double percent, String client_name) {
        this.activity = activity;
        this.shoppingCart = shoppingCart;
        this.shoppingCart_selected=shoppingCart_selected;
        this.dialog=dialog_view;
        this.single_choise=single_choise;
        this.profile = profile;
        this.percent=percent;
        this.client_name=client_name;
        if (shoppingCart_selected.size()>0) {
            calculateItems(shoppingCart_selected);
        }else {
            calculateItems(shoppingCart);
            this.shoppingCart_selected=shoppingCart;
        }
        inflater = LayoutInflater.from(activity.getApplicationContext());
        createCheckedHolder();
    }
}
