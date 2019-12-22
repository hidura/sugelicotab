package sugelico.postabsugelico.General;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sugelico.postabsugelico.General.Definitions.ClientStruct;
import sugelico.postabsugelico.General.db.Client;
import sugelico.postabsugelico.General.db.ClientHelper;
import sugelico.postabsugelico.R;


/**
 * Created by dhidalgo on 9/1/2015.
 */
public class clientConfig {
    private SQLiteDatabase dbConn;
    private Context context;
    public clientConfig(SQLiteDatabase db, Context context){
        this.dbConn=db;
        this.context = context;
    }

    public void add(JSONArray clientLst) throws JSONException {

        for (int cont = 0; cont<clientLst.length(); cont++) {

            //Saving all the information that i need to construct the cart orders later.

            JSONObject piece = (JSONObject) clientLst.get(cont);
            boolean clientStatus = checkClient(piece.getInt("code"));
            if (!clientStatus) {

                ContentValues clientVal = new ContentValues();
                clientVal.put(Client.ClientCat.code, piece.getString("code"));
                clientVal.put(Client.ClientCat.cl_name, piece.getString("name"));
                clientVal.put(Client.ClientCat.table, piece.getString("table"));

                // setting the nav drawer list adapter
                long rowID = dbConn.insert(Client.ClientCat.TableName, null, clientVal);
            }
        }
        dbConn.close();
    }

    public boolean checkClient(int code){

        Cursor clientCur = dbConn.query(Client.ClientCat.TableName,
                new String[]{Client.ClientCat.cl_name},
                Client.ClientCat.code+"="+code,
                null,
                null,
                null,
                ""
        );
        int clientAmount = clientCur.getCount();

        if (clientAmount>0) {
            clientCur.moveToFirst();
            int id = clientCur.getInt(0);
            clientCur.close();
            return true;
        }

        return false;
    }

    public ClientStruct getClientInfo(int code){

        ClientStruct clientInfo = new ClientStruct();
        if (code == 1){
            /*
            This code is not on the database.
            indicate that's for everyone on the table.
             */
            clientInfo.setName(this.context.getResources().getString(R.string.accept));
            return clientInfo;
        }
        if (!dbConn.isOpen()){
            dbConn= context.openOrCreateDatabase(ClientHelper.DATABASE_NAME
                    , 0, null);
        }
        Cursor cursor = dbConn.query(
                Client.ClientCat.TableName,  // The table to query
                new String[]{Client.ClientCat.cl_name, Client.ClientCat.last_name,
                        Client.ClientCat.telephone, Client.ClientCat.rnc},                               // The columns to return
                Client.ClientCat.code+"="+code,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ""                                 // The sort order
        );
        cursor.moveToFirst();
        for (int cont=0; cont<cursor.getCount(); cont++){
            String name = cursor.getString(0);
            if (!cursor.isNull(3)) {
                clientInfo.setRnc(cursor.getString(3));
            }else{
                clientInfo.setRnc("None");
            }
            if (!cursor.isNull(2)){
                clientInfo.setExtra(cursor.getString(3));
            }else{
                clientInfo.setExtra("");
            }
            clientInfo.setName(name);
            clientInfo.setCode(cursor.getInt(1));
        }
        cursor.close();
        dbConn.close();
        return clientInfo;
    }

}


