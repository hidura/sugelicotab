package sugelico.postabsugelico.General.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.R;

/**
 * Created by hidura on 2/17/2018.
 */

public class denominations extends BaseAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private int selectedPosition=-1;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private HashMap<String, Double> paytypes;
    private View main_view;
    private HashMap<String, Object> billDetails;
    private String[] mKeys;
    public denominations(AppCompatActivity activity,
                         UserProfile profile,
                         cmpDetails companyProfile, HashMap<String, Double> paytypes, HashMap<String, Object> billDetails) {

        this.companyProfile=companyProfile;
        this.activity = activity;
        this.profile = profile;
        this.paytypes=paytypes;
        mKeys = paytypes.keySet().toArray(new String[paytypes.size()]);
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return mKeys.length;
    }

    @Override
    public Object getItem(int i) {
        return paytypes.get(mKeys[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    static class ViewHolder {
        RadioButton chkd_option;
        TextView tblname;
        TextView preorder;
        LinearLayout tblSel;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        denominations.ViewHolder holder;
        final HashMap<String, Object> paytype= (HashMap<String, Object>) companyProfile.getPaytypes().get(position);


        final View inflate_view = inflater.inflate(R.layout.paytype_lst, null);
        ((TextView)inflate_view.findViewById(R.id.paytp_lbl)).setText(mKeys[position]);
        String total=getItem(position).toString();
        ((TextView)inflate_view.findViewById(R.id.amount_paid)).setText(total);

        return inflate_view;
    }

}

