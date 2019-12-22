package sugelico.postabsugelico.General.Adapters;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.Table_adapter;

public class CompaniesBTN extends RecyclerView.Adapter<CompaniesBTN.ViewHolder>{
    private FragmentActivity activity;
    private LayoutInflater inflater;

    private JSONArray tables;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private View numb_view;
    private UserProfile userProfile;
    @Override
    public CompaniesBTN.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tables_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CompaniesBTN.ViewHolder vh = new CompaniesBTN.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CompaniesBTN.ViewHolder holder, int position) {
        HashMap orderInfo=(HashMap) userProfile.getCompanies().get(position);
        holder.setIsRecyclable(false);

        Button table_name=(Button) holder.view.findViewById(R.id.tbl_unoccupied);
        table_name.setVisibility(View.VISIBLE);
        table_name.setText(orderInfo.get("name_cmp").toString());
        table_name.setContentDescription(orderInfo.get("company").toString());
        table_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfile.setCompany(Integer.parseInt(v.getContentDescription().toString()));
            }
        });



    }

    @Override
    public int getItemCount() {
        if (userProfile.getCompanies()!=null) {
            return userProfile.getCompanies().size();
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

    public CompaniesBTN(FragmentActivity activity,
                          UserProfile profile,
                          cmpDetails companyProfile) {

        this.activity = activity;
        this.userProfile= profile;
        this.companyProfile=companyProfile;
        inflater = LayoutInflater.from(activity.getApplicationContext());
    }





}
