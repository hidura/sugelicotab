package sugelico.postabsugelico.General.WS;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.general;
import sugelico.postabsugelico.R;

/**
 * Created by hidura on 3/5/2017.
 */

public class printerTask extends AsyncTask<Void, HashMap<String, Object>, String> {
    HashMap<String, Object> args=new HashMap<>();
    private FragmentActivity activity;
    private cmpDetails companyprofile;
    private String header;
    private UserProfile profile;
    private JSONArray printer_data;
    private HashMap<String, Double> paytype;
    private String footer;
    private String doc_type;
    private printer target_printer;
    private LinearLayout wait_layout;
    private Button btn_target;
    private int copy=0;
    public printerTask(HashMap<String, Object> param) {
        args=param;
        //activity=(FragmentActivity)param.get("activity");
        printer_data=new JSONArray();
        HashMap<Integer, CartProds> products_lst=((HashMap<Integer, CartProds>) param.get("products"));
        profile = (UserProfile)param.get("profile");
        companyprofile = (cmpDetails) param.get("companyprofile");
        target_printer=(printer)param.get("target_printer");

        if (param.containsKey("paytype")){
            paytype = (HashMap<String, Double>)param.get("paytype");
        }

        String client=param.get("client_name").toString();
        String table_info=param.get("table_info").toString();
        Object[] iterator = products_lst.keySet().toArray();
        String doc_title="Factura";
        doc_type=doc_title;
        if (args.containsKey("title")){
            doc_title=args.get("title").toString();
        }
        String ncf =null;
        String ncf_title=null;

        String ncf_exp=null;
        if (args.containsKey("ncf")){
            ncf=args.get("ncf").toString();
            ncf_title=args.get("ncf_title").toString();
            ncf_exp=args.get("ncf_exp").toString();
        }
        String rnc=null;
        if (args.containsKey("rnc"))
            rnc=args.get("rnc").toString();

        ArrayList<Integer> orderprints= new ArrayList<Integer>();
        wait_layout =(LinearLayout)param.get("wait_layout");
        btn_target=(Button)param.get("btn_data");
        double total=0.00;
        double tax=0.00;
        double prop_extra=0.00;
        String extra_label="Propina";

        double discount=0.00;
        double subtotal=0.00;
        boolean bill=false;

        copy=profile.getBillCopies();
        if (param.containsKey("copies"))
            copy = Integer.parseInt(param.get("copies").toString());
        try {
            printer_data.put(new JSONObject().put("text",companyprofile.getName()).put("type","B").put("width","2").put("align","CENTER").put("height","2"));
            printer_data.put(new JSONObject().put("text",companyprofile.getRNC()).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text",companyprofile.getTelephone()).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text",companyprofile.getAddress()).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text",table_info));
            printer_data.put(new JSONObject().put("text"," "));
            long timeInMillis = System.currentTimeMillis();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTimeInMillis(timeInMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy hh:mm:ss a");
            String dateforrow = dateFormat.format(cal1.getTime());
            printer_data.put(new JSONObject().put("text","Fecha: "+dateforrow.toString().split(" ")[0]
                    +"          HORA: "+dateforrow.toString().split(" ")[1]));
            printer_data.put(new JSONObject().put("text"," "));

            printer_data.put(new JSONObject().put("text","DATOS DEL CLIENTE"));
            if (profile.getBillType()==102){
                printer_data.put(new JSONObject().put("text","CLIENTE: "+profile.getClient().getName()).put("type", "B"));
                printer_data.put(new JSONObject().put("text"," "));
                printer_data.put(new JSONObject().put("text",profile.getClient().getExtra()));
                printer_data.put(new JSONObject().put("text"," "));

            }else {
                printer_data.put(new JSONObject().put("text","CLIENTE: "+client).put("type", "B"));
                printer_data.put(new JSONObject().put("text"," "));
            }
            if(ncf!=null){
                printer_data.put(new JSONObject().put("text","NCF:"+ncf));
                printer_data.put(new JSONObject().put("text","NCF Expira:"+ncf_exp));
            }
            if(rnc!=null){
                printer_data.put(new JSONObject().put("text","RNC:"+rnc));
            }

            printer_data.put(new JSONObject().put("text","----------------------------------").put("align", "CENTER"));
            if (ncf_title!=null){
                printer_data.put(new JSONObject().put("text",ncf_title).put("align","CENTER"));
            }else {
                printer_data.put(new JSONObject().put("text",doc_title).put("align","CENTER"));
            }
            printer_data.put(new JSONObject().put("text","----------------------------------").put("align", "CENTER"));
            String acc_header="CNT         NOMBRE"+String.format("%1$"+17+ "s", " ")+"PRECIO";
            printer_data.put(new JSONObject().put("text",acc_header).put("align", "LEFT"));

            for (int cont = 0; cont<iterator.length; cont++){
                Long position=Long.parseLong(iterator[cont].toString());
                CartProds cart_prod=products_lst.get(position);
                if (cart_prod!=null) {
                    boolean state = cart_prod.getSaveit();
                    if (cart_prod.getClient().toLowerCase().equals(client.toLowerCase())) {

                        double tax_disc=0.00;
                        if (cart_prod.getDiscount()>0.00){
                            tax_disc=cart_prod.getDiscount();
                            discount+=cart_prod.getDiscount();
                        }
                        subtotal+=cart_prod.getSubtotal();
                        tax+=cart_prod.getTax();
                        total+=cart_prod.getPrice();
                        String plate_info=cart_prod.getAmount()+
                                "x"+NumberFormat.getNumberInstance(Locale.US).format(cart_prod.getSubtotal());
                        if (cart_prod.getDiscount()>0.00){
                            String final_price_total=NumberFormat.getNumberInstance(Locale.US).
                                    format((cart_prod.getDiscount()*cart_prod.getAmount()));
                            printer_data.put(new JSONObject().put("text", "-DESC "+cart_prod.getAmount()+
                                    "x"+NumberFormat.getNumberInstance(Locale.US).format(cart_prod.getDiscount())).
                                    put("width","1").put("height","1"));
                            if (cart_prod.getTitle().length()>=20){
                                String prod_name__=cart_prod.getTitle().substring(0,20);
                                printer_data.put(new JSONObject().put("text",prod_name__+
                                        StringUtils.leftPad("-"+final_price_total,18)).
                                        put("align","LEFT").put("width","1").put("height","1").put("type","B"));
                            }else {

                                int remain = 25-cart_prod.getTitle().length();
                                printer_data.put(new JSONObject().put("text",StringUtils.rightPad(cart_prod.getTitle(),20," ")+
                                        StringUtils.leftPad("-"+final_price_total,18)).
                                        put("align","LEFT").put("width","1").put("height","1").put("type","B"));
                            }
                        }
                        printer_data.put(new JSONObject().put("text",plate_info).
                                put("width","1").put("height","1"));
                        int maxString = 30;
                        String ___prod_name__=cart_prod.getTitle();
                        if (cart_prod.getTitle().length()>=20){
                            ___prod_name__=cart_prod.getTitle().substring(0,20);
                            printer_data.put(new JSONObject().put("text",___prod_name__+
                                    StringUtils.leftPad(NumberFormat.getNumberInstance(Locale.US).format(cart_prod.getSubtotal()),18)).
                                    put("align","LEFT").put("width","1").put("height","1"));
                        }else {
                            int remain = 25-cart_prod.getTitle().length();
                            printer_data.put(new JSONObject().put("text",StringUtils.rightPad(cart_prod.getTitle(),20," ")+
                                    StringUtils.leftPad(NumberFormat.getNumberInstance(Locale.US).format(cart_prod.getSubtotal()),18)).
                                    put("align","LEFT").put("width","1").put("height","1"));
                        }
                    }
                }
            }
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text","SUB-TOTAL: "+
                    NumberFormat.getNumberInstance(Locale.US).format(new general().numberFixed(subtotal, 2) )).put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));
            if (tax>0) {
                printer_data.put(new JSONObject().put("text", "IMPUESTO: " +
                        NumberFormat.getNumberInstance(Locale.US).
                                format(new general().numberFixed(tax,2))).
                        put("align", "RIGHT").
                        put("width", "1").put("height", "2").put("type", "B"));
            }
            if (companyprofile.getBilltypes()!=null) {
                if (param.containsKey("billtp_extra")) {
                    prop_extra = Double.parseDouble(param.get("billtp_extra").toString());
                    printer_data.put(new JSONObject().put("text", extra_label+": " +
                            NumberFormat.getNumberInstance(Locale.US).
                                    format(new general().numberFixed(prop_extra,2)))
                            .put("align", "RIGHT").
                                    put("width", "1").put("height", "2").put("type", "B"));
                    total+=+prop_extra;

                }else{
                    for (Object billtype :
                            companyprofile.getBilltypes()) {
                        if (((HashMap)((HashMap) billtype).get("billdetails")).containsKey("percent_extra")
                                && Integer.parseInt(((HashMap) billtype).get("code").toString())==profile.getBillType() ){
                            Double percentExtra=Double.parseDouble(((HashMap)((HashMap) billtype).
                                    get("billdetails")).get("percent_extra").toString());
                            if (percentExtra>0) {
                                Double propTotal=subtotal*(percentExtra/100);
                                printer_data.put(new JSONObject().put("text",
                                        extra_label + ": " +
                                                NumberFormat.getNumberInstance(Locale.US).
                                                        format(new general().numberFixed(propTotal,2))).
                                        put("align", "RIGHT").
                                        put("width", "1").
                                        put("height", "2").put("type", "B"));
                                total += +propTotal;
                            }
                            break;
                        }

                    }
                }
            }
            printer_data.put(new JSONObject().put("text","DESCUENTO: "+
                    NumberFormat.getNumberInstance(Locale.US).
                            format(new general().numberFixed(discount,2))).
                    put("align","RIGHT").
                    put("width","1").put("height","2").
                    put("type","B"));
            printer_data.put(new JSONObject().put("text","TOTAL: "+NumberFormat.
                    getNumberInstance(Locale.US).format(new general().numberFixed(total,2))).
                    put("align","RIGHT").
                    put("width","1").
                    put("height","2").
                    put("type","B"));

            printer_data.put(new JSONObject().put("text"," ").
                    put("align","RIGHT").
                    put("width","1").put("height","2").
                    put("type","B"));

            if (profile.getBillType()==122){
                printer_data.put(new JSONObject().
                        put("text"," CLIENTE:_______________________________________").
                        put("align","RIGHT").
                        put("width","1").put("height","2").
                        put("type","B"));
                printer_data.put(new JSONObject().put("text"," ").
                        put("align","RIGHT").
                        put("width","1").put("height","2").put("type","B"));
                printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                        put("width","1").put("height","2").put("type","B"));

                printer_data.put(new JSONObject().put("text"," CAJERO:________________________________________").put("align","RIGHT").
                        put("width","1").put("height","2").put("type","B"));
                printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                        put("width","1").put("height","2").put("type","B"));
                printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                        put("width","1").put("height","2").put("type","B"));

            }else{
                if (paytype!=null){
                    for (int ptpcont=0; ptpcont<paytype.keySet().size(); ptpcont++){

                        printer_data.put(new JSONObject().
                                put("text",
                                        paytype.keySet().toArray()[ptpcont]+
                                                ":"+NumberFormat.getNumberInstance(Locale.US).
                                                format(new general().numberFixed(paytype.get(paytype.keySet().toArray()[ptpcont]), 2))).
                                put("align","RIGHT").
                                put("width","1").
                                put("height","2").
                                put("type","B"));
                    }
                }
            }

            printer_data.put(new JSONObject().put("text","MESERO:"+profile.getName()).put("align","RIGHT").
                    put("width","1").put("height","1"));

            printer_data.put(new JSONObject().put("bill"," "));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        //wait_layout.setVisibility(View.GONE);
        //btn_target.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {


        JSch jsch = new JSch();
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            Session session = jsch.getSession(target_printer.getUsername(), target_printer.getServer(), 22);
            session.setPassword(target_printer.getPassword());
            session.setConfig(config);
            String printer_path=""+target_printer.getPath()+" '"+target_printer.getPrinter_name()+"' ";

            String args ="";
            if (doc_type.equals("Factura")) {
                args = printer_path + "'0' " + "'" + printer_data.toString() + "' 'bill'";
            }else{
                ;
            }
            session.connect();

            String response="";
            Channel channel = session.openChannel("exec");
            for (int count =0; count <=copy; count ++) {
                String cmd = "echo '" + target_printer.getPassword() + "' | sudo -S python3 " + " " + args + "";

                // X Forwarding
                // channel.setXForwarding(true);


                ((ChannelExec) channel).setCommand(cmd);

                channel.setInputStream(null);

                ((ChannelExec) channel).setErrStream(System.err);
                InputStream in = channel.getInputStream();

                channel.connect();
                byte[] tmp = new byte[1024];
                int cont=0;
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0)
                            break;
                        response+=new String(tmp, 0, i);
                        System.out.print(new String(tmp, 0, i));
                    }
                    //channel.close();  // this closes the jsch channel
                    if (channel.isClosed() || cont>=10) {
                        if (in.available() > 0)
                            continue;
                        if (cont==10)
                            break;
                        response+="exit-status: " + channel.getExitStatus();
                        System.out.println("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        cont+=1;
                    } catch (Exception ee) {
                        response+=ee.getMessage();
                    }
                }


            }
            channel.disconnect();
            session.disconnect();
            return response;

        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            //Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            //Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
            return e.getMessage();
        }


    }

    protected void onPostExecute(String response) {
        //Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG).show();
        if (wait_layout!=null && btn_target!=null){
            wait_layout.setVisibility(View.GONE);
            btn_target.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCancelled() {

    }
}