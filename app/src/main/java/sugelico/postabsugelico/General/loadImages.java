package sugelico.postabsugelico.General;

import android.os.AsyncTask;


import java.util.HashMap;

/**
 * Created by hidura on 6/18/2016.
 */
public class loadImages extends AsyncTask<HashMap<String, Object>, String, String> {
    @Override
    protected String doInBackground(HashMap<String, Object>... params) {
        return null;
    }


//    private String iconPath=null;
//    private ImageView imgIcon;
//    private HashMap<String, Object> params;
//    private ImageLoader imageLoader;
//    @Override
//    protected String doInBackground(HashMap<String, Object>... params) {
//        this.params = (HashMap<String, Object>)params[0];
//        this.iconPath = (String)this.params.get("path");
//        this.imgIcon = (ImageView) this.params.get("icon");
//        Context context = (Context) this.params.get("context");
//        this.imageLoader = (ImageLoader)this.params.get("imageloader");
//
//        String imgPath = Environment.getExternalStorageDirectory().getPath() + "/alacarta/" + iconPath;
//
//        if (new File(imgPath).exists()) {
//            imageLoader.displayImage("file:///" + imgPath, (ImageView) imgIcon);
//        } else {
//            imageLoader.displayImage(iconPath, (ImageView) imgIcon);
//            ArrayList<Object> newparams = new ArrayList<Object>();//params for the webpage.
//
//            newparams.add("alacarta");//folder name
//            newparams.add(iconPath);//file name
//            newparams.add("");//file name
//            if (new general().isNetworkAvailable(context)) {
//                //new downloadTask().execute(newparams);
//            } else {
//                Toast.makeText(context, "No hay conexión disponible, favor conectarse a una red", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return null;
//    }
//    @Override
//    protected void onPostExecute(String result) {
//
//    }
}
