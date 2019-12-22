package sugelico.postabsugelico;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;

/**
 * Created by diegohidalgo on 1/24/17.
 */

public class confirmation extends BaseAdapter {

    private ArrayList<Products> companions;
    private CartProds product;
    private FragmentActivity activity;

    private HashMap<Integer, CartProds> shoppingCart;
    public confirmation(FragmentActivity activity, UserProfile profile, ArrayList<Products> companions,
                             HashMap<Integer, CartProds> shoppingCart, Products prod_target, View numb_view)
    {
        this.companions=companions;
        this.activity=activity;
        this.shoppingCart=shoppingCart;
    }

    @Override
    public int getCount() {
        return this.companions.size();
    }

    @Override
    public Object getItem(int i) {
        return this.companions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getApplicationContext().
                            getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view= mInflater.inflate(R.layout.companion, null);
        }
        final Products companion=this.companions.get(i);
        ((TextView)view.findViewById(R.id.code_prod)).setText(companion.getTitle().toString());
        ((TextView)view.findViewById(R.id.comp_name)).setTextColor(Color.BLACK);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartProds company=new CartProds();
                shoppingCart.put(shoppingCart.size(), company);
                company.setTitle(companion.getTitle().toString());
                company.setProduct(companion.getCode());
            }
        });

        return view;
    }
}
