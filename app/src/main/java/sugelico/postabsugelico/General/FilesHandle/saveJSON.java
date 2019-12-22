package sugelico.postabsugelico.General.FilesHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;

import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;

public class saveJSON implements java.io.Serializable{

    private Context parent;
    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Object outputObject;
    private String filePath;
    private UserProfile profile;
    private cmpDetails companyprofile;
    public saveJSON(Context c, UserProfile profile, cmpDetails companyprofile){
        this.profile=profile;
        this.companyprofile=companyprofile;
        parent = c;
    }

    public Object readObject(String fileName){
        try {
            filePath = parent.getApplicationContext().getFilesDir().getAbsolutePath()
                    + "/"+this.profile.getMainFolder()+"/" + fileName;
            fileIn = new FileInputStream(filePath);
            objectIn = new ObjectInputStream(fileIn);
            outputObject = objectIn.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputObject;
    }

    public void writeObject(Object inputObject, String fileName){
        try {
            File f = new File(parent.getApplicationContext().getFilesDir().getAbsolutePath()
                    + "/"+this.profile.getMainFolder());
            if(!f.isDirectory()){
                f.mkdir();
            }
            filePath = parent.getApplicationContext().getFilesDir().getAbsolutePath()
                    + "/"+this.profile.getMainFolder()+"/" + fileName;
            fileOut = new FileOutputStream(filePath);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(inputObject);
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}