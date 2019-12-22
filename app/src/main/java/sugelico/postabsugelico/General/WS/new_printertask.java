package sugelico.postabsugelico.General.WS;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.cmpDetails;
import sugelico.postabsugelico.General.Definitions.printer;
import sugelico.postabsugelico.R;
import sugelico.postabsugelico.Start;

/**
 * Created by diegohidalgo on 3/21/17.
 */

public class new_printertask extends AsyncTask<Void, HashMap<String, Object>, String> {
    HashMap<String, Object> args=new HashMap<>();
    private AppCompatActivity activity;
    private cmpDetails cmprofile;
    private JSONArray printerStr;
    private printer target_printer;
    private Integer preorder;
    private Integer status;
    public new_printertask(HashMap<String, Object> param) {
        args=param;
        activity=(AppCompatActivity)param.get("activity");
        printerStr=(JSONArray)param.get("lines");

        target_printer=(printer)param.get("printer");

        cmprofile=(cmpDetails)param.get("cmProfile");

        preorder=(Integer)param.get("preorder");
        status=0;

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
            String printer_path=""+target_printer.getPath()+" '"+target_printer.getPrinter_name()+"' '"+preorder+"'  ";



            String args=printer_path+"'"+printerStr.toString()+"'";
            System.out.println(args);
            // X Forwarding
            // channel.setXForwarding(true);

            session.connect();
            Channel channel= session.openChannel("exec");
            String cmd="echo '"+target_printer.getPassword()+"' | sudo -S python3 "+" "+args+"";
            ((ChannelExec) channel).setCommand(cmd);

            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();
            String response="";
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
                    status=channel.getExitStatus();
                    break;
                }
                try {
                    Thread.sleep(1000);
                    cont+=1;
                } catch (Exception ee) {
                    response+=ee.getMessage();
                    status=1;
                }
            }
            channel.disconnect();
            session.disconnect();
            return response;

        } catch (JSchException e) {
            e.printStackTrace();
            status=1;
            System.out.println(e.getMessage());
            //Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            status=1;
            System.out.println(e.getMessage());
            //Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
            return e.getMessage();
        }


    }

    protected void onPostExecute(String response) {

        if (status==0) {
            Toast.makeText(activity.getApplicationContext(),
                    activity.getResources().getString(R.string.send_success),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity.getApplicationContext(), Start.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("items", null);
            intent.putExtra("company_profile", cmprofile);
            intent.putExtra("usercode", cmprofile.getUserkey());
            activity.getApplicationContext().startActivity(intent);
        }else{
            Toast.makeText(activity.getApplicationContext(),
                    activity.getResources().getString(R.string.problem_with_comm),
                    Toast.LENGTH_LONG).show();
            Toast.makeText(activity,response,Toast.LENGTH_LONG).show();
            ((Button) activity.findViewById(R.id.send_products)).setVisibility(View.VISIBLE);
            ((LinearLayout)activity.findViewById(R.id.sendProdWait)).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCancelled() {

    }
}