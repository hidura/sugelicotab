package sugelico.postabsugelico;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.Area;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CategoryType;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

/**
 * Created by hidura on 11/7/2016.
 */

public class Area_adapter extends RecyclerView.Adapter<Area_adapter.ViewHolder>{
    private FragmentActivity activity;
    private LayoutInflater inflater;

    private HashMap<Integer, CategoryType> products_lst;
    private HashMap<Long, CartProds> shoppingCart;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private HashMap<Integer, Area> areas;
    @Override
    public Area_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        Area_adapter.ViewHolder vh = new Area_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Area_adapter.ViewHolder holder, int position) {

        final Area area = areas.get((Integer)areas.keySet().toArray()[position]);
        holder.setIsRecyclable(false);
        Button area_name= (Button) holder.view.findViewById(R.id.mobile_btn);
        area_name.setText(area.getArea_name());
        area_name.setVisibility(View.VISIBLE);
        area_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tables frg = (tables) activity.getSupportFragmentManager().
                        findFragmentById(R.id.fragment_tbl);
                frg.onTrackSelected(profile, area.getTables(), companyProfile, shoppingCart);

                final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });




    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public Area_adapter(FragmentActivity activity,
                        UserProfile profile,
                        HashMap<Integer, Area> areas,
                        cmpDetails companyProfile,
                        HashMap<Long, CartProds> shoppingCart) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.areas = areas;
        this.profile = profile;
        this.shoppingCart=shoppingCart;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }

}
