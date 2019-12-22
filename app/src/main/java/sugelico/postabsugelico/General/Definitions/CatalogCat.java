package sugelico.postabsugelico.General.Definitions;

import java.util.HashMap;


/**
 * Created by hidura on 6/18/2016.
 */
public class CatalogCat implements java.io.Serializable{
    private String title;
    private String icon;
    private HashMap<Integer,Products> products;
    private Integer code;
    private String printer;
    // boolean to set visiblity of the counter

    public CatalogCat(){}


    public String getTitle(){
        return this.title;
    }
    public String getPrinter(){
        return this.printer;
    }

    public HashMap<Integer,Products> getProducts(){
        return this.products;
    }


    public String getIcon(){
        return this.icon;
    }

    public Integer getCode(){
        return this.code;
    }


    public void setTitle(String title){
        this.title = title;
    }
    public void setPrinter(String printer){
        this.printer = printer;
    }

    public void setProducts(HashMap<Integer,Products> products){
        this.products=products;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public void setCode(Integer code){
        this.code = code;
    }

}
