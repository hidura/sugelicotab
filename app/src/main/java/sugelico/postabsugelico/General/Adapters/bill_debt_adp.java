package sugelico.postabsugelico.General.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.paytype_adp;
import sugelico.postabsugelico.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class bill_debt_adp extends RecyclerView.Adapter<bill_debt_adp.ViewHolder>  {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private JSONArray bills;
    private UserProfile profile;
    private cmpDetails companyProfile;

    @Override
    public bill_debt_adp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_lst, parent, false);
        // set the view's size, margins, paddings and layout parameters

        bill_debt_adp.ViewHolder vh = new bill_debt_adp.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final bill_debt_adp.ViewHolder holder, int position) {
        try {
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayList<paytype_adp> paytype_adps = new ArrayList<>();
            for(int cont=0; cont<companyProfile.getPaytypes().size(); cont++){
                int code=((int)((HashMap)companyProfile.getPaytypes().get(cont)).get("code"));
                String name=((HashMap)companyProfile.getPaytypes().get(cont)).get("tpname").
                        toString()+"|"+code;
                paytype_adps.add(new paytype_adp(code,name));
            }
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(activity,
                    android.R.layout.simple_spinner_item, paytype_adps);
            // Specify the layout to use when the list of choices appears
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            holder.paytypes.setAdapter(spinnerArrayAdapter);
            final JSONObject bill = bills.getJSONObject(position);
            final Double amount_left=(bill.getDouble("billtotal")-bill.getDouble("ptpaid"));
            holder.order.setText(bill.getString("billpreorder"));
            holder.total.setText(NumberFormat.getNumberInstance(Locale.US).format(bill.getDouble("billtotal")));
            holder.bill_date.setText(bill.getString("billdate"));
            holder.amount_left.setText(NumberFormat.getNumberInstance(Locale.US).
                    format(amount_left));
            holder.view_bill.setContentDescription(bill.getString("billpreorder"));
            holder.amount_paid.setContentDescription
                    (bill.getString("billpreorder"));

            holder.amount_paid.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (((EditText) view).
                                getText().toString().length()==0){
                            Toast.makeText(activity.getApplicationContext(),
                                    activity.getResources().
                                    getString(R.string.required_field), Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (!profile.getCxcbillpaid().has(view.
                                getContentDescription().toString())) {
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date date = new Date();
                                JSONObject billData=new JSONObject();
                                billData.put("billcode", bill.getString("billcode"));
                                billData.put("preorder", bill.getString("billcode"));
                                billData.put("tax", bill.getString("billtax"));
                                billData.put("total", bill.getString("billtotal"));
                                billData.put("subtotal", bill.getString("billsubtotal"));
                                billData.put("discount", bill.getString("billdisc"));
                                billData.put("billtp_extra",
                                        (bill.has("billextra")) ?
                                                bill.getString("billextra"):0.00     );
                                billData.put("client", bill.getString("client_id"));
                                billData.put("_date", date);//Add the current date
                                billData.put("billtype", bill.getString("billbilltp"));
                                billData.put("paytypelst", new JSONObject());

                                profile.getCxcbillpaid().put(view.
                                                getContentDescription().toString(),billData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Double total_paid=0.00;
                        try {
                            String[] paytypeID=holder.paytypes.getSelectedItem().toString().split("\\|");


                            if (profile.getCxcbillpaid().getJSONObject(view.
                                    getContentDescription().toString()).has("total_paid")){
                                total_paid=profile.getCxcbillpaid().getJSONObject(view.
                                        getContentDescription().toString()).getDouble("total_paid");
                            }
                            if(!profile.getCxcbillpaid().getJSONObject(view.
                                    getContentDescription().toString()).getJSONObject("paytypelst").
                                    has(paytypeID[1])) {

                                total_paid += Double.parseDouble(((EditText) view).
                                        getText().toString());
                            }else{
                                total_paid-=profile.getCxcbillpaid().getJSONObject(view.
                                        getContentDescription().toString()).
                                        getJSONObject("paytypelst").
                                        getDouble(paytypeID[1]);
                                Double curAmount = Double.parseDouble(((TextView)activity.
                                        findViewById(R.id.cxctotal_paid)).getText().
                                        toString().replace(",",""))-
                                        profile.getCxcbillpaid().getJSONObject(view.
                                        getContentDescription().toString()).getJSONObject("paytypelst").
                                        getDouble(paytypeID[1]);
                                ((TextView)activity.findViewById(R.id.cxctotal_paid)).
                                        setText(NumberFormat.getNumberInstance(Locale.US).
                                                format(curAmount));
                                total_paid += Double.parseDouble(((EditText) view).
                                        getText().toString());
                            }
                            profile.getCxcbillpaid().getJSONObject(view.
                                    getContentDescription().toString()).getJSONObject("paytypelst").
                                    put(paytypeID[1],Double.parseDouble(((EditText) view).
                                            getText().toString())).
                                    put(paytypeID[1]+"|", paytypeID[0]);
                            profile.getCxcbillpaid().getJSONObject(view.
                                    getContentDescription().toString()).
                                    put("total_paid",total_paid);
                            profile.getCxcbillpaid().getJSONObject(view.
                                    getContentDescription().toString()).
                                    put("bill_left",amount_left-total_paid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Double newLeft =amount_left-total_paid;
                        holder.paybill.setText(newLeft.toString());
                        Double curAmount = Double.parseDouble(((TextView)activity.
                                findViewById(R.id.cxctotal_paid)).getText().
                                toString().replace(",",""))+
                                Double.parseDouble(((EditText)view).getText().toString());
                        ((TextView)activity.findViewById(R.id.cxctotal_paid)).
                                setText(NumberFormat.getNumberInstance(Locale.US).
                                        format(curAmount));
                        ((EditText)view).setHint(activity.getResources().getString(R.string.pickpaytype_addval));
                        ((EditText)view).setText("");
                        return true;
                    }
                    return false;
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(bills);
        }

        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return bills.length();
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
    public bill_debt_adp(AppCompatActivity activity,
                           UserProfile profile,
                            JSONArray bills,
                           cmpDetails companyProfile) {
        this.activity = activity;
        this.profile = profile;
        this.companyProfile=companyProfile;
        this.bills=bills;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }
}