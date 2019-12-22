package sugelico.postabsugelico.General;


/**
 * Created by hidura on 11/22/2015.
 */


public class productHandle {
//    private String prodcode;
//    private String prodname;
//    private View view;
//    private SQLiteDatabase db;
//    private Dialog dialog;
//    private Context context;
//    private int tableID;
//    private HashMap<String, Object> dataExtra,aggregates;
//    private AlertDialog.Builder builder;
//    private LayoutInflater inflater;
//    private ActionBarActivity activity;
//
//    private ArrayList<CartProds> cartProducts;
//    private ArrayList<CatalogCat> ProductsCatalog;
//
//    public void productHandle(View view, HashMap<String, Object> args){
//        prodcode = (String)args.get("code");
//        prodname = (String)args.get("name");
//        cartProducts = (ArrayList<CartProds>)args.get("cart_products");
//        ProductsCatalog = (ArrayList<CatalogCat>)args.get("products");
//
//        dataExtra = new HashMap<>();
//        this.view = view;
//        activity = ((ActionBarActivity) args.get("activity"));
//        context = activity.getApplicationContext();
////        if (args.get("activity") == null){
////            context = this.view.getContext();
////        }else{
////
////        }
//
//
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        tableID=new general().getTable(context).getCode();
//        aggregates = new Aggregate(Integer.parseInt(prodcode), context).adapters;
//        // Inflate the layout to use as dialog or embedded fragment
//        builder=(AlertDialog.Builder)args.get("builder");
//
//        loadClients(view);
//
//    }
//
//    public void openClient(){
//        AlertDialog.Builder new_builder = null;
//        if (activity !=null) {
//            new_builder= new AlertDialog.Builder(activity);
//        }else{
//            new_builder= new AlertDialog.Builder(context);
//        }
//        final AlertDialog.Builder clientdialog_builder = new_builder;
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        clientdialog_builder.setTitle(context.getResources().getString(R.string.divide_acc));
//        clientdialog_builder.setMessage(context.getResources().getString(R.string.divisionaccmsg));
//        final View clientView = inflater.inflate(R.layout.fragment_client_manager, null);
//
//        clientdialog_builder.setView(clientView)
//                // Add action buttons
//                .setPositiveButton(context.getResources().getString(R.string.valid_btn), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        ((LinearLayout)clientView.findViewById(R.id.elements)).setVisibility(View.GONE);
//
//                        ((ProgressBar)clientView.findViewById(R.id.waitClient)).setVisibility(View.VISIBLE);
//                        clientdialog_builder.setTitle("Espera, te estamos registrando!");
//
//                        EditText name =(EditText)clientView.findViewById(R.id.owner);
//                        EditText document=(EditText)clientView.findViewById(R.id.ncf);
//                        EditText telephone=(EditText)clientView.findViewById(R.id.telephone);
//                        if (!name.getText().toString().equals("")) {
//
//                            if (new general().isNetworkAvailable(context)) {
////                                HashMap<String, Object> params = new HashMap<String, Object>();
////
////                                params.put("name", name.getText().toString());
////                                params.put("lastname", "");
////                                params.put("document", document.getText().toString());
////                                params.put("telephone", telephone.getText().toString());
//                                db = context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null);
//                                ContentValues clientVal = new ContentValues();
//                                dataExtra.put("client", (new general().getID(db, Client.ClientCat.TableName)+1));
//                                clientVal.put(Client.ClientCat.code, ((Integer)dataExtra.get("client")));
//
//                                clientVal.put(Client.ClientCat.cl_name, name.getText().toString());
//
//                                clientVal.put(Client.ClientCat.ncf, document.getText().toString());
//
//                                clientVal.put(Client.ClientCat.telephone, telephone.getText().toString());
//                                clientVal.put(Client.ClientCat.table, tableID);
//
//                                long rowID = db.insert(Client.ClientCat.TableName, null, clientVal);
//                                db.close();
//                                loadPortions();
//
//                            } else {
//                                Toast.makeText(context, "No hay conexión disponible, favor conectarse a una red", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }).setNegativeButton(context.getResources().getString(R.string.invalid_btn), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//
//        final AlertDialog client_dialog = clientdialog_builder.create();
//        client_dialog.show();
//        ((ListView)clientView.findViewById(R.id.cl_lst)).setAdapter(((spinnerAdapter) aggregates.get("clients")));
//        ((ListView)clientView.findViewById(R.id.cl_lst)).setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String client = ((TextView) view.findViewById(R.id.idtable)).getText().toString();
//                        dataExtra.put("client", Integer.parseInt(client));
//                        client_dialog.dismiss();
//
//                        loadPortions();
//                    }
//                }
//        );
//
//
//
//    }
//    public void selAcc(DialogInterface dialog, int which, int amount){
//
//        dataExtra.put("amount", amount);
//        if (which == 1){
//            //This means that the user select unique account.
//            dataExtra.put("client", 1);
//            loadPortions();
//        }
//        else{
//            //This means that the user select divide the account
//            openClient();
//        }
//
//    }
//
//    public void loadClients(View v){
//        /*
//         *This functions must show:
//          *  the client list.
//          *  the portion(If there's more than one).
//          *  the Guarnition(if there's anyone).
//          *  and the coccion time, if there's anyone.
//          Then when the user's finish, the system should
//         */
//
//        builder.setTitle("");
//
//        View confirmation = inflater.inflate(R.layout.confirmation, null);
//        builder.setView(confirmation);
//        final EditText np = (EditText) confirmation.findViewById(R.id.food_amount);
//
////        builder.setNegativeButton(R.string.divide_acc, new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////
////            }
////        });
//
//        builder.setPositiveButton(R.string.unique_acc, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
//
//        final AlertDialog cl_dialog = builder.create();
//        cl_dialog.show();
//        cl_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Do stuff, possibly set wantToCloseDialog to true then...
//                if (!checkAmount(np)){
//                    Toast.makeText(context, context.getResources().getString(R.string.amountwar), Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if (Integer.parseInt(np.getText().toString()) > 0){
//                    dataExtra.put("notes", ((EditText) cl_dialog.findViewById(R.id.notes)).getText());
//                    cl_dialog.dismiss();
//                    selAcc(cl_dialog, 1, Integer.parseInt(np.getText().toString()));
//
//                    return;
//                }
//                Toast.makeText(context, "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
//
//
//            }
//        });
//        cl_dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Do stuff, possibly set wantToCloseDialog to true then...
//                if (!checkAmount(np)) {
//                    Toast.makeText(context, "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if (Integer.parseInt(np.getText().toString()) > 0) {
//                    dataExtra.put("notes", ((EditText) cl_dialog.findViewById(R.id.notes)).getText());
//                    cl_dialog.dismiss();
//                    selAcc(cl_dialog, 0, Integer.parseInt(np.getText().toString()));
//
//                    return;
//                }
//                Toast.makeText(context, "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
//
//
//            }
//        });
//    }
//    public void getCompound(){
//        if (aggregates.containsKey("compounds")){
//
//            if (activity !=null) {
//                builder= new AlertDialog.Builder(activity);
//            }else{
//                builder= new AlertDialog.Builder(context);
//            }
//            final View compoundView = inflater.inflate(R.layout.additional, null);
//            ((ListView)compoundView.findViewById(R.id.additional_lst)).setAdapter(((compound_adapter) aggregates.get("compounds")));
//
//            builder.setTitle("Elija su Composición").setView(compoundView);
//            final AlertDialog _dialog = builder.create();
//            compoundView.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout listSelected = ((LinearLayout) compoundView.findViewById(R.id.listSelected));
//                    View list= ((ListView)compoundView.findViewById(R.id.additional_lst)).getChildAt(0);
//                    Integer maxVal = Integer.parseInt(((TextView) list.findViewById(R.id.cycle)).getText().toString());
//                    if (maxVal==listSelected.getChildCount()) {
//                        String products_lst = "";
//                        for (int cont = 0; cont < listSelected.getChildCount(); cont++) {
//                            LinearLayout child = (LinearLayout) listSelected.getChildAt(cont);
//                            TextView productid = (TextView) child.getChildAt(2);
//                            String idProd = productid.getText().toString();
//                            if (!products_lst.equals("")) {
//                                products_lst += ",";
//                            }
//                            products_lst += idProd;
//                        }
//                        dataExtra.put("compounds", products_lst);
//                        _dialog.dismiss();
//                        guarnition();
//                    }else{
//                        Toast.makeText(context, "Debe elegir "+maxVal, Toast.LENGTH_LONG).show();
//
//                        _dialog.setTitle("Puede elegir " + (maxVal-listSelected.getChildCount())+" mas");
//                    }
//
//                }
//            });
//            _dialog.show();
//        }else{
//            guarnition();
//        }
//    }
//
//    public void loadPortions(){
//        if (aggregates.containsKey("portions")){
//            if (((spinnerAdapter)aggregates.get("portions")).getCount()>1) {
//                if (activity !=null) {
//                    builder= new AlertDialog.Builder(activity);
//                }else{
//                    builder= new AlertDialog.Builder(context);
//                }
//                builder.setTitle("Elija su porción")
//                        .setSingleChoiceItems((spinnerAdapter) aggregates.get("portions"), 1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                View target = (View) ((spinnerAdapter) aggregates.get("portions")).getView(which, null, null);
//                                dataExtra.put("portion", ((TextView) target.findViewById(R.id.name)).getText().toString());
//                                dialog.dismiss();
//                                getCompound();
//
//                            }
//                        });
//                builder.create().show();
//            }else if(((spinnerAdapter)aggregates.get("portions")).getCount()==1){
//                View target = (View) ((spinnerAdapter) aggregates.get("portions")).getView(0, null, null);
//                dataExtra.put("portion", ((TextView) target.findViewById(R.id.name)).getText().toString());
//                //dialog.dismiss();
//                getCompound();
//            }
//        }else{
//            getCompound();
//        }
//
//    }
//
//    public void guarnition(){
//
//        if (aggregates.containsKey("companions")) {
//
//            AlertDialog.Builder pre_builder= null;
//            if (activity !=null) {
//                pre_builder= new AlertDialog.Builder(activity);
//            }else{
//                pre_builder= new AlertDialog.Builder(context);
//            }
//
//            spinnerAdapter adapt= (spinnerAdapter) aggregates.get("companions");
//            AlertDialog.Builder new_builder=pre_builder;
//            new_builder.setTitle("Elija su acompañante");
//
//
//            new_builder.setSingleChoiceItems(adapt, 0, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    View target = (View) ((spinnerAdapter) aggregates.get("companions")).getView(which, null, null);
//                    dataExtra.put("companion", ((TextView) target.findViewById(R.id.name)).getText().toString());
//                    dialog.dismiss();
//                    terms();
//                }
//            });
//            new_builder.create().show();
//        }else{
//            terms();
//        }
//    }
//
//    public void terms(){
//        if (aggregates.containsKey("terms")) {
//            Integer count = ((spinnerAdapter)aggregates.get("terms")).getCount();
//            if (count>0) {
//                AlertDialog.Builder new_builder = null;
//                if (activity !=null) {
//                    new_builder= new AlertDialog.Builder(activity);
//                }else{
//                    new_builder= new AlertDialog.Builder(context);
//                }
//                new_builder.setTitle("Elija su el termino de su almuerzo")
//                        .setSingleChoiceItems((spinnerAdapter) aggregates.get("terms"), 1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                View target = (View) ((spinnerAdapter) aggregates.get("terms")).getView(which, null, null);
//                                dataExtra.put("terms", ((TextView) target.findViewById(R.id.name)).getText().toString());
//                                add();
//                                dialog.dismiss();
//                            }
//                        });
//                new_builder.create().show();
//            }else{
//                add();
//            }
//        }else{
//            add();
//        }
//    }
//
//    public void add(){
//
//        String price ="";
//        String portion="";
//        TextView msg = ((TextView) view.findViewById(R.id.msg));
//        CartProds cart_data = new CartProds();
//
//        if (((String)dataExtra.get("portion")).indexOf(":")>0) {
//
//            String rawPrice = ((String) dataExtra.get("portion")).split(":")[1];
//            portion=((String) dataExtra.get("portion")).split(":")[0];
//            try {
//                price = NumberFormat.getNumberInstance(Locale.ENGLISH).parse(rawPrice.split(" ")[2]).toString();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        }else{
//            portion="General";
//            try {
//                price = NumberFormat.getNumberInstance(Locale.ENGLISH).parse(((String) dataExtra.get("portion")).split(" ")[2]).toString();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        cart_data.setPortion(portion);
//        cart_data.setSaveit(false);
//
//        if (dataExtra.containsKey("companion")) {
//            price=""+ (Double.parseDouble(((String) dataExtra.get("companion")).split(":")[1])+Double.parseDouble(price));
//        }
//        Integer amount = ((Integer)dataExtra.get("amount"));
//        cart_data.setAmount(Double.parseDouble(dataExtra.get("amount").toString()));
//
//
//        Double total =amount * Double.parseDouble(price);
//        db = context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null);
//        new BillHelper(context).onCreate(db);
//        ContentValues priceVal = new ContentValues();
//
//        priceVal.put(Bill.BillCat.prodname, prodname);
//        cart_data.setTitle(prodname);
//
//
//        priceVal.put(Bill.BillCat.owner, (Integer)dataExtra.get("client"));
//        ClientStruct clientdata = new ClientStruct();
//        clientdata.setCode((Integer)dataExtra.get("client"));
//
//        cart_data.setClient(clientdata);
//        priceVal.put(Bill.BillCat.amount, Integer.toString(amount));
//
//        priceVal.put(Bill.BillCat.code, prodcode);
//
//        String orderCode = Integer.toString(new general().getTable(context).getOrder())+
//                Integer.toString(Integer.parseInt(prodcode)+new general().getTable(context).getCode()+
//                        new general().getTable(context).getOrder()+new general().getID(db, Bill.BillCat.TableName)+amount);
//
//        priceVal.put(Bill.BillCat.ordercode, orderCode);
//        cart_data.setOrdercode(Integer.parseInt(orderCode));
//        if (dataExtra.containsKey("terms")) {
//            priceVal.put(Bill.BillCat.term, ((String) dataExtra.get("terms")));
//            cart_data.setTerms(((String) dataExtra.get("terms")));
//        }
//        priceVal.put(Bill.BillCat.price, Double.toString(total));
//        cart_data.setPrice(total);
//
//        priceVal.put(Bill.BillCat.portion, portion);
//        if (dataExtra.containsKey("companion")) {
//            priceVal.put(Bill.BillCat.guarnicion, ((String) dataExtra.get("companion")).split("-")[0]);
//            cart_data.setCompanion(((String) dataExtra.get("companion")).split("-")[0]);
//
//        }
//        priceVal.put(Bill.BillCat.notes, dataExtra.get("notes").toString());
//        cart_data.setNotes(dataExtra.get("notes").toString());
//        if (dataExtra.containsKey("compounds")) {
//            priceVal.put(Bill.BillCat.compound, dataExtra.get("compounds").toString());
//            cart_data.setCompound(dataExtra.get("compounds").toString());
//        }
//        priceVal.put(Bill.BillCat.table, Integer.toString(new general().getTable(view.getContext()).getCode()));
//        priceVal.put(Bill.BillCat.order, Integer.toString(new general().getTable(view.getContext()).getOrder()));
//
//        long rowID = db.insert(Bill.BillCat.TableName, null, priceVal);
//        db.close();
//        cartProducts.add(cart_data);
//        HashMap<String, Object> params=new HashMap<>();
//        params.put("activity", activity);
//        params.put("cart_products", cartProducts);
//        params.put("products", ProductsCatalog);
//        CartAdapter adapter = new CartAdapter(params);
//
//        ((GridView)activity.findViewById(R.id.cart_slide)).setAdapter(adapter);//Creating and attaching the adapter to the product list.
//        NumberFormat format = NumberFormat.getCurrencyInstance();
//
//        Double subtotal = 0.00;
//        try {
//            subtotal = total+Double.parseDouble(format.parse(((TextView)activity.findViewById(R.id.total)).getText().toString()).toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        ((TextView)activity.findViewById(R.id.total)).setText(new general().getDecimalForm(subtotal));
//
//
////        final ads advertisment = new ads();
////
////        Bundle args = new Bundle();
////        args.putInt("type",4);
////        args.putInt("SuggestBase", Integer.parseInt(prodcode));
////        args.putInt("product", Integer.parseInt(prodcode));
////        advertisment.setArguments(args);
////        advertisment.show(activity.getFragmentManager(), "Galeria");
////        intent =new Intent(context, advertisment.class);
////        //intent =new Intent(context, Catalog.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        intent.putExtra("type",4);
////        intent.putExtra("SuggestBase", Integer.parseInt(prodcode));
////        intent.putExtra("product", Integer.parseInt(prodcode));
////
////        context.startActivity(intent);
//
//
//
//
//    }
//
//    public boolean checkAmount(EditText amount){
//        if (amount.getText().toString().equals(""))
//            return false;
//        else
//            return true;
//    }
}
