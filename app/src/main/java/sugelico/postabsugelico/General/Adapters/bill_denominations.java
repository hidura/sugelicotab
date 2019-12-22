package sugelico.postabsugelico.General.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.R;

public class bill_denominations extends RecyclerView.Adapter<bill_denominations.ViewHolder>  {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private HashMap<String, Object> denominations;
    private UserProfile profile;
    private cmpDetails companyProfile;

    @Override
    public bill_denominations.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_lst, parent, false);
        // set the view's size, margins, paddings and layout parameters

        bill_denominations.ViewHolder vh = new bill_denominations.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final bill_denominations.ViewHolder holder, int position) {

        final int pos = position;
        holder.setIsRecyclable(false);
        holder.view_bill.setVisibility(View.GONE);
        holder.bill_date.setVisibility(View.GONE);
        holder.paytypes.setVisibility(View.GONE);
        holder.amount_left.setVisibility(View.GONE);
        holder.total.setVisibility(View.GONE);
        holder.order.setVisibility(View.GONE);
        holder.amount_paid.setText("0.00");
        holder.amount_paid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (holder.amount_paid.getText().length()>0){
                        try {
                            Double total = profile.getCashbox_info().getDouble("total");
                            total+=Double.parseDouble(holder.amount_paid.getText().toString());
                            profile.getCashbox_info().put("total", total);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        holder.paybill.setText(this.denominations.get((String) this.denominations.keySet().toArray()[position]).toString());
    }

    @Override
    public int getItemCount() {
        return this.denominations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        private TextView order;
        private TextView bill_date;
        private TextView total;
        private TextView amount_left;
        private EditText amount_paid;
        private Button view_bill;
        private TextView paybill;
        private Spinner paytypes;
        private ConstraintLayout main_view;
        public ViewHolder(View v) {
            super(v);

            view = v;
            order=((TextView)view.findViewById(R.id.bill_code));
            bill_date=((TextView)view.findViewById(R.id.bill_date));
            total=((TextView)view.findViewById(R.id.bill_total));
            amount_left=((TextView)view.findViewById(R.id.bill_left));
            paytypes=((Spinner)view.findViewById(R.id.cxcPayTypes));
            amount_paid=((EditText)view.findViewById(R.id.amount_paid));
            view_bill=((Button)view.findViewById(R.id.viewbill));
            paybill=((TextView)view.findViewById(R.id.paybill));
            main_view=((ConstraintLayout)view.findViewById(R.id.mainbk));
        }
    }
    public bill_denominations(AppCompatActivity activity,
                         UserProfile profile,
                         cmpDetails companyProfile) {
        this.activity = activity;
        this.profile = profile;
        this.companyProfile=companyProfile;
        this.denominations=new HashMap<String, Object>();
        for (int cont=0; cont< companyProfile.getPaytypes().size(); cont++){
            Integer code=Integer.parseInt(((HashMap<String, Object>)companyProfile.getPaytypes().get(cont)).get("code").toString());
            if (code==111){
                HashMap<String, Object> paydetails= (HashMap<String, Object>) ((HashMap<String, Object>)companyProfile.getPaytypes().get(cont)).get("paydetails");
                this.denominations= (HashMap<String, Object>)  paydetails.get("denomination");
                break;
            }
        }
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}