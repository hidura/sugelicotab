package sugelico.postabsugelico.General.WS;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Iterator;
import java.util.Locale;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.General.general;

public class printReceiveTask  extends AsyncTask<Void, HashMap<String, Object>, String> {
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
    public printReceiveTask(HashMap<String, Object> param) {
        args=param;
        //activity=(FragmentActivity)param.get("activity");
        printer_data=new JSONArray();
        activity = (AppCompatActivity) param.get("activity");
        profile = (UserProfile)param.get("profile");
        companyprofile = (cmpDetails) param.get("companyprofile");
        target_printer=(printer)param.get("target_printer");



        Double totalPaid=Double.parseDouble(param.get("total_paid").toString());
        String doc_title="RECIBO DE PAGO";
        doc_type=doc_title;
        if (args.containsKey("title")){
            doc_title=args.get("title").toString();
        }

        wait_layout =(LinearLayout)param.get("wait_layout");
        btn_target=(Button)param.get("btn_data");


        copy=profile.getBillCopies();
        if (param.containsKey("copies"))
            copy = Integer.parseInt(param.get("copies").toString());
        try {
            printer_data.put(new JSONObject().put("text",companyprofile.getName()).put("type","B").put("width","2").put("align","CENTER").put("height","2"));
            printer_data.put(new JSONObject().put("text",companyprofile.getRNC()).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text",companyprofile.getTelephone()).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text",companyprofile.getAddress()).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text"," "));
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
            printer_data.put(new JSONObject().put("text","CLIENTE: "+profile.getClient().getName()).put("type", "B"));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text",profile.getClient().getExtra()));
            printer_data.put(new JSONObject().put("text"," "));

            printer_data.put(new JSONObject().put("text","----------------------------------").put("align", "CENTER"));
            printer_data.put(new JSONObject().put("text",doc_title).put("align","CENTER"));
            printer_data.put(new JSONObject().put("text","----------------------------------").put("align", "CENTER"));
            String acc_header="RECIBO         PAGADO"+String.format("%1$"+17+ "s", " ")+"RESTANTE";
            printer_data.put(new JSONObject().put("text",acc_header).put("align", "LEFT"));

            for (int cont = 0; cont<profile.getCxcbillpaid().names().length(); cont++){
                String key = profile.getCxcbillpaid().names().getString(cont);
                JSONObject bill=profile.getCxcbillpaid().getJSONObject(key);
                printer_data.put(new JSONObject().put("text",StringUtils.rightPad(key,14," ")+
                        StringUtils.leftPad(NumberFormat.getNumberInstance(Locale.US).format(bill.getDouble("total_paid")),14)+
                        StringUtils.leftPad(NumberFormat.getNumberInstance(Locale.US).format(bill.getDouble("bill_left")),18)).
                        put("align","LEFT").put("width","1").put("height","1"));
                JSONObject paytpjson=profile.getCxcbillpaid().getJSONObject(key).
                        getJSONObject("paytypelst");
                for(int paycount=0; paycount<paytpjson.names().length(); paycount++){
                    String paytype = paytpjson.names().getString(paycount);
                    if (!paytype.contains("|")) {
                        String ptname="";
                        String raw_ptname=profile.getCxcbillpaid().getJSONObject(key).
                                getJSONObject("paytypelst").getString(paytype + "|");
                        if (raw_ptname.length()>=15){
                            ptname=raw_ptname.substring(0,15);
                            Double paid = profile.getCxcbillpaid().getJSONObject(key).
                                    getJSONObject("paytypelst").getDouble(paytype);
                            printer_data.put(new JSONObject().put("text", StringUtils.
                                    rightPad(ptname, 15, " ") +
                                    StringUtils.leftPad(NumberFormat.
                                            getNumberInstance(Locale.US).format(paid), 28)).
                                    put("align", "LEFT").
                                    put("width", "1").
                                    put("height", "1"));
                        }else {
                            ptname=raw_ptname;
                            Double paid = profile.getCxcbillpaid().getJSONObject(key).
                                    getJSONObject("paytypelst").getDouble(paytype);
                            printer_data.put(new JSONObject().put("text", StringUtils.
                                    rightPad(ptname, 20, " ") +
                                    StringUtils.leftPad(NumberFormat.
                                            getNumberInstance(Locale.US).format(paid), 28)).
                                    put("align", "LEFT").
                                    put("width", "1").
                                    put("height", "1"));
                        }

                    }
                }
            }
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text"," "));
            printer_data.put(new JSONObject().put("text","Total Pagado: "+NumberFormat.
                    getNumberInstance(Locale.US).format(new general().numberFixed(totalPaid, 2))).
                    put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));



            printer_data.put(new JSONObject().put("text"," CLIENTE:_______________________________________").put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));
            printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));
            printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));

            printer_data.put(new JSONObject().put("text"," CAJERO:________________________________________").put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));
            printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));
            printer_data.put(new JSONObject().put("text"," ").put("align","RIGHT").
                    put("width","1").put("height","2").put("type","B"));

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

            String args=printer_path+"'"+printer_data.toString()+"'";
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
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return e.getMessage();
        }


    }

    protected void onPostExecute(String response) {
        HashMap<String, Object> params =new HashMap<>();
        params.put("activity", activity);
        profile.setDialog(null);
        profile.setCxcbillpaid(null);
        params.put("profile", profile);
        params.put("companyprofile", companyprofile);
        new LoadOrdersTask(params).execute();

    }

    @Override
    protected void onCancelled() {

    }
}
