package sugelico.postabsugelico.General.Definitions;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by diegohidalgo on 3/11/17.
 */

public class Client implements Structures{
    public ArrayList<ClientStruct> clientslst;


    public ArrayList<ClientStruct> getClient(){return clientslst;}

    public void setClient(ArrayList<ClientStruct> client){this.clientslst=client;}


    @Override
    public ClientStruct find(Object value) {
        while (clientslst.iterator().hasNext()){
            ClientStruct cl_target=clientslst.iterator().next();
            if (cl_target.contains(value))
                return cl_target;
        }
        return null;
    }

    @Override
    public Object add(Object newClient) throws Exception {
        if (newClient  instanceof ClientStruct) {
            clientslst.add((ClientStruct) newClient);
            return null;
        }else {
            throw new Exception(new InputMismatchException("Solo puede ingresar ClientStruct a esta funcion."));
        }
    }

    @Override
    public Object delete(Object newClient) throws Exception {
        if (newClient  instanceof ClientStruct) {
            clientslst.remove((ClientStruct) newClient);
            return null;
        }else {
            throw new Exception(new InputMismatchException("Solo puede ingresar ClientStruct a esta funcion."));
        }
    }



}
