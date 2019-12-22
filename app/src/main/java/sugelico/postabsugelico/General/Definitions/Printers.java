package sugelico.postabsugelico.General.Definitions;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by diegohidalgo on 3/11/17.
 */



public class Printers implements Structures,java.io.Serializable{
    public ArrayList<printer> printerLst;

    public ArrayList<printer> getPrinterLst(){return printerLst;}
    public void setPrinterLst(ArrayList<printer> printerLst){this.printerLst=printerLst;}


    @Override
    public printer find(Object value) {
        while (printerLst.iterator().hasNext()){
            printer cl_target=printerLst.iterator().next();
            if (cl_target.contains(value))
                return cl_target;
        }
        return null;
    }

    @Override
    public Object add(Object object) throws Exception {
        if (object  instanceof printer) {
            printerLst.add((printer) object);
            return null;
        }else {
            throw new Exception(new InputMismatchException("Solo puede ingresar ClientStruct a esta funcion."));
        }
    }

    @Override
    public Object delete(Object object)throws Exception {
        if (object  instanceof printer) {
            printerLst.remove((printer) object);
            return null;
        }else {
            throw new Exception(new InputMismatchException("Solo puede ingresar ClientStruct a esta funcion."));
        }
    }
}
