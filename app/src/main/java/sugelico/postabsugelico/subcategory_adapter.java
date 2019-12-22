package sugelico.postabsugelico;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

/**
 * Created by hidura on 11/6/2016.
 */

public class subcategory_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {
    private FragmentActivity activity;
    private LayoutInflater inflater;

    private HashMap<Integer, CatalogCat> products_lst;
    private HashMap<Long, CartProds> shoppingCart;
    private UserProfile profile;
    private CatalogCat catalogCat;
    private cmpDetails companyProfile;
    @Override
    public category_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product, parent, false);
        // set the view's size, margins, paddings and layout parameters

        category_adapter.ViewHolder vh = new category_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(category_adapter.ViewHolder holder, int position) {

        catalogCat = products_lst.get((Integer)products_lst.keySet().toArray()[position]);

        final HashMap<Integer, Products> prodlst = products_lst.get((Integer) products_lst.keySet().toArray()[position]).getProducts();
        TextView cat_name= (TextView) holder.view.findViewById(R.id.name);
        cat_name.setText(catalogCat.getTitle());
        cat_name.setVisibility(View.VISIBLE);
        ImageView logo=((ImageView)holder.view.findViewById(R.id.product_logo));
        logo.setVisibility(View.VISIBLE);
        String imgpath = companyProfile.getPath()+catalogCat.getIcon();
        Glide.with(activity.getApplicationContext())
                .load(imgpath)
                .into(logo);
        ((TextView)holder.view.findViewById(R.id.description)).setVisibility(View.GONE);
        ((TextView)holder.view.findViewById(R.id.price)).setVisibility(View.GONE);
        ((FloatingActionButton)holder.view.findViewById(R.id.add_prod)).setVisibility(View.GONE);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products frg = null;
                frg = (products) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_prodlst);
                frg.onTrackSelected(profile, null,shoppingCart, prodlst, companyProfile);
                final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });


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

    public subcategory_adapter(FragmentActivity activity,
                            UserProfile profile,
                            HashMap<Integer, CatalogCat> products_lst,
                            HashMap<Long, CartProds> shoppingCart,
                            cmpDetails companyProfile) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.profile=profile;
        this.products_lst = products_lst;
        this.shoppingCart=shoppingCart;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}
