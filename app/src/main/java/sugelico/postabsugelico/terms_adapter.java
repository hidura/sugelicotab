package sugelico.postabsugelico;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

/**
 * Created by diegohidalgo on 12/4/16.
 */

public class terms_adapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<String> items;
    private CartProds product_shopiing;
    private Products prod_target;
    private View view;
    private UserProfile profile;
    private cmpDetails companyProfile;

    private HashMap<Integer, CartProds> shoppingCart;
    public terms_adapter(FragmentActivity activity, UserProfile profile,
                         CartProds product_shopping,
                         cmpDetails companyProfile,
                         ArrayList<String> items, Products prod_target,
                         View numb_view,
                         HashMap<Integer, CartProds> shoppingCart) {
        this.activity = activity;
        this.items = items;
        this.product_shopiing=product_shopping;
        this.prod_target=prod_target;
        this.view=numb_view;
        this.companyProfile=companyProfile;
        this.profile=profile;
        this.shoppingCart=shoppingCart;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.terms_comp, null);

        }
        convertView.findViewById(R.id.single_option).setVisibility(View.VISIBLE);
        ((RadioButton)convertView.findViewById(R.id.single_option)).setText(items.get(position));
        convertView.setContentDescription(items.get(position));
        convertView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Si hay alguna guarnicion, que el sistema genere otro adaptador y la coloque en el mismo
                        // lugar que los terminos, caso contrario que habilite el boton de aceptar.
                    }
                }
        );
        return convertView;
    }
}
