package sugelico.postabsugelico.General.Definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by hidura on 10/23/2015.
 */
public class cmpDetails implements java.io.Serializable,Structures{
    private int code;
    private String name;
    private String path;
    private String altpath;
    private String localpath;
    private String email;
    private String telephone;
    private String rnc;
    private String address;
    private String token;
    private Double credit;
    private Integer Status;
    private ArrayList<printer> printers;
    private HashMap<Integer, Products> products;
    private ArrayList<Integer> areas;
    private List<Object> billtypes;
    private List<Object> paytypes;
    private HashMap<Integer, CatalogCat> categories;
    private Integer companytp;
    private String userkey;
    public cmpDetails(){

    }


    public void setName(String name){this.name=name;}
    public void setCode(Integer code){this.code=code;}
    public void setPath(String path){this.path=path;}

    public void setAreas(ArrayList<Integer> areas) {
        this.areas = areas;
    }

    public void setEmail(String email){this.email=email;}
    public void setAltpath(String altpath){this.altpath=altpath;}
    public void setToken(String token){this.token=token;}
    public void setBilltypes(List<Object> billtypes){this.billtypes=billtypes;}
    public void setPaytypes(List<Object> paytypes){this.paytypes=paytypes;}
    public void setRNC(String rnc){this.rnc=rnc;}
    public void setPrinters(ArrayList<printer>  printers){this.printers=printers;}
    public void setProducts(HashMap<Integer, Products> products){this.products=products;}
    public void setCategories(HashMap<Integer, CatalogCat> categories){this.categories=categories;}
    public void setTelephone(String telephone){this.telephone=telephone;}
    public void setAddress(String address){this.address=address;}
    public void setCredit(Double credit){this.credit=credit;}
    public void setStatus(Integer status){this.Status=status;}
    public void setCompanytp(Integer companytp){this.companytp=companytp;}
    public void setUserkey(String userkey){this.userkey=userkey;}

    public int getCode(){return this.code;}
    public String getName(){return this.name;}
    public String getPath(){return this.path;}
    public String getUserkey(){return this.userkey;}
    public List<Object> getBilltypes(){return this.billtypes;}
    public List<Object> getPaytypes(){return this.paytypes;}
    public String getAltpath(){return this.altpath;}
    public ArrayList<printer>  getPrinters(){return this.printers;}
    public HashMap<Integer, Products>  getProducts(){return this.products;}
    public HashMap<Integer, CatalogCat> getCategories(){return this.categories;}
    public String getEmail(){return this.email;}
    public String getToken(){return this.token;}

    public ArrayList<Integer> getAreas() {
        return areas;
    }

    public String getRNC(){return this.rnc;}
    public String getTelephone(){return this.telephone;}
    public String getAddress(){return this.address;}
    public Double getCredit(){return this.credit;}
    public Integer getStatus(){return this.Status;}
    public Integer getCompanytp(){return this.companytp;}

    @Override
    public printer find(Object value) {
        for (int cont=0; cont<printers.size(); cont++){

            printer cl_target=printers.get(cont);
            if (cl_target.getPrinter_name().equals(value.toString())) {
                return cl_target;
            }else if (cl_target.getType().toString().equals(value.toString())) {
                return cl_target;
            }
        }
        return null;
    }

    @Override
    public Object add(Object object) throws Exception {
        if (object  instanceof printer) {
            printers.add((printer) object);
            return null;
        }else {
            throw new Exception(new InputMismatchException("Solo puede ingresar ClientStruct a esta funcion."));
        }
    }

    @Override
    public Object delete(Object object)throws Exception {
        if (object  instanceof printer) {
            printers.remove((printer) object);
            return null;
        }else {
            throw new Exception(new InputMismatchException("Solo puede ingresar ClientStruct a esta funcion."));
        }
    }

}