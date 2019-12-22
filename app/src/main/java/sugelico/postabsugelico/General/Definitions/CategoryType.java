package sugelico.postabsugelico.General.Definitions;

import java.util.HashMap;

/**
 * Created by hidura on 10/20/2016.
 */

public class CategoryType implements java.io.Serializable{
    public CategoryType(){}
    public Integer code;
    public String cat_tpname;
    public String avatar;
    public HashMap<Integer, CatalogCat> categories;

    public String getAvatar(){return this.avatar;}
    public Integer getCode(){return this.code;}
    public String getCat_tpname(){return this.cat_tpname;}
    public HashMap<Integer, CatalogCat> getCategories(){
        //This method returns all the categories.
        return this.categories;
    }

    public void setAvatar(String avatar){this.avatar=avatar;}
    public void setCode(Integer code){this.code=code;}
    public void setCat_tpname(String cat_tpname){this.cat_tpname=cat_tpname;}
    public void setCategories(HashMap<Integer, CatalogCat> categories){this.categories=categories;}
}
