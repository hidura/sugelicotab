package sugelico.postabsugelico.General.WS;

import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import sugelico.postabsugelico.General.connection;
import sugelico.postabsugelico.General.general;


/**
 * Created by hidura on 9/10/2015.
 */
public class PostConnection{
    private final String url =new connection().NameSpace();
    private final String port = "80";
    private HttpURLConnection connection;
    private HashMap params;
    private byte[] postData;

    public HttpURLConnection PostConnection(HashMap args, HashMap connProp, String path_url) throws IOException,MalformedURLException {
        /**
         * This class receive the params, and return a connection
         * open and ready.
         */
        params=args;

        URL url = new URL(path_url);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            connection.setRequestProperty("Connection", "close");
        }
        connection.setRequestProperty( "Accept-Encoding", "" );
        JSONObject retObject = new JSONObject(params);
        Log.e("productos enviados",retObject.toString());
        connection.setInstanceFollowRedirects(false);
        connection.setConnectTimeout(60000);
        connection.setFixedLengthStreamingMode(retObject.toString().getBytes("UTF-8").length);
        connection = new general().setProperties(connection, connProp);//Adding the properties to the connection.

        //postData = parseParam();

        //connection.setRequestProperty( "Content-Length", Integer.toString( retObject.toString().getBytes("UTF-8").length ));
        connection.setUseCaches(false);
        try{
            DataOutputStream wr = new DataOutputStream( connection.getOutputStream());
            wr.write( retObject.toString().getBytes("UTF-8") );
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        connection.connect();


        return connection;
    }


}
