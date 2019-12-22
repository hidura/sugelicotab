package sugelico.postabsugelico;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.general;

/**
 * Created by hidura on 10/25/2016.
 */


public class product_adapter extends RecyclerView.Adapter<product_adapter.ViewHolder>  {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private HashMap<Long, CartProds> shoppingCart;
    private HashMap<Integer, Products> products_lst;
    private UserProfile profile;
    private cmpDetails companyProfile;
    private Products product;
    private android.app.AlertDialog.Builder dialog_builder;
    @Override
    public product_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product, parent, false);
        // set the view's size, margins, paddings and layout parameters

        product_adapter.ViewHolder vh = new product_adapter.ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(product_adapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        product = products_lst.get((Integer)products_lst.keySet().toArray()[position]);
        final Products prod_target= products_lst.get((Integer)products_lst.keySet().toArray()[position]);
        TextView cat_name= (TextView) holder.view.findViewById(R.id.name);
        cat_name.setText(product.getTitle());
        cat_name.setVisibility(View.VISIBLE);
        ImageView logo=((ImageView)holder.view.findViewById(R.id.product_logo));
        logo.setVisibility(View.VISIBLE);
        String imgpath = companyProfile.getPath()+"/resources/site/products/"+product.getIcon();
        Glide.with(activity.getApplicationContext())
                .load(imgpath)
                .into(logo);
        ((TextView)holder.view.findViewById(R.id.description)).setText(product.getDescription());
        ((TextView)holder.view.findViewById(R.id.price)).setText(product.getSubtotal().toString());
        ((FloatingActionButton)holder.view.findViewById(R.id.add_prod)).setContentDescription(product.getCode().toString());
        ((FloatingActionButton)holder.view.findViewById(R.id.add_prod)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_builder = new android.app.AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                final CartProds product_target = new CartProds();
                product_target.setCompaniontp(false);
                product_target.setSaveit(false );
                final Long last_product =Long.parseLong(profile.getBill().toString()+v.getContentDescription().toString()+shoppingCart.size())+1;//The id of the product in the cart is always the id of the order + the id of the product + the position.
                product_target.setOrdercode(last_product);
                shoppingCart.put(last_product, product_target);

                final View numb_view =activity.getLayoutInflater().inflate(R.layout.comments, null);
                if (profile.getClients()!=null){
                    if (profile.getClients().size()==0){
                        ClientStruct client_gen=new ClientStruct();
                        client_gen.setName("GENERICO");
                        client_gen.setCode(1);
                        client_gen.setRnc("");
                        client_gen.setNcf_type(2);
                        profile.getClients().put("GENERICO", client_gen);
                    }
                }else {
                    profile.setClients(new HashMap<String, ClientStruct>());
                    ClientStruct client_gen=new ClientStruct();
                    client_gen.setNcf_type(2);
                    client_gen.setCode(1);
                    client_gen.setName("GENERICO");
                    client_gen.setRnc("");
                    profile.getClients().put("GENERICO", client_gen);
                }
                dialog_builder.setTitle(activity.getResources().getString(R.string.companion_ttl));
                dialog_builder.setView(numb_view);
                profile.setDialog(dialog_builder.create());
                GridLayoutManager manager = new GridLayoutManager(activity.getApplicationContext(),1); // MAX NUMBER OF SPACES
                ((RecyclerView)numb_view.findViewById(R.id.clientlst)).setLayoutManager(manager);
                ((RecyclerView)numb_view.findViewById(R.id.clientlst)).setVisibility(View.VISIBLE);
                ((RecyclerView)numb_view.findViewById(R.id.clientlst)).setAdapter(new cl_adapter(activity,profile,products_lst,shoppingCart, product_target));
                ((ImageButton)numb_view.findViewById(R.id.add_client)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClientStruct clientData = new ClientStruct();
                        clientData.setName(((EditText)numb_view.findViewById(R.id.cl_name)).getText().toString());
                        product_target.setClient(clientData.getName());
                        profile.getClients().put(clientData.getName(), clientData);
                        GridLayoutManager manager = new GridLayoutManager(activity.getApplicationContext(),1); // MAX NUMBER OF SPACES
                        ((RecyclerView)numb_view.findViewById(R.id.clientlst)).setLayoutManager(manager);
                        ((RecyclerView)numb_view.findViewById(R.id.clientlst)).setVisibility(View.VISIBLE);
                        ((RecyclerView)numb_view.findViewById(R.id.clientlst)).setAdapter(new cl_adapter(activity,profile,products_lst,shoppingCart, product_target));
                    }
                });
                ((Button) numb_view.findViewById(R.id.first_time)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //The split of account will be make at the end always.
                        ((TextView)numb_view.findViewById(R.id.time_selected)).setText(activity.getResources().getString(R.string.first_time));
                    }
                });

                ((Button) numb_view.findViewById(R.id.second_time)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((TextView)numb_view.findViewById(R.id.time_selected)).setText(activity.getResources().getString(R.string.second_time));
                    }
                });

                ((Button) numb_view.findViewById(R.id.thrid_time)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((TextView)numb_view.findViewById(R.id.time_selected)).setText(activity.getResources().getString(R.string.third_time));
                    }
                });


                ((Button) numb_view.findViewById(R.id.increase)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //The button that increase the amount//Restart the count of companions
                        if (product_target.getCompanion()!=null) {
                            for (int cont = 0; cont < product_target.getCompanion().split("|").length; cont++) {
                                if (!product_target.getCompanion().split("|")[cont].equals("") && !product_target.getCompanion().split("|")[cont].equals("|") ) {
                                    Long comp_id = Long.parseLong(product_target.getCompanion().split("|")[cont]);
                                    if (shoppingCart.get(comp_id) != null)
                                        shoppingCart.remove(comp_id);
                                }
                            }
                            product_target.setCompanion(null);
                        }
                        ((TextView)numb_view.findViewById(R.id.amount_area)).setText(Integer.parseInt(((TextView)numb_view.findViewById(R.id.amount_area)).getText().toString())+1+"");
                        int amount = Integer.parseInt(((TextView)numb_view.findViewById(R.id.amount_area)).getText().toString());

                        int comp_amount=0;
                        if (prod_target.getCompanion().size()>0) {
                            comp_amount=prod_target.getCompanion().get(0).getCycle();
                        }
                        ((TextView)numb_view.findViewById(R.id.amount_selected)).setText((amount*comp_amount)+"");
                        setCompTermSug(prod_target,numb_view, product_target);
                    }
                });
                ((Button) numb_view.findViewById(R.id.decrease)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Restart the count of companions
                        if (product_target.getCompanion()!=null) {
                            for (int cont = 0; cont < product_target.getCompanion().split("|").length; cont++) {
                                if (!product_target.getCompanion().split("|")[cont].equals("") && !product_target.getCompanion().split("|")[cont].equals("|") ) {
                                    Long comp_id = Long.parseLong(product_target.getCompanion().split("|")[cont]);
                                    if (shoppingCart.get(comp_id) != null)
                                        shoppingCart.remove(comp_id);
                                }
                            }
                            product_target.setCompanion(null);
                        }
                        //The button that decrease
                        if (Integer.parseInt(((TextView)numb_view.findViewById(R.id.amount_area)).getText().toString())==1){
                            ((TextView)numb_view.findViewById(R.id.amount_area)).setText("1");
                        }else {
                            ((TextView)numb_view.findViewById(R.id.amount_area)).setText(Integer.parseInt(((TextView)numb_view.findViewById(R.id.amount_area)).getText().toString())-1+"");
                        }

                        int amount = Integer.parseInt(((TextView)numb_view.findViewById(R.id.amount_area)).getText().toString());
                        int comp_amount=0;
                        if (prod_target.getCompanion().size()>0) {
                            comp_amount=prod_target.getCompanion().get(0).getCycle();
                        }
                        ((TextView)numb_view.findViewById(R.id.amount_selected)).setText((amount*comp_amount)+"");
                        setCompTermSug(prod_target,numb_view, product_target);
                    }
                });
                int comp_amount=0;
                if (prod_target.getCompanion().size()>0) {
                    comp_amount=prod_target.getCompanion().get(0).getCycle();
                }
                ((TextView)numb_view.findViewById(R.id.amount_selected)).setText((comp_amount)+"");
                setCompTermSug(prod_target,numb_view, product_target);

                final View target_view =v;
                ((Button)numb_view.findViewById(R.id.accept)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        product_target.setSaveit(false);
                        product_target.setNotes(((TextView)numb_view.findViewById(R.id.comments)).getText().toString());
                        Integer product_id=Integer.parseInt(target_view.getContentDescription().toString());
                        product_target.setProduct(product_id);
                        if (((EditText) numb_view.findViewById(R.id.amount_area)).getText().toString().length()==0){
                            Toast.makeText(numb_view.getContext(), numb_view.getResources().getString(R.string.cantleaveempty), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (product_target.getClient()==null) {
                            product_target.setClient(profile.getClient().getName());
                        }
                        product_target.setAmount(Double.parseDouble(((EditText) numb_view.findViewById(R.id.amount_area)).getText().toString()));

                        product_target.setTime_pos(((TextView)numb_view.findViewById(R.id.time_selected)).getText().toString());

                        product_target.setDiscount(0.00);
                        product_target.setSubtotal((prod_target.getSubtotal()-product_target.getDiscount()) * product_target.getAmount());
                        product_target.setTax(((prod_target.getTax()/100)*product_target.getSubtotal()));
                        product_target.setPrice(product_target.getSubtotal()+product_target.getTax());

                        product_target.setTitle(prod_target.getTitle());

                        Double cur_total=Double.parseDouble(((TextView)activity.findViewById(R.id.total)).getText().toString())+product_target.getSubtotal();
                        ((TextView)activity.findViewById(R.id.total)).setText(cur_total.toString());

                        profile.getDialog().dismiss();
                        cart_prods frg = (cart_prods) activity.getSupportFragmentManager().
                                findFragmentById(R.id.fragment_cart);
                        frg.onTrackSelected(shoppingCart, profile, companyProfile);

                        final android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    }
                });
                ((Button)numb_view.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CartProds prod_del = shoppingCart.get(last_product);
                        if (prod_del.getCompanion() !=null) {
                            for (int cont = 0; cont < prod_del.getCompanion().split("\\|").length; cont++) {
                                String[] idcode=prod_del.getCompanion().split("\\|");

                                shoppingCart.remove(Long.parseLong(idcode[0]));
                            }
                        }

                        if (prod_del.getCompound() !=null) {
                            for (int cont = 0; cont < prod_del.getCompound().split("\\|").length; cont++) {
                                shoppingCart.remove(Long.parseLong(prod_del.getCompound().split("\\|")[cont]));
                            }
                        }

                        shoppingCart.remove(last_product);
                        profile.getDialog().cancel();

                    }
                });


                profile.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });

                profile.getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                });
                //profile.getDialog().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                profile.getDialog().show();

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
    public product_adapter(FragmentActivity activity, UserProfile profile,
                           HashMap<Integer, Products> products_lst,
                           HashMap<Long, CartProds> shoppingCart,
                           cmpDetails companyProfile) {
        this.activity = activity;
        this.shoppingCart = shoppingCart;
        this.companyProfile=companyProfile;
        this.products_lst = products_lst;
        this.profile = profile;
        inflater = LayoutInflater.from(activity.getApplicationContext());

    }


    public void setCompTermSug(final Products prod_target, final View numb_view, final CartProds product_target){
        if (prod_target.getTerms().size()>0) {
            //Just if there are terms add it.
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            ((LinearLayout) numb_view.findViewById(R.id.terms)).setVisibility(View.VISIBLE);
            ((LinearLayout) numb_view.findViewById(R.id.terms)).removeAllViews();
            final int cycle=1;
            ((TextView) numb_view.findViewById(R.id.product_comp)).setText(activity.getResources().getString(R.string.term_lbl));
            for (String term:
                    prod_target.getTerms()) {
                //Setting up the terms.
                Button radioButtonView = new Button(activity);
                radioButtonView.setText(term);
                radioButtonView.setTextColor(Color.WHITE);
                radioButtonView.setTextSize(18);
                radioButtonView.setTypeface(null, Typeface.BOLD);
                radioButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        product_target.setTerms(((Button)view).getText().toString());
                        HashMap<String, LinearLayout> container=new HashMap<>();
                        if (prod_target.getCompanion().size()>0) {
                            String comp_cat = "";
                            ((TextView) numb_view.findViewById(R.id.product_comp)).
                                    setText(activity.getResources().getString
                                            (R.string.companions_lbl));
                            ((LinearLayout) numb_view.findViewById(R.id.terms)).removeAllViews();
                            HashMap<String, ArrayList<Products>> prods = new
                                    general().getOrderByCat(companyProfile.getProducts(),
                                    prod_target.getCompanion());
                            SortedSet<String> keys = new TreeSet<>(prods.keySet());

                            for (String key : keys) {
                                for (Products companion :
                                    prods.get(key)) {
                                final Products comp = companion;

                                System.out.println(comp.getCode());
                                System.out.println(companyProfile.getProducts().get(comp.getCode()));
                                String catname = companyProfile.getProducts().get(comp.getCode()).getCat_name();
                                if (!catname.equals(comp_cat)) {
                                    comp_cat = companyProfile.getProducts().get(comp.getCode()).getCat_name();
                                    TextView catView = new TextView(activity);
                                    catView.setText(companyProfile.getProducts().get(comp.getCode()).getCat_name());
                                    catView.setTextColor(Color.WHITE);
                                    catView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    catView.setTypeface(null, Typeface.BOLD_ITALIC);
                                    LinearLayout.LayoutParams p_view = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.FILL_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(catView, p_view);
                                }
                                Button radioButtonView = new Button(activity);
                                radioButtonView.setText(companion.getTitle());
                                radioButtonView.setTextColor(Color.WHITE);
                                radioButtonView.setTextSize(18);
                                radioButtonView.setTypeface(null, Typeface.BOLD);
                                radioButtonView.setContentDescription(companion.getCode().toString());
                                radioButtonView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        addCompanion(product_target, comp, numb_view);

                                        setAdditional(prod_target, numb_view, product_target);


                                    }
                                });
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.FILL_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(radioButtonView, p);
                            }
                        }
                        }else {
                            ((Button)numb_view.findViewById(R.id.accept)).setEnabled(true);
                        }
                    }
                });
                ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(radioButtonView, p);
            }

        }
        else if (prod_target.getCompanion().size()>0){
            ((LinearLayout) numb_view.findViewById(R.id.terms)).removeAllViews();
            String comp_cat="";
            ((TextView) numb_view.findViewById(R.id.product_comp)).
                    setText(activity.getResources().getString
                            (R.string.companions_lbl));
            HashMap<String, ArrayList<Products>> prods = new
                    general().getOrderByCat(companyProfile.getProducts(),
                    prod_target.getCompanion());
            SortedSet<String> keys = new TreeSet<>(prods.keySet());

            for (String key : keys) {
                for (Products companion :
                        prods.get(key)) {

                    final Products comp = companion;
                    if (!companyProfile.getProducts().get(comp.getCode()).getCat_name().equals(comp_cat)) {
                        comp_cat = companyProfile.getProducts().get(comp.getCode()).getCat_name();
                        TextView catView = new TextView(activity);
                        catView.setText(companyProfile.getProducts().get(comp.getCode()).getCat_name());
                        catView.setTextColor(Color.WHITE);
                        catView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        catView.setTypeface(null, Typeface.BOLD_ITALIC);
                        LinearLayout.LayoutParams p_view = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.FILL_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(catView, p_view);
                    }
                    Button radioButtonView = new Button(activity);
                    radioButtonView.setText(companion.getTitle());
                    radioButtonView.setTextColor(Color.WHITE);
                    radioButtonView.setContentDescription(companion.getCode().toString());
                    radioButtonView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addCompanion(product_target, comp, numb_view);
                            setAdditional(prod_target, numb_view, product_target);
                        }
                    });
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(radioButtonView, p);
                }
            }
        }
        else
            ((Button)numb_view.findViewById(R.id.accept)).setEnabled(true);
    }


    public void setAdditional(final Products prod_target, final View numb_view, final CartProds product_target){
        ((LinearLayout) numb_view.findViewById(R.id.terms)).removeAllViews();
        String comp_cat="";
        ((TextView) numb_view.findViewById(R.id.product_comp)).
                setText(activity.getResources().getString
                        (R.string.optionals_lbl));
        HashMap<String, ArrayList<Products>> prods = new
                general().getOrderByCat(companyProfile.getProducts(),
                prod_target.getOptinoal());
        SortedSet<String> keys = new TreeSet<>(prods.keySet());

        for (String key : keys) {
            for (Products companion :
                    prods.get(key)) {
                final Products comp = companion;
                if (!companyProfile.getProducts().get(comp.getCode()).getCat_name().equals(comp_cat)) {
                    comp_cat = companyProfile.getProducts().get(comp.getCode()).getCat_name();
                    TextView catView = new TextView(activity);
                    catView.setText(companyProfile.getProducts().get(comp.getCode()).getCat_name());
                    catView.setTextColor(Color.WHITE);
                    catView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    catView.setTypeface(null, Typeface.BOLD_ITALIC);
                    LinearLayout.LayoutParams p_view = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(catView, p_view);
                }
                Button radioButtonView = new Button(activity);
                radioButtonView.setText(companion.getTitle());
                radioButtonView.setTextColor(Color.WHITE);
                radioButtonView.setContentDescription(companion.getCode().toString());
                radioButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addAdditional(product_target, comp, numb_view);
                    }
                });
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                ((LinearLayout) numb_view.findViewById(R.id.terms)).addView(radioButtonView, p);
            }
        }
    }
    public void addAdditional(CartProds product_target, Products comp, View numb_view){

        CartProds company=new CartProds();

        String oldCompanion=product_target.getCompanion();
        if (product_target.getClient()!=null)
            company.setClient(product_target.getClient());
        Long cmpID=Long.parseLong(profile.getBill().toString()+
                comp.getCode().toString()+shoppingCart.size())+1000;
        shoppingCart.put(cmpID, company);
        company.setCompaniontp(true);
        company.setTitle(comp.getTitle().toString());
        company.setOrdercode(cmpID);
        company.setProduct(comp.getCode());
        if (((EditText) numb_view.findViewById(R.id.amount_area)).getText().toString().length()==0){
            Toast.makeText(numb_view.getContext(), numb_view.getResources().getString(R.string.cantleaveempty), Toast.LENGTH_LONG).show();
            return;
        }

        company.setAmount(1.00);//Has to be one by default because you just hit one time the button

        company.setPrice(((comp.getPrice() == null) ? 0.00 : comp.getPrice()) * company.getAmount());
        company.setSubtotal(((comp.getSubtotal() == null) ? 0.00 : comp.getSubtotal()) * company.getAmount());
        company.setTax(((comp.getTax() == null) ? 0.00 : comp.getTax()) * company.getAmount());
        company.setSaveit(false);
        company.setClient(profile.getClient().getName());
        product_target.setCompanion(cmpID.toString());
        if (oldCompanion==null){
            oldCompanion=cmpID.toString();
        }else {
            oldCompanion+="|"+cmpID.toString();
        }


        product_target.setCompanion(oldCompanion);

    }



    public void addCompanion(CartProds product_target, Products comp, View numb_view){

        CartProds company=new CartProds();

        String oldCompanion=product_target.getCompanion();
        if (product_target.getClient()!=null)
            company.setClient(product_target.getClient());
        Long cmpID=Long.parseLong(profile.getBill().toString()+
                comp.getCode().toString()+shoppingCart.size())+1000;
        shoppingCart.put(cmpID, company);
        company.setCompaniontp(true);
        company.setTitle(comp.getTitle().toString());
        company.setOrdercode(cmpID);
        company.setProduct(comp.getCode());
        if (((EditText) numb_view.findViewById(R.id.amount_area)).getText().toString().length()==0){
            Toast.makeText(numb_view.getContext(), numb_view.getResources().getString(R.string.cantleaveempty), Toast.LENGTH_LONG).show();
            return;
        }
        ((Button)numb_view.findViewById(R.id.accept)).setEnabled(true);
        company.setAmount(1.00);//Has to be one by default because you just hit one time the button

        company.setPrice(((comp.getPrice() == null) ? 0.00 : comp.getPrice()) * company.getAmount());
        company.setSubtotal(((comp.getSubtotal() == null) ? 0.00 : comp.getSubtotal()) * company.getAmount());
        company.setTax(((comp.getTax() == null) ? 0.00 : comp.getTax()) * company.getAmount());
        company.setSaveit(false);
        company.setClient(profile.getClient().getName());
        product_target.setCompanion(cmpID.toString());
        if (oldCompanion==null){
            oldCompanion=cmpID.toString();
        }else {
            oldCompanion+="|"+cmpID.toString();
        }


        product_target.setCompanion(oldCompanion);
        int comp_amount_left = Integer.parseInt(((TextView)numb_view.findViewById(R.id.amount_selected)).getText().toString());
        if ((comp_amount_left-1)==0) {
            ((Button) numb_view.findViewById(R.id.accept)).setEnabled(true);
            ((LinearLayout) numb_view.findViewById(R.id.terms)).removeAllViews();
            ((TextView)numb_view.findViewById(R.id.amount_selected)).setText("0");
        }else {
            ((TextView)numb_view.findViewById(R.id.amount_selected)).setText((comp_amount_left-1)+"");
        }
    }



}
