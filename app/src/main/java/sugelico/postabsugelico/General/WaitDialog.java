package sugelico.postabsugelico.General;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by hidura on 9/26/2015.
 */
public class WaitDialog{
    private Activity activity;
    public WaitDialog(Activity activity) {

        this.activity=activity;
    }


    public ProgressDialog showDialog(String dialogTitle, String dialogMsg){
        if (dialogMsg==null)
            dialogMsg="Cargando, por favor espere...";
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.show(this.activity, dialogTitle,
                dialogMsg, true);
        return dialog;
    }

}
