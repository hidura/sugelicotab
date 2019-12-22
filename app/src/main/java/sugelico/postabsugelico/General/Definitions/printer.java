package sugelico.postabsugelico.General.Definitions;


/**
 * Created by diegohidalgo on 3/11/17.
 */

public class printer implements Find, Structures, java.io.Serializable{
    private String path;
    private String server;
    private String username;
    private String password;
    private String printer_name;
    private String program;
    private Integer type;
    private String categories;

    public String getPath(){return path;}
    public String getPrinter_name(){return printer_name;}
    public String getServer(){return server;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getProgram(){return program;}
    public Integer getType(){return type;}
    public String getCategories(){return categories;}

    public void setPath(String path){this.path=path;}
    public void setProgram(String program){this.program=program;}
    public void setPrinter_name(String printer_name){this.printer_name=printer_name;}
    public void setServer(String server){this.server=server;}
    public void setUsername(String username){this.username=username;}
    public void setPassword(String password){this.password=password;}
    public void setType(Integer type){this.type=type;}
    public void setCategories(String categories){this.categories=categories;}


    @Override
    public Boolean contains(Object value) {
        if (value.equals(printer_name)){
            return true;
        }

        return null;
    }

    @Override
    public Object find(Object value) {
        /*
            This method is here just
            for the categories field.
            Because to make a better implementation of that list I
            Use the add and delete from the interface Structures.
         */
        return null;
    }

    @Override
    public Object add(Object object) {
        return null;
    }

    @Override
    public Object delete(Object object) {
        return null;
    }
}
