package sugelico.postabsugelico;

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
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

/**
 * Created by hidura on 10/25/2016.
 */

public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {
    private FragmentActivity activity;
    private LayoutInflater inflater;

    private HashMap<Integer, CategoryType> products_lst;
    private HashMap<Long, CartProds> shoppingCart;
    private UserProfile profile;
    private CategoryType catalogCat;
    private cmpDetails companyProfile;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_type, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        catalogCat = products_lst.get((Integer)products_lst.keySet().toArray()[position]);
        final HashMap<Integer, CatalogCat> categories = products_lst.get((Integer)products_lst.keySet().toArray()[position]).getCategories();
        TextView cat_name= (TextView) holder.view.findViewById(R.id.cat_type_name);
        cat_name.setText(catalogCat.getCat_tpname());
        cat_name.setVisibility(View.VISIBLE);
        ((ImageView)holder.view.findViewById(R.id.type_prod_avatar)).setVisibility(View.VISIBLE);
        String imgpath = companyProfile.getPath()+catalogCat.getAvatar();
        Glide.with(activity.getApplicationContext())
                .load(imgpath)
                .into(((ImageView)holder.view.findViewById(R.id.type_prod_avatar)));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products frg = null;
                frg = (products) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_prodlst);
                frg.onTrackSelected(profile, categories,shoppingCart, null, companyProfile);
                final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
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

    public category_adapter(FragmentActivity activity,
                            UserProfile profile,
                            HashMap<Integer, CategoryType> products_lst,
                            HashMap<Long, CartProds> shoppingCart,
                            cmpDetails companyProfile) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.products_lst = products_lst;
        this.profile = profile;
        this.shoppingCart=shoppingCart;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}