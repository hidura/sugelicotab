package sugelico.postabsugelico;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;

/**
 * Created by diegohidalgo on 2/10/17.
 */

public class process_prods  extends RecyclerView.Adapter<cart_adapter.ViewHolder>  {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private HashMap<Long, CartProds> shoppingCart;
    private UserProfile profile;
    private CartProds product;
    private int selectedPosition=-1;
    private View dialog;
    private boolean single_choise;
    @Override
    public cart_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prods_proc, parent, false);
        // set the view's size, margins, paddings and layout parameters

        cart_adapter.ViewHolder vh = new cart_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(cart_adapter.ViewHolder holder, final int position) {

        product = shoppingCart.get((Long) shoppingCart.keySet().toArray()[position]);
        ((TextView)holder.view.findViewById(R.id.name)).setText(product.getTitle());
        final Long orderCode=product.getOrderCode();
        holder.setIsRecyclable(false);
        ((TextView)holder.view.findViewById(R.id.price)).setText(product.getSubtotal().toString());
        ((TextView)holder.view.findViewById(R.id.client_name)).setText(product.getClient().toString());
        ((TextView)holder.view.findViewById(R.id.discount)).setText("Descuento: RD$0.00");
        if (product.getDiscount()>0.00){
            ((TextView)holder.view.findViewById(R.id.discount)).setText("Descuento: RD$"+product.getDiscount().toString());
        }

        if (product.getNotes()!=null){
            ((TextView)holder.view.findViewById(R.id.notes)).setText(product.getNotes());
            ((TextView)holder.view.findViewById(R.id.notes)).setVisibility(View.VISIBLE);
        }
        if (!single_choise) {
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setTag(position);
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setContentDescription(product.getOrderCode().toString());
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String orderCodes = ((TextView) dialog.findViewById(R.id.ordercodes)).getText().toString();
                    if (isChecked) {
                        if (orderCodes.length() > 0) {
                            orderCodes += "|";
                        }
                        ((TextView) dialog.findViewById(R.id.ordercodes)).setText(orderCodes + buttonView.getContentDescription().toString());
                    } else {
                        orderCodes = "";
                        String[] orders = orderCodes.split("|");
                        for (int i = 0; i < orders.length; i++) {
                            if (orders[i].equals(buttonView.getContentDescription().toString())) {
                                orders[i] = null;
                                break;
                            } else {
                                if (orderCodes.length() > 0)
                                    orderCodes += "|";
                                orderCodes += orders[i];
                            }
                        }
                        ((TextView) dialog.findViewById(R.id.ordercodes)).setText(orderCodes);

                    }
                    checkedHolder[position] = isChecked;
                }
            });
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setChecked(checkedHolder[position]);
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setVisibility(View.VISIBLE);
            ((RadioButton) holder.view.findViewById(R.id.singleProd)).setVisibility(View.GONE);
        }else {
            ((CheckBox) holder.view.findViewById(R.id.addprod2lst)).setVisibility(View.GONE);
            ((RadioButton) holder.view.findViewById(R.id.singleProd)).setVisibility(View.VISIBLE);
            ((RadioButton) holder.view.findViewById(R.id.singleProd)).setTag(position);
            ((RadioButton) holder.view.findViewById(R.id.singleProd)).setContentDescription(product.getOrderCode().toString());
            ((RadioButton) holder.view.findViewById(R.id.singleProd)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectedPosition = (Integer)view.getTag();
                    notifyDataSetChanged();

                    ((TextView) dialog.findViewById(R.id.ordercodes)).setText(view.getContentDescription().toString());
                }
            });
            if (selectedPosition>=0) {
                ((RadioButton) holder.view.findViewById(R.id.singleProd)).setChecked(position == selectedPosition);
            }
        }
    }
    public boolean[] checkedHolder;

    private void createCheckedHolder() {
        checkedHolder = new boolean[getItemCount()];
    }

    @Override
    public int getItemCount() {
        if (shoppingCart!=null) {
            return shoppingCart.keySet().size();
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
    public process_prods(FragmentActivity activity, UserProfile profile,
                         HashMap<Long, CartProds> shoppingCart, View dialog_view, boolean single_choise) {
        this.activity = activity;
        this.shoppingCart = shoppingCart;
        this.dialog=dialog_view;
        this.single_choise=single_choise;
        this.profile = profile;
        inflater = LayoutInflater.from(activity.getApplicationContext());
        createCheckedHolder();
    }
}
