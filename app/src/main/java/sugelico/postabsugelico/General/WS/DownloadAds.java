package sugelico.postabsugelico.General.WS;

import android.os.AsyncTask;


import java.util.HashMap;


/**
 * Created by hidura on 11/8/2015.
 */
public class DownloadAds extends AsyncTask<HashMap<String, Object>, String, String>

{
    @Override
    protected String doInBackground(HashMap<String, Object>... params) {
        return null;
    }
//    private Context context;
//    private Integer type;
//    private Integer product;
//    private Integer suggested;
//    private RelativeLayout window;
//    private ProgressDialog dialog;
//
//    private CountDownTimer timer;
//    @Override
//    protected String doInBackground(HashMap<String, Object>... sUrl) {
//
//        context = (Context)sUrl[0].get("context");
//
//        dialog = (ProgressDialog) sUrl[0].get("dialog");
//        type = (Integer)  sUrl[0].get("type");
//        product = (Integer)  sUrl[0].get("product");
//        suggested = (Integer)  sUrl[0].get("suggested");
//        window = (RelativeLayout)sUrl[0].get("window");
//        InputStream input = null;
//        OutputStream output = null;
//        HttpURLConnection connection = null;
//
//
//        String urlPath = new connection().NameSpace()+"/?code=getPromoForMobile&company="+new cmpDetails().getCode()+"&promo_type="+type;
//        if (product>0){
//            urlPath+="&product="+product;
//        }else{
//            Calendar now = Calendar.getInstance();
//            urlPath+="&date="+now.get(Calendar.YEAR)+"/"+
//                    (now.get(Calendar.MONTH)+1)+"/"+
//            now.get(Calendar.DAY_OF_MONTH);
//        }
//        try {
//            URL url = new URL(urlPath);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(2000);
//            connection.connect();
//            connection.setReadTimeout(2000);
//            // expect HTTP 200 OK, so we don't mistakenly save error report
//            // instead of the file
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                return "Server returned HTTP " + connection.getResponseCode()
//                        + " " + connection.getResponseMessage();
//            }
//
//            // this will be useful to display download percentage
//            // might be -1: server did not report the length
//            int fileLength = connection.getContentLength();
//
//            // download the file
//            input = connection.getInputStream();
//            BufferedReader br = null;
//            StringBuilder sb = new StringBuilder();
//
//            String line;
//
//            br = new BufferedReader(new InputStreamReader(input));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            goMenu();
//            return e.toString();
//
//        } finally {
//
//            try {
//                if (output != null)
//                    output.close();
//                if (input != null)
//                    input.close();
//            } catch (IOException ignored) {
//            }
//
//            if (connection != null)
//                connection.disconnect();
//        }
//
//    }
//
//    protected void onPostExecute(String adList) {
//        //Toast.makeText(context, adList, Toast.LENGTH_LONG).show();
//        try {
//            SQLiteDatabase db = context.
//                    openOrCreateDatabase(CatalogHelper.DATABASE_NAME, 0, null);
//
////            ((Button) window.findViewById(R.id.skip)).setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    //Creating tables if there's one that have been created.
////                    goMenu();
////                }
////            });
//            JSONObject piece = new JSONObject(adList);
//            ImageView imgIcon = (ImageView) window.findViewById(R.id.adImage);
//            String path_img=piece.getString("path");
//            String imgPath = Environment.getExternalStorageDirectory().getPath()+"/alacarta/" +path_img;
//            File img = new File(imgPath);
//            imgIcon.setVisibility(View.VISIBLE);
//            //Setting up the orientation by default is horizontal
//
////            if (piece.getInt("orientation") == 2){
////                //This means that the orientation of the picture is portait.
////
////            }
////            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
////            imgIcon.setLayoutParams(layoutParams);
//            if (!piece.getString("message").equals("null"))
//                ((TextView) window.findViewById(R.id.message)).setText(piece.getString("message"));
//            if (img.exists()) {//If the file exist then show the file on the cellphone
//                Picasso.with(context).load(img).into(imgIcon);
//            } else {//Else download the image and read the remote image.
//                ArrayList<Object> params = new ArrayList<Object>();//params for the webpage.
//                params.add("alacarta/");//folder name
//                params.add(piece.getString("path"));//file name
//
//                String urlPath = "";
//                if (product > 0){
//                    urlPath = new connection().NameSPaceWS() + "/resources/ads/" + new cmpDetails().getCode()+"/";
//                    params.add("/resources/ads/");
//                }else {
//                    urlPath = new connection().NameSPaceWS() + "/resources/ads/" + new cmpDetails().getCode()+"/";
//                    params.add("/resources/ads/");
//                }
//                urlPath+=piece.getString("path");
//
//                Picasso.with(context).load(urlPath).into(imgIcon);
//
//                if (new general().isNetworkAvailable(context)){
//                    new downloadTask().execute(params);
//                }else{
//                    Toast.makeText(context,
//                            "No hay conexión disponible, favor conectarse a una red", Toast.LENGTH_SHORT).show();
//                }
//            }
//            Integer duration = (piece.getInt("duration")+1);
//            if (duration>0) {
//                timer=new CountDownTimer(duration*1000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                        ((TextView) window.findViewById(R.id.timer)).setText("" + millisUntilFinished / 1000);
//                    }
//
//                    public void onFinish() {
//                        ((TextView) window.findViewById(R.id.timer)).setText("0");
//                        goMenu();
//                    }
//                }.start();
//            }
//            if (!piece.getString("product").equals("null")){
//                ((LinearLayout) window.findViewById(R.id.product_short)).setVisibility(View.VISIBLE);
//                ((Button) window.findViewById(R.id.buy)).setContentDescription(piece.getString("product"));
//                ((Button) window.findViewById(R.id.buy)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (timer != null){
//                            timer.cancel();
//                        }
////                        Intent intent = new Intent(context, Product.class);
////                        intent.putExtra("product", v.getContentDescription());
////                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        context.startActivity(intent);
//
//                        HashMap<String, Object> params = new HashMap<String, Object>();
//                        AlertDialog.Builder builder = new AlertDialog.Builder((Activity)context);
//
////                        CardView parent = new CardView(v.getContext());
////                        params.put("code", v.getContentDescription().toString());
////                        params.put("name", new general().getProductName(context, Integer.parseInt(v.getContentDescription().toString())).get("name"));
////                        params.put("builder", builder);
////                        new productHandle().productHandle(parent, params);
//
//                    }
//                });
//            }
//
//
////                ContentValues values = new ContentValues();
////
////                int promoCode = piece.getInt("code");
////                boolean prod_exist=new general().Exist(db, Ad.AdCat.TableName, (Ad.AdCat.code + "=" + promoCode));
////
////                if (!prod_exist) {
////                    Integer id = new general().getID(db, Ad.AdCat.TableName);
////                    values.put(Ad.AdCat.KeyID, id);
////                    values.put(Ad.AdCat.code, piece.getInt("code"));
////                    values.put(Ad.AdCat.product, piece.getString("product"));
////                    values.put(Ad.AdCat.duration, piece.getString("duration"));
////                    values.put(Ad.AdCat.path, piece.getString("path"));
////                    values.put(Ad.AdCat.message, piece.getString("message"));
////                    values.put(Ad.AdCat.adsize, piece.getString("filesize"));
////                    values.put(Ad.AdCat.filetype, piece.getString("filetype"));
////                    long newRowId = db.insert(Ad.AdCat.TableName, null, values);
////                }
//
//            db.close();
//
//
//        }catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println(adList);
//            goMenu();
//        }
//
//
//        dialog.dismiss();
//
//
//    }
//    public void goMenu(){
//        SQLiteDatabase db = context.openOrCreateDatabase(BillHelper.DATABASE_NAME, 0, null);
//        new BillHelper(context).onCreate(db);
//        new CatalogHelper(context).onCreate(db);
//        new CategoriesHelper(context).onCreate(db);
//        new ClientHelper(context).onCreate(db);
//        new CompanionHelper(context).onCreate(db);
//        new ImagesHelper(context).onCreate(db);
//        new OptionalsHelper(context).onCreate(db);
//        new PriceHelper(context).onCreate(db);
//        db.close();
//        if (timer != null){
//            timer.cancel();
//        }
//        Intent intent = new Intent(context, Catalog.class);
//        if (suggested>0)
//            intent.putExtra("SuggestBase", suggested);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
}
