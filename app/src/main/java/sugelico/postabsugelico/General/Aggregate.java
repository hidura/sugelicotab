package sugelico.postabsugelico.General;


/**
 * Created by hidura on 10/3/2015.
 */
public class Aggregate {
    /*
    This class creates the
    aggregates of every products,
    return a list of adapters :
        Clients.
        Portions(If it have).
        Guarnitions/Companions(If it have).
        Coction levels(If it have).
     */
//    public HashMap<String, Object> adapters;
//    private int codeProd;
//    private SQLiteDatabase db;
//    private Context context;
//    public Aggregate(int codeProd, Context context){
//        this.adapters = new HashMap<String, Object>();
//        this.codeProd = codeProd;
//        this.context=context;
//        this.db = context.openOrCreateDatabase(ClientHelper.DATABASE_NAME, 0, null);
//        getClients();
//        getPortions();
//        getTerms();
//        getCompanions();
//        getCompounds();
//    }
//
//    public void getClients(){
//        //Filling the client area.
//        this.adapters.put("clients", new spinnerAdapter(context, new general().updClLst(0, context)));
//    }
//
//    public void getCompounds(){
//        Cursor prodCompound =this.db.rawQuery("SELECT " +
//                CatalogTbl.CatalogCat.TableName + "." +
//                CatalogTbl.CatalogCat.compounds + " FROM " +
//                CatalogTbl.CatalogCat.TableName + " WHERE " +
//                CatalogTbl.CatalogCat.TableName + "." +
//                CatalogTbl.CatalogCat.code + "=" + codeProd
//                , null);
//        if (prodCompound.getCount()>0) {
//            prodCompound.moveToFirst();
//            if (!prodCompound.isNull(0)) {
//                if (prodCompound.getString(0).split(";").length > 1) {
//                    String cycle = prodCompound.getString(0).split(";")[1];
//                    String[] compound = prodCompound.getString(0).split(";")[0].split(",");
//
//
//                    ArrayList<NavItemsSpinner> compoundLst = new ArrayList<>();
//                    for (int comppos = 0; comppos < compound.length; comppos++) {
//                        String sqlStr = "SELECT " +
//                                CatalogTbl.CatalogCat.TableName + "." +
//                                CatalogTbl.CatalogCat.nameprod + "," +
//                                CatalogTbl.CatalogCat.TableName + "." +
//                                CatalogTbl.CatalogCat.code + " " +
//                                " FROM " +
//                                CatalogTbl.CatalogCat.TableName + "," +
//                                CompanionTbl.CompanionCat.TableName + " WHERE " +
//                                CatalogTbl.CatalogCat.TableName + "." + CatalogTbl.CatalogCat.code + "=" + compound[comppos];
//                        Cursor companionCur = this.db.rawQuery(sqlStr, null);
//                        companionCur.moveToFirst();
//                        NavItemsSpinner companion = new NavItemsSpinner();
//                        String name = companionCur.getString(0);
//                        companion.setName(name);
//                        String ID = companionCur.getString(1);
//                        companion.setID(ID);
//                        companion.setCycle(Integer.parseInt(cycle));
//                        compoundLst.add(companion);
//                        companionCur.moveToNext();
//                    }
//                    if (compoundLst.size() > 0) {
//                        this.adapters.put("compounds", new compound_adapter(context, compoundLst));
//                    }
//                }
//            }
//        }
//        prodCompound.close();
//    }
//
//    public void getPortions(){
//        Cursor pricursor = db.query(
//                PriceTbl.PriceCat.TableName,  // The table to query
//                new String[]{PriceTbl.PriceCat.price,
//                        PriceTbl.PriceCat.product,
//                        PriceTbl.PriceCat.comment},                               // The columns to return
//                PriceTbl.PriceCat.product + "=" + this.codeProd,// The columns for the WHERE clause
//                null,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                PriceTbl.PriceCat.price + " ASC"                                 // The sort order
//        );
//
//        ArrayList<NavItemsSpinner> portionLst = new ArrayList<>();
//        pricursor.moveToFirst();
//
//        for (int pripos = 0; pripos < pricursor.getCount(); pripos++) {
//            NavItemsSpinner portion = new NavItemsSpinner();
//            String raw_price=pricursor.getString(0).toString();
//            String price="";
//            if (raw_price.equals("")){
//                price="0.00";
//            }else {
//                price=new general().getDecimalForm(Double.parseDouble(raw_price));
//            }
//            String portion_name = pricursor.getString(2);
//            portion.setID(price);
//            portion.setName(portion_name + ": RD$ " + price);
//            pricursor.moveToNext();
//            portionLst.add(portion);
//        }
//        if (portionLst.size() > 0) {
//            this.adapters.put("portions", new spinnerAdapter(context, portionLst));
//        }
//
//        pricursor.close();
//    }
//
//    public void getCompanions(){
//
//        String sqlStr="SELECT "+CatalogTbl.CatalogCat.TableName+"."+
//                CatalogTbl.CatalogCat.nameprod+","+
//                CatalogTbl.CatalogCat.TableName+"."+
//                CatalogTbl.CatalogCat.code+", "+
//                CompanionTbl.CompanionCat.TableName+"."+
//                CompanionTbl.CompanionCat.cycle+", "+
//                PriceTbl.PriceCat.TableName+"."+
//                PriceTbl.PriceCat.price+" FROM "+
//                CatalogTbl.CatalogCat.TableName+","+
//                PriceTbl.PriceCat.TableName+","+
//                CompanionTbl.CompanionCat.TableName+" WHERE "+
//                CatalogTbl.CatalogCat.TableName+"."+CatalogTbl.CatalogCat.code+"="+
//                CompanionTbl.CompanionCat.TableName+"."+CompanionTbl.CompanionCat.companion+" AND "+
//                PriceTbl.PriceCat.TableName+"."+PriceTbl.PriceCat.product+"="+
//                CatalogTbl.CatalogCat.TableName+"."+CatalogTbl.CatalogCat.code+" AND "+
//                CompanionTbl.CompanionCat.TableName+"."+CompanionTbl.CompanionCat.product+"="+codeProd;
//
//        Cursor companionCur = this.db.rawQuery(sqlStr , null);
//
//        ArrayList<NavItemsSpinner> compLst = new ArrayList<>();
//        companionCur.moveToFirst();
//        for(int comppos = 0; comppos< companionCur.getCount(); comppos++) {
//
//            NavItemsSpinner companion = new NavItemsSpinner();
//            String name = companionCur.getString(0);
//            if (!companionCur.isNull(3)){
//                if (!companionCur.getString(3).toString().equals("")) {
//                    name += "- " + context.getResources().getString(R.string.extra_cost) + ":" + companionCur.getString(3);
//                }else{
//                    name += "- "+context.getResources().getString(R.string.extra_cost)+":0.00";
//                }
//            }else{
//                name += "- "+context.getResources().getString(R.string.extra_cost)+":0.00";
//            }
//            companion.setName(name);
//            String ID = companionCur.getString(1);
//            companion.setID(ID);
//
//            compLst.add(companion);
//
//            companionCur.moveToNext();
//
//        }
//        if (compLst.size()>0){
//            this.adapters.put("companions", new spinnerAdapter(context, compLst));
//        }
//        companionCur.close();
//    }
//
//    public void getTerms(){
//        HashMap<Integer, String> terms=new HashMap<>();
//        terms.put(1,"Vuelta y vuelta");
//        terms.put(2,"Rojo o ingles");
//        terms.put(3,"Termino medio");
//        terms.put(4, "Tres cuartos");
//        terms.put(5, "Bien cocido");
//        terms.put(6, "Frozen");
//        terms.put(7, "Normal");
//
//        Cursor cursor = db.query(
//                CatalogTbl.CatalogCat.TableName,  // The table to query
//                new String[]{CatalogTbl.CatalogCat.terms},                          // The columns to return
//                CatalogTbl.CatalogCat.code + "=" + this.codeProd,// The columns for the WHERE clause
//                null,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                ""                                 // The sort order
//        );
//        if (cursor.getCount()>0) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0)) {
//                String conditions = cursor.getString(0);
//                ArrayList<NavItemsSpinner> termLst = new ArrayList<>();
//                String[] condLst = conditions.split(",");
//
//                for (int cont = 0; cont < condLst.length; cont++) {
//                    if (!condLst[cont].equals("")) {
//                        int condition = Integer.parseInt(condLst[cont]);
//                        NavItemsSpinner termsItem = new NavItemsSpinner();
//                        termsItem.setID(Integer.toString(condition));
//                        termsItem.setName(terms.get(condition));
//                        termLst.add(termsItem);
//                    }
//                }
//                this.adapters.put("terms", new spinnerAdapter(context, termLst));
//            }
//        }
//    }
}
